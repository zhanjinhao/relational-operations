package cn.addenda.ro.grammar.function.handler;

import cn.addenda.ro.grammar.constant.InnerType;
import cn.addenda.ro.grammar.error.ROError;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;

/**
 * @Author ISJINHAO
 * @Date 2021/7/24 22:58
 */
public interface FunctionHandler extends ROError {

    String functionName();

    InnerType innerType();

    void staticCheck(Function function, CurdType type);

    Object evaluate(Function function, CurdType type, Object... parameters);

}
