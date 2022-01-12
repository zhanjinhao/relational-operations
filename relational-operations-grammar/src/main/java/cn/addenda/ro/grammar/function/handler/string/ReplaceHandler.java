package cn.addenda.ro.grammar.function.handler.string;

import cn.addenda.ro.common.constant.InnerType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;
import cn.addenda.ro.grammar.ast.statement.Identifier;
import cn.addenda.ro.grammar.ast.statement.Literal;

import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2021/7/28 19:11
 */
public class ReplaceHandler extends AbstractFunctionHandler {

    public ReplaceHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "replace";
    }

    @Override
    public InnerType innerType() {
        return InnerType.STRING;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 3);
        List<Curd> parameterList = function.getParameterList();
        checkType(parameterList.get(0), function, Literal.class, Identifier.class);
        checkType(parameterList.get(1), function, Literal.class);
        checkType(parameterList.get(2), function, Literal.class);
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {

        String str = (String) parameters[0];
        String oldSeg = (String) parameters[1];
        String newSeg = (String) parameters[2];

        return str.replaceAll(oldSeg, newSeg);
    }

}
