package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @Author ISJINHAO
 * @Date 2021/8/15 16:16
 */
public class TimeUnit extends Curd {

    private final Curd curd;

    private final Token timeType;

    public TimeUnit(Token timeType, Curd curd) {
        this.timeType = timeType;
        this.curd = curd;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitTimeUnit(this);
    }

    public Curd getCurd() {
        return curd;
    }

    public Token getTimeType() {
        return timeType;
    }
}