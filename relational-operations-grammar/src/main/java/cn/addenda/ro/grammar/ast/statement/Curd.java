package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.CurdPrinter;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.DeepCloneVisitor;

/**
 * @Author ISJINHAO
 * @Date 2021/3/1 11:35
 */
public abstract class Curd {

    private final AstMetaData astMetaData = new AstMetaData();

    private final CurdPrinter curdPrinter = new CurdPrinter();

    private static final DeepCloneVisitor deepCloneVisitor = new DeepCloneVisitor();

    public Curd() {
        astMetaData.setCurd(this);
    }

    public abstract <R> R accept(CurdVisitor<R> curdVisitor);

    @Override
    public String toString() {
        return this.accept(curdPrinter);
    }

    public AstMetaData getAstMetaData() {
        return astMetaData;
    }

    public Curd deepClone() {
        return this.accept(deepCloneVisitor);
    }

}
