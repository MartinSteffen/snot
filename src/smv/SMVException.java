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
 * @version $Id: SMVException.java,v 1.4 2001-06-26 07:02:22 swprakt Exp $
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
