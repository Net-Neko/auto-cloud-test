package org.zzf.service;

import org.zzf.dto.ReportDTO;
import org.zzf.req.ReportSaveReq;

/**
 * @author 詹泽峰
 * @date 2025/12/31 22:44
 */
public interface ReportService {
    ReportDTO save(ReportSaveReq req);
}
