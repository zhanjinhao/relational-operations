package cn.addenda.ro.grammar.test.ast.statement;

import cn.addenda.ro.grammar.error.reporter.DumbROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.CurdParserFactory;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.StatementParser;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementAstMetaDataDetector;

/**
 * @Author ISJINHAO
 * @Date 2022/1/5 19:30
 */
public class StatementDetectorTest {

    static String[] sqls = new String[]{
            "a > c + b and now() > e and t1.f is not null",
            "-(100+200)",
            "SCORE.DEGREE >= GRADE.LOW and SCORE.DEGREE <= GRADE.UPP"
    };

    public static void main(String[] args) {

        for (String sql : sqls) {
            StatementParser statementParser = CurdParserFactory.createStatementParser(sql);
            Curd parse = statementParser.parse();
            parse.accept(new StatementAstMetaDataDetector(null, new DumbROErrorReporterDelegate()));
            System.out.println(parse.getAstMetaData());

        }

    }


}
