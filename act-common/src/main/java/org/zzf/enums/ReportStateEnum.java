package org.zzf.enums;

/**
 * 压测报告执行状态枚举
 *
 * @author 詹泽峰
 * @date 2026/03/19
 */
public enum ReportStateEnum {

    /**
     * 待执行
     */
    PENDING(0, "待执行"),

    /**
     * 执行中
     */
    EXECUTING(1, "执行中"),

    /**
     * 统计报告
     */
    COUNTING_REPORT(2, "统计报告"),

    /**
     * 执行成功
     */
    EXECUTE_SUCCESS(3, "执行成功"),

    /**
     * 执行失败
     */
    EXECUTE_FAIL(4, "执行失败");

    /**
     * 状态编码（存入数据库）
     */
    private final Integer code;

    /**
     * 状态描述（前端展示/日志打印）
     */
    private final String desc;

    ReportStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // ===================== 通用工具方法（生产必备） =====================
    /**
     * 根据code获取枚举
     */
    public static ReportStateEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ReportStateEnum state : values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }
        return null;
    }

    /**
     * 根据name获取枚举
     */
    public static ReportStateEnum getByName(String name) {
        if (name == null) {
            return null;
        }
        for (ReportStateEnum state : values()) {
            if (state.name().equals(name)) {
                return state;
            }
        }
        return null;
    }

    // ===================== getter =====================
    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
