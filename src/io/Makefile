#----------------------------------------------------------------------
#	Io routines for Snt SFC's
#----------------------------------------------------------------------


################################################

LEXLOGFILE = JLex.log
PACKAGE    = io
################################################

all:	Main.class


Main.class: Parser.class Main.java
	javac Main.java

Parser.class:	Parser.java Yylex.java ParseException.java
	javac Parser.java sym.java Yylex.java ParseException.java

Yylex.java:		SFC.lex
	java JLex.Main SFC.lex >$(LEXLOGFILE)
	mv SFC.lex.java Yylex.java


Parser.java:	SFC.cup
	java java_cup.Main -symbols sym -parser Parser -nonterms -package $(PACKAGE) SFC.cup 




tests: 
	cd test; make all

clean:
	\rm -f *.class *~ Yylex.java Scanner.lex.java sym.java Parser.java $(LEXLOGFILE) $(CUPLOGFILE)

localdoc:	parser.java	sym.java
	javadoc  -version -author -d $(LOCALDOCDIR) *.java

www:
	cp $(LOCALDOCDIR)/*.html $(WEBDIR)

# ------------------------------------------------------------------
#	$Log: not supported by cvs2svn $
#	Revision 1.6  2001/07/01 06:24:51  swprakt
#	istep is there
#	
#	Revision 1.5  2001/06/30 15:03:22  swprakt
#	OK
#	
#	Revision 1.4  2001/06/30 13:44:06  swprakt
#	OK
#	
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









