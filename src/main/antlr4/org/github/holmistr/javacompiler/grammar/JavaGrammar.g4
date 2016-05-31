grammar JavaGrammar;


COMMENT:            '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT:        '//' .*? '\n' -> channel(HIDDEN);
WS :                [ \r\t\u000C\n]+ -> channel(HIDDEN);


TRUE:               'true';
FALSE:              'false';

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
INT:                Non_zero_digit Digits?;

fragment Non_zero_digit:    [1-9];
fragment Digits:            [0-9]+;
fragment FloatTypeSuffix:    [fF];



init: expression+;

expressionWithEof: expression EOF; // helper rule for testing purposes
expression: '(' expression ')'
    | expression (PLUS | MINUS) expression
    | expression (MULTIPLY | DIVIDE) expression
    | expression (LT | LTE | GT | GTE) expression
    | expression (EQUAL | NOT_EQUAL) expression
    | expression (AND | OR) expression
    | atom;

atom: number | bool;
number: (PLUS | MINUS)? numberHelper ;
numberHelper: INT | FLOAT;
bool: TRUE | FALSE;


