package org.zzf.req.stress;

import lombok.Data;

/**
 * 压力测试用例更新请求数据传输对象
 * 用于接收前端传递的压力测试用例更新参数
 *
 * @author 詹泽峰
 * @date 2025/12/17 21:33
 */
@Data
public class StressCaseUpdateReq {

    /**
     * 用例ID
     */
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 模块ID
     */
    private Long moduleId;

    /**
     * 环境ID
     */
    private Long environmentId;

    /**
     * 用例名称
     */
    private String name;

    /**
     * 用例描述
     */
    private String description;

    /**
     * 断言配置
     */
    private String assertion;

    /**
     * 关联参数配置
     */
    private String relation;

    /**
     * 压力源类型
     */
    private String stressSourceType;

    /**
     * 线程组配置
     */
    private String threadGroupConfig;

    /**
     * JMX文件URL地址
     */
    private String jmxUrl;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 请求方法(GET/POST等)
     */
    private String method;

    /**
     * 查询参数
     */
    private String query;

    /**
     * 请求头信息
     */
    private String header;

    /**
     * 请求体内容
     */
    private String body;

    /**
     * 请求体类型
     */
    private String bodyType;
}
