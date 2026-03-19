package org.zzf.dto.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 项目的数据传输对象
 *
 * @author 詹泽峰
 * @date 2025/12/04 17:21
 */
@Data
public class ProjectDTO {
    /**
     * 项目唯一标识符
     */
    private Long id;

    /**
     * 项目管理员ID
     */
    private Long projectAdmin;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目描述信息
     */
    private String description;

    /**
     * 项目创建时间
     */
    private Date gmtCreate;

    /**
     * 项目最后修改时间
     */
    private Date gmtModified;
}
