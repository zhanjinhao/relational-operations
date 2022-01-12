package cn.addenda.ro.grammar.function.handler.logic;

import cn.addenda.ro.common.constant.InnerType;
import cn.addenda.ro.grammar.function.handler.ErrorReportableFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;

import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2021/7/31 8:45
 */
public class DecodeHandler extends ErrorReportableFunctionHandler {

    @Override
    public String functionName() {
        return "decode";
    }

    @Override
    public InnerType innerType() {
        return InnerType.ALL;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        List<Curd> parameterList = function.getParameterList();
        if (parameterList == null) {
            error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE);
            return;
        }
        if (parameterList.size() % 2 == 1) {
            error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE);
            return;
        }
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {
        int length = parameters.length;
        Object stand = parameters[0];

        for (int i = 1; i < length - 1; i = i + 2) {
            Object condition = parameters[i];
            Object result = parameters[i + 1];
            if (stand.equals(condition)) {
                return result;
            }
        }

        return parameters[length - 1];
    }

}
