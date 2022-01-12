package cn.addenda.ro.grammar.test.ast.statement;

import cn.addenda.ro.grammar.ast.CurdParserFactory;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.StatementParser;

/**
 * @Author ISJINHAO
 * @Date 2021/12/22 10:16
 */
public class StatementParserTest {


    static String[] sqls = new String[]{
            "a > c + b and now() > e and f is not null",
            "-(100+200)",
    };

    public static void main(String[] args) {

        for (int i = 0; i < sqls.length; i++) {
            StatementParser statementParser = CurdParserFactory.createStatementParser(sqls[i]);
            Curd parse = statementParser.parse();

//            System.out.println(parse);

            String s1 = sqls[i].replaceAll("\\s+", "");
            String s2 = parse.toString().replaceAll("\\s+", "");

            if (s1.equals(s2)) {
                System.out.println(s1);
                System.out.println(s2);
            } else {
                System.err.println(s1);
                System.err.println(s2);
            }
        }

    }

}
