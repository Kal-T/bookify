package com.ikal.bookify.service;

import com.ikal.bookify.dto.ClassRequest;
import com.ikal.bookify.exception.ResourceNotFoundException;
import com.ikal.bookify.model.Class;
import com.ikal.bookify.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;

    public List<Class> getAvailableClasses(String country) {
        LocalDateTime currentTime = LocalDateTime.now();
        return classRepository.findAvailableClasses(country, currentTime);
    }

    public Class getClassById(Long classId) {
        return classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with ID: " + classId));
    }

    public Class createClass(ClassRequest classEntity) {
        Class newClass = new Class(classEntity.getName(), classEntity.getCountry(), classEntity.getCreditsRequired(), classEntity.getScheduleTime(), classEntity.getEndTime(), classEntity.getAvailableSlots());
        return classRepository.save(newClass);
    }

    public Class updateClass(Long classId, ClassRequest classDetails) {
        Class existingClass = getClassById(classId);

        existingClass.setName(classDetails.getName());
        existingClass.setCountry(classDetails.getCountry());
        existingClass.setCreditsRequired(classDetails.getCreditsRequired());
        existingClass.setScheduleTime(classDetails.getScheduleTime());
        existingClass.setEndTime(classDetails.getEndTime());
        existingClass.setAvailableSlots(classDetails.getAvailableSlots());

        return classRepository.save(existingClass);
    }

    public void deleteClass(Long classId) {
        Class existingClass = getClassById(classId);
        classRepository.delete(existingClass);
    }
}

