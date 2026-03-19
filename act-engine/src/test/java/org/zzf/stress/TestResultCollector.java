package org.zzf.stress;

import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.samplers.SampleEvent;

/**
 * @author 詹泽峰
 * @date 2026/02/26 15:17
 */
public class TestResultCollector extends ResultCollector{

    public TestResultCollector() {
        super();
    }

    // 参数类型：摘要器
    public TestResultCollector(Summariser summariser) {
        super(summariser);
    }

    @Override
    public void sampleOccurred(SampleEvent e) {
        // 先执行父类默认的采样收集逻辑
        super.sampleOccurred(e);
        // 打印采样标签（通常是接口名称/请求名称）
        System.out.println("label="+ e.getResult().getSampleLabel());
        // 打印请求头（比如Content-Type、Cookie等发送给服务器的头信息）
        System.out.println("getRequestHeaders="+ e.getResult().getRequestHeaders());
        // 打印响应头（比如服务器返回的Content-Type、Set-Cookie等）
        System.out.println("getResponseHeaders="+ e.getResult().getResponseHeaders());
        // 打印该采样所属的线程组名称（压测时区分不同线程组的请求）
        System.out.println("ThreadGroup="+e.getThreadGroup());
        // 打印响应码（比如200表示成功，404表示资源不存在，500表示服务器错误）
        System.out.println("ResponseCode="+e.getResult().getResponseCode());
        // 打印执行采样的主机名（分布式压测时可区分不同节点）
        System.out.println("Hostname="+e.getHostname());
        // 打印响应内容（以字符串形式，比如接口返回的JSON/HTML等）
        System.out.println("getResponseDataAsString="+e.getResult().getResponseDataAsString());

        // 获取该采样的所有断言结果（JMeter中添加的断言，比如响应码断言、内容断言）
        AssertionResult[] assertionResults = e.getResult().getAssertionResults();
        // 遍历所有断言结果，打印断言名称和失败信息（如果断言失败）
        for (AssertionResult assertionResult : assertionResults) {
            System.out.println("AssertionResult="+assertionResult.getName()+",FailureMessage="+assertionResult.getFailureMessage());
        }
    }
}
