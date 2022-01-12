package cn.addenda.ro.core;

import cn.addenda.ro.common.exception.ROException;

/**
 * @Author ISJINHAO
 * @Date 2021/4/11 16:57
 */
public class OperatorException extends ROException {

    public OperatorException(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public OperatorException(String message) {
        super(0, message);
    }

}
