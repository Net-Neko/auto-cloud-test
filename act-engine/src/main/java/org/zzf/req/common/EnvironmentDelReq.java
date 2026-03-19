package org.zzf.req.common;

import lombok.Data;

/**
 * 环境删除请求参数类
 *
 * @author 詹泽峰
 * @date 2025/12/10 22:46
 */
@Data
public class EnvironmentDelReq {
    /**
     * 环境ID
     */
    private Long id;

    /**
     * 所属项目ID
     */
    private Long projectId;
}
