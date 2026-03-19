package org.zzf.controller.common;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.zzf.util.JsonData;
import org.zzf.req.common.ProjectDelReq;
import org.zzf.req.common.ProjectSaveReq;
import org.zzf.req.common.ProjectUpdateReq;
import org.zzf.service.common.ProjectService;

/**
 * 项目管理控制器类
 *
 * @author 詹泽峰
 * @date 2025/12/04 16:48
 */
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
    @Resource
    private ProjectService projectService;

    /**
     * 获取项目列表
     * 调用项目服务获取所有项目信息并返回成功响应
     *
     * @return JsonData 包含项目列表数据的成功响应对象
     */
    @GetMapping("/list")
    public JsonData list() {
        return JsonData.buildSuccess(projectService.list());
    }

    /**
     * 保存项目信息
     * 处理项目保存请求并返回成功响应
     *
     * @return JsonData 表示保存操作成功的响应对象
     */
    @PostMapping("/save")
    public JsonData save(@RequestBody ProjectSaveReq req) {
        return JsonData.buildSuccess(projectService.save(req));
    }

    /**
     * 更新项目信息
     *
     * @param req 项目更新请求参数对象，包含需要更新的项目信息
     * @return JsonData 响应结果，包含更新操作的结果数据
     */
    @PostMapping("/update")
    public JsonData update(@RequestBody ProjectUpdateReq req) {
        return JsonData.buildSuccess(projectService.update(req));
    }

    /**
     * 删除项目接口
     *
     * @param req 包含要删除项目ID的请求对象
     * @return 返回删除成功与否的JSON数据结果
     */
    @PostMapping("/delete")
    public JsonData delete(@RequestBody ProjectDelReq req) {
        return JsonData.buildSuccess(projectService.delete(req.getId()));
    }
}
