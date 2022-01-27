package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;

import java.util.List;

/**
 * @author 01395265
 * @date 2021/3/3
 */
public class InCondition extends Curd {

    // in or not
    private Token in;

    private Token identifier;

    // curd list
    private List<Curd> range;

    // select
    private Curd curd;

    // select 场景
    public InCondition(Token in, Token identifier, Curd curd) {
        this.in = in;
        this.identifier = identifier;
        this.curd = curd;
    }

    // list 场景
    public InCondition(Token in, Token identifier, List<Curd> range) {
        this.in = in;
        this.identifier = identifier;
        this.range = range;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitInCondition(this);
    }


    public Token getIn() {
        return in;
    }

    public Token getIdentifier() {
        return identifier;
    }

    public List<Curd> getRange() {
        return range;
    }

    public Curd getCurd() {
        return curd;
    }
}
