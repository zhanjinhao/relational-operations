package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.Parser;
import cn.addenda.ro.grammar.constant.DateConst;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;
import cn.addenda.ro.grammar.ast.statement.*;
import cn.addenda.ro.grammar.ast.create.InsertParser;
import cn.addenda.ro.grammar.ast.delete.DeleteParser;
import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.retrieve.SelectParser;
import cn.addenda.ro.grammar.ast.retrieve.SingleSelect;
import cn.addenda.ro.grammar.ast.retrieve.SingleSelectType;
import cn.addenda.ro.grammar.ast.update.UpdateParser;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author ISJINHAO
 * @Date 2021/7/22 19:16
 */
public abstract class AbstractCurdParser implements Parser<Curd>, ROErrorReporter {

    protected final TokenSequence tokenSequence;

    private final FunctionEvaluator functionEvaluator;

    private final Set<String> functionNameSet = new HashSet<>();

    protected final ROErrorReporter errorReporterDelegate;

    protected AbstractCurdParser(TokenSequence tokenSequence, FunctionEvaluator functionEvaluator) {
        this.errorReporterDelegate = new AstROErrorReporterDelegate(tokenSequence);
        this.tokenSequence = tokenSequence;
        this.functionEvaluator = functionEvaluator;
        functionNameSet.addAll(functionEvaluator.functionNameSet());
    }

    @Override
    public void error(int errorCode) {
        errorReporterDelegate.error(errorCode);
    }

    protected void saveSingleSelectContext(Curd curd, Curd parent, SingleSelectType type) {
        if (curd instanceof SingleSelect) {
            SingleSelect singleSelect = (SingleSelect) curd;
            if (singleSelect.getSingleSelectType() == null || singleSelect.getSingleSelectType() == SingleSelectType.UNDETERMINED) {
                singleSelect.setSingleSelectType(type);
                singleSelect.setParentSelectStatement(parent);
            }
        } else if (curd instanceof Select) {
            Select select = (Select) curd;
            saveSingleSelectContext(select.getLeftCurd(), select, type);
            saveSingleSelectContext(select.getRightCurd(), select, type);
        } else if (curd instanceof BinaryArithmetic) {
            BinaryArithmetic binaryArithmetic = (BinaryArithmetic) curd;
            saveSingleSelectContext(binaryArithmetic.getLeftCurd(), binaryArithmetic, type);
            saveSingleSelectContext(binaryArithmetic.getRightCurd(), binaryArithmetic, type);
        } else if (curd instanceof UnaryArithmetic) {
            UnaryArithmetic unaryArithmetic = (UnaryArithmetic) curd;
            saveSingleSelectContext(unaryArithmetic.getCurd(), unaryArithmetic, type);
        } else if (curd instanceof Comparison) {
            Comparison comparison = (Comparison) curd;
            saveSingleSelectContext(comparison.getLeftCurd(), comparison, type);
            saveSingleSelectContext(comparison.getRightCurd(), comparison, type);
        } else if (curd instanceof WhereSeg) {
            WhereSeg whereSeg = (WhereSeg) curd;
            saveSingleSelectContext(whereSeg.getLogic(), curd, type);
        }
    }


    protected void doFunctionStaticCheck(Function function) {
        Class<? extends AbstractCurdParser> aClass = this.getClass();

        if (aClass.isAssignableFrom(SelectParser.class)) {
            functionEvaluator.staticCheck(function, CurdType.SELECT);
        } else if (aClass.isAssignableFrom(UpdateParser.class)) {
            functionEvaluator.staticCheck(function, CurdType.UPDATE);
        } else if (aClass.isAssignableFrom(InsertParser.class)) {
            functionEvaluator.staticCheck(function, CurdType.INSERT);
        } else if (aClass.isAssignableFrom(DeleteParser.class)) {
            functionEvaluator.staticCheck(function, CurdType.DELETE);
        } else if (aClass.isAssignableFrom(StatementParser.class)) {
            functionEvaluator.staticCheck(function, CurdType.STATEMENT);
        }

    }

    public FunctionEvaluator getFunctionEvaluator() {
        return functionEvaluator;
    }

    protected boolean checkFunction(Token current, Token next) {
        if (current == null || next == null) {
            return false;
        }
        return checkFunctionName(current) && TokenType.LEFT_PAREN.equals(next.getType());
    }

    protected boolean checkTimeUnit(Token current, Token next) {
        if (current == null || next == null) {
            return false;
        }
        return (DateConst.YEAR.equals(current) || DateConst.MONTH.equals(current) || DateConst.DAY.equals(current)
            || DateConst.HOUR.equals(current) || DateConst.MINUTE.equals(current)
            || DateConst.SECOND.equals(current) || DateConst.MICROSECOND.equals(current)) && TokenType.FROM.equals(next.getType());
    }

    private boolean checkFunctionName(Token token) {
        if (token == null) {
            return false;
        }
        return TokenType.IDENTIFIER.equals(token.getType()) && functionNameSet.contains((String) token.getLiteral());
    }

}
