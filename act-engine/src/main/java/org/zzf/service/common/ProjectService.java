package org.zzf.service.common;

import org.zzf.dto.common.ProjectDTO;
import org.zzf.req.common.ProjectDelReq;
import org.zzf.req.common.ProjectSaveReq;
import org.zzf.req.common.ProjectUpdateReq;

import java.util.List;

/**
 * 项目管理服务接口
 * 定义项目相关的基础操作，包括项目的增删改查等功能
 *
 * @author 詹泽峰
 * @date 2025/12/04 17:06
 */
public interface ProjectService {
    /**
     * 获取项目列表
     *
     * @return 项目列表
     */
    List<ProjectDTO> list();

    /**
     * 保存项目信息
     *
     * @param req 项目保存请求参数对象，包含需要保存的项目信息
     * @return 返回保存操作影响的记录数，通常为1表示保存成功，0表示保存失败
     */
    int save(ProjectSaveReq req);

    /**
     * 更新项目信息
     *
     * @param req 包含项目更新信息的请求对象，不能为空
     * @return 返回受影响的记录数，通常为1表示更新成功，0表示未找到匹配的记录
     */
    int update(ProjectUpdateReq req);

    /**
     * 删除项目信息
     *
     * @param id 欲删除项目ID
     * @return 删除操作影响的记录数，通常为1表示删除成功，0表示删除失败
     */
    int delete(Long id);
}
