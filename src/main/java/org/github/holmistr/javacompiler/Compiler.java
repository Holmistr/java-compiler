package org.github.holmistr.javacompiler;

import org.antlr.v4.runtime.tree.ParseTree;
import org.github.holmistr.javacompiler.element.Class;
import org.github.holmistr.javacompiler.element.ConstantPool;
import org.github.holmistr.javacompiler.element.File;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;
import org.github.holmistr.javacompiler.processor.BaseProcessor;
import org.github.holmistr.javacompiler.util.Util;
import org.github.holmistr.javacompiler.visitor.ClassVisitor;
import org.github.holmistr.javacompiler.writer.ClassFileWriter;

import java.io.IOException;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Compiler {

    private String inputFile;
    private String outputFile;

    public Compiler(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public static void main(String[] args) throws Exception {
        //TODO: remove this
        /*if (args.length != 2) {
            throw new IllegalArgumentException("You must supply exactly one argument: the source code file.");
        }*/

        args = new String[]{"", "./src/test/resources/testSources/TestClass.txt"};
        String outputFile = args[1].substring(args[1].lastIndexOf('/') + 1, args[1].lastIndexOf('.')) + ".class";

        new Compiler(args[1], outputFile).compile();
    }

    private void compile() throws IOException {
        File file = parseFile();

        ConstantPool constantPool = new ConstantPool();
        BaseProcessor baseProcessor = new BaseProcessor(constantPool, file);
        baseProcessor.processFile();

        file.setConstantPool(constantPool);

        ClassFileWriter writer = new ClassFileWriter(outputFile);
        writer.write(file);

        System.out.println();
        //TODO: write the parsed input
    }

    private File parseFile() {
        JavaGrammarParser parser = Util.createGrammarParserFromFile(inputFile);
        ParseTree tree = parser.start(); // begin parsing at init rule
        ClassVisitor visitor = new ClassVisitor();
        Class clazz = visitor.visit(tree);

        File file = new File();
        file.setClazz(clazz);
        file.setFilename(inputFile);

        return file;
    }

}
