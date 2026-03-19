package org.zzf.service.common;

import org.zzf.dto.common.CaseInfoDTO;
import org.zzf.enums.TestTypeEnum;

/**
 * @author 詹泽峰
 * @date 2026/02/27 15:59
 */
public interface ResultSenderService {

    /**
     * 发送测试结果
     *
     * @author: 詹泽峰
     * @date: 2026/2/27 16:44
     * @param caseInfoDTO 用例信息
     * @param testTypeEnum 测试类型
     * @param result 测试结果
     */
    void sendResult(CaseInfoDTO caseInfoDTO, TestTypeEnum testTypeEnum, String result);
}
