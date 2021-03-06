package cn.addenda.ro.grammar.lexical.token;

import cn.addenda.ro.error.ROError;

import java.util.Objects;

/**
 * @Author ISJINHAO
 * @Date 2021/2/20 17:03
 */
public class Token implements ROError {

    private final TokenType type;
    /**
     * 存 number、string、true、false、null
     */
    private Object literal;

    private int index = -1;

    public Token(TokenType type, Object literal) {
        this.type = type;
        this.literal = literal;
    }

    public Token(TokenType type, Object literal, int index) {
        this.type = type;
        this.literal = literal;
        this.index = index;
    }


    @Override
    public String toString() {
        return "Token {" +
                "type: " + type +
                ", literal: \"" + literal +
                "\"}";
    }

    public TokenType getType() {
        return type;
    }

    public Object getLiteral() {
        return literal;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type && Objects.equals(literal, token.literal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, literal);
    }

    public boolean checkSimplePrimary() {
        return this.type != null && (this.type.equals(TokenType.NUMBER) || this.type.equals(TokenType.STRING) ||
                this.type.equals(TokenType.FALSE) || this.type.equals(TokenType.TRUE) || this.type.equals(TokenType.NULL));
    }

    public Token deepClone() {
        return new Token(this.getType(), this.getLiteral(), this.getIndex());
    }

}