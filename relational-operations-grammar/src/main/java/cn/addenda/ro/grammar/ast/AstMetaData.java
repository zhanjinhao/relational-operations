package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.statement.Curd;

import cn.addenda.ro.grammar.ast.statement.Identifier;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;
import java.util.*;

/**
 * 一个 AstMetaData 对应着一个VIEW，描述了它引用的条件列和返回列，以及在整个AST中的位置。
 *
 * @Author ISJINHAO
 * @Date 2022/1/4 22:03
 */
public class AstMetaData {

    private static final AstMetaData ROOT = new AstMetaData();

    private static final AliasTable UNDETERMINED_TABLE = new AliasTable(new Identifier(new Token(TokenType.IDENTIFIER, "UNDETERMINED")), "UNDETERMINED");

    private final Map<AliasTable, List<AliasColumn>> conditionColumnMap = new HashMap<>();
    private final Map<AliasTable, List<AliasColumn>> resultColumnMap = new HashMap<>();

    private int depth = 1;

    // 只有 Select SingleSelect Insert Delete Update
    private Curd curd;

    private final List<AstMetaData> children = new ArrayList<>();

    private AstMetaData parent = ROOT;

    public AstMetaData() {
        conditionColumnMap.put(UNDETERMINED_TABLE, new ArrayList<>());
        resultColumnMap.put(UNDETERMINED_TABLE, new ArrayList<>());
    }

    public void putUndeterminedConditionColumn(String column) {
        conditionColumnMap.get(UNDETERMINED_TABLE).add(new AliasColumn(column, null));
    }

    public void putUndeterminedResultColumn(String column, String alias) {
        resultColumnMap.get(UNDETERMINED_TABLE).add(new AliasColumn(column, alias));
    }

    /**
     * 将astMetaData的conditionColumnMap合并到当前对象中
     */
    public void mergeColumnMap(AstMetaData astMetaData) {
        mergeConditionColumnMap(astMetaData.getConditionColumnMap());
        mergeResultColumnMap(astMetaData.getResultColumnMap());
    }

    /**
     * 将thatConditionColumnMap合并到当前对象中
     */
    public void mergeConditionColumnMap(Map<AliasTable, List<AliasColumn>> thatConditionColumnMap) {
        Set<Map.Entry<AliasTable, List<AliasColumn>>> entries = thatConditionColumnMap.entrySet();
        for (Map.Entry<AliasTable, List<AliasColumn>> entry : entries) {
            AliasTable key = entry.getKey();
            List<AliasColumn> value = entry.getValue();
            if (this.conditionColumnMap.containsKey(key)) {
                this.conditionColumnMap.get(key).addAll(value);
            } else {
                this.conditionColumnMap.put(key, value);
            }
        }
    }

    /**
     * 将thatResultColumnMap合并到当前对象中
     */
    public void mergeResultColumnMap(Map<AliasTable, List<AliasColumn>> thatResultColumnMap) {
        Set<Map.Entry<AliasTable, List<AliasColumn>>> entries = thatResultColumnMap.entrySet();
        for (Map.Entry<AliasTable, List<AliasColumn>> entry : entries) {
            AliasTable key = entry.getKey();
            List<AliasColumn> value = entry.getValue();
            if (this.resultColumnMap.containsKey(key)) {
                this.resultColumnMap.get(key).addAll(value);
            } else {
                this.resultColumnMap.put(key, value);
            }
        }
    }

    public Map<AliasTable, List<AliasColumn>> getConditionColumnMap() {
        return conditionColumnMap;
    }

    public Map<AliasTable, List<AliasColumn>> getResultColumnMap() {
        return resultColumnMap;
    }

    public List<AstMetaData> getChildren() {
        return children;
    }

    public Curd getCurd() {
        return curd;
    }

    public void setCurd(Curd curd) {
        this.curd = curd;
    }

    public AstMetaData getParent() {
        return parent;
    }

    public void setParent(AstMetaData parent) {
        this.parent = parent;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "AstMetaData{" +
            ", depth=" + depth +
            ", conditionColumnMap=" + conditionColumnMap +
            ", resultColumnMap=" + resultColumnMap +
            ", children=" + children +
            ", parent=" + parent +
            '}';
    }
}
