#----------------------------------------------------------------------
#	Io routines for Snot SFC's
#----------------------------------------------------------------------


################################################

LEXLOGFILE = JLex.log
PACKAGE    = io
################################################

all:	Parser.class

Parser.class:	Parser.java Yylex.java ParseException.java
	javac Parser.java Sym.java Yylex.java ParseException.java

Yylex.java:		SFC.lex
	java JLex.Main SFC.lex >$(LEXLOGFILE)
	mv SFC.lex.java Yylex.java


Parser.java:	SFC.cup
	java java_cup.Main -symbols Sym -parser Parser -nonterms -package $(PACKAGE) SFC.cup 



clean:
	\rm -f *.class *~ Yylex.java Scanner.lex.java Sym.java Parser.java $(LEXLOGFILE) $(CUPLOGFILE)

localdoc:	parser.java	Sym.java
	javadoc  -version -author -d $(LOCALDOCDIR) *.java

www:
	cp $(LOCALDOCDIR)/*.html $(WEBDIR)

# ------------------------------------------------------------------
#	$Log: not supported by cvs2svn $
#	Revision 1.3  2001/06/30 12:19:35  swprakt
#	I renamed the files
#	
#		Scanner.lex Scanner.cup
#	
#	to
#	
#		SFC.lex SFC.cup
#	
#	to make the naming more conventional (also the grammar-file is not a scanner.)
#	
#	
#	[Steffen]
#	
#	Revision 1.2  2001/06/13 14:15:36  swprakt
#	*** empty log message ***
#	
# -------------------------------------------------------------------	









