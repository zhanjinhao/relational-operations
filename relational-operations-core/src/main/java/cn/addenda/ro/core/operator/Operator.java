package cn.addenda.ro.core.operator;

import cn.addenda.ro.core.OperatorRunTimeContext;

/**
 * @Author ISJINHAO
 * @Date 2021/12/30 15:19
 */
public interface Operator {

    Object operate(OperatorRunTimeContext operatorRunTimeContext);

}
