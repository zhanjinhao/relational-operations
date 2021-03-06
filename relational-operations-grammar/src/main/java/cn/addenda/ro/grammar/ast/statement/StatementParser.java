package cn.addenda.ro.grammar.ast.statement;

import cn.addenda.ro.grammar.ast.AbstractCurdParser;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.statement.visitor.StatementGrammarValidator;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * logic               ->  condition (("or" | "and") condition)*
 * condition           ->  comparison
 * comparison          ->  binaryArithmetic ((">" | "<" | ">=" | "<=" | "!=" | "=" | "like" | "contains" | isNot) binaryArithmetic)?
 * isNot               ->  "is" ("not")?
 * binaryArithmetic    ->  unaryArithmetic (("+" | "-" | "*" | "/") unaryArithmetic)*
 * unaryArithmetic     ->  ("!"|"-") unaryArithmetic | primary
 * primary             ->  #{xxx} | ? | "true" | "false" | "null" | NUMBER | STRING | IDENTIFIER | grouping | function
 * grouping            ->  "(" logic ")"
 * <p>
 * function            ->  functionName "(" functionParameter? ("," functionParameter)* ")"
 * functionParameter   ->  logic | timeInterval | timeUnit | function
 * timeInterval        ->  "interval" NUMBER IDENTIFIER
 * timeUnit            ->  IDENTIFIER "from" primary
 * <p>
 * whereSeg            ->  "where" logic
 * assignmentList      ->  (IDENTIFIER "=" binaryArithmetic) ("," IDENTIFIER "=" binaryArithmetic)*
 * columnList	       ->  IDENTIFIER ("," IDENTIFIER)*
 *
 * @Author ISJINHAO
 * @Date 2021/4/5 12:11
 */
public class StatementParser extends AbstractCurdParser {

    public StatementParser(TokenSequence tokenSequence, FunctionEvaluator functionEvaluator) {
        super(tokenSequence, functionEvaluator);
    }

    @Override
    public Curd parse() {
        Curd logic = logic();
        if (logic == null) {
            error(AstROErrorReporterDelegate.STATEMENT_logic_VALIDATION);
            return null;
        }
        logic.accept(new StatementGrammarValidator(null, this.errorReporterDelegate));
        return logic;
    }

    /**
     * condition (("or" | "and") condition)*
     */
    protected Curd logic() {
        Curd left = condition();
        while (tokenSequence.curEqual(TokenType.OR, TokenType.AND)) {
            Token token = tokenSequence.takeCur();
            tokenSequence.advance();
            Curd right = condition();
            left = new Logic(left, token, right);
        }
        if (left == null) {
            error(AstROErrorReporterDelegate.STATEMENT_logic_PARSE);
            return null;
        }
        return left;
    }


    /**
     * comparison
     */
    protected Curd condition() {
        return comparison();
    }


    /**
     * binaryArithmetic (comparisonSymbol binaryArithmetic)?
     */
    protected Curd comparison() {
        Curd left = binaryArithmetic();
        Curd token = comparisonSymbol();
        if (token != null) {
            Curd right = binaryArithmetic();
            return new Comparison(left, token, right);
        }
        if (left == null) {
            error(AstROErrorReporterDelegate.STATEMENT_comparison_PARSE);
            return null;
        }
        return left;
    }


    /**
     * ">" | "<" | ">=" | "<=" | "!=" | "=" | "like" | "contains" | isNot
     */
    private Curd comparisonSymbol() {
        if (tokenSequence.curEqual(TokenType.LIKE, TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS,
                TokenType.LESS_EQUAL, TokenType.BANG_EQUAL, TokenType.EQUAL, TokenType.CONTAINS)) {
            Token token = tokenSequence.takeCur();
            tokenSequence.advance();
            return new Identifier(token);
        } else if (tokenSequence.curEqual(TokenType.IS)) {
            Token isToken = tokenSequence.takeCur();
            tokenSequence.advance();
            Token notToken = null;
            if (tokenSequence.curEqual(TokenType.NOT)) {
                notToken = tokenSequence.takeCur();
                tokenSequence.advance();
            }
            return new IsNot(isToken, notToken);
        }
        return null;
    }


    /**
     * unaryArithmetic (("+" | "-" | "*" | "/") unaryArithmetic)*
     */
    protected Curd binaryArithmetic() {
        Curd expression = unaryArithmetic();
        while (tokenSequence.curEqual(TokenType.PLUS, TokenType.MINUS,
                TokenType.STAR, TokenType.SLASH)) {
            Token operator = tokenSequence.takeCur();
            tokenSequence.advance();
            Curd right = unaryArithmetic();
            expression = new BinaryArithmetic(expression, operator, right);
        }
        if (expression == null) {
            error(AstROErrorReporterDelegate.STATEMENT_binaryArithmetic_PARSE);
            return null;
        }
        return expression;
    }


    /**
     * ("!"|"-") unaryArithmetic | primary
     */
    protected Curd unaryArithmetic() {
        // ???????????????
        if (tokenSequence.curEqual(TokenType.BANG, TokenType.MINUS)) {
            Token operator = tokenSequence.takeCur();
            tokenSequence.advance();
            Curd curd = unaryArithmetic();
            return new UnaryArithmetic(operator, curd);
        }
        return primary();
    }


    /**
     * #{xxx} | ? | "true" | "false" | "null" | NUMBER | STRING | IDENTIFIER | grouping | function
     */
    protected Curd primary() {
        if (tokenSequence.equalThenAdvance(TokenType.HASH_MARK_PLACEHOLDER, TokenType.PARAMETER,
                TokenType.TRUE, TokenType.FALSE, TokenType.NULL, TokenType.NUMBER, TokenType.STRING)) {
            return new Literal(tokenSequence.takePre());
        }

        // "(" logic ")"
        if (tokenSequence.equalThenAdvance(TokenType.LEFT_PAREN)) {
            Curd curd = logic();
            consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.SELECT_primary_PARSE);
            return new Grouping(curd);
        }

        // IDENTIFIER
        if (tokenSequence.curEqual(TokenType.IDENTIFIER)) {
            Token current = tokenSequence.takeCur();
            Token next = tokenSequence.takeNext();
            if (checkFunction(current, next)) {
                return function();
            }
            tokenSequence.advance();
            return new Identifier(current);
        }
        return null;
    }


    /**
     * functionName "(" functionParameter? ("," functionParameter)* ")"
     */
    protected Curd function() {
        Token method = tokenSequence.takeCur();
        tokenSequence.advance();
        consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.STATEMENT_function_PARSE);

        // ?????????????????????
        if (tokenSequence.equalThenAdvance(TokenType.RIGHT_PAREN)) {
            return new Function(method);
        }

        // ??????????????????
        ArrayList<Curd> parameterList = new ArrayList<>();
        do {
            parameterList.add(functionParameter());
        } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
        consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.STATEMENT_function_PARSE);
        Function function = new Function(method, parameterList);
        doFunctionStaticCheck(function);
        return function;
    }


    /**
     * logic | timeInterval | timeUnit | function
     */
    protected Curd functionParameter() {
        Curd curd;
        Token current = tokenSequence.takeCur();
        Token next = tokenSequence.takeNext();
        if (checkFunction(current, next)) {
            curd = function();
        } else if (TokenType.INTERVAL.equals(current.getType())) {
            curd = timeInterval();
        } else if (checkTimeUnit(current, next)) {
            curd = timeUnit();
        } else {
            curd = logic();
        }
        return curd;
    }


    /**
     * "interval" NUMBER IDENTIFIER
     */
    protected Curd timeInterval() {
        consume(TokenType.INTERVAL, AstROErrorReporterDelegate.STATEMENT_timeInterval_PARSE);

        Token time;
        Token token;

        if (tokenSequence.curEqual(TokenType.NUMBER)) {
            time = tokenSequence.takeCur();
            tokenSequence.advance();
        } else {
            error(AstROErrorReporterDelegate.STATEMENT_timeInterval_PARSE);
            return null;
        }

        if (tokenSequence.curEqual(TokenType.IDENTIFIER)) {
            token = tokenSequence.takeCur();
            tokenSequence.advance();
        } else {
            error(AstROErrorReporterDelegate.STATEMENT_timeInterval_PARSE);
            return null;
        }
        Long interval = null;
        Object value = time.getLiteral();
        if (value instanceof Integer) {
            interval = Long.parseLong(value.toString());
        } else if (value instanceof Long) {
            interval = (Long) value;
        }

        return new TimeInterval(token, interval);
    }


    /**
     * IDENTIFIER "from" primary
     */
    private Curd timeUnit() {
        Token unit = tokenSequence.takeCur();
        tokenSequence.advance();
        consume(TokenType.FROM, AstROErrorReporterDelegate.STATEMENT_timeUnit_PARSE);
        Curd curd = primary();

        return new TimeUnit(unit, curd);
    }


    /**
     * "where" logic
     */
    protected Curd whereSeg() {
        consume(TokenType.WHERE, AstROErrorReporterDelegate.STATEMENT_whereSeg_PARSE);
        return new WhereSeg(logic());
    }


    /**
     * (IDENTIFIER "=" binaryArithmetic) ("," IDENTIFIER "=" binaryArithmetic)*
     */
    protected Curd assignmentList() {
        List<AssignmentList.Entry> entryList = new ArrayList<>();
        do {
            Token token = consume(TokenType.IDENTIFIER, AstROErrorReporterDelegate.STATEMENT_assignmentList_PARSE);
            consume(TokenType.EQUAL, AstROErrorReporterDelegate.STATEMENT_assignmentList_PARSE);
            Curd curd = binaryArithmetic();
            entryList.add(new AssignmentList.Entry(token, curd));
        } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
        return new AssignmentList(entryList);
    }


    /**
     * IDENTIFIER ("," IDENTIFIER)*
     */
    protected List<Token> columnList() {
        List<Token> tokenList = new ArrayList<>();
        do {
            consume(TokenType.IDENTIFIER, AstROErrorReporterDelegate.STATEMENT_columnList_PARSE);
            tokenList.add(tokenSequence.takePre());
        } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
        return tokenList;
    }


    protected Token consume(TokenType tokenType, int errorCode) {
        Token consume = tokenSequence.consume(tokenType);
        if (consume == null) {
            error(errorCode);
            return null;
        }
        return consume;
    }


}
