#----------------------------------------------------------------------
# developers' top-level Makefile for the Siff project
# $Id: Makefile,v 1.6 2001-05-04 09:00:08 swprakt Exp $
#----------------------------------------------------------------------

# The targets installing gobal web-infos can be executed only
# by the maintainers

################################################

TOOLNAME      = Snot

WEBDIR        = /home/info/www/inf/deRoever/SS01/Java/$(TOOLNAME)
WEBDOCDIR     = $(WEBDIR)/doc
SNAPSHOTSDIR  = $(WEBDIR)/snapshots
LOCALDOCDIR   = doc
PACKAGEDIRS   = absynt

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
	(cd ./parser; make)
	(cd ./simulator; make)
	(cd ./modcheck; make)
	(cd ./position; make)



jar:
	jar cvf mist.jar ./absynt/*.class \
	    ./utils/*.class \
	    ./editor/*.class \
	    ./editor/einterface/*.class\
	    ./editor/images/*.gif\
	    ./gui/*.class \
	    ./checks1/*.class \
	    ./parser/*.class \
	    ./simulator/*.class \
	    ./modcheck/*.class \
	    ./position1/*.class \
	    ./position2/*.class \

jarinstall: 
	cp mist_v*.jar $(BASELINESDIR)


clean:
	(cd ./absynt; make clean)
	(cd ./utils; make clean)
	(cd ./editor; make clean)
	(cd ./gui; make clean)
	(cd ./checks; make clean)
	(cd ./parser; make clean)
	(cd ./modcheck; make clean)
	(cd ./simulator; make clean)
	(cd ./position; make clean)
	(cd $(LOCALDOCDIR); make clean)




%.class:	%.java
		javac $(JAVACOPTS) $<


doc:	;
	javadoc  -version -author -d $(WEBDOCDIR) $(PACKAGEDIRS)

localdoc:	;
	javadoc  -version -author -d $(LOCALDOCDIR) $(PACKAGEDIRS)
	cp ./simulator/specification.txt $(LOCALDOCDIR)





