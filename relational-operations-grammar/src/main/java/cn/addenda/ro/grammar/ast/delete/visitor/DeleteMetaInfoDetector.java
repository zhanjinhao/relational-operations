package cn.addenda.ro.grammar.ast.delete.visitor;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementAstMetaInfoVisitorForDelegation;
import cn.addenda.ro.grammar.ast.delete.Delete;

import java.util.Set;

/**
 * @Author ISJINHAO
 * @Date 2021/4/10 9:20
 */
public class DeleteMetaInfoDetector extends DeleteVisitorWithDelegate<Set<Token>> {

    private DeleteMetaInfo deleteMetaInfo;

    public DeleteMetaInfoDetector() {
        super.init(new StatementAstMetaInfoVisitorForDelegation(this));
    }

    @Override
    public Set<Token> visitDelete(Delete delete) {
        init(delete);
        Token tableName = delete.getTableName();
        deleteMetaInfo.addReferenceTable(tableName);
        Set<Token> accept = delete.getWhereSeg().accept(this);
        deleteMetaInfo.addReferenceColumnSet(accept);
        return null;
    }

    private void init(Delete delete) {
        deleteMetaInfo = new DeleteMetaInfo(delete);
        delete.setSingleDeleteMetaInfo(deleteMetaInfo);
    }


}
