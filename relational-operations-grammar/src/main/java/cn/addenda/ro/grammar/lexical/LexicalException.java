package cn.addenda.ro.grammar.lexical;

import cn.addenda.ro.grammar.GrammarException;

/**
 * @Author ISJINHAO
 * @Date 2021/2/24 10:05
 */
public class LexicalException extends GrammarException {

    public LexicalException(int errorCode, String errorInfo) {
        super(errorCode, errorInfo);
    }

}
