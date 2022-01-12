package cn.addenda.ro.core.test.statement;

import cn.addenda.ro.core.operator.MathOperationDelegate;

/**
 * @Author ISJINHAO
 * @Date 2022/1/1 19:43
 */
public class MathOperationDelegateTest {

    public static void main(String[] args) {

        System.out.println(MathOperationDelegate.negate(1234567890111122222L));
        System.out.println(MathOperationDelegate.nand(true));

        System.out.println(MathOperationDelegate.plus(1234567890111122222L, 1234567890111122222L));
        System.out.println(MathOperationDelegate.minus(3, 2));
        System.out.println(MathOperationDelegate.multiply(1234567890111122222L, 1234567890111122222L));
        System.out.println(MathOperationDelegate.divide(3d, 2));
        System.out.println(MathOperationDelegate.contains("12345678", "234"));
        System.out.println(MathOperationDelegate.like("12345678", "?234%"));
        System.out.println(MathOperationDelegate.like("12345678", "??234%"));

    }

}
