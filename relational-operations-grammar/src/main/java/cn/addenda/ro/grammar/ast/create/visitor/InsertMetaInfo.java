package cn.addenda.ro.grammar.ast.create.visitor;


import cn.addenda.ro.grammar.ast.AstMetaInfo;
import cn.addenda.ro.grammar.ast.statement.Curd;

/**
 * @Author ISJINHAO
 * @Date 2021/4/3 17:55
 */
public class InsertMetaInfo extends AstMetaInfo {

    public InsertMetaInfo(Curd curd) {
        super(curd);
    }
}
