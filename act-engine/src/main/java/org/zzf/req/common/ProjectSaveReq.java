package org.zzf.req.common;

import lombok.Data;

/**
 * 项目保存请求数据传输对象
 *
 * @author 詹泽峰
 * @date 2025/12/04 20:23
 */
@Data
public class ProjectSaveReq {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目描述信息
     */
    private String description;
}
