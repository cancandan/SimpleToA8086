grammar Simple;

prog:   stm+ EOF;

stm :   ID ':=' expr            #assign
    |   'print' expr            #print
    |   'if' expr 'then' stm    #ifthen
    |   'while' expr 'do' stm   #whiledo
    |   'begin' opt_stmts 'end' #beginend
    ;

opt_stmts : stmt_list?;

stmt_list : stm (';' stm)*;

expr:   expr op=MUL expr        #mul
    |   expr op=DIV expr        #div
    |   expr op=MOD expr        #mod
    |   expr op=ADD expr        #add
    |   expr op=SUB expr        #sub
    |   INT                     #int
    |   ID                      #id
    |   '(' expr ')'            #parens
    ;

MUL :   '*' ;
DIV :   '/' ;
MOD :   'mod';
ADD :   '+' ;
SUB :   '-' ;
ID  :   [a-zA-Z]+ ;      // match identifiers
INT :   [0-9]+ ;         // match integers
WS: [ \r\n\t] + -> skip;
