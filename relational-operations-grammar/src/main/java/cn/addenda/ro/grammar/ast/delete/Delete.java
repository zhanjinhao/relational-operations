package cn.addenda.ro.grammar.ast.delete;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.delete.visitor.DeleteMetaInfo;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @Author ISJINHAO
 * @Date 2021/4/8 20:38
 */
public class Delete extends Curd {

    private DeleteMetaInfo deleteMetaInfo;

    private Token tableName;

    private Curd whereSeg;

    public Delete(Token tableName, Curd whereSeg) {
        this.tableName = tableName;
        this.whereSeg = whereSeg;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitDelete(this);
    }

    public Token getTableName() {
        return tableName;
    }

    public Curd getWhereSeg() {
        return whereSeg;
    }

    public void setSingleDeleteMetaInfo(DeleteMetaInfo deleteMetaInfo) {
        this.deleteMetaInfo = deleteMetaInfo;
    }
}
