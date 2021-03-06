package cn.addenda.ro.grammar.ast.create;

import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.create.visitor.InsertGrammarValidator;
import cn.addenda.ro.grammar.ast.retrieve.Select;
import cn.addenda.ro.grammar.ast.retrieve.SelectType;
import cn.addenda.ro.grammar.ast.retrieve.SingleSelectType;
import cn.addenda.ro.grammar.ast.statement.AssignmentList;
import cn.addenda.ro.grammar.ast.statement.Curd;
import cn.addenda.ro.grammar.ast.statement.StatementParser;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2021/4/3 17:50
 */
public class InsertParser extends StatementParser {

    public InsertParser(TokenSequence tokenSequence, FunctionEvaluator functionEvaluator) {
        super(tokenSequence, functionEvaluator);
    }

    /**
     * insert              ->  "insert" (constrict)? "into" IDENTIFIER (((insertValuesRep | insertSetRep) onDuplicateKey?) | (insertSelectRep))
     * constrict           ->  ignore
     * insertValuesRep     ->  "(" IDENTIFIER (, IDENTIFIER)* ")" "values" ("(" binaryArithmetic (, binaryArithmetic)* ")") ("," "(" binaryArithmetic (, binaryArithmetic)* ")")*
     * insertSetRep        ->  "set" assignmentList
     * insertSelectRep     ->  ("(" IDENTIFIER (, IDENTIFIER)* ")")? select
     * binaryArithmetic ↑  ->  unaryArithmetic (("+" | "-" | "*" | "/") unaryArithmetic)*
     * unaryArithmetic ↑   ->  ("!"|"-") unaryArithmetic | primary
     * primary ↑           ->  #{xxx} | ? | "true" | "false" | "null" | NUMBER | STRING | IDENTIFIER | grouping | function
     * function ↑          ->  functionName "(" binaryArithmetic? ("," binaryArithmetic)* ")"
     * onDuplicateKey      ->  "on" "duplicate" "key" "update" assignmentList
     * assignmentList ↑    ->  (IDENTIFIER "=" binaryArithmetic) ("," IDENTIFIER "=" binaryArithmetic)*
     * columnList ↑	       ->  IDENTIFIER ("," IDENTIFIER)*
     */
    @Override
    public Curd parse() {
        Curd insert = insert();
        consume(TokenType.EOF, AstROErrorReporterDelegate.CURD_not_end_PARSE);
        insert.accept(new InsertGrammarValidator(this.errorReporterDelegate));
        return insert;
    }

    private Curd insert() {
        consume(TokenType.INSERT, AstROErrorReporterDelegate.INSERT_insert_PARSE);

        Token token = tokenSequence.takeCur();
        Token constrict = null;
        if (TokenType.IGNORE.equals(token.getType())) {
            constrict = token;
            tokenSequence.advance();
        }
        consume(TokenType.INTO, AstROErrorReporterDelegate.INSERT_insert_PARSE);

        Token tableName = tokenSequence.takeCur();
        tokenSequence.advance();

        InsertType insertType;
        Curd curd;
        if (tokenSequence.curEqual(TokenType.SET)) {
            curd = insertSetRep();
            insertType = InsertType.SET;
        } else {
            // valuesRep or selectRep
            consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.INSERT_insert_PARSE);
            List<Token> tokens = columnList();
            consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.INSERT_insert_PARSE);
            if (tokenSequence.curEqual(TokenType.SELECT)) {
                curd = insertSelectRep(tokens);
                insertType = InsertType.SELECT;
            } else if (tokenSequence.curEqual(TokenType.VALUES)) {
                curd = valuesRep(tokens);
                insertType = InsertType.VALUES;
            } else {
                error(AstROErrorReporterDelegate.INSERT_insert_PARSE);
                return null;
            }
        }

        if (!tokenSequence.checkExistsToken()) {
            return new Insert(constrict, tableName, curd, insertType);
        }

        return new Insert(constrict, tableName, curd, onDuplicateKey(), insertType);
    }

    private Curd insertSelectRep(List<Token> tokens) {
        InsertSelectParser insertSelectParser = new InsertSelectParser(this.tokenSequence, getFunctionEvaluator());
        Select select = (Select) insertSelectParser.parse();
        InsertSelectRep insertSelectRep = new InsertSelectRep(tokens, select);
        select.setSelectType(SelectType.INSERT);
        saveSingleSelectContext(select, SingleSelectType.INSERT);
        return insertSelectRep;
    }


    private Curd onDuplicateKey() {
        consume(TokenType.ON, AstROErrorReporterDelegate.INSERT_onDuplicateKey_PARSE);
        consume(TokenType.DUPLICATE, AstROErrorReporterDelegate.INSERT_onDuplicateKey_PARSE);
        consume(TokenType.KEY, AstROErrorReporterDelegate.INSERT_onDuplicateKey_PARSE);
        consume(TokenType.UPDATE, AstROErrorReporterDelegate.INSERT_onDuplicateKey_PARSE);

        Curd curd = assignmentList();
        return new OnDuplicateKey(curd);
    }


    /**
     * ("(" IDENTIFIER (, IDENTIFIER)* ")")? "values" ("(" binaryArithmetic (, binaryArithmetic)* ")") ("," "(" binaryArithmetic (, binaryArithmetic)* ")")*
     *
     * @param tokenList
     */
    private Curd valuesRep(List<Token> tokenList) {

        consume(TokenType.VALUES, AstROErrorReporterDelegate.INSERT_insertValuesRep_PARSE);

        int currentCount = -1;
        if (tokenList != null) {
            currentCount = tokenList.size();
        }
        List<List<Curd>> curdStatementsList = new ArrayList<>();
        // 处理多行的值
        do {
            List<Curd> curdList = new ArrayList<>();
            // 处理一行的值
            consume(TokenType.LEFT_PAREN, AstROErrorReporterDelegate.INSERT_insertValuesRep_PARSE);
            do {
                Curd binaryArithmetic = binaryArithmetic();
                curdList.add(binaryArithmetic);
            } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
            consume(TokenType.RIGHT_PAREN, AstROErrorReporterDelegate.INSERT_insertValuesRep_PARSE);
            if (curdList.size() == currentCount || currentCount == -1) {
                currentCount = curdList.size();
            } else {
                error(AstROErrorReporterDelegate.INSERT_insertValuesRep_PARSE);
            }
            curdStatementsList.add(curdList);
        } while (tokenSequence.equalThenAdvance(TokenType.COMMA));
        if (curdStatementsList.isEmpty()) {
            error(AstROErrorReporterDelegate.INSERT_insertValuesRep_PARSE);
        }
        return new InsertValuesRep(tokenList, curdStatementsList);
    }

    /**
     * "set" (IDENTIFIER "=" binaryArithmetic) ("," IDENTIFIER "=" binaryArithmetic)*
     */
    private Curd insertSetRep() {
        consume(TokenType.SET, AstROErrorReporterDelegate.INSERT_insertSetRep_PARSE);
        Curd curd = assignmentList();
        if (!(curd instanceof AssignmentList)) {
            error(AstROErrorReporterDelegate.INSERT_insertSetRep_PARSE);
        }
        AssignmentList assignmentList = (AssignmentList) curd;
        if (assignmentList.getEntryList() == null || assignmentList.getEntryList().isEmpty()) {
            error(AstROErrorReporterDelegate.INSERT_insertSetRep_PARSE);
        }
        return new InsertSetRep(curd);
    }


}
