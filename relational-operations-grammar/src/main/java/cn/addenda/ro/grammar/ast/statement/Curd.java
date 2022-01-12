package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.CurdPrinter;
import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @Author ISJINHAO
 * @Date 2021/3/1 11:35
 */
public abstract class Curd {

    private final AstMetaData astMetaData = new AstMetaData();

    private final CurdPrinter curdPrinter = new CurdPrinter();

    public abstract <R> R accept(CurdVisitor<R> curdVisitor);

    @Override
    public String toString() {
        return this.accept(curdPrinter);
    }

    public AstMetaData getAstMetaData() {
        return astMetaData;
    }



}
