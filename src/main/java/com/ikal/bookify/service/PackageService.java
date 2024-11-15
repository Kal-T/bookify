package com.ikal.bookify.service;

import com.ikal.bookify.dto.CardInfo;
import com.ikal.bookify.dto.PurchaseRequest;
import com.ikal.bookify.exception.PaymentException;
import com.ikal.bookify.exception.PurchasePackageFailException;
import com.ikal.bookify.exception.ResourceNotFoundException;
import com.ikal.bookify.exception.UserNotFoundException;
import com.ikal.bookify.model.Package;
import com.ikal.bookify.model.User;
import com.ikal.bookify.model.UserPackage;
import com.ikal.bookify.repository.PackageRepository;
import com.ikal.bookify.repository.UserPackageRepository;
import com.ikal.bookify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserPackageRepository userPackageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    public List<Package> getAvailablePackages(String country) {
        return packageRepository.findByCountry(country);
    }

    public UserPackage purchasePackage(Long userId, PurchaseRequest purchaseRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Package aPackage = packageRepository.findById(purchaseRequest.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with ID: " + purchaseRequest.getPackageId()));

        if (user.getCountry() == null || !user.getCountry().equalsIgnoreCase(aPackage.getCountry())) {
            throw new PurchasePackageFailException("User's country does not match the package's country.");
        }

        // Check if the package has expired
        if (aPackage.getExpiryDate() != null && aPackage.getExpiryDate().isBefore(LocalDate.now())) {
            throw new PurchasePackageFailException("Package has expired");
        }

        // Process payment before adding the package
        boolean cardAdded = paymentService.addPaymentCard(purchaseRequest.getCardInfo());
        if (!cardAdded) {
            throw new PaymentException("Failed to add payment card.");
        }

        boolean paymentSuccess = paymentService.paymentCharge(user.getUserId().toString(), aPackage.getPrice().doubleValue());
        if (!paymentSuccess) {
            throw new PaymentException("Payment failed. Please try again.");
        }

        // Check if the user already has this package active
        Optional<UserPackage> existingUserPackageOpt = userPackageRepository.findUserIdAndPackageId(user.getUserId(), aPackage.getPackageId());

        if (existingUserPackageOpt.isPresent()) {
            UserPackage existingUserPackage = existingUserPackageOpt.get();

            existingUserPackage.setRemainingCredits(existingUserPackage.getRemainingCredits() + aPackage.getCredits());
            existingUserPackage.setPurchaseDate(LocalDateTime.now());

            return userPackageRepository.save(existingUserPackage);
        } else {
            UserPackage userPackage = new UserPackage();
            userPackage.setUser(user);
            userPackage.setAPackage(aPackage);
            userPackage.setRemainingCredits(aPackage.getCredits());
            userPackage.setStatus(UserPackage.Status.ACTIVE);
            userPackage.setPurchaseDate(LocalDateTime.now());

            return userPackageRepository.save(userPackage);
        }
    }

    public List<UserPackage> getUserPackages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return userPackageRepository.findByUserAndStatus(user, UserPackage.Status.ACTIVE);
    }
}

