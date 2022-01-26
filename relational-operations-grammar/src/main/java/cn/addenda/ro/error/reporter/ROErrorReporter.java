package cn.addenda.ro.error.reporter;

import cn.addenda.ro.error.ROError;

public interface ROErrorReporter {

    /**
     * 这个操作是终止操作，即线程运行到这，后面的代码就不再执行了
     */
    void error(int errorCode);


    /**
     * 这个操作是终止操作，即线程运行到这，后面的代码就不再执行了
     */
    default void error(int errorCode, ROError attachment) {
        error(errorCode);
    }

}
