package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @Author ISJINHAO
 * @Date 2021/3/3 22:30
 */
public class GroupFunction extends Curd {

    private final Token method;

    private final Curd curd;

    public GroupFunction(Token method, Curd curd) {
        this.method = method;
        this.curd = curd;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitGroupFunction(this);
    }

    public Token getMethod() {
        return method;
    }

    public Curd getCurd() {
        return curd;
    }
}
