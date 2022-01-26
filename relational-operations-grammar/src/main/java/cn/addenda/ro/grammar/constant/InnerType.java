package cn.addenda.ro.grammar.constant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @Author ISJINHAO
 * @Date 2021/7/27 22:04
 */
public enum InnerType {
    STRING, NUMBER, DATE, ALL, UNDETERMINED;

    public static InnerType getInnerType(Object object) {
        Class<?> aClass = object.getClass();
        return getInnerType(aClass);
    }

    public static InnerType getInnerType(Class<?> aClass) {
        if (String.class.isAssignableFrom(aClass)) {
            return STRING;
        } else if (Long.class.isAssignableFrom(aClass) || long.class.isAssignableFrom(aClass)
                || Integer.class.isAssignableFrom(aClass) || int.class.isAssignableFrom(aClass)
                || BigInteger.class.isAssignableFrom(aClass) || BigDecimal.class.isAssignableFrom(aClass)
                || Short.class.isAssignableFrom(aClass) || short.class.isAssignableFrom(aClass)) {
            return NUMBER;
        } else if (Date.class.isAssignableFrom(aClass) || LocalDateTime.class.isAssignableFrom(aClass)
                || LocalDate.class.isAssignableFrom(aClass) || LocalTime.class.isAssignableFrom(aClass)) {
            return DATE;
        }
        return UNDETERMINED;
    }
}
