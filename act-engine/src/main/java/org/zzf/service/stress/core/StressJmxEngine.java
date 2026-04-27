package org.zzf.service.stress.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.save.SaveService;
import org.apache.jorphan.collections.HashTree;
import org.springframework.context.ApplicationContext;
import org.zzf.dto.ReportDTO;
import org.zzf.model.StressCaseDO;
import org.zzf.service.common.FileService;
import org.zzf.service.common.ResultSenderService;
import org.zzf.service.common.impl.KafkaResultSenderServiceImpl;
import cn.hutool.core.io.FileUtil;
import org.zzf.util.CustomFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author 詹泽峰
 * @date 2026/03/11 17:40
 */
@Slf4j
public class StressJmxEngine extends BaseStressEngine {

    public StressJmxEngine(StressCaseDO stressCaseDO, ReportDTO reportDTO, ApplicationContext applicationContext) {
        this.stressCaseDO = stressCaseDO;
        this.reportDTO = reportDTO;
        this.applicationContext = applicationContext;
    }

    @Override
    public void initStressEngine() {
        this.engine = new StandardJMeterEngine();
    }

    /**
     * 组装测试计划
     *
     * @author: 詹泽峰
     * @date: 2026/4/8 14:38
     * @param
     * @return void
     */
    @Override
    public void assembleTestPlan() {
        File jmxFile = null;
        HashTree testPlanTree = null;

        try {
            // 在项目目录下创建临时JMX文件，而非系统临时目录
            String tempDir = System.getProperty("user.dir") + File.separator + "jmx-output" + File.separator;
            FileUtil.mkdir(tempDir);
            jmxFile = new File(tempDir + "jemter-script-" + System.currentTimeMillis() + ".jmx");

            try (OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(jmxFile), StandardCharsets.UTF_8)) {
                //读取远程文件的内容，写到本地文件
                FileService fileService = applicationContext.getBean(FileService.class);
                String tempAccessFileUrl = fileService.getTempAccessFileUrl(stressCaseDO.getJmxUrl(), 10, TimeUnit.MINUTES);
                String content = CustomFileUtil.readRemoteFile(tempAccessFileUrl);
                fileWriter.write(content);
            }

            // 加载测试计划 jmx脚本
            testPlanTree = SaveService.loadTree(jmxFile);

            // 转换测试计划树
            JMeter.convertSubTree(testPlanTree, false);

            //获取自定义结果收集器
            ResultSenderService resultSenderService = applicationContext.getBean(KafkaResultSenderServiceImpl.class);
            EngineSampleCollector engineSampleCollector = super.getEngineSampleCollector(resultSenderService);
            testPlanTree.add(testPlanTree.getArray()[0],engineSampleCollector);

        } catch (Exception e) {
            log.error("组装测试计划失败", e);
            throw new RuntimeException("组装测试计划失败", e);
        } finally {
            //删除临时文件
            if(jmxFile != null){
                boolean flag = jmxFile.delete();
                log.info("临时本地jmx文件路径：{}",jmxFile.getAbsolutePath());
                if(!flag){
                    log.error("删除临时文件失败");
                }
            }
        }
        super.setTestPlanHashTree(testPlanTree);
    }
}
