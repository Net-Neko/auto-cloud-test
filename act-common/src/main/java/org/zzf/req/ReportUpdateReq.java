package org.zzf.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 詹泽峰
 * @date 2026/02/21 12:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportUpdateReq {

    /**
     * 测试报告id
     */
    private Long id;

    /**
     * 执行状态
     */
    private String executeState;


    /**
     * 结束时间
     */
    private Long endTime;

}
