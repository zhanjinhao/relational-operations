package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.ast.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author ISJINHAO
 * @Date 2021/3/1 11:35
 */
public abstract class Curd {

    private final AstMetaData astMetaData = new AstMetaData();

    private final CurdPrinter curdPrinter = new CurdPrinter();

    private static final DeepCloneVisitor deepCloneVisitor = new DeepCloneVisitor();

    private static final Map<String, IdentifierFillTNVisitor> tNToIdentifierFillTNMap = new HashMap<>();

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

    public void fillTableName(String tableName) {
        IdentifierFillTNVisitor identifierFillTNVisitor = tNToIdentifierFillTNMap.get(tableName);
        if (identifierFillTNVisitor == null) {
            identifierFillTNVisitor = new IdentifierFillTNVisitor(tableName);
            tNToIdentifierFillTNMap.put(tableName, identifierFillTNVisitor);
        }
        this.accept(identifierFillTNVisitor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(toString().replaceAll("\\s+", " "), o.toString().replaceAll("\\s+", " "));
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString().replaceAll("\\s+", ""));
    }
}
