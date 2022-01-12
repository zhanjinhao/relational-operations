package cn.addenda.ro.grammar.ast.update.visitor;

import cn.addenda.ro.grammar.ast.statement.*;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementVisitorForDelegation;

/**
 * @Author ISJINHAO
 * @Date 2021/4/8 18:08
 */
public abstract class UpdateVisitorWithDelegate<R> extends UpdateVisitor<R> {

    protected StatementVisitorForDelegation<R> delegate;

    protected void init(StatementVisitorForDelegation<R> delegate) {
        this.delegate = delegate;
    }

    @Override
    public R visitWhereSeg(WhereSeg whereSeg) {
        return whereSeg.accept(delegate);
    }

    @Override
    public R visitLogic(Logic logic) {
        return logic.accept(delegate);
    }

    @Override
    public R visitComparison(Comparison comparison) {
        return comparison.accept(delegate);
    }

    @Override
    public R visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        return binaryArithmetic.accept(delegate);
    }

    @Override
    public R visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        return unaryArithmetic.accept(delegate);
    }

    @Override
    public R visitLiteral(Literal literal) {
        return literal.accept(delegate);
    }

    @Override
    public R visitGrouping(Grouping grouping) {
        return grouping.accept(delegate);
    }

    @Override
    public R visitIdentifier(Identifier identifier) {
        return identifier.accept(delegate);
    }

    @Override
    public R visitFunction(Function function) {
        return function.accept(delegate);
    }

    @Override
    public R visitAssignmentList(AssignmentList assignmentList) {
        return assignmentList.accept(delegate);
    }

    @Override
    public R visitTimeInterval(TimeInterval timeInterval) {
        return timeInterval.accept(delegate);
    }

    @Override
    public R visitTimeUnit(TimeUnit timeUnit) {
        return timeUnit.accept(delegate);
    }

    @Override
    public R visitIsNot(IsNot isNot) {
        return isNot.accept(this);
    }
}
