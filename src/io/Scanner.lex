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
"program"             { return new Symbol(sym.PROGRAM);         }
"{"                   { return new Symbol(sym.LBRACE);          }
"}"                   { return new Symbol(sym.RBRACE);          }
"channel"             { return new Symbol(sym.CHANNEL);         }
"process"             { return new Symbol(sym.PROCESS);         }
"put"                 { return new Symbol(sym.PUT);             }
"get"                 { return new Symbol(sym.GET);             }
";"                   { return new Symbol(sym.SEMICOL);         }
"while"               { return new Symbol(sym.WHILE);           }
"if"                  { return new Symbol(sym.IF);              }
"("                   { return new Symbol(sym.LPAREN);          }
")"                   { return new Symbol(sym.RPAREN);          }
"else"                { return new Symbol(sym.ELSE);            }
"="                   { return new Symbol(sym.ASSIGN);          }
","                   { return new Symbol(sym.COMMA);           }
"assert"              { return new Symbol(sym.ASSERT);          }
"int"                 { return new Symbol(sym.INT);             }
"bool"                { return new Symbol(sym.BOOL);            }
"true"                { return new Symbol(sym.TRUE);            }
"false"               { return new Symbol(sym.FALSE);           }
"&&"                  { return new Symbol(sym.AND);             }
"||"                  { return new Symbol(sym.OR);              }
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


{name}   { return new Symbol(sym.NAME, yytext()); }
{number} { return new Symbol(sym.NUMBER, new Integer(yytext())); }
.        { System.err.println("[Modul parser] in Zeile "+yyline+": unbek. Zeichen: "+yytext()); }
