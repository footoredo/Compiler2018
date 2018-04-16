grammar Mx;

compilationUnit
  : (classDeclaration | methodDeclaration | variableDeclaration)* EOF
  ;

expression
  : primary
  | expression '.' Identifier
  | expression '[' expression ']'
  | expression '(' expressionList? ')'
  | 'new' creator
  | '(' expression ')'
  | expression ('++' | '--')
  | ('+'|'-'|'++'|'--') expression
  | ('~'|'!') expression
  | expression ('*'|'/'|'%') expression
  | expression ('+'|'-') expression
  | expression ('<=' | '>=' | '>' | '<') expression
  | expression ('==' | '!=') expression
  | expression '&' expression
  | expression '^' expression
  | expression '|' expression
  | expression '&&' expression
  | expression '||' expression
  | <assoc=right> expression '=' expression
  ;

expressionList
  : expression (',' expression)*
  ;

creator
  : primitiveType
  | classType arguements?
  | arrayCreator
  ;

arrayCreator
  : baseTypeSpec ('[' expression ']')* ('[' ']')*
  ;

arguements
  : '(' expressionList? ')'
  ;

primary
  : Identifier
  | literal
  ;

literal
  : IntLiteral
  | StringLiteral
  | BoolLiteral
  | 'null'
  ;

IntLiteral
  : Sign? Digits
  ;

fragment
Sign
  : '+' | '-'
  ;

fragment
Digits
  : [0-9]+
  ;

BoolLiteral
  : 'true' | 'false'
  ;

StringLiteral
  : '"' StringCharacters? '"'
  ;

fragment
StringCharacters
  : StringCharacter+
  ;

fragment
StringCharacter
  : ~["\\]
  | EscapeSequence
  ;

fragment
EscapeSequence
  : '\\' [btnfr"'\\]
  ;

variableDeclaration
  : typeSpec variableDeclarator ';'
  ;

variableDeclarator
  : variableDeclaratorId '=' variableInitializer
  ;

variableDeclaratorId
  : Identifier
  ;

variableInitializer
  : expression
  ;

primitiveType
  : 'bool'
  | 'int'
  | 'string'
  ;

typeSpec
  : baseTypeSpec
  | typeSpec '[' ']'
  ;

baseTypeSpec
  : primitiveType
  | classType
  ;

classType
  : Identifier
  ;

classDeclaration
  : 'class' Identifier classBody
  ;

classBody
  : '{' memberDeclaration* '}'
  ;

memberDeclaration
  : methodDeclaration
  | constructorDeclaration
  | variableDeclaration
  ;

methodDeclaration
  : (typeSpec | 'void') Identifier parameters block
  ;

parameters
  : '(' parametersList? ')'
  ;

parametersList
  : parameter (',' parameter)*
  ;

parameter
  : typeSpec variableDeclaratorId
  ;

constructorDeclaration
  : Identifier parameters block
  ;

block
  : '{' blockStatement* '}'
  ;

blockStatement
  : localVariableDeclarationStatement
  | statement
  ;

statement
  : block
  | 'if' '(' expression ')' statement ('else' statement)?
  | 'for' '(' forControl ')' statement
  | 'while' '(' expression ')' statement
  | 'return' expression? ';'
  | 'break' ';'
  | 'continue' ';'
  | ';'
  | expression ';'
  ;


forControl
  : forInit? ';' expression? ';' forUpdate?
  ;

forInit
  : localVariableDeclaration
  | expressionList
  ;

forUpdate
  : expressionList
  ;

localVariableDeclarationStatement
  : localVariableDeclaration ';'
  ;

localVariableDeclaration
  : typeSpec variableDeclarator
  ;

Identifier
  : Letter LetterOrDigit*
  ;

fragment
Letter
  : [a-zA-Z_]
  ;

fragment
LetterOrDigit
  : [a-zA-Z0-9_]
  ;

WS  :  [ \t\r\n]+ -> channel(HIDDEN) // CodeBuff needs to see all whitespace (even after comments)
    ;

COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> channel(HIDDEN)
    ;
