package cn.addenda.ro.grammar.function.handler.date;

import cn.addenda.ro.common.constant.InnerType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 *
 * 日期转时间戳
 *
 * @Author ISJINHAO
 * @Date 2021/4/11 14:54
 */
public class UnixTimestampHandler extends AbstractFunctionHandler {

    public UnixTimestampHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "unix_timestamp";
    }

    @Override
    public InnerType innerType() {
        return InnerType.NUMBER;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 1);
        List<Curd> parameterList = function.getParameterList();
        checkDate(parameterList.get(0), function);
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {

        Object date = parameters[0];

        if (date instanceof Date) {
            return ((Date) date).getTime();
        } else if (date instanceof LocalDateTime) {
            return ((LocalDateTime) date).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } else if (date instanceof LocalDate) {
            return ((LocalDate) date).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }

        error(FunctionROErrorReporterDelegate.FUNCTION_dateType_EVALUATE, function);

        return null;
    }

}
