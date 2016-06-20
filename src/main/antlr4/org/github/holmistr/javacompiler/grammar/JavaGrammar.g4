grammar JavaGrammar;


COMMENT:            '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT:        '//' .*? '\n' -> channel(HIDDEN);
WS :                [ \r\t\u000C\n]+ -> channel(HIDDEN);


//keywords
TRUE:               'true';
FALSE:              'false';

//operators
PLUS:               '+';
MINUS:              '-';
MULTIPLY:           '*';
DIVIDE:             '/';
LT:                 '<';
LTE:                '<=';
GT:                 '>';
GTE:                '>=';
EQUAL:              '==';
NOT_EQUAL:          '!=';
AND:                '&&';
OR:                 '||';

ESCAPED_QUOTE:      '\\"';

FLOAT:              Digits? '.' Digits FloatTypeSuffix? | Digits FloatTypeSuffix;
INT:                ('0' | Non_zero_digit Digits?);
STRING_CONSTRUCT:   '"' (ESCAPED_QUOTE | .)*? '"';

IDENTIFIER:         ('$' | Letter | '_')('$' | Letter| Digit | '_')*;

fragment Non_zero_digit:        [1-9];
fragment Digit:                 [0-9];
fragment Digits:                Digit+;
fragment FloatTypeSuffix:       [fF];
fragment Letter:                [a-zA-Z];
fragment Letters:               Letter+;



start: packageStatement? importStatement* clazz;

packageStatement: 'package' identifier ('.' identifier)* ';';

importStatement: 'import' identifier ('.' identifier)* '.*'? ';';

clazzWithEof: clazz EOF; //helper class for testing purposes
clazz: clazzAccessModifiers? 'class' identifier '{' (field | method)* '}';

clazzAccessModifiers: 'public';

field: fieldAndMethodAccessModifiers* variableDeclarationWithInitialization ';';

method: fieldAndMethodAccessModifiers* returnType? identifier '(' parametersDeclaration? ')' '{' methodBody '}' ;
returnType: identifier ('[' ']')* | 'void';

parametersDeclaration: parameterDeclaration (',' parameterDeclaration)*;
parameterDeclaration: variableDeclaration;

methodBody: statements;

fieldAndMethodAccessModifiers: 'public' | 'private' | 'static';

statementWithEof: statement EOF; //helper rule for testing purposes
statement: 'for' '(' forType ')' block
    | 'while' '(' expression ')' block
    | 'if' '(' expression ')' block ('else' block)?
    | 'break' ';'
    | 'continue' ';'
    | 'return' expression? ';'
    | switchConstruct
    | variableDeclarationWithInitialization ';'
    | statementExpression ';';
statementExpression: expression;
statementsWithEof: statements EOF; //helper rule for testing purposes
statements: statement*;

block : '{' statements '}' | statement;

forType: (expression | variableDeclarationWithInitialization)? ';' expression? ';' expression?;

switchConstruct: 'switch' '(' expression ')' '{' ('case' expression ':' statements)* ('default:' statements)? '}';

variableDeclaration: variableDeclarationType identifier;
variableDeclarationType: type | type ('[' ']')* ;
variableDeclarationWithInitialization: variableDeclaration ('=' expression)?;

expressionWithEof: expression EOF; // helper rule for testing purposes
expression: '(' expression ')'                      # parenthessesExpression
    | expression PLUS expression                    # add
    | expression MINUS expression                   # subtract
    | expression MULTIPLY expression                # multiply
    | expression DIVIDE expression                  # divide
    | expression LT expression                      # lt
    | expression LTE expression                     # lte
    | expression GT expression                      # gt
    | expression GTE expression                     # gte
    | expression (EQUAL | NOT_EQUAL) expression     # equality
    | expression (AND | OR) expression              # logical
    | expression ('++' | '--')                      # unaryAddition
    | ('!') expression                              # negation
    | expression ('[' expression ']')+              # array
    | expression '(' expressionList? ')'            # methodCall
    | 'new' identifier ( '(' expressionList? ')' | ('[' expression ']')+ )  # new
    | expression '=' expression                     # assignment
    | '(' type ')' expression                       # typeCast
    | expression '.' expression                     # chain
    | stringConstruct                               # stringConstruction
    | type                                          # atom
    ;

expressionList: expression (',' expression)*;

type: number
    | bool
    | identifier
    ;
number: (PLUS | MINUS)? numberHelper ;
numberHelper: INT | FLOAT;
bool: TRUE | FALSE;
stringConstruct: STRING_CONSTRUCT;
identifier: IDENTIFIER;


