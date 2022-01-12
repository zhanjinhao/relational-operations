package cn.addenda.ro.grammar.ast.delete.visitor;

import cn.addenda.ro.grammar.ast.AstMetaInfo;
import cn.addenda.ro.grammar.ast.statement.Curd;

/**
 * @Author ISJINHAO
 * @Date 2021/4/10 9:37
 */
public class DeleteMetaInfo extends AstMetaInfo {
    public DeleteMetaInfo(Curd curd) {
        super(curd);
    }
}
