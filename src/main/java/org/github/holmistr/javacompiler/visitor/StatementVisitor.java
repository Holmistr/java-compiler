package org.github.holmistr.javacompiler.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.github.holmistr.javacompiler.element.statement.Statement;
import org.github.holmistr.javacompiler.element.statement.VariableDeclaration;
import org.github.holmistr.javacompiler.grammar.JavaGrammarBaseVisitor;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;

/**
 * Visitor for statements -  method body.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class StatementVisitor extends JavaGrammarBaseVisitor<Statement> {

    @Override
    public Statement visitStatementExpression(@NotNull JavaGrammarParser.StatementExpressionContext ctx) {
        return ctx.accept(new ExpressionVisitor());
    }

    @Override
    public Statement visitVariableDeclarationWithInitialization(@NotNull JavaGrammarParser.VariableDeclarationWithInitializationContext ctx) {
        VariableDeclaration statement = new VariableDeclaration();
        statement.setIdentifier(ctx.variableDeclaration().identifier().getText());
        statement.setType(ctx.variableDeclaration().variableDeclarationType().type().getText());

        if (ctx.expression() != null) {
            statement.setValue(ctx.expression().accept(new ExpressionVisitor()));
        }

        return statement;
    }

    @Override
    public Statement visitChildren(@NotNull RuleNode node) {
        Statement result = defaultResult();
        int n = node.getChildCount();
        for (int i=0; i<n; i++) {
            if (!shouldVisitNextChild(node, result) || node.getChild(i) instanceof TerminalNode) {
                break;
            }

            ParseTree c = node.getChild(i);
            Statement childResult = c.accept(this);
            result = aggregateResult(result, childResult);
        }

        return result;
    }
}
