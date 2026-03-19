package org.zzf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author 詹泽峰
 * @date 2026/02/20 22:19
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Value("${async.executor.core-size}")
    private int coreSize;

    @Value("${async.executor.max-size}")
    private int maxSize;

    @Value("${async.executor.queue-capacity}")
    private int queueCapacity;

    @Bean(name = "minioUploadExecutor")
    public Executor minioUploadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("MinioAsync-");
        executor.initialize();
        return executor;
    }
}
