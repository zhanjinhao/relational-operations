package cn.addenda.ro.grammar.ast.create.visitor;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementAstMetaInfoVisitorForDelegation;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author ISJINHAO
 * @Date 2021/4/5 21:47
 */
public class InsertMetaInfoDetector extends InsertVisitorWithDelegate<Set<Token>> {

    private InsertMetaInfo insertMetaInfo;

    public InsertMetaInfoDetector() {
        super.init(new StatementAstMetaInfoVisitorForDelegation(this));
    }

    @Override
    public Set<Token> visitInsert(Insert insert) {
        init(insert);

        Token tableName = insert.getTableName();
        insertMetaInfo.addReferenceTable(tableName);

        Set<Token> referenceColumnSet = insert.getCurd().accept(this);
        Curd onDuplicateUpdate = insert.getOnDuplicateUpdate();
        if(onDuplicateUpdate != null) {
            referenceColumnSet.addAll(insert.getOnDuplicateUpdate().accept(this));
        }
        insertMetaInfo.addReferenceColumnSet(referenceColumnSet);

        return referenceColumnSet;
    }

    @Override
    public Set<Token> visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        return new HashSet<>(insertValuesRep.getColumnList());
    }

    @Override
    public Set<Token> visitInsertSetRep(InsertSetRep insertSetRep) {
        return insertSetRep.getEntryList().accept(this);
    }

    @Override
    public Set<Token> visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        return onDuplicateKey.getCurd().accept(this);
    }

    @Override
    public Set<Token> visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        return new HashSet<>(insertSelectRep.getColumnList());
    }

    private void init(Insert insert) {
        insertMetaInfo = new InsertMetaInfo(insert);
        insert.setSingleInsertMetaInfo(insertMetaInfo);
    }

}
