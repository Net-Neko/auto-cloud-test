package org.zzf.dto.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 环境数据传输对象
 *
 * @author 詹泽峰
 * @date 2025/12/04 21:57
 */
@Data
public class EnvironmentDTO {

    /**
     * 主键ID
     */
    private Long id;

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
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;
}
