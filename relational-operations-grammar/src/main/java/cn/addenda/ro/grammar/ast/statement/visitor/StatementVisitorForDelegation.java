package cn.addenda.ro.grammar.ast.statement.visitor;

import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.AbstractCurdVisitor;
import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.Update;

/**
 * 此类本质是一个delegate
 *
 * @Author ISJINHAO
 * @Date 2021/4/7 18:07
 */
public abstract class StatementVisitorForDelegation<R> extends AbstractCurdVisitor<R> {

    /**
     * StatementVisitorForDelegation 作为一个委托存在，可能会递归到非statement语法，
     * 所以对于非statement语法，需要需要用client去visit语法。
     */
    protected CurdVisitor<R> client;

    protected StatementVisitorForDelegation(CurdVisitor<R> client) {
        super();
        this.client = client;
    }

    @Override
    public final R visitSelect(Select select) {
        return select.accept(client);
    }

    @Override
    public final R visitSingleSelect(SingleSelect singleSelect) {
        return singleSelect.accept(client);
    }

    @Override
    public final R visitColumnSeg(ColumnSeg columnSeg) {
        return columnSeg.accept(client);
    }

    @Override
    public final R visitColumnRep(ColumnRep columnRep) {
        return columnRep.accept(client);
    }

    @Override
    public R visitCaseWhen(CaseWhen caseWhen) {
        return caseWhen.accept(client);
    }

    @Override
    public final R visitTableSeg(TableSeg tableSeg) {
        return tableSeg.accept(client);
    }

    @Override
    public final R visitTableRep(TableRep tableRep) {
        return tableRep.accept(client);
    }

    @Override
    public final R visitInCondition(InCondition inCondition) {
        return inCondition.accept(client);
    }

    @Override
    public final R visitExistsCondition(ExistsCondition existsCondition) {
        return existsCondition.accept(client);
    }

    @Override
    public final R visitGroupBySeg(GroupBySeg groupBySeg) {
        return groupBySeg.accept(client);
    }

    @Override
    public final R visitOrderBySeg(OrderBySeg orderBySeg) {
        return orderBySeg.accept(client);
    }

    @Override
    public final R visitLimitSeg(LimitSeg limitSeg) {
        return limitSeg.accept(client);
    }

    @Override
    public final R visitGroupFunction(GroupFunction groupFunction) {
        return groupFunction.accept(client);
    }

    @Override
    public final R visitInsert(Insert insert) {
        return insert.accept(client);
    }

    @Override
    public final R visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        return insertValuesRep.accept(client);
    }

    @Override
    public final R visitInsertSetRep(InsertSetRep insertSetRep) {
        return insertSetRep.accept(client);
    }

    @Override
    public final R visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        return insertSelectRep.accept(client);
    }

    @Override
    public final R visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        return onDuplicateKey.accept(client);
    }

    @Override
    public R visitUpdate(Update update) {
        return update.accept(client);
    }

    @Override
    public R visitDelete(Delete delete) {
        return delete.accept(client);
    }
}
