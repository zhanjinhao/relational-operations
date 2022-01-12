package cn.addenda.ro.grammar.function.handler.date;

import cn.addenda.ro.common.constant.InnerType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;
import cn.addenda.ro.grammar.ast.statement.Literal;
import cn.addenda.ro.grammar.ast.statement.TimeUnit;

import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2021/8/15 20:33
 */
public class ExtractHandler extends AbstractFunctionHandler {

    public ExtractHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "extract";
    }

    @Override
    public InnerType innerType() {
        return InnerType.NUMBER;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 1);
        List<Curd> parameterList = function.getParameterList();
        checkType(parameterList.get(0), function, TimeUnit.class);
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {

        TimeUnit timeUnit = (TimeUnit) parameters[0];

        Token token = timeUnit.getTimeType();
        Literal literal = (Literal) timeUnit.getCurd();

        int numericValue = getNumericValue(literal.getValue().getLiteral(), token);

        if (numericValue == -1) {
            error(FunctionROErrorReporterDelegate.FUNCTION_formatPattern_EVALUATE);
            return null;
        }

        return numericValue;
    }
}
