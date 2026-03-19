package org.zzf.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 詹泽峰
 * @date 2026/02/27 16:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseInfoDTO {
    /**
     * 用例id，或者步骤id
     */
    private Long id;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 名称
     */
    private String name;
}
