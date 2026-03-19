package org.zzf.stress;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.zzf.EngineApplication;
import org.zzf.service.common.FileService;
import org.zzf.util.CustomFileUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author 詹泽峰
 * @date 2026/02/24 13:00
 */
@SpringBootTest(classes = EngineApplication.class)
@Slf4j
public class FileTest {

    @Resource
    private FileService fileService;

    @Test
    public void testTempFileApi(){
        String tempAccessFileUrl = fileService.getTempAccessFileUrl("a79315ad87354dbeb6bb7b224031484d.jmx", 60, TimeUnit.SECONDS);
        System.out.println(tempAccessFileUrl);
    }

    @Test
    public void testReadRemoteFile(){
        String content = CustomFileUtil.readRemoteFile("http://106.55.254.162:9000/bucket/a79315ad87354dbeb6bb7b224031484d.jmx?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20260225%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260225T103819Z&X-Amz-Expires=60&X-Amz-SignedHeaders=host&X-Amz-Signature=9d63b436a3d464c6ab3ad4be608ce04f407aec18db252775ad1225c35b291bc1");
        System.out.println(content);
    }

}
