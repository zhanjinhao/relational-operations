package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @Author ISJINHAO
 * @Date 2021/3/2 19:56
 */
public abstract class Unary extends Curd {

    private Token operator;
    private Curd curd;

    public Unary(Token operator, Curd curd) {
        this.operator = operator;
        this.curd = curd;
    }

    public Token getOperator() {
        return operator;
    }

    public Curd getCurd() {
        return curd;
    }

}
