package cn.addenda.ro.core;

import cn.addenda.ro.grammar.error.ROError;

/**
 * @Author ISJINHAO
 * @Date 2022/1/1 17:26
 */
public class OperatorROErrorWrapper implements ROError {

    private boolean checkOperand = false;

    private final Object object;

    public OperatorROErrorWrapper(Object object, boolean checkOperand) {
        this.object = object;
        this.checkOperand = checkOperand;
    }

    public Object getObject() {
        return object;
    }

    public boolean getCheckOperand() {
        return checkOperand;
    }

}
