package org.zzf.req.stress;

import lombok.Data;

/**
 * 压力测试用例模块保存请求类
 *
 * @author 詹泽峰
 * @date 2025/12/21 21:49
 */
@Data
public class StressCaseModuleSaveReq {

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 模块名称
     */
    private String name;
}
