package org.zzf.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zzf.dto.ReportDTO;
import org.zzf.req.ReportSaveReq;
import org.zzf.service.ReportService;
import org.zzf.util.JsonData;

/**
 * 测试报告控制器类
 *
 * @author 詹泽峰
 * @date 2025/12/31 16:34
 */
@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    @PostMapping("/save")
    public JsonData save(@RequestBody ReportSaveReq req){
        ReportDTO reportDTO = reportService.save(req);

        return JsonData.buildSuccess(reportDTO);
    }
}
