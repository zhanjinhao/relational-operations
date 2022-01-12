package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.statement.Curd;

import java.util.*;

/**
 * 一个 AstMetaData 对应着一个VIEW，描述了它引用的条件列和返回列，以及在整个AST中的位置。
 *
 * @Author ISJINHAO
 * @Date 2022/1/4 22:03
 */
public class AstMetaData {

    private Curd curd;

    private static final AstMetaData ROOT = new AstMetaData();

    private int depth = 1;

    private static final AliasEntry UNDETERMINED = new AliasEntry("UNDETERMINED", "UNDETERMINED");

    private final Map<AliasEntry, List<AliasEntry>> conditionColumnMap = new HashMap<>();
    private final Map<AliasEntry, List<AliasEntry>> resultColumnMap = new HashMap<>();

    private final List<AstMetaData> children = new ArrayList<>();

    private AstMetaData parent = ROOT;

    public AstMetaData() {
        conditionColumnMap.put(UNDETERMINED, new ArrayList<>());
        resultColumnMap.put(UNDETERMINED, new ArrayList<>());
    }

    public void putUndeterminedConditionColumn(String column) {
        conditionColumnMap.get(UNDETERMINED).add(new AliasEntry(column, null));
    }

    public void putUndeterminedResultColumn(String column, String alias) {
        resultColumnMap.get(UNDETERMINED).add(new AliasEntry(column, alias));
    }

    /**
     * 将astMetaData的conditionColumnMap合并到当前对象中
     */
    public void mergeColumnMap(AstMetaData astMetaData) {
        Map<AliasEntry, List<AliasEntry>> thatConditionColumnMap = astMetaData.getConditionColumnMap();
        mergeConditionColumnMap(thatConditionColumnMap);
        Map<AliasEntry, List<AliasEntry>> thatResultColumnMap = astMetaData.getResultColumnMap();
        mergeResultColumnMap(thatResultColumnMap);
    }

    /**
     * 将thatConditionColumnMap合并到当前对象中
     */
    public void mergeConditionColumnMap(Map<AliasEntry, List<AliasEntry>> thatConditionColumnMap) {
        Set<Map.Entry<AliasEntry, List<AliasEntry>>> entries = thatConditionColumnMap.entrySet();
        for (Map.Entry<AliasEntry, List<AliasEntry>> entry : entries) {
            if (this.conditionColumnMap.containsKey(entry.getKey())) {
                this.conditionColumnMap.get(entry.getKey()).addAll(entry.getValue());
            } else {
                this.conditionColumnMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 将thatResultColumnMap合并到当前对象中
     */
    public void mergeResultColumnMap(Map<AliasEntry, List<AliasEntry>> thatResultColumnMap) {
        Set<Map.Entry<AliasEntry, List<AliasEntry>>> entries = thatResultColumnMap.entrySet();
        for (Map.Entry<AliasEntry, List<AliasEntry>> entry : entries) {
            if (this.resultColumnMap.containsKey(entry.getKey())) {
                this.resultColumnMap.get(entry.getKey()).addAll(entry.getValue());
            } else {
                this.resultColumnMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public Map<AliasEntry, List<AliasEntry>> getConditionColumnMap() {
        return conditionColumnMap;
    }

    public Map<AliasEntry, List<AliasEntry>> getResultColumnMap() {
        return resultColumnMap;
    }

    public List<AstMetaData> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "AstMetaData{" +
            "curd=" + curd +
            ", depth=" + depth +
            ", conditionColumnMap=" + conditionColumnMap +
            ", resultColumnMap=" + resultColumnMap +
            ", children=" + children +
            ", parent=" + parent +
            '}';
    }
}
