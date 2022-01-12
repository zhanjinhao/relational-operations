package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @author 01395265
 * @date 2021/4/6
 */
public class OnDuplicateKey extends Curd {

    private Curd curd;

    public OnDuplicateKey(Curd curd) {
        this.curd = curd;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitOnDuplicateKey(this);
    }

    public Curd getCurd() {
        return curd;
    }
}
