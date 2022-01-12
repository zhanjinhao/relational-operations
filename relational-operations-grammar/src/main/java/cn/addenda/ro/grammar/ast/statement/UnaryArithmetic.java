package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class UnaryArithmetic extends Unary {

    public UnaryArithmetic(Token operator, Curd curd) {
        super(operator, curd);
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitUnaryArithmetic(this);
    }
}
