/*
 * SMVTranslator.java
 *
 * Created on 14. Mai 2001, 22:08
 */

package smv;

import absynt.*;
import java.io.PrintStream;


/**
 *
 * @author  Kevin Köser / Tobias Kloss
 * @version 0.1
 */
public class SMVTranslator extends java.lang.Object {
    private SFC sfc;
    
    /**
     *
     * Creates new SMVTranslator 
     * @param sfc SFC-object to work on
     *
     */
    public SMVTranslator(SFC sfc) {
        this.sfc = sfc;
    }
    
    /**
     *
     * Prints SMV commands in Stream
     * @return Bytestream with SMV commands
     *
     */
    public PrintStream toStream() {
        return new PrintStream();
    }
    
}
