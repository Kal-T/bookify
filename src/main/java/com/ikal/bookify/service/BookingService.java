package com.ikal.bookify.service;

import com.ikal.bookify.exception.BookingException;
import com.ikal.bookify.exception.ResourceNotFoundException;
import com.ikal.bookify.exception.UserNotFoundException;
import com.ikal.bookify.model.*;
import com.ikal.bookify.model.Class;
import com.ikal.bookify.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private WaitlistRepository waitlistRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserPackageRepository userPackageRepository;

    @Autowired
    private BookingCancellationRepository bookingCancellationRepository;

    @Autowired
    private WaitlistCancellationRepository waitlistCancellationRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public synchronized String bookClass(Long userId, Long classId) {
        String cacheKey = "class_" + classId;

        // Retrieve available slots from Redis
        Integer availableSlots = (Integer) redisTemplate.opsForValue().get(cacheKey);

        // Retrieve class details
        Class scheduledClass = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        if (availableSlots == null) {
            // Set available slots in Redis when class is retrieved or created
            availableSlots = scheduledClass.getAvailableSlots();  // Assuming availableSlots is a method or field in the Class model
            redisTemplate.opsForValue().set(cacheKey, availableSlots);
        }

        // Ensure booking is made before the class starts
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(scheduledClass.getScheduleTime())) {
            throw new BookingException("Booking is not allowed after the class start time.");
        }

        // Fetch user's active package
        UserPackage activePackage = userPackageRepository.findMostRecentActivePackageByUserId(userId)
                .orElseThrow(() -> new BookingException("No active package found for the user."));

        // Check if the user has enough credits
        if (activePackage.getRemainingCredits() < scheduledClass.getCreditsRequired()) {
            throw new BookingException("Insufficient credits in the active package.");
        }

        // Deduct the required credits from the user's package
        activePackage.setRemainingCredits(activePackage.getRemainingCredits() - scheduledClass.getCreditsRequired());
        userPackageRepository.save(activePackage);

        // Handle waitlist scenario if no slots are available
        if (availableSlots == null || availableSlots <= 0) {
            waitlistRepository.save(new ClassWaitlist(userId, classId, LocalDateTime.now()));
            return "Insufficient slots. Credits deducted and added to waitlist.";
        }

        // Create and save the booking
        ClassBooking booking = new ClassBooking(userId, classId, ClassBooking.BookingStatus.BOOKED, scheduledClass.getCreditsRequired(), currentTime, null, null, ClassBooking.CheckInStatus.PENDING);
        bookingRepository.save(booking);

        // Update Redis cache
        redisTemplate.opsForValue().decrement(cacheKey);

        return "Booking successful";
    }

    public void checkIn(Long userId, Long bookingId) {

        ClassBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingException("Booking not found"));

        if(!Objects.equals(userId, booking.getUserId())){
            throw new BookingException("Wrong user checked-in.");
        }

        Class bookedClass = classRepository.findById(booking.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime classStartTime = bookedClass.getScheduleTime();

        if (currentTime.isAfter(classStartTime)) {
            throw new BookingException("Check-in is not allowed after the class start time.");
        }

        // Ensure the booking status is valid for check-in
        if (booking.getBookingStatus() != ClassBooking.BookingStatus.BOOKED) {
            throw new BookingException("Only booked classes can be checked in.");
        }

        booking.setCheckInStatus(ClassBooking.CheckInStatus.CHECKED_IN);
        booking.setCheckInTime(currentTime);
        bookingRepository.save(booking);

        User user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        emailService.sendCheckInConfirmation(user.getEmail());

    }

    public void cancelBooking(Long userId, Long bookingId) {
        ClassBooking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingException("Booking not found"));

        if(!Objects.equals(userId, booking.getUserId())){
            throw new BookingException("Wrong user checked-in.");
        }

        Class bookedClass = classRepository.findById(booking.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime classStartTime = bookedClass.getScheduleTime();
        long hoursBeforeClass = java.time.Duration.between(currentTime, classStartTime).toHours();

        // Create a cancellation entry
        BookingCancellations cancellation = new BookingCancellations();
        cancellation.setBookingId(bookingId);
        cancellation.setRefundStatus(BookingCancellations.RefundStatus.PENDING);  // Initially, refund is pending
        cancellation.setRefundAmount(0);
        bookingCancellationRepository.save(cancellation);

        // Refund logic based on cancellation time within 4 hour or not
        if (hoursBeforeClass > 4) {
            refundCreditsToUser(booking.getUserId(), bookedClass.getCreditsRequired());

            cancellation.setRefundStatus(BookingCancellations.RefundStatus.REFUNDED);
            cancellation.setRefundAmount(bookedClass.getCreditsRequired());  // Refund the full credits
            bookingCancellationRepository.save(cancellation);
        }else{
            cancellation.setRefundStatus(BookingCancellations.RefundStatus.NOT_REFUNDED);
            cancellation.setRefundAmount(0);  // No refund amount
            bookingCancellationRepository.save(cancellation);
        }
        booking.setBookingStatus(ClassBooking.BookingStatus.CANCELED);
        booking.setCanceledAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    public void cancelWaitlist(Long userId, Long classId) {
        // Find the waitlist entry for the user and class
        ClassWaitlist waitlistEntry = waitlistRepository.findByUserIdAndClassId(userId, classId)
                .orElseThrow(() -> new BookingException("Waitlist entry not found for the specified user and class."));

        if(!Objects.equals(userId, waitlistEntry.getUserId())){
            throw new BookingException("Wrong user checked-in.");
        }

        // Remove the user from the waitlist
        waitlistRepository.delete(waitlistEntry);

        Class bookedClass = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime classStartTime = bookedClass.getScheduleTime();
        long hoursBeforeClass = java.time.Duration.between(currentTime, classStartTime).toHours();

        // Create a cancellation entry
        WaitlistCancellations cancellation = new WaitlistCancellations();
        cancellation.setWaitlistId(waitlistEntry.getWaitlistId());
        cancellation.setRefundStatus(WaitlistCancellations.RefundStatus.PENDING);  // Initially, refund is pending
        cancellation.setRefundAmount(0);
        waitlistCancellationRepository.save(cancellation);

        // Refund logic based on cancellation time within 4 hour or not
        if (hoursBeforeClass > 4) {
            refundCreditsToUser(userId, bookedClass.getCreditsRequired());

            cancellation.setRefundStatus(WaitlistCancellations.RefundStatus.REFUNDED);
            cancellation.setRefundAmount(bookedClass.getCreditsRequired());  // Refund the full credits
            waitlistCancellationRepository.save(cancellation);
        }else{
            cancellation.setRefundStatus(WaitlistCancellations.RefundStatus.NOT_REFUNDED);
            cancellation.setRefundAmount(0);  // No refund amount
            waitlistCancellationRepository.save(cancellation);
        }

        // Notify the user about the cancellation
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        emailService.sendWaitlistCancellationEmail(user.getEmail());
    }

    public void processMissedCheckInAndWaitlist() {
        LocalDateTime currentTime = LocalDateTime.now();  // Get the current time

        // Find all classes whose scheduled time is in the future and are not COMPLETED
        List<Class> allClasses = classRepository.findAllByScheduleTimeAfterAndStatusNot(currentTime, Class.ClassStatus.COMPLETED);

        for (Class bookedClass : allClasses) {
            LocalDateTime classStartTime = bookedClass.getScheduleTime();  // Get the class start time
            String cacheKey = "class_" + bookedClass.getClassId();  // Redis cache key for available slots

            // Process missed check-ins for the class
            for (ClassBooking booking : bookingRepository.findByClassIdAndBookingStatus(bookedClass.getClassId(), ClassBooking.BookingStatus.BOOKED)) {
                if (currentTime.isAfter(classStartTime) &&
                        booking.getCheckInStatus() == ClassBooking.CheckInStatus.PENDING) {

                    // Mark the booking as MISSED
                    booking.setCheckInStatus(ClassBooking.CheckInStatus.MISSED);
                    bookingRepository.save(booking);

                    // Increase available slot in Redis since the user missed the check-in (freed up the slot)
                    redisTemplate.opsForValue().increment(cacheKey);
                }
            }

            List<ClassWaitlist> waitlist = waitlistRepository.findByClassId(bookedClass.getClassId());

            // Sort the waitlist by the timestamp or createdAt field in ascending order for FIFO processing
            waitlist.sort(Comparator.comparing(ClassWaitlist::getAddedAt));

            for (ClassWaitlist waitlistEntry : waitlist) {
                // Check Redis to ensure there are available slots
                Integer availableSlots = (Integer) redisTemplate.opsForValue().get(cacheKey);

                if (availableSlots == null) {
                    // Set available slots in Redis when class is retrieved or created
                    availableSlots = bookedClass.getAvailableSlots();  // Assuming availableSlots is a method or field in the Class model
                    redisTemplate.opsForValue().set(cacheKey, availableSlots);
                }

                if (availableSlots > 0) {
                    // Create a new booking for the user from the waitlist
                    ClassBooking booking = new ClassBooking(waitlistEntry.getUserId(), bookedClass.getClassId(), ClassBooking.BookingStatus.BOOKED, bookedClass.getCreditsRequired(), LocalDateTime.now(), null, null, ClassBooking.CheckInStatus.CHECKED_IN);
                    bookingRepository.save(booking);

                    // Decrement available slots in Redis since a user from the waitlist is booked
                    redisTemplate.opsForValue().decrement(cacheKey);
                    waitlistRepository.delete(waitlistEntry);
                }
            }
        }
    }

    public void processClassCompletionForAll() {
        LocalDateTime now = LocalDateTime.now();

        // Find all classes where the end time is before the current time and the class is not yet completed
        List<Class> scheduledClasses = classRepository.findByEndTimeBeforeAndStatusNot(now, Class.ClassStatus.COMPLETED);

        for (Class scheduledClass : scheduledClasses) {
            // Mark each class as completed
            scheduledClass.setStatus(Class.ClassStatus.COMPLETED);
            scheduledClass.setEndTime(now);

            // Save the updated class status
            classRepository.save(scheduledClass);

            // Notify users and refund credits
            List<ClassWaitlist> waitlist = waitlistRepository.findByClassId(scheduledClass.getClassId());
            for (ClassWaitlist waitlistEntry : waitlist) {
                refundCreditsToUser(waitlistEntry.getUserId(), scheduledClass.getCreditsRequired());
            }

            // Remove class cache entry
            String cacheKey = "class_" + scheduledClass.getClassId();
            redisTemplate.delete(cacheKey);
        }
    }


    private void refundCreditsToUser(Long userId, Integer requiredCredits) {
        UserPackage activePackage = userPackageRepository.findMostRecentActivePackageByUserId(userId)
                .orElseThrow(() -> new BookingException("No active package found for the user."));

        // Refund the credits to the selected package
        activePackage.setRemainingCredits(activePackage.getRemainingCredits() + requiredCredits);
        userPackageRepository.save(activePackage);

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
        emailService.sendRefundEmail(user.getEmail());

        System.out.println("Refunded " + requiredCredits + " credits to active package of user " + userId);
    }
}

