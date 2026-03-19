package org.zzf.service.common;

import org.zzf.dto.common.EnvironmentDTO;
import org.zzf.req.common.EnvironmentSaveReq;
import org.zzf.req.common.EnvironmentUpdateReq;

import java.util.List;

/**
 * @author 詹泽峰
 * @date 2025/12/04 21:53
 */
public interface EnvironmentService {

    /**
     * 获取环境列表
     *
     * @param projectId 项目ID
     * @return 环境列表
     */
    List<EnvironmentDTO> list(Long projectId);

    int save(EnvironmentSaveReq req);

    int update(EnvironmentUpdateReq req);

    int delete(Long id, Long projectId);
}
