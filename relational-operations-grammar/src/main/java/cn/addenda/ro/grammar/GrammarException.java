package cn.addenda.ro.grammar;


import cn.addenda.ro.common.exception.ROException;

/**
 * @Author ISJINHAO
 * @Date 2021/7/18 10:51
 */
public class GrammarException extends ROException {

    public GrammarException(int errorCode, String errorInfo) {
        super(errorCode, errorInfo);
    }

    public GrammarException(int errorCode, String errorInfo, Throwable cause) {
        super(errorCode, errorInfo, cause);
    }

}
