package org.zzf.service.common;

import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 文件管理接口
 *
 * @author 詹泽峰
 * @date 2026/02/20 20:40
 */
public interface FileService {
    /**
     * 异步上传
     *
     * @param bytes 文件字节数组
     * @param originalName 原始文件名
     * @param contentType 内容类型
     * @return 包含文件名的未来对象
     */
    CompletableFuture<String> uploadAsync(byte[] bytes, String originalName, String contentType);

    /**
     *
     * @author: 詹泽峰
     * @date: 2026/2/22 19:50
     * @param filePath
     * @param expiry
     * @param timeUnit
     * @return java.lang.String
     */
    String getTempAccessFileUrl(String filePath, Integer expiry, TimeUnit timeUnit);
}
