package cn.addenda.ro.grammar.ast.retrieve;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.statement.Binary;

/**
 * @author 01395265
 * @date 2021/3/2
 */
public class Select extends Binary {

    private Token allToken;

    private SelectType selectType;

    public Select(Curd leftCurd, Token token, Token allToken, Curd rightCurd) {
        super(leftCurd, token, rightCurd);
        this.allToken = allToken;
    }

    public Select(Curd left) {
        super(left, null, null);
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitSelect(this);
    }

    public void setSelectType(SelectType selectType) {
        this.selectType = selectType;
    }

    public SelectType getSelectType() {
        return selectType;
    }

    public Token getAllToken() {
        return allToken;
    }
}
