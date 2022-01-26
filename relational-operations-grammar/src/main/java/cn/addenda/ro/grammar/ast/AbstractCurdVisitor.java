package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.error.reporter.ROErrorReporter;

/**
 * @Author ISJINHAO
 * @Date 2021/7/29 14:27
 */
public abstract class AbstractCurdVisitor<R> implements CurdVisitor<R>, ROErrorReporter {

    protected ROErrorReporter errorReporter;

    public AbstractCurdVisitor() {
    }

    @Override
    public void error(int errorCode) {
        errorReporter.error(errorCode);
    }

    public void setErrorReporter(ROErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

}
