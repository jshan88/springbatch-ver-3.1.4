package com.jshan.batch.jobs.rsvpReport.configuration;

import com.jshan.batch.jobs.rsvpReport.listener.RsvpReportJobListener;
import com.jshan.batch.jobs.rsvpReport.listener.SnapshotNewGuestsStepListener;
import com.jshan.batch.jobs.rsvpReport.listener.SummarizeGuestsStepListener;
import com.jshan.batch.jobs.rsvpReport.persistence.InMemorySummaryMap;
import com.jshan.batch.jobs.rsvpReport.processor.GuestDailyProcessor;
import com.jshan.batch.jobs.rsvpReport.processor.GuestSnapshotProcessor;
import com.jshan.batch.jobs.rsvpReport.reader.GuestSnapshotReader;
import com.jshan.batch.jobs.rsvpReport.tasklet.EventMetaDataSetTasklet;
import com.jshan.batch.jobs.rsvpReport.writer.GuestDailySnapshotWriter;
import com.jshan.batch.jobs.rsvpReport.persistence.InMemorySummaryMap.SummaryKey;
import com.jshan.batch.jobs.rsvpReport.writer.GuestDailySummaryWriter;
import lombok.extern.slf4j.Slf4j;
import com.jshan.batch.entity.privacy.Guest;
import com.jshan.batch.entity.privacy.GuestDailySnapshot;
import com.jshan.batch.jobs.rsvpReport.reader.GuestDailyReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class RsvpReportConfiguration {
    public static final String RSVP_REPORT_JOB = "rsvpReportJob";
    public static final String EXTRACT_META_DATA_STEP = "extractMetaData";
    public static final String LOAD_GUEST_SNAPSHOTS_STEP = "loadGuestSnapshots";
    public static final String SUMMARIZE_GUESTS_STEP = "summarizeGuests";
    private final PlatformTransactionManager eventTransactionManager;
    private final PlatformTransactionManager privacyTransactionManager;

    public RsvpReportConfiguration(@Qualifier("eventTransactionManager") PlatformTransactionManager eventTransactionManager,
                                   @Qualifier("privacyTransactionManager") PlatformTransactionManager privacyTransactionManager ) {
        this.eventTransactionManager = eventTransactionManager;
        this.privacyTransactionManager = privacyTransactionManager;
    }

    /**
     * 일자별 RSVP Report 생성을 위한 Daily Summary Job
     *
     * @param metaDataStep          필요한 metadata 사전 세팅
     * @param snapshotNewGuestsStep 게스트 리스트 스냅샷 추출
     * @param summarizeGuestsStep   게스트 스냅샷 Summary 생성
     * @param jobListener           Job 완료 시, 스냅샷 데이터 Truncate
     */
    @Bean
    public Job rsvpReportJob(JobRepository jobRepository, RsvpReportJobListener jobListener,
                             Step metaDataStep, Step snapshotNewGuestsStep, Step summarizeGuestsStep) {
        String[] requiredParameters = {"requestDate"};
        String[] optionalParameters = {};

        return new JobBuilder(RSVP_REPORT_JOB, jobRepository)
                    .validator(new DefaultJobParametersValidator(requiredParameters, optionalParameters))
                    .start(metaDataStep)
                    .next(snapshotNewGuestsStep)
                    .next(summarizeGuestsStep)
                    .listener(jobListener)
                    .build();
    }

    /**
     * Job 수행에 필요한 Metadata 추출 및 JobExecution 등록. <br>
     * - {@link EventMetaDataSetTasklet} : Metadata 추출 <br>
     * - {@link ExecutionContextPromotionListener} : Metadata 를 JobExecution 으로 승격
     */
    @Bean
    @JobScope
    public Step metaDataStep(JobRepository jobRepository,
                             EventMetaDataSetTasklet eventMetaDataSetTasklet) {
        return new StepBuilder(EXTRACT_META_DATA_STEP, jobRepository)
            .tasklet(eventMetaDataSetTasklet, eventTransactionManager)
            .listener(promotionListener())
            .build();
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        String[] metadata = {"rsvpEventIdSet"}; // {"groupMap", "subOrganizationMap"};
        executionContextPromotionListener.setKeys(metadata);
        return executionContextPromotionListener;
    }

    /**
     * RSVP 행사 게스트 스냅샷 적재 <br>
     *
     * @param guestDailyReader {@link GuestDailyReader} : Privacy Guest 추출
     * @param guestDailyProcessor {@link GuestDailyProcessor} : 객체 변환 (Guest -> Snapshot)
     * @param guestDailySnapshotWriter {@link GuestDailySnapshotWriter} : 스냅샷 데이터 temp table 적재
     */
    @Bean
    @JobScope
    public Step snapshotNewGuestsStep(JobRepository jobRepository,
                                      GuestDailyReader guestDailyReader,
                                      GuestDailyProcessor guestDailyProcessor,
                                      GuestDailySnapshotWriter guestDailySnapshotWriter,
                                      SnapshotNewGuestsStepListener snapshotNewGuestsStepListener) {
        return new StepBuilder(LOAD_GUEST_SNAPSHOTS_STEP, jobRepository)
            .<Guest, GuestDailySnapshot>chunk(100, privacyTransactionManager)
            .reader(guestDailyReader)
            .processor(guestDailyProcessor)
            .writer(guestDailySnapshotWriter)
            .listener(snapshotNewGuestsStepListener)
            .build();
    }

    /**
     * RSVP 스냅샷 Summarize <br>
     *
     * @param guestSnapshotReader {@link GuestSnapshotReader} : Snapshot 데이터 추출
     * @param guestSnapshotProcessor {@link GuestSnapshotProcessor} : 객체 변환 (Snapshot -> SummaryKey)
     * @param guestDailySummaryWriter {@link GuestDailySummaryWriter} : SummaryKey 별 count 를 in-memory map 에 적재
     * @param summarizeGuestsStepListener {@link SummarizeGuestsStepListener} : in-memory map 을 DB 적재
     * @see InMemorySummaryMap#summaryCountMap()
     */
    @Bean
    @JobScope
    public Step summarizeGuestsStep(JobRepository jobRepository,
                                    GuestSnapshotReader guestSnapshotReader,
                                    GuestSnapshotProcessor guestSnapshotProcessor,
                                    GuestDailySummaryWriter guestDailySummaryWriter,
                                    SummarizeGuestsStepListener summarizeGuestsStepListener) {
        return new StepBuilder(SUMMARIZE_GUESTS_STEP, jobRepository)
            .<GuestDailySnapshot, SummaryKey>chunk(100, privacyTransactionManager)
            .reader(guestSnapshotReader)
            .processor(guestSnapshotProcessor)
            .writer(guestDailySummaryWriter)
            .listener(summarizeGuestsStepListener)
            .build();
    }
}
