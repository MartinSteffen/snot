package io;

import java_cup.runtime.Symbol;

%%
%cup
%line
%{
%}

comment = ("//".*)
space   = [\ \t\n\b\015]+
letter  = [A-Za-z_]
digit   = [0-9]
number  = {digit}+
name    = ({letter}({letter}|{digit})*)
%%


{comment}             { }
{space}               { }
"begin"               { return new Symbol(sym.BEGIN);           }
"actions"             { return new Symbol(sym.ACTIONS);         }
"steps"               { return new Symbol(sym.STEPS);           }
"init"                { return new Symbol(sym.INIT);            }
"transitions"         { return new Symbol(sym.TRANSITIONS);     }
"when"                { return new Symbol(sym.TRANSITIONS);     }
"->"                  { return new Symbol(sym.FROMTO);          }
"["                   { return new Symbol(sym.LBRACKET);        }
"]"                   { return new Symbol(sym.RBRACKET);        }
";"                   { return new Symbol(sym.SEM);             }
"("                   { return new Symbol(sym.LPAREN);          }
")"                   { return new Symbol(sym.RPAREN);          }
"="                   { return new Symbol(sym.ASSIGN);          }
","                   { return new Symbol(sym.COMMA);           }
"int"                 { return new Symbol(sym.INT);             }
"bool"                { return new Symbol(sym.BOOL);            }
"true"                { return new Symbol(sym.TRUE);            }
"false"               { return new Symbol(sym.FALSE);           }
"&&"                  { return new Symbol(sym.AND);             }
"||"                  { return new Symbol(sym.OR);              }
"and"                 { return new Symbol(sym.AND);             }
"or"                  { return new Symbol(sym.OR);              }
"/"                   { return new Symbol(sym.DIV);             }
"=="                  { return new Symbol(sym.EQ);              }
">="                  { return new Symbol(sym.GEQ);             }
">"                   { return new Symbol(sym.GREATER);         }
"<="                  { return new Symbol(sym.LEQ);             }
"<"                   { return new Symbol(sym.LESS);            }
"-"                   { return new Symbol(sym.MINUS);           }
"!"                   { return new Symbol(sym.NEG);             }
"+"                   { return new Symbol(sym.PLUS);            }
"*"                   { return new Symbol(sym.TIMES);           }
"!="                  { return new Symbol(sym.NEQ);             }


{name}                { return new Symbol(sym.NAME, yytext()); }
{number}              { return new Symbol(sym.NUMBER, new Integer(yytext())); }
.                     { System.err.println("[Modul io] in line "+yyline+": unknow char: "+yytext()); }
