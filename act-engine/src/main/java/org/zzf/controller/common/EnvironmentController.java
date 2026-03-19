package org.zzf.controller.common;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.zzf.util.JsonData;
import org.zzf.req.common.EnvironmentDelReq;
import org.zzf.req.common.EnvironmentSaveReq;
import org.zzf.req.common.EnvironmentUpdateReq;
import org.zzf.service.common.EnvironmentService;

/**
 * 环境管理控制器类
 *
 * @author 詹泽峰
 * @date 2025/12/04 21:46
 */
@RestController
@RequestMapping("/api/v1/env")
public class EnvironmentController {

    @Resource
    private EnvironmentService environmentService;

    /**
     * 获取环境列表
     *
     * @param projectId 项目ID
     * @return 环境列表数据
     */
    @GetMapping("/list")
    public JsonData list(@RequestParam("projectId")Long projectId) {
        return JsonData.buildSuccess(environmentService.list(projectId));
    }

    /**
     * 保存新的环境配置
     *
     * @param req 环境保存请求对象，包含环境的基本信息
     * @return JsonData 包含保存结果的成功响应数据
     */
    @PostMapping("/save")
    public JsonData save(@RequestBody EnvironmentSaveReq req) {
        return JsonData.buildSuccess(environmentService.save(req));
    }

    /**
     * 更新环境配置
     *
     * @param req 环境更新请求对象，包含待更新环境的完整信息
     * @return JsonData 包含更新结果的成功响应数据
     */
    @PostMapping("/update")
    public JsonData update(@RequestBody EnvironmentUpdateReq req) {
        return JsonData.buildSuccess(environmentService.update(req));
    }

    /**
     * 删除环境配置
     *
     * @param req 环境删除请求对象，包含环境ID和项目ID
     * @return JsonData 包含删除结果的成功响应数据
     */
    @PostMapping("/del")
    public JsonData delete(@RequestBody EnvironmentDelReq req) {
        return JsonData.buildSuccess(environmentService.delete(req.getId(), req.getProjectId()));
    }
}
