package org.zzf.controller.common;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zzf.service.common.FileService;
import org.zzf.util.JsonData;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 文件管理控制器类
 *
 * @author 詹泽峰
 * @date 2026/02/20 20:27
 */
@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return JsonData 返回操作结果
     * @author 詹泽峰
     * @date 2026/02/20 20:27
     */
    @PostMapping("/upload")
    public JsonData upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) return JsonData.buildError("文件为空");

        try {
            // 在 Request 线程内转为字节数组，脱离 MultipartFile 生命周期
            byte[] bytes = file.getBytes();
            String originalName = file.getOriginalFilename();
            String contentType = file.getContentType();

            // 丢给异步线程池
            fileService.uploadAsync(bytes, originalName, contentType);

            return JsonData.buildSuccess("文件上传成功");
        } catch (IOException e) {
            return JsonData.buildError("读取文件失败");
        }
    }

    @GetMapping("/temp-access-url")
    public JsonData getTempAccessFileUrl(@RequestParam String filePath, @RequestParam(defaultValue = "60") Integer expiry, @RequestParam(defaultValue = "MINUTES") String timeUnitStr) {
        TimeUnit timeUnit = TimeUnit.valueOf(timeUnitStr.toUpperCase());
        String tempUrl = fileService.getTempAccessFileUrl(filePath, expiry, timeUnit);
        return JsonData.buildSuccess(tempUrl);
    }
}
