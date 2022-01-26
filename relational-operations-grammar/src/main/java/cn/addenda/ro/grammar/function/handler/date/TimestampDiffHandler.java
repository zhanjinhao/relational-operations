package cn.addenda.ro.grammar.function.handler.date;

import cn.addenda.ro.grammar.constant.InnerType;
import cn.addenda.ro.grammar.constant.DateConst;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.function.handler.AbstractFunctionHandler;
import cn.addenda.ro.grammar.function.handler.FunctionROErrorReporterDelegate;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.Attachment;
import cn.addenda.ro.grammar.ast.statement.CurdType;
import cn.addenda.ro.grammar.ast.statement.Function;
import cn.addenda.ro.grammar.ast.statement.Identifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2021/7/27 23:23
 */
public class TimestampDiffHandler extends AbstractFunctionHandler {

    public TimestampDiffHandler(FunctionEvaluator functionEvaluator) {
        super(functionEvaluator);
    }

    private LocalTime zeroTime = LocalTime.parse("00:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
    private LocalDate zeroDate = LocalDate.parse("1970-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    @Override
    public String functionName() {
        return "timestampdiff";
    }

    @Override
    public InnerType innerType() {
        return InnerType.DATE;
    }

    @Override
    public void staticCheck(Function function, CurdType type) {
        checkParameterSize(function, 3);
        List<Curd> parameterList = function.getParameterList();
        checkType(parameterList.get(0), function, Identifier.class);
        checkDate(parameterList.get(1), function);
        checkDate(parameterList.get(2), function);

        parameterList.set(0, new Attachment(parameterList.get(0)));

    }

    @Override
    public Object evaluate(Function function, CurdType type, Object... parameters) {

        Token interval = ((Identifier) ((Attachment) parameters[0]).getAttachment()).getName();
        Object start = parameters[1];
        Object end = parameters[2];

        // 时间只能和时间计算
        if ((start instanceof LocalTime && !(end instanceof LocalTime)) ||
                (!(start instanceof LocalTime) && end instanceof LocalTime)) {
            error(FunctionROErrorReporterDelegate.FUNCTION_unkonw_EVALUATE);
            return null;
        }
        LocalDateTime startDateTime = completeDate(start);
        LocalDateTime sendDateTime = completeDate(end);

        if (DateConst.YEAR.equals(interval)) {
            return ChronoUnit.YEARS.between(startDateTime, sendDateTime);
        } else if (DateConst.MONTH.equals(interval)) {
            return ChronoUnit.MONTHS.between(startDateTime, sendDateTime);
        } else if (DateConst.DAY.equals(interval)) {
            return ChronoUnit.DAYS.between(startDateTime, sendDateTime);
        } else if (DateConst.HOUR.equals(interval)) {
            return ChronoUnit.HOURS.between(startDateTime, sendDateTime);
        } else if (DateConst.MINUTE.equals(interval)) {
            return ChronoUnit.MINUTES.between(startDateTime, sendDateTime);
        } else if (DateConst.SECOND.equals(interval)) {
            return ChronoUnit.SECONDS.between(startDateTime, sendDateTime);
        } else if (DateConst.MICROSECOND.equals(interval)) {
            return ChronoUnit.MICROS.between(startDateTime, sendDateTime);
        }
        error(FunctionROErrorReporterDelegate.FUNCTION_formatPattern_EVALUATE);
        return null;
    }

    private LocalDateTime completeDate(Object date) {
        if (date instanceof Date) {
            return ((Date) date).toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
        } else if (date instanceof LocalDate) {
            return ((LocalDate) date).atTime(zeroTime);
        } else if (date instanceof LocalTime) {
            return zeroDate.atTime((LocalTime) date);
        } else if (date instanceof LocalDateTime) {
            return (LocalDateTime) date;
        }
        error(FunctionROErrorReporterDelegate.FUNCTION_unkonw_EVALUATE);
        return null;
    }

}
