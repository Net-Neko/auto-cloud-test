package org.zzf.service.stress.core;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.SamplingStatCalculator;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.zzf.dto.ReportDTO;
import org.zzf.dto.StressSampleResultDTO;
import org.zzf.dto.common.CaseInfoDTO;
import org.zzf.enums.TestTypeEnum;
import org.zzf.model.StressCaseDO;
import org.zzf.service.common.ResultSenderService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 引擎采样结果收集器
 *
 * @author 詹泽峰
 * @date 2026/02/27 17:23
 */
@Slf4j
public class EngineSampleCollector extends ResultCollector {

    private Map<String, SamplingStatCalculator> calculatorMap = new HashMap<>();
    private ResultSenderService resultSenderService;
    private ReportDTO reportDTO;
    private StressCaseDO stressCaseDO;

    public EngineSampleCollector() {
        super();
    }

    public EngineSampleCollector(StressCaseDO stressCaseDO, Summariser summariser, ResultSenderService resultSenderService, ReportDTO reportDTO) {
        super(summariser);
        this.stressCaseDO = stressCaseDO;
        this.resultSenderService = resultSenderService;
        this.reportDTO = reportDTO;
    }

    /**
     * 采样事件发生时的核心处理方法，每次请求执行完成都会触发该方法，是压测数据收集的入口
     *
     * @author: 詹泽峰
     * @date: 2026/3/9 16:06
     * @param event 采样事件对象，包含本次请求的完整结果
     */
    @Override
    public void sampleOccurred(SampleEvent event) {
        // 先调用父类的采样事件处理逻辑
        super.sampleOccurred(event);
        // 获取采样事件中的结果对象
        SampleResult result = event.getResult();
        // 获取采样标签（用于区分不同的采样维度）
        String sampleLabel = result.getSampleLabel();

        // 根据标签从map中获取对应的统计计算器
        SamplingStatCalculator calculator = calculatorMap.get(sampleLabel);
        // 如果该标签的计算器不存在，则新建并添加到map中
        if (calculator == null) {
            calculator = new SamplingStatCalculator(sampleLabel);
            calculator.addSample(result);
            calculatorMap.put(sampleLabel, calculator);
        } else {
            // 如果该标签的计算器已存在，则更新计算器中的数据
            calculator.addSample(result);
        }

        // 封装采样器结果数据
        StressSampleResultDTO sampleResultInfoDTO = StressSampleResultDTO.builder()
                .reportId(reportDTO.getId())
                .sampleTime(result.getTimeStamp())
                .samplerLabel(result.getSampleLabel())
                .samplerCount(calculator.getCount())
                .meanTime(calculator.getMean())
                .minTime(calculator.getMin().intValue())
                .maxTime(calculator.getMax().intValue())
                .errorPercentage(calculator.getErrorPercentage())
                .errorCount(calculator.getErrorCount())
                .requestRate(calculator.getRate())
                .receiveKBPerSecond(calculator.getKBPerSecond())
                .sentKBPerSecond(calculator.getSentKBPerSecond())
                .requestLocation(event.getResult().getUrlAsString())
                .requestHeader(event.getResult().getRequestHeaders())
                .requestBody(event.getResult().getSamplerData())
                .responseCode(event.getResult().getResponseCode())
                .responseHeader(event.getResult().getResponseHeaders())
                .responseData(event.getResult().getResponseDataAsString())
                .build();

        // 处理断言信息（错误信息才处理）
        AssertionResult[] assertionResults = event.getResult().getAssertionResults();
        StringBuilder assertMsg = new StringBuilder();
        if(Objects.nonNull(assertionResults)) {
            for (AssertionResult assertionResult : assertionResults) {
                assertMsg.append("name=").append(assertionResult.getName())
                        .append(",msg=").append(assertionResult.getFailureMessage()).append(",");
            }
        }
        sampleResultInfoDTO.setAssertInfo(assertMsg.toString());

        // 序列化json对象
        String sampleResultInfoJson = JSON.toJSONString(sampleResultInfoDTO);

        // 发送测试结果
        CaseInfoDTO caseInfoDTO = new CaseInfoDTO(stressCaseDO.getId(),stressCaseDO.getModuleId(),stressCaseDO.getName());
        resultSenderService.sendResult(caseInfoDTO, TestTypeEnum.STRESS,sampleResultInfoJson);
    }
}
