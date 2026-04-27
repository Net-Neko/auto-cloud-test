package org.zzf.service.stress.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.springframework.context.ApplicationContext;
import org.zzf.dto.ReportDTO;
import org.zzf.enums.ReportStateEnum;
import org.zzf.feign.ReportFeignService;
import org.zzf.model.StressCaseDO;
import org.zzf.req.ReportUpdateReq;
import org.zzf.service.common.ResultSenderService;
import org.zzf.util.StressTestUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

/**
 * 压测引擎
 *
 * @author 詹泽峰
 * @date 2026/03/11 17:25
 */
@Data
@Slf4j
public abstract class BaseStressEngine {

    // ======================== 常量定义 ========================
    private static final String JMX_STATIC_DIR = "static";
    private static final String JMX_FILE_SUFFIX = ".jmx";
    private static final String SUMMARISER_NAME_KEY = "summariser.name";
    private static final String SUMMARISER_DEFAULT_NAME = "summary";

    /**
     * 测试计划
     */
    protected HashTree testPlanHashTree;

    /**
     * 测试引擎
     */
    protected StandardJMeterEngine engine;

    /**
     * 测试用例
     */
    protected StressCaseDO stressCaseDO;

    /**
     * 测试报告
     */
    protected ReportDTO reportDTO;

    /**
     * spring的应用上下文
     */
    protected ApplicationContext applicationContext;

    /**
     * 压测是否执行异常
     */
    protected boolean stressTestFailed;

    /**
     * 模板方法，定义压测执行标准流程
     *
     * @param
     * @author: 詹泽峰
     * @date: 2026/3/11 17:31
     */
    public final void startStressTest() {
        // 前置参数校验 + 初始化JMeter环境
        this.preParameterCheck();
        log.info("===== 启动压测任务 | 用例ID：{} | 用例名称：{} | 报告ID：{} =====",
                stressCaseDO.getId(), stressCaseDO.getName(), reportDTO.getId());
        try {
            //初始化测试引擎
            this.initStressEngine();

            //组装测试计划 抽象方法
             this.assembleTestPlan();

            //方便调试使用，可以不用
            this.hashTree2Jmx();

            //运行测试
            this.run();

            log.info("===== 压测任务执行完成 | 用例ID：{} =====", stressCaseDO.getId());
        } catch (Exception e) {
            log.error("===== 压测任务执行异常 | 用例ID：{} | 报告ID：{} =====", stressCaseDO.getId(), reportDTO.getId(), e);
            // 记录异常标记，统计报告完成后再更新最终状态
            this.stressTestFailed = true;
        } finally {
            // 分开执行，防止一个方法抛异常，导致其他方法无法执行
            try {
                // 运行完用例后，清理相关的资源
                this.clearData();
            } catch (Exception e) {
                log.error("清理压测临时资源失败", e);
            }

            try {
                // 更新测试报告
                this.updateReport(ReportStateEnum.COUNTING_REPORT);
            } catch (Exception e) {
                log.error("最终更新报告状态失败 | 报告ID：{}", reportDTO.getId(), e);
            }
        }
        // 释放JMeter引擎资源，防止内存泄漏
    }

    /**
     * 获取结果收集器
     *
     * @author: 詹泽峰
     * @date: 2026/3/19 18:37
     */
    public EngineSampleCollector getEngineSampleCollector(ResultSenderService resultSenderService) {

        // Summariser对象
        Summariser summer = null;
        // Summariser名称
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (!summariserName.isEmpty()) {
            // 创建Summariser对象
            summer = new Summariser(summariserName);
        }
        //使用自定义结果收集器
        EngineSampleCollector collector = new EngineSampleCollector(stressCaseDO,summer,resultSenderService,reportDTO);
        //如果要调整收集器名称
        collector.setName(stressCaseDO.getName());
        collector.setEnabled(Boolean.TRUE);

        return collector;

    }

    /**
     * 前置校验
     *
     * @author: 詹泽峰
     * @date: 2026/3/18 18:37
     */
    private void preParameterCheck() {
        StressTestUtil.initJmeterProperties();

        if (Objects.isNull(stressCaseDO)) {
            throw new IllegalArgumentException("压测用例不能为空");
        }
        if (Objects.isNull(reportDTO)) {
            throw new IllegalArgumentException("压测报告不能为空");
        }
        if (Objects.isNull(applicationContext)) {
            throw new IllegalArgumentException("Spring上下文不能为空");
        }
    }

    /**
     * 初始化引擎，子类可覆盖
     *
     * @author: 詹泽峰
     * @date: 2026/3/19 18:47
     */
    public void initStressEngine() {

    }

    /**
     * 组装测试计划,交给子类进行实现
     *
     * @author: 詹泽峰
     * @date: 2026/3/18 18:37
     */
    public abstract void assembleTestPlan();


    /**
     * 将测试计划转为 jmx 文件
     *
     * @author: 詹泽峰
     * @date: 2026/3/18 18:37
     */
    private void hashTree2Jmx() {

        if (Objects.isNull(testPlanHashTree)) {
            log.warn("测试计划为空，跳过生成JMX文件");
            return;
        }

        String dirPath = System.getProperty("user.dir") + File.separator + JMX_STATIC_DIR + File.separator;
        FileUtil.mkdir(dirPath);
        String localJmxPath = dirPath + IdUtil.simpleUUID() + ".jmx";

        // 使用 try-with-resources 自动关闭文件流，防止句柄泄露
        try (FileOutputStream out = new FileOutputStream(localJmxPath)) {
            SaveService.loadProperties();
            SaveService.saveTree(testPlanHashTree, out);
            log.info("JMX文件已生成: {}", localJmxPath);
        } catch (Exception e) {
            log.error("保存本地jmx失败", e);
        }
    }

    /**
     * 运行压测
     *
     * @author: 詹泽峰
     * @date: 2026/3/19 18:37
     */
    private void run() {
        if (Objects.isNull(engine) || Objects.isNull(testPlanHashTree)) {
            throw new IllegalStateException("压测引擎或测试计划为空，请检查初始化步骤");
        }

        if (engine.isActive()) {
            log.warn("JMeter引擎正在运行中，请勿重复执行");
            return;
        }

        engine.configure(testPlanHashTree);
        engine.run();
    }

    /**
     * 清理相关资源文件 TODO
     *
     * @author: 詹泽峰
     * @date: 2026/3/19 18:37
     */
    private void clearData() {

    }

    /**
     * 更新测试报告
     *
     * @author: 詹泽峰
     * @date: 2026/3/19 18:40
     */
    private void updateReport(ReportStateEnum state) {
        try {
            ReportFeignService reportFeignService = applicationContext.getBean(ReportFeignService.class);
            ReportUpdateReq req = ReportUpdateReq.builder()
                    .id(reportDTO.getId())
                    .executeState(state.name())
                    .endTime(System.currentTimeMillis())
                    .build();
            reportFeignService.update(req);
            log.info("报告状态更新成功 | ID：{} | 状态：{}", reportDTO.getId(), state);
        } catch (Exception e) {
            log.error("报告状态更新失败 | 报告ID：{}", reportDTO.getId(), e);
        }
    }
}
