package org.zzf.controller.stress;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.zzf.util.JsonData;
import org.zzf.req.stress.StressCaseModuleDelReq;
import org.zzf.req.stress.StressCaseModuleSaveReq;
import org.zzf.req.stress.StressCaseModuleUpdateReq;
import org.zzf.service.stress.StressCaseModuleService;

/**
 * 压力测试用例模块控制器
 * 提供压力测试用例模块的增删改查等基本操作接口
 * @author 詹泽峰
 * @date 2025/12/21 21:22
 */
@RestController
@RequestMapping("/api/v1/stress_case_module")
public class StressCaseModuleController {
    @Resource
    private StressCaseModuleService stressCaseModuleService;

    /**
     * 获取压力测试用例模块列表
     * @param projectId 项目ID
     * @return 模块列表数据
     */
    @GetMapping("/list")
    public JsonData list(@RequestParam("projectId")Long projectId){
        return JsonData.buildSuccess(stressCaseModuleService.list(projectId));
    }

    /**
     * 根据ID查找压力测试用例模块详情
     * @param projectId 项目ID
     * @param moduleId 模块ID
     * @return 模块详情数据
     */
    @GetMapping("/find")
    public JsonData findById(@RequestParam("projectId") Long projectId, @RequestParam("moduleId") Long moduleId){
        return JsonData.buildSuccess(stressCaseModuleService.findById(projectId,moduleId));
    }

    /**
     * 删除压力测试用例模块
     * @param req 删除请求参数
     * @return 删除结果
     */
    @PostMapping("/del")
    public JsonData delete(@RequestBody StressCaseModuleDelReq req){
        return JsonData.buildSuccess(stressCaseModuleService.delete(req.getProjectId(),req.getId()));
    }

    /**
     * 保存压力测试用例模块
     * @param req 保存请求参数
     * @return 保存结果
     */
    @PostMapping("/save")
    public JsonData save(@RequestBody StressCaseModuleSaveReq req){
        return JsonData.buildSuccess(stressCaseModuleService.save(req));
    }

    /**
     * 更新压力测试用例模块
     * @param req 更新请求参数
     * @return 更新结果
     */
    @PostMapping("/update")
    public JsonData update(@RequestBody StressCaseModuleUpdateReq req){
        return JsonData.buildSuccess(stressCaseModuleService.update(req));
    }
}
