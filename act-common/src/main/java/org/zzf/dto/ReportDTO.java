package org.zzf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 詹泽峰
 * @date 2026/01/02 18:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private Long id;

    private Long projectId;

    private Long caseId;

    private String type;

    private String name;

    private String executeState;

    private String summary;

    private Long startTime;

    private Long endTime;

    private Long expandTime;

    private Long quantity;

    private Long passQuantity;

    private Long failQuantity;

    private Date gmtCreate;

    private Date gmtModified;
}
