package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class TableRep extends Curd {

    private Curd curd;
    private Token alias;

    /**
     * operator是别名，curd 是真实表
     */
    public TableRep(Curd curd, Token alias) {
        this.curd = curd;
        this.alias = alias;
    }

    /**
     * selectStatement是真实表
     */
    public TableRep(Curd curd) {
        this.curd = curd;
    }


    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitTableRep(this);
    }

    public Curd getCurd() {
        return curd;
    }

    public Token getAlias() {
        return alias;
    }
}
