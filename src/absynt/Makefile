#----------------------------------------------------------------------
#	Abstract syntax for Snot SFC's
#	-------------------------------
#----------------------------------------------------------------------


all:	$(patsubst %.java,%.class,$(wildcard *.java))
debug:	; make JAVACOPTS="-g" all

%.class:	%.java
		javac $(JAVACOPTS) $<


clean:
	\rm -f *.class *~

localdoc:	;
	javadoc  -version -author -d doc *.java