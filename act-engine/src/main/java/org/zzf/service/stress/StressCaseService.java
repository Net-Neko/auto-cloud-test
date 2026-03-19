package org.zzf.service.stress;

import org.zzf.dto.stress.StressCaseDTO;
import org.zzf.req.stress.StressCaseSaveReq;
import org.zzf.req.stress.StressCaseUpdateReq;

/**
 * 压力测试用例服务接口
 * 定义压力测试用例的核心业务操作规范
 *
 * @author 詹泽峰
 * @date 2025/12/17 21:14
 */
public interface StressCaseService {
    /**
     * 根据项目ID和用例ID查询压力测试用例详情
     *
     * @param projectId 项目ID
     * @param caseId 用例ID
     * @return 压力测试用例DTO对象
     */
    StressCaseDTO findById(Long projectId, Long caseId);

    /**
     * 根据项目ID和用例ID删除压力测试用例
     *
     * @param id 用例ID
     * @param projectId 项目ID
     * @return 删除成功的记录数
     */
    int delete(Long id, Long projectId);

    /**
     * 保存新的压力测试用例
     *
     * @param req 压力测试用例保存请求对象
     * @return 保存成功的记录数
     */
    int save(StressCaseSaveReq req);

    /**
     * 更新压力测试用例信息
     *
     * @param req 压力测试用例更新请求对象
     * @return 更新成功的记录数
     */
    int update(StressCaseUpdateReq req);

    /**
     * 执行指定的压力测试用例
     *
     * @param projectId 项目ID
     * @param caseId 用例ID
     * @return 执行结果状态码
     */
    int execute(Long projectId, Long caseId);
}
