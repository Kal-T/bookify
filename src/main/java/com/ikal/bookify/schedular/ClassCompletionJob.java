package com.ikal.bookify.schedular;

import com.ikal.bookify.service.BookingService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class ClassCompletionJob implements Job {
    @Autowired
    private BookingService bookingService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        bookingService.processClassCompletionForAll();
    }
}
