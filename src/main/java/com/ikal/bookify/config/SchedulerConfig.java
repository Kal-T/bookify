package com.ikal.bookify.config;

import com.ikal.bookify.schedular.ClassCompletionJob;
import com.ikal.bookify.schedular.WaitlistProcessingJob;
import de.chandre.quartz.spring.AutowiringSpringBeanJobFactory;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig {

    @Autowired
    private WaitlistProcessingJob waitlistProcessingJob;

    @Autowired
    private ClassCompletionJob classCompletionJob;

    @Autowired
    private AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory;

    @Bean
    public JobDetail waitlistProcessingJobDetail() {
        return JobBuilder.newJob(WaitlistProcessingJob.class)
                .withIdentity("waitlistProcessingJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger waitlistProcessingTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(waitlistProcessingJobDetail())
                .withIdentity("waitlistProcessingTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1)  // Executes every 1 minute
                        .repeatForever())
                .build();
    }

    @Bean
    public JobDetail classCompletionJobDetail() {
        return JobBuilder.newJob(ClassCompletionJob.class)
                .withIdentity("classCompletionJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger classCompletionTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(classCompletionJobDetail())
                .withIdentity("classCompletionTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(30) // Executes every 30 minutes
                        .repeatForever())
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobDetails(waitlistProcessingJobDetail(), classCompletionJobDetail());
        schedulerFactoryBean.setTriggers(waitlistProcessingTrigger(), classCompletionTrigger());
        schedulerFactoryBean.setJobFactory(autowiringSpringBeanJobFactory);
        return schedulerFactoryBean;
    }
}
