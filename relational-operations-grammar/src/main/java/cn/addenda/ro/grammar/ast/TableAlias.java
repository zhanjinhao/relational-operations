package cn.addenda.ro.grammar.ast;

import java.util.Objects;

/**
 * @Author ISJINHAO
 * @Date 2022/1/7 11:21
 */
public class TableAlias {
    private String origin;
    private String alias;

    public TableAlias(String origin, String alias) {
        this.origin = origin;
        this.alias = alias;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableAlias that = (TableAlias) o;
        return Objects.equals(origin, that.origin) && Objects.equals(alias, that.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, alias);
    }

    @Override
    public String toString() {
        return "[" + "origin='" + origin + '\'' +
                ", alias='" + alias + '\'' + ']';
    }

}
