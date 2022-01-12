package cn.addenda.ro.grammar.ast.retrieve;


import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.statement.Unary;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class ColumnRep extends Unary {

    /**
     * @param curd 真实的表
     * @param operator  别名
     */
    public ColumnRep(Curd curd, Token operator) {
        super(operator, curd);
    }

    public ColumnRep(Curd curd) {
        super(null, curd);
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitColumnRep(this);
    }

}
