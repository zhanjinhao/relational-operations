package cn.addenda.ro.grammar.function.handler.date;

import cn.addenda.ro.common.constant.InnerType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;
import cn.addenda.ro.grammar.ast.statement.Literal;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * 时间戳转日期
 *
 * @Author ISJINHAO
 * @Date 2021/4/11 15:51
 */
public class FromUnixtimeHandler extends AbstractFunctionHandler {

    public FromUnixtimeHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "from_unixtime";
    }

    @Override
    public InnerType innerType() {
        return InnerType.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 1);
        List<Curd> parameterList = function.getParameterList();
        checkType(parameterList.get(0), function, Literal.class);
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {
        Object parameter = parameters[0];
        Class<?> parameterClass = parameter.getClass();
        if (int.class.isAssignableFrom(parameterClass) || long.class.isAssignableFrom(parameterClass)
                || Integer.class.isAssignableFrom(parameterClass) || Long.class.isAssignableFrom(parameterClass)
                || BigInteger.class.isAssignableFrom(parameterClass)) {
            // from_unixtime传入的是秒，需要 multiply 1000
            Instant instant = Instant.ofEpochMilli(Long.parseLong(parameter.toString()) * 1000);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        error(FunctionROErrorReporterDelegate.FUNCTION_dateParameter_EVALUATE, function);
        return null;
    }
}
