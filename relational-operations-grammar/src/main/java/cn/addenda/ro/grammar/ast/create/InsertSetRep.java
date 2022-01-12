package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @Author ISJINHAO
 * @Date 2021/4/5 21:12
 */
public class InsertSetRep extends Curd {

    private Curd entryList;

    public InsertSetRep(Curd entryList) {
        this.entryList = entryList;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitInsertSetRep(this);
    }

    public Curd getEntryList() {
        return entryList;
    }

}
