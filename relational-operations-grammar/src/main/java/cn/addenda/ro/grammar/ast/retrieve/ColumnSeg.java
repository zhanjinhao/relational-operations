package cn.addenda.ro.grammar.ast.retrieve;


import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;

import java.util.List;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class ColumnSeg extends Curd {

    private Token restriction;

    private List<Curd> columnRepList;

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitColumnSeg(this);
    }

    public ColumnSeg(Token restriction, List<Curd> columnRepList) {
        this.restriction = restriction;
        this.columnRepList = columnRepList;
    }

    public Token getRestriction() {
        return restriction;
    }

    public List<Curd> getColumnRepList() {
        return columnRepList;
    }
}
