package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;

import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2021/7/21 22:43
 */
public class InsertSelectRep extends Curd {

    private List<Token> columnList;
    private Curd select;

    public InsertSelectRep(List<Token> columnList, Curd select) {
        this.columnList = columnList;
        this.select = select;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitInsertSelectRep(this);
    }

    public List<Token> getColumnList() {
        return columnList;
    }

    public Curd getSelect() {
        return select;
    }
}
