package com.weatherwhere.airservice.config;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    private Long startTime;
    private Long endTime;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
            jdbcTemplate.query("select measuring_station from air.air_pollution_daily",
                    (rs, row) -> RealTimeAirEntity.builder().stationName(rs.getString(1)).build()
            ).forEach(person -> log.info("Found <{{}}> in the database.", person));
            endTime = System.currentTimeMillis();
            System.out.println("Job took " + (endTime - startTime) + "ms");
        }
    }
}
