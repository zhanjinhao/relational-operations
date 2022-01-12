package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @Author ISJINHAO
 * @Date 2021/3/2 19:53
 */
public abstract class Binary extends Curd {

    private final Curd leftCurd;

    private final Token token;

    private final Curd rightCurd;

    public Binary(Curd leftCurd, Token token, Curd rightCurd) {
        this.leftCurd = leftCurd;
        this.token = token;
        this.rightCurd = rightCurd;
    }

    public Curd getLeftCurd() {
        return leftCurd;
    }

    public Token getToken() {
        return token;
    }

    public Curd getRightCurd() {
        return rightCurd;
    }
}
