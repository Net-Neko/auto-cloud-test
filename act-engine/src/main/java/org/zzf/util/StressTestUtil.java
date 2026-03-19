package org.zzf.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.util.JMeterUtils;

import java.io.File;

/**
 * jmeter测试工具类
 *
 * @author 詹泽峰
 * @date 2025/12/29 16:25
 */
@Slf4j
public class StressTestUtil {

    /**
     * 初始化标记：保证全局只执行一次初始化
     */
    private static volatile boolean IS_INITIALIZED = false;

    /**
     * 获取jmeter的home路径
     * TODO: 后续部署上线后调整
     *
     * @return java.lang.String JMeter的home路径
     */
    public static String getJmeterHome() {
        try {
            // String path = StressTestUtil.class.getClassLoader().getResource("jmeter").getPath();
            String path = "E:\\NetNeko\\workspace\\project\\jmeter";
            return path;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取jmeter的bin目录
     *
     * @return java.lang.String JMeter的bin目录
     */
    public static String getJmeterHomeBin() {
        return getJmeterHome() + File.separator + "bin";
    }

    /**
     * 初始化jmeter配置文件
     *
     * @return
     */
    public static synchronized void initJmeterProperties() {
        if (IS_INITIALIZED) {
            log.debug("JMeter 环境已初始化，跳过重复执行");
            return;
        }

        String jmeterHome = getJmeterHome();
        String jmeterHomeBin = getJmeterHomeBin();

        try {
            // 加载jmeter配置文件
            JMeterUtils.loadJMeterProperties(jmeterHomeBin + File.separator + "jmeter.properties");

            // 设置JMeter安装目录
            JMeterUtils.setJMeterHome(jmeterHome);

            // 避免中文响应乱码
            JMeterUtils.setProperty("sampleresult.default.encoding", "UTF-8");

            // 初始化本地环境
            JMeterUtils.initLocale();

            IS_INITIALIZED = true;
        } catch (Exception e) {
            log.error("初始化JMeter环境失败", e);
            throw new RuntimeException("JMeter 启动失败", e);
        }
    }

    /**
     * 获取jmeter引擎
     *
     * @return org.apache.jmeter.engine.StandardJMeterEngine JMeter引擎
     */
    public static StandardJMeterEngine getJMeterEngine() {
        // 初始化配置
        initJmeterProperties();
        StandardJMeterEngine jMeterEngine = new StandardJMeterEngine();
        return jMeterEngine;
    }
}
