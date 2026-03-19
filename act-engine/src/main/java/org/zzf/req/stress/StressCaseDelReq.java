package org.zzf.req.stress;

import lombok.Data;

/**
 * 压力测试用例删除请求数据传输对象
 * 用于接收前端传递的压力测试用例删除参数
 *
 * @author 詹泽峰
 * @date 2025/12/18 21:22
 */
@Data
public class StressCaseDelReq {
    /**
     * 用例ID
     */
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;
}
