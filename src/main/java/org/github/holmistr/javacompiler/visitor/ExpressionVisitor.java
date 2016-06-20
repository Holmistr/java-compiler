package org.github.holmistr.javacompiler.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.github.holmistr.javacompiler.element.expression.*;
import org.github.holmistr.javacompiler.element.expression.ChainExpression;
import org.github.holmistr.javacompiler.grammar.JavaGrammarBaseVisitor;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ExpressionVisitor extends JavaGrammarBaseVisitor<Expression> {

    @Override
    public Expression visitStringConstruction(@NotNull JavaGrammarParser.StringConstructionContext ctx) {
        String stringWithoutQuotes = ctx.getText().substring(1, ctx.getText().length()-1);
        StringExpression expression = new StringExpression(stringWithoutQuotes);

        return expression;
    }

    @Override
    public Expression visitNumber(@NotNull JavaGrammarParser.NumberContext ctx) {
        int value = Integer.parseInt(ctx.getText());
        IntegerExpression expression = new IntegerExpression(value);

        return expression;
    }

    @Override
    public Expression visitBool(@NotNull JavaGrammarParser.BoolContext ctx) {
        boolean value = Boolean.parseBoolean(ctx.getText());
        BooleanExpression expression = new BooleanExpression(value);

        return expression;
    }

    @Override
    public Expression visitIdentifier(@NotNull JavaGrammarParser.IdentifierContext ctx) {
        String identifier = ctx.getText();
        IdentifierExpression expression = new IdentifierExpression(identifier);

        return expression;
    }

    @Override
    public Expression visitAdd(@NotNull JavaGrammarParser.AddContext ctx) {
        Expression leftOperand = ctx.expression(0).accept(this);
        Expression rightOperand = ctx.expression(1).accept(this);
        AddExpression expression = new AddExpression(leftOperand, rightOperand);

        return expression;
    }

    @Override
    public Expression visitMultiply(@NotNull JavaGrammarParser.MultiplyContext ctx) {
        Expression leftOperand = ctx.expression(0).accept(this);
        Expression rightOperand = ctx.expression(1).accept(this);
        MultiplyExpression expression = new MultiplyExpression(leftOperand, rightOperand);

        return expression;
    }

    @Override
    public Expression visitSubtract(@NotNull JavaGrammarParser.SubtractContext ctx) {
        Expression leftOperand = ctx.expression(0).accept(this);
        Expression rightOperand = ctx.expression(1).accept(this);
        SubtractExpression expression = new SubtractExpression(leftOperand, rightOperand);

        return expression;
    }

    @Override
    public Expression visitDivide(@NotNull JavaGrammarParser.DivideContext ctx) {
        Expression leftOperand = ctx.expression(0).accept(this);
        Expression rightOperand = ctx.expression(1).accept(this);
        DivideExpression expression = new DivideExpression(leftOperand, rightOperand);

        return expression;
    }

    @Override
    public Expression visitGt(@NotNull JavaGrammarParser.GtContext ctx) {
        Expression leftOperand = ctx.expression(0).accept(this);
        Expression rightOperand = ctx.expression(1).accept(this);
        GreaterThanExpression expression = new GreaterThanExpression(leftOperand, rightOperand);

        return expression;
    }

    @Override
    public Expression visitGte(@NotNull JavaGrammarParser.GteContext ctx) {
        Expression leftOperand = ctx.expression(0).accept(this);
        Expression rightOperand = ctx.expression(1).accept(this);
        GreaterThanOrEqualExpression expression = new GreaterThanOrEqualExpression(leftOperand, rightOperand);

        return expression;
    }

    @Override
    public Expression visitLt(@NotNull JavaGrammarParser.LtContext ctx) {
        Expression leftOperand = ctx.expression(0).accept(this);
        Expression rightOperand = ctx.expression(1).accept(this);
        LessThanExpression expression = new LessThanExpression(leftOperand, rightOperand);

        return expression;
    }

    @Override
    public Expression visitLte(@NotNull JavaGrammarParser.LteContext ctx) {
        Expression leftOperand = ctx.expression(0).accept(this);
        Expression rightOperand = ctx.expression(1).accept(this);
        LessThanOrEqualExpression expression = new LessThanOrEqualExpression(leftOperand, rightOperand);

        return expression;
    }

    @Override
    public Expression visitNew(@NotNull JavaGrammarParser.NewContext ctx) {
        String className = ctx.identifier().getText();

        List<Expression> arguments = new ArrayList<>();
        if (ctx.expressionList() != null) {
            ExpressionVisitor expressionVisitor = new ExpressionVisitor();
            arguments = ctx.expressionList().expression().stream()
                    .map(argument -> argument.accept(expressionVisitor))
                    .collect(Collectors.toList());
        }

        NewExpression expression = new NewExpression(className, arguments);
        return expression;
    }

    @Override
    public Expression visitChain(@NotNull JavaGrammarParser.ChainContext ctx) {
        List<Expression> chain = new ArrayList<>();
        bubbleDownChain(ctx, chain);

        ChainExpression chainExpressionStatement = new ChainExpression();
        chainExpressionStatement.setChain(chain);

        return chainExpressionStatement;
    }

    @Override
    public Expression visitMethodCall(@NotNull JavaGrammarParser.MethodCallContext ctx) {
        MethodCallExpression methodCall = new MethodCallExpression();
        methodCall.setName(ctx.expression().getText());

        if (ctx.expressionList() != null) {
            ExpressionVisitor expressionVisitor = new ExpressionVisitor();
            List<Expression> arguments = ctx.expressionList().expression().stream()
                    .map(argument -> argument.accept(expressionVisitor))
                    .collect(Collectors.toList());
            methodCall.setArguments(arguments);
        }

        return methodCall;
    }

    /**
     * When using chain operator '.', we want to create a chain of connected statement invocations
     * to allow chained expressions like System.out.println() etc.
     *
     * @param context
     * @param output
     */
    private void bubbleDownChain(ParseTree context, List<Expression> output) {
        if (!(context instanceof JavaGrammarParser.ChainContext)
                && !(context instanceof TerminalNode)) {
            Expression leaf = context.accept(new ExpressionVisitor());
            output.add(leaf);
            return;
        }

        for (int i = 0; i < context.getChildCount(); i++) {
            bubbleDownChain(context.getChild(i), output);
        }
    }
}
