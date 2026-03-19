package org.zzf.dto.stress;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 压力测试用例数据传输对象
 * 用于压力测试用例的数据展示和传输
 *
 * @author 詹泽峰
 * @date 2025/12/17 21:33
 */
@Data
public class StressCaseDTO {
    /**
     * 用例唯一标识符
     */
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 所属接口模块ID
     */
    private Long moduleId;

    /**
     * 环境配置唯一标识符
     */
    private Long environmentId;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 响应断言
     */
    private String assertion;

    /**
     * 可变参数
     */
    private String relation;

    /**
     * 压测类型 [simple jmx]
     */
    private String stressSourceType;

    /**
     * 压测参数
     */
    private String threadGroupConfig;

    /**
     * jmx文件地址
     */
    private String jmxUrl;

    /**
     * 接口路径
     */
    private String path;

    /**
     * 请求方法 [GET POST PUT PATCH DELETE HEAD OPTIONS]
     */
    private String method;

    /**
     * 查询参数
     */
    private String query;

    /**
     * 请求头
     */
    private String header;

    /**
     * 请求体
     */
    private String body;

    /**
     * 请求体格式 [raw form-data json]
     */
    private String bodyType;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;
}
