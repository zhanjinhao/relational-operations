package cn.addenda.ro.grammar.ast.update.visitor;

import cn.addenda.ro.common.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementGrammarValidator;
import cn.addenda.ro.grammar.ast.update.Update;

/**
 * @Author ISJINHAO
 * @Date 2022/1/3 11:57
 */
public class UpdateGrammarValidator extends UpdateVisitorWithDelegate<Void> {

    public UpdateGrammarValidator(ROErrorReporter roErrorReporter) {
        super.init(new StatementGrammarValidator(this, roErrorReporter));
        setErrorReporter(roErrorReporter);
    }

    @Override
    public Void visitUpdate(Update update) {
        Curd assignmentList = update.getAssignmentList();
        if (assignmentList == null) {
            error(AstROErrorReporterDelegate.UPDATE_update_VALIDATION);
        } else {
            assignmentList.accept(this);
        }

        Curd whereSeg = update.getWhereSeg();
        if (whereSeg != null) {
            whereSeg.accept(this);
        }
        return null;
    }

}
