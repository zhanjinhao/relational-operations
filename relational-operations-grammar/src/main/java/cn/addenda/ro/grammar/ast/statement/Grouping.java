package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class Grouping extends Curd {

    private Curd curd;

    public Grouping(Curd curd) {
        this.curd = curd;
    }

    public Curd getCurd() {
        return curd;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitGrouping(this);
    }
}
