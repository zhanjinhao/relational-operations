package cn.addenda.ro.grammar.function.evaluator;

import cn.addenda.ro.grammar.constant.InnerType;
import cn.addenda.ro.error.ROError;
import cn.addenda.ro.grammar.function.handler.FunctionHandler;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;

import java.util.Map;
import java.util.Set;

/**
 * @Author ISJINHAO
 * @Date 2021/4/11 14:02
 */
public interface FunctionEvaluator extends ROError {

    FunctionHandler getFunctionHandler(String functionName);

    Set<String> functionNameSet();

    Map<String, InnerType> functionNameInnerType();

    void staticCheck(Function function, CurdType type);

    Object evaluate(Function function, CurdType type, Object... parameters);

    void addFunctionHandler(Class<? extends FunctionHandler> functionHandlerClass);

}
