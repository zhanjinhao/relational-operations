package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.statement.Binary;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class TableSeg extends Binary {

    // cross or left or right or null
    private Token qualifier;

    // join or ,
//    private Token token;

    /**
     * logic
     */
    private Curd condition;


    public TableSeg(Token qualifier, Curd leftCurd, Token token, Curd rightCurd, Curd condition) {
        super(leftCurd, token, rightCurd);
        this.qualifier = qualifier;
        this.condition = condition;
    }

    public TableSeg(Token qualifier, Curd leftCurd, Token token, Curd rightCurd) {
        super(leftCurd, token, rightCurd);
        this.qualifier = qualifier;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitTableSeg(this);
    }

    public Curd getCondition() {
        return condition;
    }

    public Token getQualifier() {
        return qualifier;
    }

}
