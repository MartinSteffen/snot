########################################################
## $Id: Makefile,v 1.2 2001-04-21 12:08:08 swprakt Exp $
########################################################





TEXDIR      = /home/ms/tex/inputs
TEXDIR2     = /home/ms/dc/tex/inputs
TEXDIR3     = ../tex/inputs
DOC         = main
HEVEA       = hevea
HEVEAOPTS   = -fix -exec xxdate.exe
HACHA       = hacha

WEBDIR      = /home/info/www/inf/deRoever/SS01/Java/Snot




.SUFFIXES:
.SUFFIXES: .c

test:
	true

clean:
	rm -f *~ *.dvi *.log *.aux *.ps *.html *motif.gif *.haux *.toc *.blg

%.dvi: *.tex
	latex ankuendigung.tex

ps:	main.dvi
	dvips -o main.ps main.dvi

smallps:	
	rm -f main-small.ps
	psnup -2  main.ps > main-small.ps


www:
	cp *.html  $(WEBDIR)




html: $(DOC).tex
	$(HEVEA) $(HEVEAOPTS) $(TEXDIR2)/ocamlweb.sty $(TEXDIR3)/handout-title.tex $(TEXDIR3)/macros.hva $(TEXDIR3)/macros.tex $(DOC).tex -o index.html
#	$(HACHA) -o index-tex.html $(DOC).html



