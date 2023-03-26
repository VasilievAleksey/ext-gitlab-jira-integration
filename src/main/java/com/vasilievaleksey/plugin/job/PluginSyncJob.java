package com.vasilievaleksey.plugin.job;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.scheduler.JobRunner;
import com.atlassian.scheduler.JobRunnerRequest;
import com.atlassian.scheduler.JobRunnerResponse;
import com.atlassian.scheduler.SchedulerService;
import com.atlassian.scheduler.SchedulerServiceException;
import com.atlassian.scheduler.config.JobConfig;
import com.atlassian.scheduler.config.JobId;
import com.atlassian.scheduler.config.JobRunnerKey;
import com.atlassian.scheduler.config.Schedule;
import com.vasilievaleksey.plugin.service.RepositoryService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

import static com.atlassian.scheduler.config.JobConfig.forJobRunnerKey;
import static com.atlassian.scheduler.config.RunMode.RUN_ONCE_PER_CLUSTER;

@Component
public class PluginSyncJob implements JobRunner, InitializingBean, DisposableBean {
    @ComponentImport
    private final SchedulerService schedulerService;
    private final RepositoryService repositoryService;

    private final static String IDENTIFIER = PluginSyncJob.class.getName();
    private final static JobRunnerKey JOB_RUNNER_KEY = JobRunnerKey.of(IDENTIFIER);
    private final static JobId JOB_ID = JobId.of(IDENTIFIER);
    private final static long JOB_INTERVAL = 60000L;

    @Autowired
    public PluginSyncJob(SchedulerService schedulerService, RepositoryService repositoryService) {
        this.schedulerService = schedulerService;
        this.repositoryService = repositoryService;
    }

    @Nullable
    @Override
    public JobRunnerResponse runJob(@Nonnull JobRunnerRequest jobRunnerRequest) {
        repositoryService.cloneNewRepositories();
        return JobRunnerResponse.success("Success");
    }

    @Override
    public void destroy() {
        schedulerService.unscheduleJob(JOB_ID);
    }

    @Override
    public void afterPropertiesSet() {
        schedulerService.registerJobRunner(JOB_RUNNER_KEY, this);
        final JobConfig configuration = forJobRunnerKey(JOB_RUNNER_KEY)
                .withRunMode(RUN_ONCE_PER_CLUSTER)
                .withSchedule(Schedule.forInterval(JOB_INTERVAL, new Date(System.currentTimeMillis() + JOB_INTERVAL)));

        try {
            schedulerService.scheduleJob(JOB_ID, configuration);
        } catch (SchedulerServiceException e) {
            e.printStackTrace();
        }
    }
}
