package cn.addenda.ro.grammar.ast.update.visitor;

import cn.addenda.ro.grammar.ast.AstMetaInfo;
import cn.addenda.ro.grammar.ast.statement.Curd;

/**
 * @Author ISJINHAO
 * @Date 2021/4/3 17:55
 */
public class UpdateMetaInfo extends AstMetaInfo {

    public UpdateMetaInfo(Curd curd) {
        super(curd);
    }
}
