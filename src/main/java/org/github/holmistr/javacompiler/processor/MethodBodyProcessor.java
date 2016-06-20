package org.github.holmistr.javacompiler.processor;

import org.github.holmistr.javacompiler.constantpool.Constants;
import org.github.holmistr.javacompiler.element.AccessFlag;
import org.github.holmistr.javacompiler.element.ConstantPool;
import org.github.holmistr.javacompiler.element.Method;
import org.github.holmistr.javacompiler.element.expression.*;
import org.github.holmistr.javacompiler.element.expression.IdentifierExpression;
import org.github.holmistr.javacompiler.element.expression.ChainExpression;
import org.github.holmistr.javacompiler.element.expression.MethodCallExpression;
import org.github.holmistr.javacompiler.element.statement.Statement;
import org.github.holmistr.javacompiler.element.statement.VariableDeclaration;
import org.github.holmistr.javacompiler.instruction.*;
import org.github.holmistr.javacompiler.util.ByteCodeUtil;
import org.github.holmistr.javacompiler.util.TypeUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class MethodBodyProcessor {

    private ConstantPool constantPool;
    private Method method;
    private Map<String, Variable> localVariables = new HashMap<>();

    private int maxStack = 0;
    private int maxLocals = 0;

    private int currentStack = 0;
    private int currentLocals = 0;

    public MethodBodyProcessor(ConstantPool constantPool, Method method) {
        this.constantPool = constantPool;
        this.method = method;

        currentStack = method.getMaxStack() != 0 ? method.getMaxStack() : 0;
        currentLocals = method.getMaxLocals() != 0 ? method.getMaxLocals() : 0;

        if (!method.getAccessFlags().contains(AccessFlag.STATIC)) { //if it's instance method, "this" is passed to the local variables
            currentLocals = 1;
            Variable thisVariable = new Variable();
            thisVariable.setIdentifier("this");
            thisVariable.setType(ExpressionType.REFERENCE);
            thisVariable.setFramePosition(0);
            localVariables.put("this", thisVariable);
        }

        currentLocals += method.getParameters().size();
        setMaxLocals(currentLocals);
        setMaxStack(currentStack);
    }

    public void processBody() {
        for (Statement statement: method.getBody()) {
            processStatement(statement);
        }

        if (method.getReturnType() == null || method.getReturnType().equals("void")) { //if constructor or void method, add extra return
            Return returnInstruction = new Return();
            method.getInstructions().add(returnInstruction);
        }

        method.setMaxStack(maxStack);
        method.setMaxLocals(maxLocals);
    }

    private void processStatement(Statement statement) {
        if (statement instanceof VariableDeclaration) {
            processVariableDeclaration((VariableDeclaration) statement);
        } else if (statement instanceof ChainExpression) {
            processChain((ChainExpression) statement);
        } else if (statement instanceof Expression) {
            processExpression((Expression) statement);
        } else {
            throw new RuntimeException("Fatal error, unknown statement type.");
        }
    }

    private void processVariableDeclaration(VariableDeclaration statement) {
        if (localVariables.containsKey(statement.getIdentifier())) {
            throw new IllegalStateException("Variable is already defined in this context");
        }

        Variable variable = new Variable();
        variable.setIdentifier(statement.getIdentifier());
        variable.setType(ExpressionType.parse(statement.getType()));
        if (variable.getType() == ExpressionType.REFERENCE) {
            variable.setClassName(statement.getType());
        }
        variable.setFramePosition(localVariables.size() != 0 ? localVariables.values().stream().mapToInt(var -> var.getFramePosition()).max().getAsInt() + 1 : 0);
        localVariables.put(statement.getIdentifier(), variable);

        if (statement.getValue() != null) {
            processStatement(statement.getValue());

            statement.getValue().resolveType(localVariables);
            Instruction instruction;
            if (statement.getValue().getType() == ExpressionType.INT || statement.getValue().getType() == ExpressionType.BOOLEAN) {
                instruction = new Istore(variable.getFramePosition());
            } else {
                instruction = new Astore(variable.getFramePosition());
            }

            method.getInstructions().add(instruction);
            setMaxStack(--currentStack);
            setMaxLocals(++currentLocals);
        }
    }

    private void processChain(ChainExpression statement) {
        List<Expression> chain = statement.getChain();
        String caller = ((IdentifierExpression) chain.get(0)).getValue();

        try {
            Class clazz = Class.forName(TypeUtil.getFullNameInDotFormat(caller));
        } catch (RuntimeException | ClassNotFoundException e) { //it's not class name (ergo static call or static field access, it must be variable access
            // load the reference and store the class name as caller
            Variable variable = localVariables.get(caller);
            if (variable == null) {
                throw new IllegalStateException("Variable '" + caller +"' is not defined.");
            }

            if (variable.getType() != ExpressionType.REFERENCE) {
                throw new IllegalStateException("Cannot call methods on primitive type. Variable: " + caller);
            }

            Aload instruction = new Aload(variable.getFramePosition());
            method.getInstructions().add(instruction);
            setMaxStack(++currentStack);

            caller = TypeUtil.getFullNameInDotFormat(variable.getClassName());
        }

        for (int i = 1; i < chain.size(); i++) {
            Class clazz = null;
            try {
                clazz = Class.forName(TypeUtil.getFullNameInDotFormat(caller));
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); //TODO: handle this properly
            }
            String clazzName = TypeUtil.getFullNameInFileFormat(caller);

            Statement tmp = chain.get(i);
            if (tmp instanceof IdentifierExpression) { //field access
                String fieldName = ((IdentifierExpression) tmp).getValue();
                Field field;
                try {
                    field = clazz.getField(fieldName);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e); //TODO: handle this properly
                }

                int clazzNameIndex = constantPool.addUtf8(clazzName);
                int clazzIndex = constantPool.addClass(clazzNameIndex);
                int fieldNameIndex = constantPool.addUtf8(fieldName);
                int fieldTypeIndex = constantPool.addUtf8(ByteCodeUtil.createTypeDescriptor(field.getType().getCanonicalName()));
                int nameAndTypeIndex = constantPool.addNameAndType(fieldNameIndex, fieldTypeIndex);
                int fieldRefIndex = constantPool.addFieldRef(clazzIndex, nameAndTypeIndex);

                GetStatic instruction = new GetStatic(fieldRefIndex);
                method.getInstructions().add(instruction);
                setMaxStack(++currentStack);

                caller = field.getType().getCanonicalName();
            } else if (tmp instanceof MethodCallExpression) { //method call
                processMethodCall((MethodCallExpression) tmp, caller);
            }
        }
    }

    private String processMethodCall(MethodCallExpression statement, String caller) {
        for (Expression expression: statement.getArguments()) {
            processStatement(expression);
        }

        Class clazz = null;
        try {
            clazz = Class.forName(caller);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        java.lang.reflect.Method classMethod = null;
        try {
            classMethod = clazz.getMethod(statement.getName(), TypeUtil.getMethodParameterClasses(localVariables, statement.getArguments()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        int callerClassNameIndex = constantPool.addUtf8(TypeUtil.getFullNameInFileFormat(caller));
        int callerClassIndex = constantPool.addClass(callerClassNameIndex);

        int methodNameIndex = constantPool.addUtf8(statement.getName());
        int methodDescriptorIndex = constantPool.addUtf8(ByteCodeUtil.createMethodDescriptor(classMethod));
        int nameAndTypeIndex = constantPool.addNameAndType(methodNameIndex, methodDescriptorIndex);
        int methodRefIndex = constantPool.addMethodRef(callerClassIndex, nameAndTypeIndex);

        InvokeVirtual instruction = new InvokeVirtual(methodRefIndex);
        method.getInstructions().add(instruction);
        currentStack -= 1 + statement.getArguments().size();
        setMaxStack(currentStack);

        return null;
    }

    private void processExpression(Expression expression) {
        if (expression instanceof IdentifierExpression) {
            IdentifierExpression identifierExpression = (IdentifierExpression) expression;
            if (!localVariables.containsKey(identifierExpression.getValue())) {
                throw new IllegalStateException("Variable '" + identifierExpression.getValue() + "' is not defined.");
            }

            Variable variable = localVariables.get(identifierExpression.getValue());
            Instruction instruction;
            if (variable.getType() == ExpressionType.INT || variable.getType() == ExpressionType.BOOLEAN) {
                instruction = new Iload(variable.getFramePosition());
            } else {
                instruction = new Aload(variable.getFramePosition());
            }
            method.getInstructions().add(instruction);
            setMaxStack(++currentStack);

        } else if (expression instanceof MethodCallExpression) {
            processMethodCall((MethodCallExpression) expression, null);

        } else if (expression instanceof StringExpression) {
            StringExpression stringExpression = (StringExpression) expression;
            int valueIndex = constantPool.addUtf8(stringExpression.getValue());
            int stringIndex = constantPool.addString(valueIndex);

            Ldc instruction = new Ldc(stringIndex);
            method.getInstructions().add(instruction);
            setMaxStack(++currentStack);

        } else if (expression instanceof IntegerExpression) {
            IntegerExpression integerExpression = (IntegerExpression) expression;

            Bipush instruction = new Bipush(integerExpression.getValue());
            method.getInstructions().add(instruction);
            setMaxStack(++currentStack);

        } else if (expression instanceof BooleanExpression) {
            BooleanExpression booleanExpression = (BooleanExpression) expression;

            Bipush instruction = new Bipush(booleanExpression.getValue() ? 1 : 0);
            method.getInstructions().add(instruction);
            setMaxStack(++currentStack);

        } else if (expression instanceof AddExpression) {
            AddExpression addExpression = (AddExpression) expression;
            if (addExpression.getType() == null) {
                addExpression.resolveType(localVariables);
            }

            processExpression(addExpression.getLeftOperand());
            processExpression(addExpression.getRightOperand());
            if (addExpression.getType() == ExpressionType.INT) {
                Iadd instruction = new Iadd();
                method.getInstructions().add(instruction);
                setMaxStack(--currentStack);
            } else if (addExpression.getType() == ExpressionType.STRING) { //syntax sugar
                //TODO: implement
            }
        } else if (expression instanceof MultiplyExpression) {
            MultiplyExpression multiplyExpression = (MultiplyExpression) expression;
            if (multiplyExpression.getType() == null) {
                multiplyExpression.resolveType(localVariables);
            }

            processExpression(multiplyExpression.getLeftOperand());
            processExpression(multiplyExpression.getRightOperand());

            Imul instruction = new Imul();
            method.getInstructions().add(instruction);
            setMaxStack(--currentStack);
        } else if (expression instanceof SubtractExpression) {
            SubtractExpression subtractExpression = (SubtractExpression) expression;
            if (subtractExpression.getType() == null) {
                subtractExpression.resolveType(localVariables);
            }

            processExpression(subtractExpression.getLeftOperand());
            processExpression(subtractExpression.getRightOperand());

            Isub instruction = new Isub();
            method.getInstructions().add(instruction);
            setMaxStack(--currentStack);
        } else if (expression instanceof DivideExpression) {
            DivideExpression divideExpression = (DivideExpression) expression;
            if (divideExpression.getType() == null) {
                divideExpression.resolveType(localVariables);
            }

            processExpression(divideExpression.getLeftOperand());
            processExpression(divideExpression.getRightOperand());

            Idiv instruction = new Idiv();
            method.getInstructions().add(instruction);
            setMaxStack(--currentStack);
        } else if (expression instanceof LessThanExpression) {
            LessThanExpression lessThanExpression = (LessThanExpression) expression;
            if (lessThanExpression.getType() == null) {
                lessThanExpression.resolveType(localVariables);
            }

            processExpression(lessThanExpression.getLeftOperand());
            processExpression(lessThanExpression.getRightOperand());

            Bipush instruction2 = new Bipush(1);
            Bipush instruction4 = new Bipush(0);
            Goto instruction3 = new Goto(Goto.LENGTH + instruction4.getLength());
            IfIcmpGe instruction1 = new IfIcmpGe(IfIcmpGe.LENGTH + instruction2.getLength() + instruction3.getLength());

            method.getInstructions().add(instruction1);
            method.getInstructions().add(instruction2);
            method.getInstructions().add(instruction3);
            method.getInstructions().add(instruction4);
            currentStack -= 2;
            setMaxStack(currentStack);
        } else if (expression instanceof LessThanOrEqualExpression) {
            LessThanOrEqualExpression lessThanOrEqualExpression = (LessThanOrEqualExpression) expression;
            if (lessThanOrEqualExpression.getType() == null) {
                lessThanOrEqualExpression.resolveType(localVariables);
            }

            processExpression(lessThanOrEqualExpression.getLeftOperand());
            processExpression(lessThanOrEqualExpression.getRightOperand());

            Bipush instruction2 = new Bipush(1);
            Bipush instruction4 = new Bipush(0);
            Goto instruction3 = new Goto(Goto.LENGTH + instruction4.getLength());
            IfIcmpGt instruction1 = new IfIcmpGt(IfIcmpGt.LENGTH + instruction2.getLength() + instruction3.getLength());

            method.getInstructions().add(instruction1);
            method.getInstructions().add(instruction2);
            method.getInstructions().add(instruction3);
            method.getInstructions().add(instruction4);
            currentStack -= 2;
            setMaxStack(currentStack);
        } else if (expression instanceof GreaterThanExpression) {
            GreaterThanExpression greaterThanExpression = (GreaterThanExpression) expression;
            if (greaterThanExpression.getType() == null) {
                greaterThanExpression.resolveType(localVariables);
            }

            processExpression(greaterThanExpression.getLeftOperand());
            processExpression(greaterThanExpression.getRightOperand());

            Bipush instruction2 = new Bipush(1);
            Bipush instruction4 = new Bipush(0);
            Goto instruction3 = new Goto(Goto.LENGTH + instruction4.getLength());
            IfIcmpLe instruction1 = new IfIcmpLe(IfIcmpLe.LENGTH + instruction2.getLength() + instruction3.getLength());

            method.getInstructions().add(instruction1);
            method.getInstructions().add(instruction2);
            method.getInstructions().add(instruction3);
            method.getInstructions().add(instruction4);
            currentStack -= 2;
            setMaxStack(currentStack);
        }else if (expression instanceof GreaterThanOrEqualExpression) {
            GreaterThanOrEqualExpression greaterThanOrEqualExpression = (GreaterThanOrEqualExpression) expression;
            if (greaterThanOrEqualExpression.getType() == null) {
                greaterThanOrEqualExpression.resolveType(localVariables);
            }

            processExpression(greaterThanOrEqualExpression.getLeftOperand());
            processExpression(greaterThanOrEqualExpression.getRightOperand());

            Bipush instruction2 = new Bipush(1);
            Bipush instruction4 = new Bipush(0);
            Goto instruction3 = new Goto(Goto.LENGTH + instruction4.getLength());
            IfIcmpLt instruction1 = new IfIcmpLt(IfIcmpLt.LENGTH + instruction2.getLength() + instruction3.getLength());

            method.getInstructions().add(instruction1);
            method.getInstructions().add(instruction2);
            method.getInstructions().add(instruction3);
            method.getInstructions().add(instruction4);
            currentStack -= 2;
            setMaxStack(currentStack);
        } else if (expression instanceof NewExpression) {
            NewExpression newExpression = (NewExpression) expression;
            process(newExpression);
        }
    }

    private void process(NewExpression expression) {
        int classNameIndex = constantPool.addUtf8(TypeUtil.getFullNameInFileFormat(expression.getClassName()));
        int classIndex = constantPool.addClass(classNameIndex);

        New instruction = new New(classIndex);
        method.getInstructions().add(instruction);
        setMaxStack(++currentStack);

        Dup instruction2 = new Dup();
        method.getInstructions().add(instruction2);
        setMaxStack(++currentStack);

        expression.getArguments().stream().forEach(argument -> processStatement(argument));

        Class clazz = null;
        try {
            clazz = Class.forName(TypeUtil.getFullNameInDotFormat(expression.getClassName()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        java.lang.reflect.Constructor constructor = null;
        try {
            constructor = clazz.getConstructor(TypeUtil.getMethodParameterClasses(localVariables, expression.getArguments()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        int constructorNameIndex = constantPool.addUtf8(Constants.DEFAULT_CONSTRUCTOR);
        int constructorDescriptorIndex = constantPool.addUtf8(ByteCodeUtil.createMethodDescriptor(constructor));
        int nameAndTypeIndex = constantPool.addNameAndType(constructorNameIndex, constructorDescriptorIndex);
        int methodRefIndex = constantPool.addMethodRef(classIndex, nameAndTypeIndex);

        InvokeSpecial instruction3 = new InvokeSpecial(methodRefIndex);
        method.getInstructions().add(instruction3);
    }

    private void setMaxStack(int currentStack) {
        if (currentStack > maxStack) {
            maxStack = currentStack;
        }
    }

    private void setMaxLocals(int currentLocals) {
        if (currentLocals > maxLocals) {
            maxLocals = currentLocals;
        }
    }
}
