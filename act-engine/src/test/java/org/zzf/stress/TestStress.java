package org.zzf.stress;

import org.apache.jmeter.JMeter;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author 詹泽峰
 * @date 2025/12/15 21:16
 */
public class TestStress {
    @Test
    public void testJmeterScript() throws IOException {
        // 初始化环境
        // JMeter路径
        String jmeterPath = "E:\\Workspace\\apache-jmeter-5.4.1";
        // JMeter根目录
        File jmeterHome = new File(jmeterPath);
        // JMX文件路径
        String jmxFilePath = "E:\\Workspace\\apache-jmeter-5.4.1\\bin\\save\\test1.jmx";
        // JMX文件
        File jmxFile = new File(jmxFilePath);
        // JMeter配置文件路径
        File jmeterProperties = new File(jmeterHome.getPath() + File.separator + "bin" + File.separator + "jmeter.properties");
        // 设置JMeter根目录
        JMeterUtils.setJMeterHome(jmeterHome.getPath());
        // 加载JMeter配置文件
        JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
        // JMeter标准引擎
        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        // 脚本配置
        // 测试计划树
        HashTree testPlanTree = new HashTree();
        // 设置文件服务器的基础脚本路径
        FileServer.getFileServer().setBaseForScript(jmxFile);
        // 加载测试计划树 jmx脚本
        testPlanTree = SaveService.loadTree(jmxFile);
        // 转换测试计划树
        JMeter.convertSubTree(testPlanTree, false);

        // 结果收集器配置
        // Summariser对象
        Summariser summer = null;
        // Summariser名称
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (!summariserName.isEmpty()) {
            // 创建Summariser对象
            summer = new Summariser(summariserName);
        }
        // 结果日志文件名
        String logFile = "example.csv";
        // 结果收集器
//        ResultCollector logger = new ResultCollector (summer);
        // TODO：需区分不同请求
        TestResultCollector logger = new TestResultCollector(summer);
        logger.setFilename(logFile);
        // 将结果收集器添加到测试计划树上
        testPlanTree.add(testPlanTree.getArray()[0], logger);
        // 配置JMeter引擎
        jmeter.configure(testPlanTree);

        //测试用例运行
        // 运行JMeter测试
        jmeter.run();
    }
}
