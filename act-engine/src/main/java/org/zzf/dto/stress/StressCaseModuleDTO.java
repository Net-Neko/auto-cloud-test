package org.zzf.dto.stress;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 压测模块数据传输对象
 *
 * @author 詹泽峰
 * @date 2025/12/21 21:41
 */
@Data
public class StressCaseModuleDTO {
    /**
     * 模块唯一标识符
     */
    private Long id;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 接口模块名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 模块下的压测用例列表
     */
    private List<StressCaseDTO> list;
}
