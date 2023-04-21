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

import java.time.LocalDateTime;

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
            jdbcTemplate.query("select * from air.air_pollution_daily",
                    (rs, row) -> RealTimeAirEntity.builder()
                            .stationName(rs.getString("measuring_station"))
                            .dataTime(rs.getObject("data_time", LocalDateTime.class))
                            .so2Grade(rs.getInt("so2_grade"))
                            .khaiValue(rs.getInt("khai_value"))
                            .so2Value(rs.getDouble("so2_value"))
                            .coValue(rs.getDouble("co_value"))
                            .pm10Value(rs.getInt("pm10_value"))
                            .o3Grade(rs.getInt("o3_grade"))
                            .khaiGrade(rs.getInt("khai_grade"))
                            .pm25Value(rs.getInt("pm25_value"))
                            .no2Grade(rs.getInt("no2_grade"))
                            .pm25Grade(rs.getInt("pm25_grade"))
                            .coGrade(rs.getInt("co_grade"))
                            .no2Value(rs.getDouble("no2_value"))
                            .pm10Grade(rs.getInt("pm10_grade"))
                            .o3Value(rs.getDouble("o3_value"))
                            .build()
            ).forEach(person -> log.info("Found <{{}}> in the database.", person));
            endTime = System.currentTimeMillis();
            System.out.println("Job took " + (endTime - startTime) + "ms");
        }
    }

}
