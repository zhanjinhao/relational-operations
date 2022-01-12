package cn.addenda.ro.grammar.ast.delete.visitor;

import cn.addenda.ro.common.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementGrammarValidator;

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
        Curd whereSeg = delete.getWhereSeg();
        if (whereSeg == null) {
            error(AstROErrorReporterDelegate.DELETE_delete_VALIDATION);
        } else {
            whereSeg.accept(this);
        }

        return null;
    }

}
