package io;

/**
 * Exception-Class for to report back parse-errors.
 *
 * @author Martin Steffen
 * @version $Id: ParseException.java,v 1.2 2001-06-13 14:17:24 swprakt Exp $
 *
 */

public class ParseException extends Exception{

    
    /**
     * Default-Konstruktor, generates vanilla exception
     */
    ParseException(){
        super("syntax error");
    }
	
    ParseException(String errString){
        super(errString);
    }

}
