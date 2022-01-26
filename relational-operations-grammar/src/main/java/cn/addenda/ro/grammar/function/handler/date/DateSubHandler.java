package cn.addenda.ro.grammar.function.handler.date;

import cn.addenda.ro.grammar.constant.InnerType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionHandler;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;
import cn.addenda.ro.grammar.ast.statement.TimeInterval;

/**
 * @Author ISJINHAO
 * @Date 2021/7/27 21:46
 */
public class DateSubHandler extends AbstractFunctionHandler {

    public DateSubHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "date_sub";
    }

    @Override
    public InnerType innerType() {
        return InnerType.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        FunctionHandler dateAddFunction = functionEvaluator.getFunctionHandler("date_add");
        dateAddFunction.staticCheck(function, type);
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {
        FunctionHandler dateAddFunction = functionEvaluator.getFunctionHandler("date_add");

        TimeInterval interval = (TimeInterval) parameters[1];
        TimeInterval intervalParameter = new TimeInterval(interval.getTimeType(), -interval.getInterval());

        return dateAddFunction.evaluate(function, type, parameters[0], intervalParameter);
    }

}
