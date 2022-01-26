package cn.addenda.ro.grammar.function.handler.date;

import cn.addenda.ro.grammar.constant.InnerType;
import cn.addenda.ro.grammar.function.handler.ErrorReportableFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2021/4/11 14:56
 */
public class NowHandler extends ErrorReportableFunctionHandler {

    @Override
    public String functionName() {
        return "now";
    }

    @Override
    public InnerType innerType() {
        return InnerType.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        List<Curd> parameterList = function.getParameterList();
        if (parameterList != null || !parameterList.isEmpty()) {
            error(FunctionROErrorReporterDelegate.FUNCTION_parameter_PARSE, function);
            return;
        }
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {
        return LocalDateTime.now();
    }
}
