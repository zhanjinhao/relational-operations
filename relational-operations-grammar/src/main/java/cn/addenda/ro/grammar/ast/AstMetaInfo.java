package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author ISJINHAO
 * @Date 2021/4/4 10:48
 */
public abstract class AstMetaInfo {

    /**
     * 与当前对象（SingleSelectAstMetaInfo）对应的 SingleSelect
     */
    private Curd curd;

    public AstMetaInfo(Curd curd) {
        this.curd = curd;
    }

    private Set<Token> referenceTableSet = new HashSet<>();

    private Set<Token> referenceColumnSet = new HashSet<>();

    public void addReferenceColumn(Token token) {
        referenceColumnSet.add(token);
    }

    public void addReferenceColumnSet(Set<Token> tokenSet) {
        referenceColumnSet.addAll(tokenSet);
    }

    public void addReferenceTable(Token token) {
        referenceTableSet.add(token);
    }

    public void addReferenceTableSet(Set<Token> tokenSet) {
        referenceTableSet.addAll(tokenSet);
    }

    public Set<Token> getReferenceColumnSet() {
        return referenceColumnSet;
    }

    public Set<Token> getReferenceTableSet() {
        return referenceTableSet;
    }

    public Curd getSelectStatement() {
        return curd;
    }

    protected void setCurdStatement(Curd curd) {
        this.curd = curd;
    }

}


