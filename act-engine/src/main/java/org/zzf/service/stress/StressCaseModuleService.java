package org.zzf.service.stress;

import org.zzf.dto.stress.StressCaseModuleDTO;
import org.zzf.req.stress.StressCaseModuleSaveReq;
import org.zzf.req.stress.StressCaseModuleUpdateReq;

import java.util.List;

/**
 * 压力测试用例模块服务接口
 * 定义压力测试用例模块的核心业务操作规范
 *
 * @author 詹泽峰
 * @date 2025/12/21 21:28
 */
public interface StressCaseModuleService {
    /**
     * 根据项目ID获取压力测试用例模块列表
     *
     * @param projectId 项目ID
     * @return 压力测试用例模块DTO对象列表
     */
    List<StressCaseModuleDTO> list(Long projectId);

    /**
     * 根据项目ID和模块ID查询压力测试用例模块详情
     *
     * @param projectId 项目ID
     * @param moduleId 模块ID
     * @return 压力测试用例模块DTO对象
     */
    StressCaseModuleDTO findById(Long projectId, Long moduleId);

    /**
     * 根据项目ID和模块ID删除压力测试用例模块
     *
     * @param projectId 项目ID
     * @param id 模块ID
     * @return 删除成功的记录数
     */
    int delete(Long projectId, Long id);

    /**
     * 保存新的压力测试用例模块
     *
     * @param req 压力测试用例模块保存请求对象
     * @return 保存成功的记录数
     */
    int save(StressCaseModuleSaveReq req);

    /**
     * 更新压力测试用例模块信息
     *
     * @param req 压力测试用例模块更新请求对象
     * @return 更新成功的记录数
     */
    int update(StressCaseModuleUpdateReq req);
}
