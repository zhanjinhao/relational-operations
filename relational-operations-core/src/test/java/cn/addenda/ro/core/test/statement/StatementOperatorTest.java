package cn.addenda.ro.core.test.statement;

import cn.addenda.ro.core.OperatorRunTimeContext;
import cn.addenda.ro.core.operator.Operator;
import cn.addenda.ro.core.operator.OperatorFactory;

import java.time.LocalDateTime;

/**
 * @Author ISJINHAO
 * @Date 2022/1/1 20:33
 */
public class StatementOperatorTest {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
    }

    static void test1() {
        Operator statementOperator = OperatorFactory.createStatementOperator("a > 4 and b is not null and c contains '123' and d = str_to_date('2021-12-12 00:00:00', '%Y-%m-%d %H:%i:%s')");
        OperatorRunTimeContext operatorRunTimeContext = new OperatorRunTimeContext();
        operatorRunTimeContext.put("a", 5);
        operatorRunTimeContext.put("b", true);
        operatorRunTimeContext.put("c", "12345");
        operatorRunTimeContext.put("d", LocalDateTime.of(2021, 12, 12, 0, 0, 0));
        System.out.println(statementOperator.operate(operatorRunTimeContext));
    }

    static void test2() {
        Operator statementOperator = OperatorFactory.createStatementOperator("a > 4 and b is not null and c contains '123' and d = str_to_date('2021-12-12 00:00:00', '%Y-%m-%d %H:%i:%s')");
        OperatorRunTimeContext operatorRunTimeContext = new OperatorRunTimeContext();
        operatorRunTimeContext.put("a", 5);
        operatorRunTimeContext.put("b", true);
        operatorRunTimeContext.put("c", "12345");
        operatorRunTimeContext.put("d", LocalDateTime.of(2021, 12, 12, 0, 0, 1));
        System.out.println(statementOperator.operate(operatorRunTimeContext));
    }

    static void test3() {
        Operator statementOperator = OperatorFactory.createStatementOperator("date_sub(date_add(str_to_date('2021-12-12 08:00:00', '%Y-%m-%d %H:%i:%s'), interval 10 day), interval 2 hour)");
        OperatorRunTimeContext operatorRunTimeContext = new OperatorRunTimeContext();
        operatorRunTimeContext.put("a", 5);
        operatorRunTimeContext.put("b", true);
        operatorRunTimeContext.put("c", "12345");
        operatorRunTimeContext.put("d", LocalDateTime.of(2021, 12, 12, 0, 0, 1));
        System.out.println(statementOperator.operate(operatorRunTimeContext));
    }

    static void test4() {
        Operator statementOperator = OperatorFactory.createStatementOperator("extract(hour from str_to_date('2021-12-12 08:00:00', '%Y-%m-%d %H:%i:%s'))");
        OperatorRunTimeContext operatorRunTimeContext = new OperatorRunTimeContext();
        operatorRunTimeContext.put("a", 5);
        operatorRunTimeContext.put("b", true);
        operatorRunTimeContext.put("c", "12345");
        operatorRunTimeContext.put("d", LocalDateTime.of(2021, 12, 12, 0, 0, 1));
        System.out.println(statementOperator.operate(operatorRunTimeContext));
    }

}
