/*
 * SMVException.java
 *
 * Created on 28. Mai 2001, 22:14
 */

package smv;

import java.lang.Exception;

/**
 *
 * @author  Kevin Köser / Tobias Kloss
 * @version 0.1
 */
public class SMVException extends Exception {
    
    /**
     *
     * Creates new SMVException 
     *
     */
    public SMVException() {
	super("Fehler beim Übersetzen des SFCs nach SMV");
    }
}
