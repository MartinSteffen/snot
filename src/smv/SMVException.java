/*
 * SMVException.java
 *
 * Created on 28. Mai 2001, 22:14
 */

package smv;

import java.lang.Exception;

/**
 *
 * @author  Kevin Koeser / Tobias Kloss
 * @version 0.2
 */
public class SMVException extends Exception {
    
    /**
     *
     * Creates new SMVException 
     *
     */
    public SMVException(String s) {
	super(s);
    }
}
