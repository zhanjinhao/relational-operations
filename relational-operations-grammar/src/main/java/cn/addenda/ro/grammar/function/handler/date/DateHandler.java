package cn.addenda.ro.grammar.function.handler.date;

import cn.addenda.ro.grammar.constant.InnerType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2021/7/31 22:59
 */
public class DateHandler extends AbstractFunctionHandler {

    public DateHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "date";
    }

    @Override
    public InnerType innerType() {
        return InnerType.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 1);
        List<Curd> parameterList = function.getParameterList();
        checkDate(parameterList.get(0), function);
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {
        Object parameter = parameters[0];

        if (parameter instanceof Date) {
            Date date = (Date) parameter;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        } else if (parameter instanceof LocalDate) {
            return parameter;
        } else if (parameter instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) parameter;
            return localDateTime.toLocalDate();
        }
        error(FunctionROErrorReporterDelegate.FUNCTION_dateType_EVALUATE, function);
        return null;
    }
}
