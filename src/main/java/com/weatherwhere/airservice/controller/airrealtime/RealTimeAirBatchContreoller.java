package com.weatherwhere.airservice.controller.airrealtime;

import com.weatherwhere.airservice.config.RealTimeAirBatchConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/air/batch")
@RequiredArgsConstructor
public class RealTimeAirBatchContreoller {
    private final RealTimeAirBatchConfiguration realTimeAirBatchConfiguration;
    private final JobLauncher jobLauncher;

    private Job job;

    //특정 job만 실행시키기 위해 Qualifier어노테이션 사용 후 setJob해주기
    @Autowired
    @Qualifier("realtimeJob")
    public void setrealtimeJob(Job job) {
        this.job = job;
    }
    @GetMapping("/realtime")
    public String RealTimeAirStartBatch() throws Exception {
        Long startTime = System.currentTimeMillis();
        realTimeAirBatchConfiguration.initialize();
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(this.job, jobParameters);
        Long endTime = System.currentTimeMillis();

        return "!!!Batch Job Started: " + jobExecution.getStatus() +"총 걸린시간:"+ (endTime-startTime);
    }

}
