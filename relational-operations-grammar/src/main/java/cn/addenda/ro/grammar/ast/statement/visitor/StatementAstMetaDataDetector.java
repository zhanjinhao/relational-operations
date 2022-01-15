package cn.addenda.ro.grammar.ast.statement.visitor;

import cn.addenda.ro.common.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.statement.*;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2022/1/5 17:30
 */
public class StatementAstMetaDataDetector extends StatementVisitorForDelegation<AstMetaData> {

    public StatementAstMetaDataDetector(CurdVisitor<AstMetaData> client, ROErrorReporter roErrorReporter) {
        super(client);
        setErrorReporter(roErrorReporter);
    }

    // ---------------------------------------------------------
    //  Statement涉及到的字段都先认为是条件列，具体是什么由client决定
    // ---------------------------------------------------------

    @Override
    public AstMetaData visitWhereSeg(WhereSeg whereSeg) {
        AstMetaData astMetaDataCur = whereSeg.getAstMetaData();
        Curd logic = whereSeg.getLogic();
        astMetaDataCur.mergeColumnMap(logic.accept(this));
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitLogic(Logic logic) {
        return visitBinary(logic);
    }

    @Override
    public AstMetaData visitComparison(Comparison comparison) {
        return visitBinary(comparison);
    }

    @Override
    public AstMetaData visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        return visitBinary(binaryArithmetic);
    }

    @Override
    public AstMetaData visitBinary(Binary binary) {
        AstMetaData astMetaDataCur = binary.getAstMetaData();
        astMetaDataCur.setCurd(binary);

        Curd leftCurd = binary.getLeftCurd();
        astMetaDataCur.mergeColumnMap(leftCurd.accept(this));

        Curd rightCurd = binary.getRightCurd();
        if (rightCurd != null) {
            astMetaDataCur.mergeColumnMap(rightCurd.accept(this));
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        AstMetaData astMetaDataCur = unaryArithmetic.getAstMetaData();
        astMetaDataCur.mergeColumnMap(unaryArithmetic.getCurd().accept(this));
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitLiteral(Literal literal) {
        return literal.getAstMetaData();
    }

    @Override
    public AstMetaData visitGrouping(Grouping grouping) {
        AstMetaData astMetaDataCur = grouping.getAstMetaData();

        AstMetaData astMetaData = grouping.getCurd().accept(this);
        astMetaDataCur.mergeColumnMap(astMetaData);
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitIdentifier(Identifier identifier) {
        AstMetaData astMetaDataCur = identifier.getAstMetaData();

        Token value = identifier.getName();
        if (TokenType.IDENTIFIER.equals(value.getType()) || TokenType.STAR.equals(value.getType())) {
            astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(value.getLiteral()));
        }
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitFunction(Function function) {
        AstMetaData astMetaDataCur = function.getAstMetaData();
        List<Curd> parameterList = function.getParameterList();
        if (parameterList != null) {
            for (Curd curd : parameterList) {
                astMetaDataCur.mergeColumnMap(curd.accept(this));
            }
        }
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitAssignmentList(AssignmentList assignmentList) {
        AstMetaData astMetaDataCur = assignmentList.getAstMetaData();
        List<AssignmentList.Entry> entryList = assignmentList.getEntryList();
        for (AssignmentList.Entry entry : entryList) {
            Token columnName = entry.getColumnName();
            astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(columnName.getLiteral()));
            astMetaDataCur.mergeColumnMap(entry.getValue().accept(this));
        }
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitTimeInterval(TimeInterval timeInterval) {
        return timeInterval.getAstMetaData();
    }

    @Override
    public AstMetaData visitTimeUnit(TimeUnit timeUnit) {
        return timeUnit.getAstMetaData();
    }

    @Override
    public AstMetaData visitIsNot(IsNot isNot) {
        return isNot.getAstMetaData();
    }

}
