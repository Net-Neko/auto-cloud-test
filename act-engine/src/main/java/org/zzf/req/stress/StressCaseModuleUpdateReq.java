package org.zzf.req.stress;

import lombok.Data;

/**
 * 压力测试用例模块更新请求类
 *
 * @author 詹泽峰
 * @date 2025/12/24 12:30
 */
@Data
public class StressCaseModuleUpdateReq {

    /**
     * 模块ID
     */
    private Long id;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 模块名称 - 更新后的模块名称
     */
    private String name;
}
