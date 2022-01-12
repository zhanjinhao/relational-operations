package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class LimitSeg extends Curd {

    // 需要取的值
    private final Token num;

    // 跳过的数量
    private final Token offset;

    public LimitSeg(Token num, Token offset) {
        this.num = num;
        this.offset = offset;
    }

    public LimitSeg(Token num) {
        this.num = num;
        this.offset = null;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitLimitSeg(this);
    }

    public Token getNum() {
        return num;
    }

    public Token getOffset() {
        return offset;
    }

}
