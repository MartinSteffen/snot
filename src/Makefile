#----------------------------------------------------------------------
# developers' top-level Makefile for the Snot project
# $Id: Makefile,v 1.20 2001-05-23 13:06:15 swprakt Exp $
#----------------------------------------------------------------------

# The targets installing gobal web-infos can be executed only
# by the maintainers

####xxxx#y############################################

TOOLNAME      = Snot

WEBDIR        = /home/info/www/inf/deRoever/SS01/Java/$(TOOLNAME)
WEBDOCDIR     = $(WEBDIR)/doc
SNAPSHOTSDIR  = $(WEBDIR)/snapshots
LOCALDOCDIR   = doc
PACKAGEDIRS   = absynt\
	        utils\
	        editor\
	        gui\
	        smv\
#	        checks\


################################################



all:	packets $(patsubst %.java,%.class,$(wildcard *.java))

debug:	; make JAVACOPTS="-g" all

run:	all
	java gui.GUI



packets:
	(cd ./absynt; make)
	(cd ./utils; make)
	(cd ./editor; make)
	(cd ./gui; make)
	(cd ./checks; make)
	(cd ./smv; make)
	(cd ./simulator; make)

jar:
	jar cvf snot.jar ./absynt/*.class \
	    ./utils/*.class \

jarinstall: 
	cp mist_v*.jar $(BASELINESDIR)


clean:
	(cd ./absynt; make clean)
	(cd ./utils; make clean)
	(cd ./editor; make clean)
	(cd ./gui; make clean)
	(cd ./checks; make clean)
	(cd ./smv; make clean)
	(cd ./simulator; make clean)



%.class:	%.java
		javac $(JAVACOPTS) $<


doc:	;
	javadoc  -version -author -d $(WEBDOCDIR) $(PACKAGEDIRS)

localdoc:	;
	javadoc  -version -author -d $(LOCALDOCDIR) $(PACKAGEDIRS)
	cp ./simulator/specification.txt $(LOCALDOCDIR)





