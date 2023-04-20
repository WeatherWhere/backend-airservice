package com.weatherwhere.airservice.config;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.airrealtime.StationNameDTO;
import com.weatherwhere.airservice.service.airrealtime.ParseCSVService;
import com.weatherwhere.airservice.service.airrealtime.RealTimeAirService;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;


@Log4j2
@Configuration
@RequiredArgsConstructor
public class RealTimeAirBatchConfiguration {
    private final RealTimeAirService realTimeAirService;
    private final ParseCSVService parseCSVService;

    private List<StationNameDTO> stationNameDtoList = new ArrayList<>();
    private List<RealTimeAirEntity> collectData = new ArrayList<>();
    private int index;

    public void initialize() {
        stationNameDtoList = parseCSVService.ParseCSV();
        collectData = realTimeAirService.makeEntityList(stationNameDtoList);
        index = 0;
    }

    //ItemReader
    @Bean
    public ItemReader<RealTimeAirEntity> restItCollectReader() {
        return new ItemReader<RealTimeAirEntity>() {
            @Override
            public RealTimeAirEntity read() {
                // ItemReader는 반복문으로 동작한다.
                // 하나씩 ItemWriter로 전달해야 한다.
                RealTimeAirEntity nextCollect = null;

                if (index < collectData.size()) {
                    // 전체 리스트에서 하나씩 추출해서, 하나씩 Writer로 전달한다.
                    nextCollect = collectData.get(index);
                    System.out.println(index);
                    index++;
                }
                return nextCollect;
            }
        };
    }

    // ItemWriter
    @Bean
    public JpaItemWriter<RealTimeAirEntity> jpaItemWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<RealTimeAirEntity> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    // step
    @Bean
    public Step realtimeStep(JobRepository jobRepository,
                        PlatformTransactionManager transactionManager, JpaItemWriter<RealTimeAirEntity> writer) {
        return new StepBuilder("realtimeStep", jobRepository)
                .<RealTimeAirEntity, RealTimeAirEntity>chunk(1, transactionManager)
                .reader(restItCollectReader())
                .writer(writer)
                .build();
    }

    // job
    @Bean
    public Job realtimeJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step jpaStep) {
        return new JobBuilder("realtimeJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(jpaStep)
                .end()
                .build();
    }

}
