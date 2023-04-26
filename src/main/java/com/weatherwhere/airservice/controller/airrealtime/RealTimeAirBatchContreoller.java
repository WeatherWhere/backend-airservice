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
@RequestMapping("/air")
@RequiredArgsConstructor
public class RealTimeAirBatchContreoller {
    private final RealTimeAirBatchConfiguration realTimeAirBatchConfiguration;
    private final JobLauncher jobLauncher;

    private Job job;

    /**
     * 특정 job만 실행시키키 위해서 Qualifier 어노테이션 사용후 setJob 설정
     *
     * @param job
     */
    @Autowired
    @Qualifier("realtimeJob")
    public void setrealtimeJob(Job job) {
        this.job = job;
    }

    /**
     * 실시간 대기 정보 Batch 돌리는 api
     *
     * @return Batch 작업이 성공적으로 수행됐음을 알리는 String 리턴
     * @throws Exception
     */
    @GetMapping("/batch/realtime")
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
