package cn.addenda.ro.grammar.function.handler.date;

import cn.addenda.ro.grammar.constant.InnerType;
import cn.addenda.ro.grammar.constant.DateConst;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.function.handler.date.format.DateFormatParserFactory;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;
import cn.addenda.ro.grammar.ast.statement.Literal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * 字符串转日期
 *
 * @Author ISJINHAO
 * @Date 2021/4/11 15:49
 */
public class StrToDateHandler extends AbstractFunctionHandler {

    public StrToDateHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    @Override
    public String functionName() {
        return "str_to_date";
    }

    @Override
    public InnerType innerType() {
        return InnerType.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 2);
        List<Curd> parameterList = function.getParameterList();
        checkType(parameterList.get(0), function, Literal.class);
        checkType(parameterList.get(1), function, Literal.class);
    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {

        String dateStr = (String) parameters[0];
        String pattern = (String) parameters[1];

        List<Token> tokens = DateFormatParserFactory.patternParse(pattern);

        if (tokens == null) {
            return null;
        }

        InnerStringBuilder dateStrJava = new InnerStringBuilder();
        InnerStringBuilder patternJava = new InnerStringBuilder();

        Flag flag = Flag.initFlag();

        int index = 0;

        int dateStrLen = dateStr.length();

        try {
            for (Token token : tokens) {
                if (DateConst.YEAR_FORMAT.equals(token)) {
                    if (!flag.isEmpty(0)) {
                        error(FunctionROErrorReporterDelegate.FUNCTION_repeatedPattern_EVALUATE);
                        return null;
                    }
                    dateStrJava.append(dateStr, index, index + 4);
                    index = index + 4;
                    patternJava.append("yyyy");
                    flag.setFlag(0);
                } else if (DateConst.MONTH_FORMAT.equals(token)) {
                    if (!flag.isEmpty(1)) {
                        error(FunctionROErrorReporterDelegate.FUNCTION_repeatedPattern_EVALUATE);
                        return null;
                    }
                    dateStrJava.append(dateStr, index, index + 2);
                    index = index + 2;
                    patternJava.append("MM");
                    flag.setFlag(1);
                } else if (DateConst.DAY_FORMAT.equals(token)) {
                    if (!flag.isEmpty(2)) {
                        error(FunctionROErrorReporterDelegate.FUNCTION_repeatedPattern_EVALUATE);
                        return null;
                    }
                    dateStrJava.append(dateStr, index, index + 2);
                    index = index + 2;
                    patternJava.append("dd");
                    flag.setFlag(2);
                } else if (DateConst.HOUR_FORMAT.equals(token)) {
                    if (!flag.isEmpty(3)) {
                        error(FunctionROErrorReporterDelegate.FUNCTION_repeatedPattern_EVALUATE);
                        return null;
                    }
                    dateStrJava.append(dateStr, index, index + 2);
                    index = index + 2;
                    patternJava.append("HH");
                    flag.setFlag(3);
                } else if (DateConst.MINUTE_FORMAT.equals(token)) {
                    if (!flag.isEmpty(4)) {
                        error(FunctionROErrorReporterDelegate.FUNCTION_repeatedPattern_EVALUATE);
                        return null;
                    }
                    dateStrJava.append(dateStr, index, index + 2);
                    index = index + 2;
                    patternJava.append("mm");
                    flag.setFlag(4);
                } else if (DateConst.SECOND_FORMAT.equals(token)) {
                    if (!flag.isEmpty(5)) {
                        error(FunctionROErrorReporterDelegate.FUNCTION_repeatedPattern_EVALUATE);
                        return null;
                    }
                    dateStrJava.append(dateStr, index, index + 2);
                    index = index + 2;
                    patternJava.append("ss");
                    flag.setFlag(5);
                } else if (DateConst.MICROSECOND_FORMAT.equals(token)) {
                    if (!flag.isEmpty(6)) {
                        error(FunctionROErrorReporterDelegate.FUNCTION_repeatedPattern_EVALUATE);
                        return null;
                    }
                    int i = 0;
                    StringBuilder sb = new StringBuilder();
                    char c;
                    while (i < 6 && (i + index) < dateStrLen && Character.isDigit(c = dateStr.charAt(i + index))) {
                        dateStrJava.append(c);
                        i++;
                    }
                    index = index + i;
                    while (i < 9) {
                        sb.append('0');
                        i++;
                    }
                    dateStrJava.append(sb.toString());
                    patternJava.append("n");
                    flag.setFlag(6);
                } else if (TokenType.IDENTIFIER.equals(token.getType())) {
                    Object patternLiteral = token.getLiteral();
                    if (!(patternLiteral instanceof String)) {
                        error(FunctionROErrorReporterDelegate.FUNCTION_dateParameter_EVALUATE);
                        return null;
                    }
                    String patternStr = (String) patternLiteral;
                    String substring = dateStr.substring(index, patternStr.length() + index);
                    if (!patternStr.equals(substring)) {
                        error(FunctionROErrorReporterDelegate.FUNCTION_dateParameter_EVALUATE);
                        return null;
                    }
                    index = index + patternStr.length();
                }
            }
            if (index != dateStrLen) {
                error(FunctionROErrorReporterDelegate.FUNCTION_dateParameter_EVALUATE);
                return null;
            }

            boolean time = flag.isTime();
            boolean date = flag.isDate();
            if (time && date) {
                return LocalDateTime.parse(dateStrJava.toString(), DateTimeFormatter.ofPattern(patternJava.toString()));
            } else if (time) {
                return LocalTime.parse(dateStrJava.toString(), DateTimeFormatter.ofPattern(patternJava.toString()));
            } else if (date) {
                return LocalDate.parse(dateStrJava.toString(), DateTimeFormatter.ofPattern(patternJava.toString()));
            }
        } catch (IndexOutOfBoundsException | DateTimeParseException e) {
            error(FunctionROErrorReporterDelegate.FUNCTION_unkonw_EVALUATE);
            return null;
        }
        return null;
    }

    private static class Flag {

        private int flag;

        private Flag(int slotNum) {
            this.flag = 1 << slotNum;
        }

        public static Flag initFlag() {
            return new Flag(7);
        }

        public boolean isEmpty(int index) {
            return (flag & 1 << index) == 0;
        }

        public void setFlag(int index) {
            flag = flag | 1 << index;
        }

        public boolean isDate() {
            return (flag & ((1 << 3) - 1)) != 0;
        }

        public boolean isTime() {
            return (flag & (((1 << 4) - 1)) << 3) != 0;
        }

    }


    private static class InnerStringBuilder {

        private StringBuilder stringBuilder = new StringBuilder();

        public InnerStringBuilder append(String str) {
            stringBuilder.append(str).append("-");
            return this;
        }

        public InnerStringBuilder append(char c) {
            stringBuilder.append(c);
            return this;
        }

        public InnerStringBuilder append(CharSequence s, int start, int end) {
            stringBuilder.append(s, start, end).append("-");
            return this;
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }

        public char charAt(int index) {
            return stringBuilder.charAt(index);
        }

    }


}
