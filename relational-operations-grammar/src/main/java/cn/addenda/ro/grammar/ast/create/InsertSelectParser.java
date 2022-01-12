package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.retrieve.SelectType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.retrieve.SelectParser;

/**
 * @Author ISJINHAO
 * @Date 2021/7/22 19:09
 */
public class InsertSelectParser extends SelectParser {

    public InsertSelectParser(TokenSequence tokenSequence, FunctionEvaluator functionEvaluator) {
        super(tokenSequence, functionEvaluator);
    }

    @Override
    public Curd parse() {
        Select select = (Select) select();
        select.setSelectType(SelectType.INSERT);
        return select;
    }

}
