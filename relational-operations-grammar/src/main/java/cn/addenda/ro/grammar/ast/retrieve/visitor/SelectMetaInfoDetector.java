package cn.addenda.ro.grammar.ast.retrieve.visitor;

import cn.addenda.ro.common.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.statement.Function;
import cn.addenda.ro.grammar.ast.statement.Identifier;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementAstMetaInfoVisitorForDelegation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 最终的目的是找出来可运行的 Select 及其对应的环境。本质是计算 singleSelect 对集合和字段的引用情况。
 *
 * @Author ISJINHAO
 * @Date 2021/3/10 19:24
 */
public class SelectMetaInfoDetector extends SelectVisitorWithDelegate<Set<Token>> {

    private int depth = -1;

    private int order = -1;

    private SingleSelectMetaInfo singleSelectMetaInfo = new SingleSelectMetaInfo(depth, order);

    public SelectMetaInfoDetector(ROErrorReporter errorReporter) {
        super.init(new StatementAstMetaInfoVisitorForDelegation(this));
        setErrorReporter(errorReporter);
    }

    @Override
    public synchronized Set<Token> visitSelect(Select select) {
        return delegate.visitBinary(select);
    }


    @Override
    public Set<Token> visitSingleSelect(SingleSelect singleSelect) {
        pushStack(singleSelect);

        Set<Token> resultColumns = singleSelect.getColumnSeg().accept(this);
        singleSelectMetaInfo.addResultColumnSet(resultColumns);

        Curd tableSeg = singleSelect.getTableSeg();
        if (tableSeg != null) {
            Set<Token> referenceTables = tableSeg.accept(this);
            singleSelectMetaInfo.addReferenceTableSet(referenceTables);
        }

        Curd whereSeg = singleSelect.getWhereSeg();
        if (whereSeg != null) {
            Set<Token> accept = whereSeg.accept(this);
            if (accept != null) {
                singleSelectMetaInfo.addReferenceColumnSet(accept);
            }
        }

        Curd groupBySeg = singleSelect.getGroupBySeg();
        if (groupBySeg != null) {
            Set<Token> accept = groupBySeg.accept(this);
            if (accept != null) {
                singleSelectMetaInfo.addReferenceColumnSet(accept);
            }
        }

        Curd orderBySeg = singleSelect.getOrderBySeg();
        if (orderBySeg != null) {
            Set<Token> accept = orderBySeg.accept(this);
            if (accept != null) {
                singleSelectMetaInfo.addReferenceColumnSet(accept);
            }
        }

        popStack();
        expandSingleSelectAstMetaInfo(singleSelect);
        return new HashSet<>();
    }

    private static final Token STAR_TOKEN = new Token(TokenType.STAR, "*");

    private void expandSingleSelectAstMetaInfo(SingleSelect singleSelect) {
        SingleSelectMetaInfo astMetaInfo = singleSelect.getSingleSelectAstMetaInfo();
        Set<Token> referenceColumnSet = astMetaInfo.getReferenceColumnSet();
        Set<Token> returnColumnSet = astMetaInfo.getReturnColumnSet();
        Set<Token> referenceTableSet = astMetaInfo.getReferenceTableSet();

        Token tableName = null;
        boolean accurate = false;
        if (referenceTableSet.size() == 1) {
            tableName = (Token) referenceTableSet.toArray()[0];
            accurate = true;
        }
        if (accurate && referenceColumnSet.remove(STAR_TOKEN)) {
            Token newStar = new Token(TokenType.IDENTIFIER, tableName.getLiteral() + ".*");
            referenceColumnSet.add(newStar);
        }
        if (accurate && returnColumnSet.remove(STAR_TOKEN)) {
            Token newStar = new Token(TokenType.IDENTIFIER, tableName.getLiteral() + ".*");
            returnColumnSet.add(newStar);
        }
    }

    private void pushStack(SingleSelect singleSelect) {
        depth++;
        order++;
        SingleSelectMetaInfo parent = this.singleSelectMetaInfo;
        singleSelectMetaInfo = new SingleSelectMetaInfo(singleSelect, parent, depth, order);
        parent.addChild(singleSelectMetaInfo);
        singleSelect.setAstMetaInfo(singleSelectMetaInfo);
    }

    /**
     * @return 返回的是添加returnColumn，side-effect是添加innerColumn。
     */
    @Override
    public Set<Token> visitColumnRep(ColumnRep columnRep) {
        Set<Token> returnColumnSet = new HashSet<>();

        Curd curd = columnRep.getCurd();
        if (curd == null) {
            error(AstROErrorReporterDelegate.SELECT_columnRep_PARSE);
            return null;
        }

        Token operator = columnRep.getOperator();
        Set<Token> accept = curd.accept(this);
        if (accept == null) {
            error(AstROErrorReporterDelegate.SELECT_columnRep_PARSE);
            return null;
        }
        if (curd instanceof Function) {
            return returnColumnSet;
        }

        // 最多能接受一列
        accept.forEach(item -> {
            if (!item.checkSimplePrimary()) {
                singleSelectMetaInfo.addReferenceColumn(item);
                returnColumnSet.add(item);
            }
        });

        if (operator != null) {
            // 设置别名
            singleSelectMetaInfo.putColumnAlias(curd, operator);
        }
        return returnColumnSet;
    }

    private void popStack() {
        singleSelectMetaInfo = singleSelectMetaInfo.getParent();
        depth--;
    }

    /**
     * @return 返回的是returnColumn。
     */
    @Override
    public Set<Token> visitColumnSeg(ColumnSeg columnSeg) {
        Set<Token> columns = new HashSet<>();
        List<Curd> columnRepList = columnSeg.getColumnRepList();
        for (Curd curd : columnRepList) {
            Set<Token> accept = curd.accept(this);
            // ColumnRep 返回的Set只会存在一个值。
            if (accept.size() == 1) {
                Token token = (Token) accept.toArray()[0];
                if (TokenType.STAR.equals(token.getType()) && columns.contains(token)) {
                    error(AstROErrorReporterDelegate.SELECT_columnSeg_VALIDATION);
                }
                columns.add(token);
            }
        }
        return columns;
    }

    /**
     * @return 返回的是引用的表。
     */
    @Override
    public Set<Token> visitTableSeg(TableSeg tableSeg) {
        // 表的连接条件是innerColumn
        Curd condition = tableSeg.getCondition();
        if (condition != null) {
            Set<Token> accept = condition.accept(this);
            singleSelectMetaInfo.addReferenceColumnSet(accept);
        }
        return delegate.visitBinary(tableSeg);
    }

    /**
     * 三种情况：
     * 1、Identifier Identifier：前者是真实表，后者是表名。
     * 2、Identifier：真实表。
     * 3、SingleSelectValue Identifier：前者是真实表，后者是表名。
     * 在第一种情况下，前者是引用的表，后者是select语句使用的表。这种情况下，和第三种情况一样，前者被认为是临时表。
     *
     * @return 返回的是引用的表
     */
    @Override
    public Set<Token> visitTableRep(TableRep tableRep) {
        Set<Token> tokenList = new HashSet<>();
        Curd curd = tableRep.getCurd();
        Token alias = tableRep.getAlias();

        // Identifier Identifier
        if ((curd instanceof Identifier) && alias != null) {
            Token tableName = ((Identifier) curd).getName();
            singleSelectMetaInfo.putTempTable(curd, alias);
            tokenList.add(tableName);
        }
        // SelectValue Identifier
        else if ((curd instanceof SingleSelect) && alias != null) {
            singleSelectMetaInfo.putTempTable(curd, alias);
            tokenList.add(alias);
            curd.accept(this);
        }
        // Identifier
        else if (curd instanceof Identifier) {
            Token tableName = ((Identifier) curd).getName();
            tokenList.add(tableName);
        } else {
            error(AstROErrorReporterDelegate.SELECT_tableRep_PARSE);
        }
        return tokenList;
    }


    @Override
    public Set<Token> visitInCondition(InCondition inCondition) {
        Set<Token> tokenList = new HashSet<>();

        // 列名要存起来
        Token identifier = inCondition.getIdentifier();
        tokenList.add(identifier);

        // singleSelect要递归
        Curd curd = inCondition.getCurd();
        if (curd instanceof Select) {
            curd.accept(this);
        }
        return tokenList;
    }

    @Override
    public Set<Token> visitExistsCondition(ExistsCondition existsCondition) {
        // ExistsCondition 的 operator 存的是 exists 或 not exists

        Curd curd = existsCondition.getCurd();
        curd.accept(this);

        return new HashSet<>();
    }

    /**
     * 返回的是 innerColumns
     *
     * @param groupBySeg
     */
    @Override
    public Set<Token> visitGroupBySeg(GroupBySeg groupBySeg) {
        Set<Token> set = new HashSet<>(groupBySeg.getColumnList());
        Curd having = groupBySeg.getHaving();
        if (having != null) {
            set.addAll(having.accept(this));
        }
        return set;
    }

    /**
     * 返回的是 innerColumns
     *
     * @param orderBySeg
     */
    @Override
    public Set<Token> visitOrderBySeg(OrderBySeg orderBySeg) {
        List<OrderBySeg.OrderItem> columnList = orderBySeg.getColumnList();
        return columnList.stream().map(OrderBySeg.OrderItem::getColumn).collect(Collectors.toSet());
    }

    @Override
    public Set<Token> visitLimitSeg(LimitSeg limitSeg) {
        return new HashSet<>();
    }

    @Override
    public Set<Token> visitGroupFunction(GroupFunction groupFunction) {
        Set<Token> set = new HashSet<>();
        Curd curd = groupFunction.getCurd();
        set.addAll(curd.accept(this));
        return set;
    }

    @Override
    public Set<Token> visitCaseWhen(CaseWhen caseWhen) {
        Curd value = caseWhen.getValue();
        HashSet<Token> tokens = new HashSet<>();
        Set<Token> accept = value.accept(this);
        if (accept != null) {
            tokens.addAll(accept);
        }

        Curd defaultValue = caseWhen.getDefaultValue();
        if (defaultValue != null) {
            accept = defaultValue.accept(this);
            if (accept != null) {
                tokens.addAll(accept);
            }
        }

        List<Curd> conditionList = caseWhen.getConditionList();
        List<Curd> resultList = caseWhen.getResultList();
        int size = conditionList.size();
        for (int i = 0; i < size; i++) {
            accept = conditionList.get(i).accept(this);
            if (accept != null) {
                tokens.addAll(accept);
            }
            accept = resultList.get(i).accept(this);
            if (accept != null) {
                tokens.addAll(accept);
            }
        }

        return tokens;
    }

}
