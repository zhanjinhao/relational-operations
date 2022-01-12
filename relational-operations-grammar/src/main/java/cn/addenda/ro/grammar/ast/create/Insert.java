package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.create.visitor.InsertMetaInfo;

/**
 * @Author ISJINHAO
 * @Date 2021/4/5 21:00
 */
public class Insert extends Curd {

    private InsertType insertType;

    private InsertMetaInfo insertMetaInfo;

    // ignore?
    private Token constrict;

    private Curd onDuplicateUpdate;

    private Token tableName;

    // insertValuesRep | insertSetRep | insertSelectRep
    private Curd curd;

    public Insert(Token constrict, Token tableName, Curd curd, InsertType insertType) {
        this.tableName = tableName;
        this.curd = curd;
        this.constrict = constrict;
        this.insertType = insertType;
    }

    public Insert(Token constrict, Token tableName, Curd curd, Curd onDuplicateUpdate, InsertType insertType) {
        this.tableName = tableName;
        this.curd = curd;
        this.constrict = constrict;
        this.onDuplicateUpdate = onDuplicateUpdate;
        this.insertType = insertType;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitInsert(this);
    }

    public Token getConstrict() {
        return constrict;
    }

    public Curd getOnDuplicateUpdate() {
        return onDuplicateUpdate;
    }

    public InsertMetaInfo getSingleInsertMetaInfo() {
        return insertMetaInfo;
    }

    public void setSingleInsertMetaInfo(InsertMetaInfo insertMetaInfo) {
        this.insertMetaInfo = insertMetaInfo;
    }

    public Token getTableName() {
        return tableName;
    }

    public Curd getCurd() {
        return curd;
    }

    public InsertType getInsertType() {
        return insertType;
    }

}
