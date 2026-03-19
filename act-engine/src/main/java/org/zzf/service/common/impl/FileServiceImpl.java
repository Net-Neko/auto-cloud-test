package org.zzf.service.common.impl;

import cn.hutool.core.lang.UUID;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.zzf.config.MinIoConfig;
import org.zzf.enums.BizCodeEnum;
import org.zzf.exception.BizException;
import org.zzf.service.common.FileService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 文件管理接口实现类
 *
 * @author 詹泽峰
 * @date 2026/02/20 20:45
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinIoConfig minIoConfig;

    /**
     * 异步上传实现
     *
     * @author: 詹泽峰
     * @date: 2026/2/21 10:49
     * @param bytes 文件字节数组
     * @param originalName 文件原始名称
     * @param contentType 文件内容类型
     * @return java.util.concurrent.CompletableFuture<java.lang.String>
     */
    @Async("minioUploadExecutor")
    @Override
    public CompletableFuture<String> uploadAsync(byte[] bytes, String originalName, String contentType) {
        // 统一生成文件名：UUID + 原后缀
        String fileName = UUID.randomUUID().toString().replace("-", "")
                + originalName.substring(originalName.lastIndexOf("."));

        try (InputStream is = new ByteArrayInputStream(bytes)) {
            // MinIO 的 putObject 方法不直接接收零散参数，而是接收一个封装好的 PutObjectArgs 对象。
            minioClient.putObject(
                    PutObjectArgs.builder()
                            // 指定上传桶名称
                            .bucket(minIoConfig.getBucketName())
                            // 指定在 MinIO 里的文件名
                            .object(fileName)
                            // inputStream 是数据源， bytes.length 是文件大小，-1表示让 SDK 自动决定如何分片上传
                            .stream(is, bytes.length, -1)
                            // 文件内容类型
                            .contentType(contentType)
                            .build()
            );
            log.info("异步上传成功: {}", fileName);
            return CompletableFuture.completedFuture(fileName);
        } catch (Exception e) {
            log.error("Minio上传失败: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     *
     * @author: 詹泽峰
     * @date: 2026/2/24 18:03
     * @param filePath
     * @param expiry
     * @param timeUnit
     * @return java.lang.String
     */
    @Override
    public String getTempAccessFileUrl(String filePath, Integer expiry, TimeUnit timeUnit) {
        // 业务规则校验
        validateFileExist(filePath);
        validateExpiryTime(expiry, timeUnit);

        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(minIoConfig.getBucketName())
                .object(filePath)
                .expiry(expiry, timeUnit)
                .method(Method.GET)
                .build();
        try {
            return minioClient.getPresignedObjectUrl(args);
        } catch (MinioException e) {
            throw new BizException(BizCodeEnum.MINIO_SERVICE_ERROR, e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    // 私有方法：业务规则校验（抽离成独立方法，便于维护）
    private void validateFileExist(String filePath) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minIoConfig.getBucketName())
                            .object(filePath)
                            .build()
            );
        } catch (io.minio.errors.ErrorResponseException e) {
            // 使用 SDK 标准错误码判断
            if ("NoSuchKey".equals(e.errorResponse().code()) || "NoSuchObject".equals(e.errorResponse().code())) {
                throw new BizException(BizCodeEnum.MINIO_FILE_NOT_EXIST, e);
            }
            log.error("检查文件是否存在时发生未知异常", e);
            throw new BizException(BizCodeEnum.MINIO_SERVICE_ERROR, e);
        } catch (Exception e) {
            log.error("检查文件是否存在时发生未知异常", e);
            throw new BizException(BizCodeEnum.MINIO_SERVICE_ERROR, e);
        }
    }

    private void validateExpiryTime(Integer expiry, TimeUnit timeUnit) {
        long expirySeconds = timeUnit.toSeconds(expiry);
        if (expirySeconds > 7 * 24 * 60 * 60) {
            throw new BizException(BizCodeEnum.MINIO_EXPIRE_TIME_OVER_LIMIT);
        }
        if (expiry <= 0) {
            throw new BizException(BizCodeEnum.MINIO_PARAM_ERROR);
        }
    }
}
