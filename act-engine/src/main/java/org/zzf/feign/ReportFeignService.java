package org.zzf.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zzf.req.ReportSaveReq;
import org.zzf.req.ReportUpdateReq;
import org.zzf.util.JsonData;

/**
 * @author 詹泽峰
 * @date 2026/01/11 16:07
 */
@FeignClient("data.service")
public interface ReportFeignService {
    /**
     * 初始化测试报告接口
     */
    @PostMapping("/api/v1/report/save")
    JsonData save(@RequestBody ReportSaveReq req);



    /**
     * 更新测试报告接口
     */
    @PostMapping("/api/v1/report/update")
    void update(@RequestBody ReportUpdateReq req);
}
