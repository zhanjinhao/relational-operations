package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @Author ISJINHAO
 * @Date 2021/2/20 21:33
 */
public class Literal extends Curd {

    public Literal(Token value) {
        this.value = value;
    }

    @Override
    public <R> R accept(CurdVisitor<R> expressionVisitor) {
        return expressionVisitor.visitLiteral(this);
    }

    private final Token value;

    public Token getValue() {
        return value;
    }
}