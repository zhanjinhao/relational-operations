package cn.addenda.ro.grammar.function.handler.string;

import cn.addenda.ro.grammar.constant.InnerType;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;
import cn.addenda.ro.grammar.ast.statement.Literal;

/**
 * @Author ISJINHAO
 * @Date 2021/7/28 19:13
 */
public class ConcatHandler extends AbstractFunctionHandler {

    public ConcatHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "concat";
    }

    @Override
    public InnerType innerType() {
        return InnerType.STRING;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        for (Curd curd : function.getParameterList()) {
            checkType(curd, function, Literal.class);
        }
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {

        StringBuilder sb = new StringBuilder();
        for (Object parameter : parameters) {
            sb.append(parameter);
        }

        return sb.toString();
    }
}
