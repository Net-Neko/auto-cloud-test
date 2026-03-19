package org.zzf.service.Impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.zzf.dto.ReportDTO;
import org.zzf.mapper.ReportMapper;
import org.zzf.model.ReportDO;
import org.zzf.req.ReportSaveReq;
import org.zzf.service.ReportService;
import org.zzf.util.SpringBeanUtil;

/**
 * 压测报告管理服务实现类
 *
 * @author 詹泽峰
 * @date 2025/12/31 22:45
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Override
    public ReportDTO save(ReportSaveReq req) {
        ReportDO reportDO = SpringBeanUtil.copyProperties(req, ReportDO.class);
        reportMapper.insert(reportDO);

        ReportDTO reportDTO = ReportDTO.builder().id(reportDO.getId())
                .projectId(reportDO.getProjectId())
                .name(reportDO.getName()).build();

        return reportDTO;
    }
}
