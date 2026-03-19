package org.zzf.req.common;

import lombok.Data;

import java.util.Date;

/**
 * 环境保存请求参数类
 *
 * @author 詹泽峰
 * @date 2025/12/04 22:14
 */
@Data
public class EnvironmentSaveReq {
    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 环境名称
     */
    private String name;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 环境域名
     */
    private String domain;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 环境描述
     */
    private String description;

}
