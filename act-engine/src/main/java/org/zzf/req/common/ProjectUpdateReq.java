package org.zzf.req.common;

import lombok.Data;

/**
 * 项目更新请求数据传输对象
 *
 * @author 詹泽峰
 * @date 2025/12/04 20:38
 */
@Data
public class ProjectUpdateReq {

    /**
     * 项目唯一标识符
     */
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目描述信息
     */
    private String description;
}
