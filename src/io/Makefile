#----------------------------------------------------------------------
#	Io routines for Snot SFC's
#----------------------------------------------------------------------


################################################

LEXLOGFILE = JLex.log
PACKAGE    = io
################################################

all:	Parser.class

Parser.class:	Parser.java Yylex.java ParseException.java
	javac Parser.java sym.java Yylex.java ParseException.java

Yylex.java:		SFC.lex
	java JLex.Main SFC.lex >$(LEXLOGFILE)
	mv SFC.lex.java Yylex.java


Parser.java:	SFC.cup
	java java_cup.Main -parser Parser -nonterms -package $(PACKAGE) SFC.cup 



clean:
	\rm -f *.class *~ Yylex.java Scanner.lex.java sym.java Parser.java $(LEXLOGFILE) $(CUPLOGFILE)

localdoc:	parser.java	sym.java
	javadoc  -version -author -d $(LOCALDOCDIR) *.java

www:
	cp $(LOCALDOCDIR)/*.html $(WEBDIR)

# ------------------------------------------------------------------
#	$Log: not supported by cvs2svn $
#	Revision 1.2  2001/06/13 14:15:36  swprakt
#	*** empty log message ***
#	
# -------------------------------------------------------------------	









