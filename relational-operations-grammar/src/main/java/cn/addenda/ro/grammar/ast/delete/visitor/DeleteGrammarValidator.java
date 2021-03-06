package cn.addenda.ro.grammar.ast.delete.visitor;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementGrammarValidator;
import cn.addenda.ro.grammar.lexical.token.Token;

/**
 * @Author ISJINHAO
 * @Date 2022/1/3 22:40
 */
public class DeleteGrammarValidator extends DeleteVisitorWithDelegate<Void> {

    public DeleteGrammarValidator(ROErrorReporter roErrorReporter) {
        super.init(new StatementGrammarValidator(this, roErrorReporter));
        setErrorReporter(roErrorReporter);
    }

    @Override
    public Void visitDelete(Delete delete) {
        Token tableName = delete.getTableName();
        if (tableName == null) {
            error(AstROErrorReporterDelegate.DELETE_delete_VALIDATION);
        }

        Curd whereSeg = delete.getWhereSeg();
        if (whereSeg != null) {
            whereSeg.accept(this);
        }

        return null;
    }

}
