package io;

/**
 * Exception-Class for to report back oarse-errors 
 * weiterzuleiten.
 *
 * @author Martin Steffen
 * @version $Id: ParseException.java,v 1.1 2001-06-13 14:11:20 swprakt Exp $
 *
 */

public class ParseException extends Exception{

    
    /**
     * Default-Konstruktor, erzeugt Exception mit Fehler: "syntax error"
     */
    ParseException(){
        super("syntax error");
    }
	
    /**
     * Konstruktor, erzeugt Exception mit Fehler errString
     * @parameter 
     * - errString: Nachricht
     */
    ParseException(String errString){
        super(errString);
    }

}
