/*
 * EditorException.java
 *
 * Created on 29. Mai 2001, 16:29
 */

package editor;

/**
 *
 * @author  Andreas Lukosch / Natalja Froidenberg
 * @version 0.9
 */

public class EditorException extends java.lang.Exception {

    /**
 * Creates new <code>EditorException</code> without detail message.
     */
    public EditorException() {
    }


    /**
 * Constructs an <code>EditorException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EditorException(String msg) {
        super(msg);
    }
}


