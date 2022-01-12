package cn.addenda.ro.grammar.function.evaluator;

import cn.addenda.ro.common.constant.InnerType;
import cn.addenda.ro.common.error.ROError;
import cn.addenda.ro.common.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.function.handler.FunctionHandler;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author ISJINHAO
 * @Date 2021/7/24 23:21
 */
public abstract class AbstractFunctionEvaluator implements FunctionEvaluator, ROErrorReporter {

    private final HashMap<String, FunctionHandler> functionHandlerMap = new HashMap<>();

    private final ROErrorReporter errorReporterDelegate = new FunctionROErrorReporterDelegate();

    @Override
    public void addFunctionHandler(Class<? extends FunctionHandler> functionHandlerClass) {
        if (functionHandlerClass == null) {
            return;
        }
        try {
            FunctionHandler functionHandler;
            // 对于AbstractFunctionHandler来说，需要调用带FunctionEvaluator参数的构造方法
            if (AbstractFunctionHandler.class.isAssignableFrom(functionHandlerClass)) {
                Constructor<? extends FunctionHandler> constructor = functionHandlerClass.getConstructor(FunctionEvaluator.class);
                functionHandler = constructor.newInstance(this);
            } else {
                functionHandler = functionHandlerClass.newInstance();
            }
            if (functionHandlerMap.containsKey(functionHandler.functionName())) {
                error(FunctionROErrorReporterDelegate.FUNCTION_HANDLER_REPEATED_PARSE);
                return;
            }
            functionHandlerMap.put(functionHandler.functionName(), functionHandler);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            error(FunctionROErrorReporterDelegate.FUNCTION_HANDLER_INSTANTIATION_PARSE);
        }
    }

    @Override
    public FunctionHandler getFunctionHandler(String functionName) {
        return functionHandlerMap.get(functionName);
    }

    @Override
    public Set<String> functionNameSet() {
        return functionHandlerMap.keySet();
    }

    @Override
    public Map<String, InnerType> functionNameInnerType() {
        Map<String, InnerType> map = new HashMap<>();
        functionHandlerMap.forEach((key, value) -> map.put(key, value.innerType()));
        return map;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        String functionName = (String) function.getMethod().getLiteral();
        functionHandlerMap.get(functionName).staticCheck(function, type);
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {
        String functionName = (String) function.getMethod().getLiteral();
        return functionHandlerMap.get(functionName).evaluate(function, type, parameters);
    }

    @Override
    public void error(int errorCode) {
        errorReporterDelegate.error(errorCode);
    }

    @Override
    public void error(int errorCode, ROError attachment) {
        errorReporterDelegate.error(errorCode, attachment);
    }

}
