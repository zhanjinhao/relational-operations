package cn.addenda.ro.grammar.ast.retrieve.visitor;

import cn.addenda.ro.common.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementAstMetaDataDetector;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2022/1/5 20:17
 */
public class SelectAstMetaDataDetector extends SelectVisitorWithDelegate<AstMetaData> {

    public SelectAstMetaDataDetector(ROErrorReporter roErrorReporter) {
        super.init(new StatementAstMetaDataDetector(this, roErrorReporter));
        setErrorReporter(roErrorReporter);
    }

    @Override
    public AstMetaData visitSelect(Select select) {
        AstMetaData astMetaDataCur = select.getAstMetaData();
        AstMetaData leftAstMetaData = select.getLeftCurd().accept(this);
        astMetaDataCur.getChildren().add(leftAstMetaData);

        Curd rightCurd = select.getRightCurd();
        if (rightCurd != null) {
            astMetaDataCur.getChildren().add(rightCurd.accept(this));
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitSingleSelect(SingleSelect singleSelect) {
        AstMetaData astMetaDataCur = singleSelect.getAstMetaData();

        Curd columnSeg = singleSelect.getColumnSeg();
        astMetaDataCur.mergeColumnMap(columnSeg.accept(this));

        Curd tableSeg = singleSelect.getTableSeg();
        astMetaDataCur.mergeColumnMap(tableSeg.accept(this));

        Curd whereSeg = singleSelect.getWhereSeg();
        if (whereSeg != null) {
            astMetaDataCur.mergeColumnMap(whereSeg.accept(this));
        }

        Curd groupBySeg = singleSelect.getGroupBySeg();
        if (groupBySeg != null) {
            astMetaDataCur.mergeColumnMap(groupBySeg.accept(this));
        }

        Curd orderBySeg = singleSelect.getOrderBySeg();
        if (orderBySeg != null) {
            astMetaDataCur.mergeColumnMap(orderBySeg.accept(this));
        }

        Curd limitSeg = singleSelect.getLimitSeg();
        if (limitSeg != null) {
            astMetaDataCur.mergeColumnMap(limitSeg.accept(this));
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitColumnSeg(ColumnSeg columnSeg) {
        AstMetaData astMetaDataCur = columnSeg.getAstMetaData();

        List<Curd> columnRepList = columnSeg.getColumnRepList();
        for (Curd curd : columnRepList) {
            astMetaDataCur.mergeColumnMap(curd.accept(this));
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitColumnRep(ColumnRep columnRep) {
        AstMetaData astMetaDataCur = columnRep.getAstMetaData();

        Curd curd = columnRep.getCurd();

        astMetaDataCur.mergeColumnMap(curd.accept(this));

        return astMetaDataCur;
    }


    @Override
    public AstMetaData visitCaseWhen(CaseWhen caseWhen) {
        AstMetaData astMetaDataCur = caseWhen.getAstMetaData();

        Curd value = caseWhen.getValue();
        astMetaDataCur.mergeColumnMap(value.accept(this));

        List<Curd> conditionList = caseWhen.getConditionList();
        for (Curd curd : conditionList) {
            astMetaDataCur.mergeColumnMap(curd.accept(this));
        }

        List<Curd> resultList = caseWhen.getResultList();
        for (Curd curd : resultList) {
            astMetaDataCur.mergeColumnMap(curd.accept(this));
        }

        Curd defaultValue = caseWhen.getDefaultValue();
        astMetaDataCur.mergeColumnMap(defaultValue.accept(this));

        return astMetaDataCur;
    }


    @Override
    public AstMetaData visitTableSeg(TableSeg tableSeg) {
        AstMetaData astMetaDataCur = tableSeg.getAstMetaData();

        astMetaDataCur.mergeColumnMap(tableSeg.getLeftCurd().accept(this));

        Curd rightCurd = tableSeg.getRightCurd();
        if (rightCurd != null) {
            astMetaDataCur.mergeColumnMap(rightCurd.accept(this));
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitTableRep(TableRep tableRep) {
        AstMetaData astMetaDataCur = tableRep.getAstMetaData();

        Curd curd = tableRep.getCurd();
        if (curd instanceof Select) {
            astMetaDataCur.getChildren().addAll(curd.getAstMetaData().getChildren());
        } else {
            astMetaDataCur.mergeColumnMap(curd.accept(this));
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitInCondition(InCondition inCondition) {

        AstMetaData astMetaData = inCondition.getAstMetaData();

        Curd curd = inCondition.getCurd();

        // select 模式
        if (curd != null) {
            astMetaData.getChildren().add(curd.accept(this));
            return astMetaData;
        }

        List<Curd> range = inCondition.getRange();
        for (Curd item : range) {
            astMetaData.mergeColumnMap(item.accept(this));
        }

        return astMetaData;
    }

    @Override
    public AstMetaData visitExistsCondition(ExistsCondition existsCondition) {
        AstMetaData astMetaData = existsCondition.getAstMetaData();
        astMetaData.getChildren().add(existsCondition.getCurd().accept(this));
        return astMetaData;
    }

    @Override
    public AstMetaData visitGroupBySeg(GroupBySeg groupBySeg) {
        AstMetaData astMetaData = groupBySeg.getAstMetaData();

        List<Token> columnList = groupBySeg.getColumnList();
        for (Token token : columnList) {
            astMetaData.putUndeterminedConditionColumn(String.valueOf(token.getLiteral()));
        }

        Curd having = groupBySeg.getHaving();
        if (having != null) {
            astMetaData.mergeColumnMap(having.accept(this));
        }

        return astMetaData;
    }

    @Override
    public AstMetaData visitOrderBySeg(OrderBySeg orderBySeg) {
        AstMetaData astMetaDataCur = orderBySeg.getAstMetaData();

        List<OrderBySeg.OrderItem> columnList = orderBySeg.getColumnList();
        for (OrderBySeg.OrderItem item : columnList) {
            Token column = item.getColumn();
            astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(column.getLiteral()));
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitLimitSeg(LimitSeg limitSeg) {
        return limitSeg.getAstMetaData();
    }

    @Override
    public AstMetaData visitGroupFunction(GroupFunction groupFunction) {
        AstMetaData astMetaDataCur = groupFunction.getAstMetaData();
        Curd curd = groupFunction.getCurd();
        astMetaDataCur.mergeColumnMap(curd.accept(this));
        return astMetaDataCur;
    }

}
