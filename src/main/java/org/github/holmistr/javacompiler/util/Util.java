package org.github.holmistr.javacompiler.util;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.github.holmistr.javacompiler.grammar.JavaGrammarLexer;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class with commonly used methods.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Util {

    public static JavaGrammarParser createGrammarParserFromFile(String filename) {
        ANTLRInputStream input = null;
        try {
            InputStream is = new FileInputStream(filename);
            input = new ANTLRInputStream(is);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read the file " + filename, e);
        }

        JavaGrammarLexer lexer = new JavaGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaGrammarParser parser = new JavaGrammarParser(tokens);

        return parser;
    }

    public static JavaGrammarParser createGrammarParserFromString(String string) {
        ANTLRInputStream input = new ANTLRInputStream(string);
        JavaGrammarLexer lexer = new JavaGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaGrammarParser parser = new JavaGrammarParser(tokens);

        return parser;
    }

    //public static String getFullClassName() {

    //}
}
