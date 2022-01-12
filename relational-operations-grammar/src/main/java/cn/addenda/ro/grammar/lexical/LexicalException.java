package cn.addenda.ro.grammar.lexical;

import cn.addenda.ro.grammar.GrammarException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ISJINHAO
 * @Date 2021/2/24 10:05
 */
public class LexicalException extends GrammarException {

    public static final Map<Integer, String> scanExceptionMap = new HashMap<>();

    public LexicalException(int errorCode, String errorInfo) {
        super(errorCode, errorInfo);
    }

    public LexicalException(int errorCode, String errorInfo, Throwable cause) {
        super(errorCode, errorInfo, cause);
    }
}
