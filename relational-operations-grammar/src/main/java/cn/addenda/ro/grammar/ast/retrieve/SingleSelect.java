package cn.addenda.ro.grammar.ast.retrieve;


import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.retrieve.visitor.SingleSelectMetaInfo;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class SingleSelect extends Curd {

    private SingleSelectMetaInfo singleSelectMetaInfo;

    private SingleSelectType singleSelectType = SingleSelectType.UNDETERMINED;

    /**
     * 表示此表是什么类型的表：顶层的select、inCondition、existsCondition或真实表
     */
    private Curd parentCurd;

    private Curd columnSeg;

    private Curd tableSeg;

    private Curd whereSeg;

    private Curd groupBySeg;

    private Curd orderBySeg;

    private Curd limitSeg;

    public SingleSelect(Curd columnSeg, Curd tableSeg, Curd whereSeg,
                        Curd groupBySeg, Curd orderBySeg, Curd limitSeg) {
        this.columnSeg = columnSeg;
        this.tableSeg = tableSeg;
        this.whereSeg = whereSeg;
        this.groupBySeg = groupBySeg;
        this.orderBySeg = orderBySeg;
        this.limitSeg = limitSeg;
    }

    public Object getParentSelectStatement() {
        return parentCurd;
    }

    public void setParentSelectStatement(Curd parentCurd) {
        this.parentCurd = parentCurd;
    }

    public SingleSelectType getSingleSelectType() {
        return singleSelectType;
    }

    public void setSingleSelectType(SingleSelectType singleSelectType) {
        this.singleSelectType = singleSelectType;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitSingleSelect(this);
    }

    public Curd getColumnSeg() {
        return columnSeg;
    }

    public Curd getTableSeg() {
        return tableSeg;
    }

    public Curd getWhereSeg() {
        return whereSeg;
    }

    public Curd getGroupBySeg() {
        return groupBySeg;
    }

    public Curd getOrderBySeg() {
        return orderBySeg;
    }

    public Curd getLimitSeg() {
        return limitSeg;
    }

    public void setAstMetaInfo(SingleSelectMetaInfo singleSelectMetaInfo) {
        this.singleSelectMetaInfo = singleSelectMetaInfo;
    }
    public SingleSelectMetaInfo getSingleSelectAstMetaInfo() {
        return singleSelectMetaInfo;
    }
}
