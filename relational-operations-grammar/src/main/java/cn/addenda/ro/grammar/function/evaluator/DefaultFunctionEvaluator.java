package cn.addenda.ro.grammar.function.evaluator;

import cn.addenda.ro.grammar.function.handler.date.*;
import cn.addenda.ro.grammar.function.handler.logic.DecodeHandler;
import cn.addenda.ro.grammar.function.handler.logic.IfHandler;
import cn.addenda.ro.grammar.function.handler.string.ConcatHandler;
import cn.addenda.ro.grammar.function.handler.string.ReplaceHandler;
import cn.addenda.ro.grammar.function.handler.string.SubstringHandler;

/**
 * @Author ISJINHAO
 * @Date 2021/4/11 15:25
 */
public class DefaultFunctionEvaluator extends AbstractFunctionEvaluator {

    private static final FunctionEvaluator instance = new DefaultFunctionEvaluator();

    public static FunctionEvaluator getInstance() {
        return instance;
    }

    private DefaultFunctionEvaluator() {
        addFunctionHandler(DateAddHandler.class);
        addFunctionHandler(DateFormatHandler.class);
        addFunctionHandler(DateSubHandler.class);
        addFunctionHandler(UnixTimestampHandler.class);
        addFunctionHandler(NowHandler.class);
        addFunctionHandler(StrToDateHandler.class);
        addFunctionHandler(FromUnixtimeHandler.class);
        addFunctionHandler(TimestampDiffHandler.class);
        addFunctionHandler(DateHandler.class);
        addFunctionHandler(ExtractHandler.class);

        addFunctionHandler(ConcatHandler.class);
        addFunctionHandler(ReplaceHandler.class);
        addFunctionHandler(SubstringHandler.class);

        addFunctionHandler(DecodeHandler.class);
        addFunctionHandler(IfHandler.class);
    }

}
