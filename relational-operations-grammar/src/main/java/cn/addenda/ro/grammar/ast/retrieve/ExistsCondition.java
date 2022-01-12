package cn.addenda.ro.grammar.ast.retrieve;


import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.statement.Unary;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class ExistsCondition extends Unary {

    public ExistsCondition(Token operator, Curd curd) {
        super(operator, curd);
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitExistsCondition(this);
    }
}
