package cn.addenda.ro.grammar.ast.retrieve.visitor;


import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.AstMetaInfo;
import cn.addenda.ro.grammar.ast.statement.Curd;

import java.util.*;

/**
 * @Author ISJINHAO
 * @Date 2021/3/11 10:29
 */
public class SingleSelectMetaInfo extends AstMetaInfo {

    /**
     * 当前 singleSelect 在ast的第几层。
     */
    private int depth;

    /**
     * 当前 singleSelect 的顺序是多少（按sql解析的顺序）
     */
    private int order;

    /**
     * SingleSelectAstMetaInfo是树形结构，parent记录父亲
     */
    private SingleSelectMetaInfo parent;

    /**
     * SingleSelectAstMetaInfo是树形结构，childList记录孩子
     */
    private List<SingleSelectMetaInfo> childList = new ArrayList<>();

    private Set<Token> returnColumnSet = new HashSet<>();
    private Map<Curd, Token> tempTable = new HashMap<>();
    private Map<Curd, Token> columnAlias = new HashMap<>();

    public SingleSelectMetaInfo(Curd curd, SingleSelectMetaInfo parent, int depth, int order) {
        super(curd);
        this.parent = parent;
        this.depth = depth;
        this.order = order;
    }

    /**
     * 顶层的 SingleSelectAstMetaInfo。是整个 SingleSelectAstMetaInfo 树的根，不表示 singleSelect，
     * 可以理解为一个哨兵。它的孩子是真正的SingleSelectAstMetaInfo。
     */
    public SingleSelectMetaInfo(int depth, int order) {
        super(null);
        this.depth = depth;
        this.order = order;
    }

    public void putTempTable(Curd source, Token alias) {
        tempTable.put(source, alias);
    }

    public void putTempTableMap(Map<Curd, Token> map) {
        tempTable.putAll(map);
    }

    public void putColumnAlias(Curd source, Token alias) {
        columnAlias.put(source, alias);
    }

    public void putColumnAliasMap(Map<Curd, Token> map) {
        columnAlias.putAll(map);
    }

    public void addResultColumn(Token token) {
        returnColumnSet.add(token);
    }

    public void addResultColumnSet(Set<Token> tokenSet) {
        returnColumnSet.addAll(tokenSet);
    }

    public void addChild(SingleSelectMetaInfo child) {
        childList.add(child);
    }

    public void addChildList(List<SingleSelectMetaInfo> childList) {
        childList.addAll(childList);
    }

    public int getDepth() {
        return depth;
    }

    public int getOrder() {
        return order;
    }

    public SingleSelectMetaInfo getParent() {
        return parent;
    }

    public List<SingleSelectMetaInfo> getChildList() {
        return childList;
    }

    public Set<Token> getReturnColumnSet() {
        return returnColumnSet;
    }

}
