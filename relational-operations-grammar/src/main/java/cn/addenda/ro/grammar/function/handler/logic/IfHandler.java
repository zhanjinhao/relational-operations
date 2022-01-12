package cn.addenda.ro.grammar.function.handler.logic;


import cn.addenda.ro.common.constant.InnerType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;

import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2021/8/13 21:43
 */
public class IfHandler extends AbstractFunctionHandler {

    public IfHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "if";
    }

    @Override
    public InnerType innerType() {
        return InnerType.ALL;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 3);
        List<Curd> parameterList = function.getParameterList();
        checkType(parameterList.get(0), function, Curd.class);
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {

        Object condition = parameters[0];
        if (boolean.class.isAssignableFrom(condition.getClass()) || Boolean.class.isAssignableFrom(condition.getClass())) {
            if (Boolean.parseBoolean(condition.toString())) {
                return parameters[1];
            }
            return parameters[2];
        }

        error(FunctionROErrorReporterDelegate.FUNCTION_unkonw_EVALUATE);
        return null;
    }
}
