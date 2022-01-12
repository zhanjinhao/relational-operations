package cn.addenda.ro.core.operator;

import cn.addenda.ro.common.error.reporter.ROErrorReporter;
import cn.addenda.ro.core.OperatorRunTimeContext;
import cn.addenda.ro.core.OperatorROErrorReporterDelegate;
import cn.addenda.ro.core.OperatorROErrorWrapper;
import cn.addenda.ro.grammar.ast.AbstractCurdParser;
import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.create.Insert;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.statement.*;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementVisitorForDelegation;
import cn.addenda.ro.grammar.ast.update.Update;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * 能处理的数据类型包括：
 * char、Character、short、Short、byte、Byte、int、Integer、long、Long、double、Double、float、Float
 * boolean、Boolean、String、Date、LocalDate、LocalDateTime、BigDecimal、BigInteger、Enum。
 *
 * @Author ISJINHAO
 * @Date 2021/4/11 17:04
 */
public class StatementOperator extends StatementVisitorForDelegation<Object> implements Operator {

    private final FunctionEvaluator functionEvaluator;

    private OperatorRunTimeContext operatorRunTimeContext;

    private final CurdType type;

    private final Curd curd;

    private final ROErrorReporter roErrorReporter = new OperatorROErrorReporterDelegate();

    public StatementOperator(CurdVisitor<Object> client, AbstractCurdParser abstractCurdParser) {
        super(client);
        curd = abstractCurdParser.parse();
        if (curd instanceof Insert) {
            type = CurdType.INSERT;
        } else if (curd instanceof Delete) {
            type = CurdType.DELETE;
        } else if (curd instanceof Select) {
            type = CurdType.SELECT;
        } else if (curd instanceof Update) {
            type = CurdType.UPDATE;
        } else {
            type = CurdType.STATEMENT;
        }
        functionEvaluator = abstractCurdParser.getFunctionEvaluator();
        setErrorReporter(roErrorReporter);
    }

    public ROErrorReporter getRoErrorReporter() {
        return roErrorReporter;
    }

    @Override
    public Object operate(OperatorRunTimeContext operatorRunTimeContext) {
        this.operatorRunTimeContext = operatorRunTimeContext;
        return curd.accept(this);
    }

    @Override
    public Object visitWhereSeg(WhereSeg whereSeg) {
        return whereSeg.getLogic().accept(this);
    }

    @Override
    public Object visitLogic(Logic logic) {
        Object left = logic.getLeftCurd().accept(this);
        Curd rightSelectStatement = logic.getRightCurd();
        if (rightSelectStatement != null) {
            Object right = rightSelectStatement.accept(this);
            Token token = logic.getToken();
            assertBoolean(left, OperatorROErrorReporterDelegate.STATEMENT_logic_OPERATOR);
            assertBoolean(right, OperatorROErrorReporterDelegate.STATEMENT_logic_OPERATOR);
            switch (token.getType()) {
                case AND:
                    return (Boolean) left && (Boolean) right;
                case OR:
                    return (Boolean) left || (Boolean) right;
                default:
                    error(OperatorROErrorReporterDelegate.STATEMENT_logic_OPERATOR, "操作符类型不对，需要 'and' 或 'or'，当前是：" + token.getLiteral(), false);
            }
        }
        assertBoolean(left, OperatorROErrorReporterDelegate.STATEMENT_logic_OPERATOR);
        return left;
    }

    @Override
    public Object visitComparison(Comparison comparison) {
        Object left = comparison.getLeftCurd().accept(this);
        Curd rightSelectStatement = comparison.getRightCurd();
        if (rightSelectStatement != null) {
            Object right = rightSelectStatement.accept(this);
            Curd comparisonSymbol = comparison.getComparisonSymbol();
            if (comparisonSymbol instanceof Identifier) {
                Token operator = ((Identifier) comparisonSymbol).getName();
                switch (operator.getType()) {
                    case BANG_EQUAL:
                        return !MathOperationDelegate.equal(left, right);
                    case EQUAL:
                        return MathOperationDelegate.equal(left, right);
                    case GREATER:
                        return MathOperationDelegate.greater(left, right);
                    case GREATER_EQUAL:
                        return MathOperationDelegate.greaterEqual(left, right);
                    case LESS:
                        return MathOperationDelegate.less(left, right);
                    case LESS_EQUAL:
                        return MathOperationDelegate.lessEqual(left, right);
                    case LIKE:
                        return MathOperationDelegate.like(left, right);
                    case CONTAINS:
                        return MathOperationDelegate.contains(left, right);
                    default:
                        error(OperatorROErrorReporterDelegate.STATEMENT_comparison_OPERATOR, "操作符类型不对，需要 '>' 或 '<' 或 '>=' 或 '<=' 或 '=' 或 '!=' 或 'like' 或 'contains' 或 'is not?'，当前是：" + operator.getLiteral(), false);
                }
            } else if (comparisonSymbol instanceof IsNot) {
                IsNot isNot = (IsNot) comparisonSymbol;
                boolean nullFlag = left == null;
                Token notToken = isNot.getNotToken();
                return (notToken == null) == nullFlag;
            }
        }
        assertBoolean(left, OperatorROErrorReporterDelegate.STATEMENT_comparison_OPERATOR);
        return left;
    }

    private void assertBoolean(Object left, int error) {
        if (!(left instanceof Boolean)) {
            error(error, left, true);
        }
    }

    @Override
    public Object visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        Object left = binaryArithmetic.getLeftCurd().accept(this);
        Curd rightCurd = binaryArithmetic.getRightCurd();
        if (rightCurd != null) {
            Object right = rightCurd.accept(this);
            Token operator = binaryArithmetic.getToken();
            switch (operator.getType()) {
                case PLUS:
                    return MathOperationDelegate.plus(left, right);
                case MINUS:
                    return MathOperationDelegate.minus(left, right);
                case SLASH:
                    return MathOperationDelegate.divide(left, right);
                case STAR:
                    return MathOperationDelegate.multiply(left, right);
                default:
                    error(OperatorROErrorReporterDelegate.STATEMENT_binaryArithmetic_OPERATOR, "操作符类型不对，需要 '+' 或 '-' 或 '*' 或 '/'，当前是：" + operator.getLiteral(), false);
            }
        }
        return left;
    }

    @Override
    public Object visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        Object accept = unaryArithmetic.getCurd().accept(this);
        Token operator = unaryArithmetic.getOperator();
        switch (operator.getType()) {
            case BANG:
                return MathOperationDelegate.nand(accept);
            case MINUS:
                return MathOperationDelegate.negate(accept);
            default:
                error(OperatorROErrorReporterDelegate.STATEMENT_unaryArithmetic_OPERATOR, "操作符类型不对，需要 '-' 或 '!'，当前是：" + operator.getLiteral(), false);
        }
        return null;
    }

    @Override
    public Object visitLiteral(Literal literal) {
        Token token = literal.getValue();
        Object object = token.getLiteral();
        if (object instanceof String) {
            // 单独处理 true 和 false
            String string = (String) object;
            switch (string) {
                case "true":
                    return true;
                case "false":
                    return false;
                case "null":
                    return null;
            }
        }
        return object;
    }

    @Override
    public Object visitGrouping(Grouping grouping) {
        return grouping.getCurd().accept(this);
    }

    @Override
    public Object visitIdentifier(Identifier identifier) {
        return operatorRunTimeContext.get((String) identifier.getName().getLiteral());
    }

    /**
     * 当存在函数的递归调用时，在调用者处完成递归。降低FunctionEvaluator的实现难度。
     */
    @Override
    public Object visitFunction(Function function) {
        List<Curd> parameterList = function.getParameterList();
        int size = parameterList == null ? 0 : parameterList.size();
        Object[] parameters = new Object[size];
        for (int i = 0; i < size; i++) {
            Curd item = parameterList.get(i);
            parameters[i] = item.accept(this);
        }
        return functionEvaluator.evaluate(function, type, parameters);
    }

    @Override
    public Object visitAssignmentList(AssignmentList assignmentList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visitTimeInterval(TimeInterval timeInterval) {
        return timeInterval;
    }

    @Override
    public Object visitTimeUnit(TimeUnit timeUnit) {
        Object accept = timeUnit.getCurd().accept(this);
        return new TimeUnit(timeUnit.getTimeType(), new Literal(new Token(null, accept)));
    }

    @Override
    public Object visitIsNot(IsNot isNot) {
        return isNot;
    }

    @Override
    public Object visitAttachment(Attachment attachment) {
        return attachment;
    }

    private void error(int errorCode, Object object, boolean checkOperand) {
        roErrorReporter.error(errorCode, new OperatorROErrorWrapper(object, checkOperand));
    }

}
