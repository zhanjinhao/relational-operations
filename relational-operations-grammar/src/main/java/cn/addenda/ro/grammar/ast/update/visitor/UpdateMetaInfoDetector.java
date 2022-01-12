package cn.addenda.ro.grammar.ast.update.visitor;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementAstMetaInfoVisitorForDelegation;
import cn.addenda.ro.grammar.ast.update.Update;

import java.util.Set;

/**
 * @author 01395265
 * @date 2021/4/6
 */
public class UpdateMetaInfoDetector extends UpdateVisitorWithDelegate<Set<Token>> {

    private UpdateMetaInfo updateMetaInfo;

    public UpdateMetaInfoDetector() {
        super.init(new StatementAstMetaInfoVisitorForDelegation(this));
    }

    @Override
    public Set<Token> visitUpdate(Update update) {
        init(update);

        Token tableName = update.getTableName();
        updateMetaInfo.addReferenceTable(tableName);

        Set<Token> assignmentList = update.getAssignmentList().accept(this);
        updateMetaInfo.addReferenceColumnSet(assignmentList);
        Set<Token> whereSeg = update.getWhereSeg().accept(this);
        updateMetaInfo.addReferenceColumnSet(whereSeg);

        return updateMetaInfo.getReferenceColumnSet();
    }


    private void init(Update update) {
        updateMetaInfo = new UpdateMetaInfo(update);
        update.setSingleUpdateMetaInfo(updateMetaInfo);
    }

}
