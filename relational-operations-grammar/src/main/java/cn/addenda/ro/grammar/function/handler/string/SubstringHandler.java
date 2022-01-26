package cn.addenda.ro.grammar.function.handler.string;

import cn.addenda.ro.grammar.constant.InnerType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;
import cn.addenda.ro.grammar.ast.statement.Literal;
import cn.addenda.ro.grammar.ast.statement.UnaryArithmetic;


import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2021/7/28 19:12
 */
public class SubstringHandler extends AbstractFunctionHandler {

    public SubstringHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "substring";
    }

    @Override
    public InnerType innerType() {
        return InnerType.STRING;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        List<Curd> parameterList = function.getParameterList();
        for (Curd curd : parameterList) {
            checkType(curd, function, Literal.class, UnaryArithmetic.class);
        }
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {
        int length = parameters.length;
        if (length == 2) {
            return substring2(parameters);
        } else if (length == 3) {
            return substring3(parameters);
        }
        error(FunctionROErrorReporterDelegate.FUNCTION_unkonw_EVALUATE);
        return null;
    }

    private Object substring3(Object[] parameters) {
        Object parameter = parameters[1];
        if (parameter instanceof String) {
            return substring3String(parameters);
        } else {
            return substring3Numeric(parameters);
        }
    }

    private Object substring3Numeric(Object[] parameters) {
        String str = (String) parameters[0];
        int index = Integer.parseInt(parameters[1].toString());
        int len = Integer.parseInt(parameters[2].toString());

        int length = str.length();

        if (index < 0) {
            index = index + length;
        }

        if (index < 0) {
            return "";
        }

        int end = Math.min(index + len, length);

        return str.substring(index, end);
    }

    private Object substring3String(Object[] parameters) {
        String str = (String) parameters[0];
        String seg = (String) parameters[1];
        int len = Integer.parseInt(parameters[2].toString());

        int index = str.indexOf(seg);
        if (index == -1) {
            return "";
        }

        int end = Math.min(index + len, str.length());

        return str.substring(index + seg.length(), end);
    }

    private Object substring2(Object[] parameters) {
        String str = (String) parameters[0];
        int index = Integer.parseInt(parameters[1].toString());

        int length = str.length();

        if (index < 0) {
            index = index + length;
        }

        if (index < 0) {
            return "";
        }

        if (length < index) {
            return "";
        }
        return str.substring(index);
    }
}
