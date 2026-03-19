package org.zzf.req.common;

import lombok.Data;

/**
 * 环境更新请求数据传输对象
 * 用于接收前端传递的环境更新参数
 *
 * @author 詹泽峰
 * @date 2025/12/10 22:41
 */
@Data
public class EnvironmentUpdateReq {
    /**
     * 环境唯一标识符
     */
    private Long id;

    /**
     * 所属项目唯一标识符
     */
    private Long projectId;

    /**
     * 环境名称
     */
    private String name;

    /**
     * 访问协议(http/https)
     */
    private String protocol;

    /**
     * 域名地址
     */
    private String domain;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 环境描述信息
     */
    private String description;
}
