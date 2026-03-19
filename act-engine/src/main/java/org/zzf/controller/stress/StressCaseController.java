package org.zzf.controller.stress;

import org.springframework.web.bind.annotation.*;
import org.zzf.util.JsonData;
import org.zzf.req.stress.StressCaseDelReq;
import org.zzf.req.stress.StressCaseSaveReq;
import org.zzf.req.stress.StressCaseUpdateReq;
import org.zzf.service.stress.StressCaseService;

/**
 * 压力测试用例控制器
 * 提供压力测试用例的RESTful API接口，包括增删改查和执行功能
 *
 * @author 詹泽峰
 * @date 2025/12/17 20:21
 */
@RestController
@RequestMapping("/api/v1/stress_case")
public class StressCaseController {

    private StressCaseService stressCaseService;

    /**
     * 根据项目ID和用例ID查询压力测试用例详情
     *
     * @param projectId 项目ID
     * @param caseId 用例ID
     * @return 包含压力测试用例信息的JsonData对象
     */
    @GetMapping("/find")
    public JsonData findById(Long projectId, Long caseId) {
        return JsonData.buildSuccess(stressCaseService.findById(projectId, caseId));
    }

    /**
     * 删除指定的压力测试用例
     *
     * @param req 包含用例ID和项目ID的删除请求对象
     * @return 删除结果的JsonData对象
     */
    @PostMapping("/del")
    public JsonData delete(@RequestBody StressCaseDelReq req) {
        return JsonData.buildSuccess(stressCaseService.delete(req.getId(), req.getProjectId()));
    }

    /**
     * 保存新的压力测试用例
     * 注意：此处URL路径应与delete方法区分，避免冲突
     *
     * @param req 压力测试用例保存请求对象
     * @return 保存结果的JsonData对象
     */
    @PostMapping("/save")
    public JsonData save(@RequestBody StressCaseSaveReq req) {
        return JsonData.buildSuccess(stressCaseService.save(req));
    }

    /**
     * 更新压力测试用例信息
     *
     * @param req 压力测试用例更新请求对象
     * @return 更新结果的JsonData对象
     */
    @PostMapping("/update")
    public JsonData update(@RequestBody StressCaseUpdateReq req) {
        return JsonData.buildSuccess(stressCaseService.update(req));
    }

    /**
     * 执行指定的压力测试用例
     *
     * @param projectId 项目ID
     * @param caseId 用例ID
     * @return 执行结果的JsonData对象
     */
    @GetMapping("/execute")
    public JsonData execute(@RequestParam("projectId") Long projectId, @RequestParam("id") Long caseId) {
        stressCaseService.execute(projectId, caseId);
        return JsonData.buildSuccess();
    }
}
