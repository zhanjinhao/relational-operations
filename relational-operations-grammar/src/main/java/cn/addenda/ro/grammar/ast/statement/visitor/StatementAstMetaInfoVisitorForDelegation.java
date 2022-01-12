package cn.addenda.ro.grammar.ast.statement.visitor;


import cn.addenda.ro.grammar.ast.CurdVisitor;
import cn.addenda.ro.grammar.ast.statement.*;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author ISJINHAO
 * @Date 2021/4/6 23:31
 */
public class StatementAstMetaInfoVisitorForDelegation extends StatementVisitorForDelegation<Set<Token>> {

    public StatementAstMetaInfoVisitorForDelegation(CurdVisitor<Set<Token>> client) {
        super(client);
    }

    @Override
    public Set<Token> visitWhereSeg(WhereSeg whereSeg) {
        return whereSeg.getLogic().accept(this);
    }

    @Override
    public Set<Token> visitLogic(Logic logic) {
        return visitBinary(logic);
    }

    @Override
    public Set<Token> visitComparison(Comparison comparison) {
        return visitBinary(comparison);
    }

    @Override
    public Set<Token> visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        return visitBinary(binaryArithmetic);
    }

    @Override
    public Set<Token> visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        return unaryArithmetic.getCurd().accept(client);
    }

    @Override
    public Set<Token> visitLiteral(Literal literal) {
        Token value = literal.getValue();
        Set<Token> set = new HashSet<>();
        if (value != null) {
            set.add(value);
        }
        return set;
    }


    @Override
    public Set<Token> visitGrouping(Grouping grouping) {
        return grouping.getCurd().accept(client);
    }

    @Override
    public Set<Token> visitIdentifier(Identifier identifier) {
        Set<Token> set = new HashSet<>();
        set.add(identifier.getName());
        return set;
    }

    @Override
    public Set<Token> visitFunction(Function function) {
        Set<Token> set = new HashSet<>();
        List<Curd> parameterList = function.getParameterList();
        if (parameterList == null) {
            return set;
        }
        for (Curd curd : parameterList) {
            // 这里的条件是不包含 Literal 的，因为 literal 是文本，不是需要引用的列
            if (curd instanceof Function || curd instanceof Identifier || curd instanceof BinaryArithmetic
                    || curd instanceof UnaryArithmetic) {
                set.addAll(curd.accept(this));
            }
        }
        return set;
    }

    @Override
    public Set<Token> visitAssignmentList(AssignmentList assignmentList) {
        List<AssignmentList.Entry> entryList = assignmentList.getEntryList();

        Set<Token> set = entryList.stream().map(AssignmentList.Entry::getColumnName).collect(Collectors.toSet());
        Set<Token> valueSet = entryList.stream()
                .map(item -> item.getValue().accept(client))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        set.addAll(valueSet);
        return set;
    }

    @Override
    public Set<Token> visitTimeInterval(TimeInterval timeInterval) {
        return new HashSet<>();
    }

    @Override
    public Set<Token> visitTimeUnit(TimeUnit timeUnit) {
        return new HashSet<>();
    }

    @Override
    public Set<Token> visitBinary(Binary binary) {
        Set<Token> set = new HashSet<>();
        Set<Token> leftValue = binary.getLeftCurd().accept(client);
        if (leftValue != null) {
            set.addAll(leftValue);
        }
        Curd right = binary.getRightCurd();
        if (right != null) {
            Set<Token> rightValue = right.accept(client);
            if (rightValue != null && !rightValue.isEmpty()) {
                set.addAll(rightValue);
            }
        }
        return set;
    }

    @Override
    public Set<Token> visitIsNot(IsNot isNot) {
        return new HashSet<>();
    }

}
