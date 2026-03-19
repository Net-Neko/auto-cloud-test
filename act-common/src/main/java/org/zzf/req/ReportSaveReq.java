package org.zzf.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测试报告保存请求参数类
 *
 * @author 詹泽峰
 * @date 2025/12/31 22:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportSaveReq {

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 用例id
     */
    private Long caseId;

    /**
     * 类型 UI ,API ,STRESS
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 执行状态
     */
    private String executeState;

    /**
     * 开始时间
     */
    private Long startTime;
}
