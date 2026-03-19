package org.zzf.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Minio配置类
 *
 * @author 詹泽峰
 * @date 2026/02/20 20:01
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinIoConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    /**
     * 自动创建并注入MinioClient对象到Spring容器中
     *
     * @author: 詹泽峰
     * @date: 2026/2/20 20:21
     * @return io.minio.MinioClient
     */
    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }
}
