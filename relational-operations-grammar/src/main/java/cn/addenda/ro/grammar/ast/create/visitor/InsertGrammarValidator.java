package cn.addenda.ro.grammar.ast.create.visitor;

import cn.addenda.ro.common.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.retrieve.visitor.SelectGrammarValidator;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementGrammarValidator;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2022/1/3 22:45
 */
public class InsertGrammarValidator extends InsertVisitorWithDelegate<Void> {

    public InsertGrammarValidator(ROErrorReporter roErrorReporter) {
        super.init(new StatementGrammarValidator(this, roErrorReporter));
        setErrorReporter(roErrorReporter);
    }

    @Override
    public Void visitInsert(Insert insert) {

        Curd curd = insert.getCurd();
        Token constrict = insert.getConstrict();
        Curd onDuplicateUpdate = insert.getOnDuplicateUpdate();

        if (curd instanceof InsertSelectRep && onDuplicateUpdate != null) {
            error(AstROErrorReporterDelegate.INSERT_insert_VALIDATION);
        }

        if (constrict != null && onDuplicateUpdate != null) {
            error(AstROErrorReporterDelegate.INSERT_insert_VALIDATION);
        }

        if (curd instanceof Select) {

        }
        curd.accept(this);

        if (onDuplicateUpdate != null) {
            onDuplicateUpdate.accept(this);
        }

        return null;
    }

    @Override
    public Void visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        List<Token> columnList = insertValuesRep.getColumnList();
        List<List<Curd>> curdStatementsList = insertValuesRep.getCurdListList();
        int columnSize = columnList.size();
        for (List<Curd> curdList : curdStatementsList) {
            if (curdList.size() != columnSize) {
                error(AstROErrorReporterDelegate.INSERT_insertValuesRep_VALIDATION);
            }
            for (Curd curd : curdList) {
                curd.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitInsertSetRep(InsertSetRep insertSetRep) {
        Curd entryList = insertSetRep.getEntryList();
        if (entryList == null) {
            error(AstROErrorReporterDelegate.INSERT_insertSetRep_VALIDATION);
        } else {
            entryList.accept(this);
        }
        return null;
    }

    @Override
    public Void visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        Curd curdStatement = onDuplicateKey.getCurd();
        if (curdStatement == null) {
            error(AstROErrorReporterDelegate.INSERT_onDuplicateKey_VALIDATION);
        } else {
            curdStatement.accept(this);
        }
        return null;
    }

    @Override
    public Void visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        Curd select = insertSelectRep.getSelect();
        if (select == null) {
            error(AstROErrorReporterDelegate.INSERT_insertSelectRep_VALIDATION);
        } else {
            select.accept(new SelectGrammarValidator(this));
        }
        return null;
    }

}
