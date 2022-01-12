package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;

import java.util.List;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class OrderBySeg extends Curd {

    private List<OrderItem> columnList;

    public OrderBySeg(List<OrderItem> columnList) {
        this.columnList = columnList;
    }

    public List<OrderItem> getColumnList() {
        return columnList;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitOrderBySeg(this);
    }

    public static class OrderItem {

        private final Token column;
        private final Token orderType;

        public OrderItem(Token column, Token orderType) {
            this.column = column;
            this.orderType = orderType;
        }

        public OrderItem(Token column) {
            this.column = column;
            orderType = null;
        }

        public Token getColumn() {
            return column;
        }

        public Token getOrderType() {
            return orderType;
        }
    }
}
