package org.zzf.req.stress;

import lombok.Data;

/**
 * 压力测试用例模块删除请求类
 *
 * @author 詹泽峰
 * @date 2025/12/24 11:45
 */
@Data
public class StressCaseModuleDelReq {

    /**
     * 模块ID
     */
    private Long id;

    /**
     * 所属项目ID，用于验证权限和范围
     */
    private Long projectId;
}
