package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @Author ISJINHAO
 * @Date 2021/7/26 23:16
 */
public class TimeInterval extends Curd {

    private final Token timeType;

    private final Long interval;

    public TimeInterval(Token timeType, Long interval) {
        this.timeType = timeType;
        this.interval = interval;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitTimeInterval(this);
    }

    public Token getTimeType() {
        return timeType;
    }

    public Long getInterval() {
        return interval;
    }

}
