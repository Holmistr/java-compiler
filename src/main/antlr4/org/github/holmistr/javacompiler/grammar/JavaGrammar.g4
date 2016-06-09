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

FLOAT:              Digits? '.' Digits FloatTypeSuffix? | Digits FloatTypeSuffix;
INT:                ('0' | Non_zero_digit Digits?);
STRING_CONSTRUCT:   '"' (~'"' | '\"')* '"';

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
clazz: clazzAccessModifiers? 'class' identifier '{' member* '}';

clazzAccessModifiers: 'public';

member: field | method;

field: fieldAndMethodAccessModifiers? fieldAndMethodOtherModifiers? variableDeclarationWithInitialization ';';

method: fieldAndMethodAccessModifiers? fieldAndMethodOtherModifiers? (identifier | 'void')? identifier '(' parametersDeclaration? ')' '{' methodBody '}' ;

parametersDeclaration: parameterDeclaration (',' parameterDeclaration)*;
parameterDeclaration: variableDeclaration;

methodBody: statements;

fieldAndMethodAccessModifiers: 'public' | 'private';
fieldAndMethodOtherModifiers: 'static';

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
statements: (statement)*;

block : '{' statements '}' | statement;

forType: (expression | variableDeclarationWithInitialization)? ';' expression? ';' expression?;

switchConstruct: 'switch' '(' expression ')' '{' ('case' expression ':' statements)* ('default:' statements)? '}';

variableDeclaration: type identifier ('[' ']')* | type ('[' ']')* identifier;
variableDeclarationWithInitialization: variableDeclaration ('=' expression)?;

expressionWithEof: expression EOF; // helper rule for testing purposes
expression: '(' expression ')'
    | expression (PLUS | MINUS) expression
    | expression (MULTIPLY | DIVIDE) expression
    | expression (LT | LTE | GT | GTE) expression
    | expression (EQUAL | NOT_EQUAL) expression
    | expression (AND | OR) expression
    | expression ('++' | '--')
    | ('!') expression
    | expression ('[' expression ']')+
    | expression '(' expressionList? ')'
    | 'new' identifier ( '(' expressionList? ')' | ('[' expression ']')+ )
    | expression '=' expression
    | '(' type ')' expression
    | expression '.' identifier
    | stringConstruct
    | type;

expressionList: expression (',' expression)*;

type: number | bool | identifier;
number: (PLUS | MINUS)? numberHelper ;
numberHelper: INT | FLOAT;
bool: TRUE | FALSE;
stringConstruct: STRING_CONSTRUCT;
identifier: IDENTIFIER;


