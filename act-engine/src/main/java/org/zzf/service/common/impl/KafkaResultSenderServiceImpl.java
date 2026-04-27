package org.zzf.service.common.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zzf.dto.common.CaseInfoDTO;
import org.zzf.enums.TestTypeEnum;
import org.zzf.service.common.ResultSenderService;

/**
 * @author 詹泽峰
 * @date 2026/02/27 17:19
 */
@Service
@Slf4j
public class KafkaResultSenderServiceImpl implements ResultSenderService {
    @Override
    public void sendResult(CaseInfoDTO caseInfoDTO, TestTypeEnum testTypeEnum, String result) {
        // TODO 发送测试结果逻辑待完善
    }
}
