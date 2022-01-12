package cn.addenda.ro.core.operator;

import cn.addenda.ro.grammar.ast.CurdParserFactory;
import cn.addenda.ro.grammar.ast.statement.StatementParser;
import cn.addenda.ro.grammar.function.evaluator.DefaultFunctionEvaluator;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;

/**
 * @Author ISJINHAO
 * @Date 2022/1/1 19:37
 */
public class OperatorFactory {

    public static Operator createStatementOperator(String sql, FunctionEvaluator functionEvaluator) {
        StatementParser statementParser = CurdParserFactory.createStatementParser(sql, functionEvaluator);
        return new StatementOperator(null, statementParser);
    }

    public static Operator createStatementOperator(String sql) {
        return createStatementOperator(sql, DefaultFunctionEvaluator.getInstance());
    }

    public static Operator createStatementOperator(TokenSequence tokenSequence, FunctionEvaluator functionEvaluator) {
        StatementParser statementParser = CurdParserFactory.createStatementParser(tokenSequence, functionEvaluator);
        return new StatementOperator(null, statementParser);
    }

    public static Operator createStatementOperator(TokenSequence tokenSequence) {
        return createStatementOperator(tokenSequence, DefaultFunctionEvaluator.getInstance());
    }


}
