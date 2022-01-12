package cn.addenda.ro.grammar.lexical.scan;

import cn.addenda.ro.grammar.lexical.Scanner;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;
import cn.addenda.ro.grammar.lexical.token.TokenTypeLexemeMapping;

/**
 * @Author ISJINHAO
 * @Date 2021/2/23 19:58
 */
public abstract class AbstractScanner implements Scanner<TokenSequence> {

    protected static final int ERROR_CODE_SCANNER = 10002;

    private int index = 0;

    protected TokenSequence tokenSequence;

    protected CharSequence charSequence;

    public AbstractScanner(String source) {
        charSequence = new CharSequence(source);
        tokenSequence = new TokenSequence();
    }

    @Override
    public TokenSequence scanTokens() {
        while (!charSequence.isAtEnd()) {
            charSequence.recordLast();
            doScanTokens();
        }
        addToken(TokenType.EOF, null);
        return tokenSequence;
    }

    /**
     * 扫描 Token
     */
    abstract protected void doScanTokens();

    protected void addToken(TokenType type) {
        addToken(type, TokenTypeLexemeMapping.getLexeme(type));
    }

    protected void addToken(TokenType type, Object literal) {
        tokenSequence.addItem(new Token(type, literal, index));
        index++;
    }

}
