package cn.addenda.ro.grammar.function.handler;


import cn.addenda.ro.common.error.ROError;
import cn.addenda.ro.common.error.reporter.ROErrorReporter;

/**
 * @Author ISJINHAO
 * @Date 2021/7/27 22:15
 */
public abstract class ErrorReportableFunctionHandler implements FunctionHandler, ROErrorReporter {

    private final ROErrorReporter errorReporter = new FunctionROErrorReporterDelegate();

    @Override
    public void error(int errorCode) {
        errorReporter.error(errorCode);
    }

    @Override
    public void error(int errorCode, ROError attachment) {
        errorReporter.error(errorCode, attachment);
    }

}