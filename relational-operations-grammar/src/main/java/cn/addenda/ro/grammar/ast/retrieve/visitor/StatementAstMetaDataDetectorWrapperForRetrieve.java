package cn.addenda.ro.grammar.ast.retrieve.visitor;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.statement.*;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementVisitorForDelegation;

/**
 * @Author ISJINHAO
 * @Date 2022/1/13 22:35
 */
public class StatementAstMetaDataDetectorWrapperForRetrieve extends StatementVisitorForDelegation<AstMetaData> {

    private final StatementAstMetaDataDetector statementAstMetaDataDetector;

    public StatementAstMetaDataDetectorWrapperForRetrieve(CurdVisitor<AstMetaData> client, ROErrorReporter roErrorReporter) {
        super(client);
        setErrorReporter(roErrorReporter);
        statementAstMetaDataDetector = new StatementAstMetaDataDetector(client, roErrorReporter);
    }

    @Override
    public AstMetaData visitWhereSeg(WhereSeg whereSeg) {
        Curd curd = whereSeg.getLogic();
        if (curd instanceof Select) {
            AstMetaData astMetaDataCur = whereSeg.getAstMetaData();
            AstMetaData accept = curd.accept(this);
            astMetaDataCur.getConditionChildren().add(accept);
            accept.setParent(astMetaDataCur);
            return astMetaDataCur;
        } else {
            return statementAstMetaDataDetector.visitWhereSeg(whereSeg);
        }
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

        Curd leftCurd = binary.getLeftCurd();
        AstMetaData leftAccept = leftCurd.accept(this);
        if (leftCurd instanceof Select) {
            astMetaDataCur.getConditionChildren().add(leftAccept);
            leftAccept.setParent(astMetaDataCur);
        } else {
            astMetaDataCur.mergeColumnReference(leftAccept);
        }

        Curd rightCurd = binary.getRightCurd();
        if (rightCurd != null) {
            AstMetaData rightAccept = rightCurd.accept(this);
            if (rightCurd instanceof Select) {
                astMetaDataCur.getConditionChildren().add(rightAccept);
                rightAccept.setParent(astMetaDataCur);
            } else {
                astMetaDataCur.mergeColumnReference(rightAccept);
            }
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        Curd curd = unaryArithmetic.getCurd();
        if (curd instanceof Select) {
            AstMetaData astMetaDataCur = unaryArithmetic.getAstMetaData();
            AstMetaData accept = curd.accept(this);
            astMetaDataCur.getConditionChildren().add(accept);
            return astMetaDataCur;
        } else {
            return statementAstMetaDataDetector.visitUnaryArithmetic(unaryArithmetic);
        }
    }

    @Override
    public AstMetaData visitLiteral(Literal literal) {
        return statementAstMetaDataDetector.visitLiteral(literal);
    }

    @Override
    public AstMetaData visitGrouping(Grouping grouping) {
        return statementAstMetaDataDetector.visitGrouping(grouping);
    }

    @Override
    public AstMetaData visitIdentifier(Identifier identifier) {
        return statementAstMetaDataDetector.visitIdentifier(identifier);
    }

    @Override
    public AstMetaData visitFunction(Function function) {
        return statementAstMetaDataDetector.visitFunction(function);
    }

    @Override
    public AstMetaData visitAssignmentList(AssignmentList assignmentList) {
        return statementAstMetaDataDetector.visitAssignmentList(assignmentList);
    }

    @Override
    public AstMetaData visitTimeInterval(TimeInterval timeInterval) {
        return statementAstMetaDataDetector.visitTimeInterval(timeInterval);
    }

    @Override
    public AstMetaData visitTimeUnit(TimeUnit timeUnit) {
        return statementAstMetaDataDetector.visitTimeUnit(timeUnit);
    }

    @Override
    public AstMetaData visitIsNot(IsNot isNot) {
        return statementAstMetaDataDetector.visitIsNot(isNot);
    }
}
