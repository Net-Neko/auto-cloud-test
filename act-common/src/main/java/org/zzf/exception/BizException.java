package org.zzf.exception;

import lombok.Data;
import org.zzf.enums.BizCodeEnum;

/**
 * 全局异常处理
 *
 * @author 詹泽峰
 * @date 2025/11/20 16:49
 */
@Data
public class BizException extends RuntimeException {
    private int code;
    private String msg;
    private String detail;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public BizException(BizCodeEnum bizCodeEnum) {
        super(bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMessage();
    }

    public BizException(BizCodeEnum bizCodeEnum, Exception e) {
        super(bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMessage();
        this.detail = e.toString();
    }
}
