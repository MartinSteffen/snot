#----------------------------------------------------------------------
#	Io routines for Snot SFC's
#	-------------------------------
#----------------------------------------------------------------------



################################################

LEXLOGFILE = JLex.log
PACKAGE    = io
################################################

all:	Parser.class

Parser.class:	Parser.java	Yylex.java ParseException.java
	javac Parser.java sym.java Yylex.java ParseException.java
Yylex.java:		Scanner.lex
	java JLex.Main Scanner.lex >$(LEXLOGFILE)
	mv Scanner.lex.java Yylex.java


Parser.java:	Scanner.cup
	java java_cup.Main -parser Parser -nonterms -package $(PACKAGE) Scanner.cup 



clean:
	\rm -f *.class *~ Yylex.java Scanner.lex.java sym.java Parser.java $(LEXLOGFILE) $(CUPLOGFILE)

localdoc:	parser.java	sym.java
	javadoc  -version -author -d $(LOCALDOCDIR) *.java

www:
	cp $(LOCALDOCDIR)/*.html $(WEBDIR)



# -----------------------------------------------------------------------
#	$Log: not supported by cvs2svn $
#
# ------------------------------------------------------------------------	









