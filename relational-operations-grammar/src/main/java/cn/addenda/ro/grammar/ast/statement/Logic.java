package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class Logic extends Binary {

    public Logic(Curd leftCurd, Token token,
                 Curd rightCurd) {
        super(leftCurd, token, rightCurd);
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitLogic(this);
    }

}
