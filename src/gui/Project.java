/*
 * Project.java
 *
 * Created on 26. Mai 2001, 15:06
 */

package gui;

import java.util.*;
import java.lang.*;
import java.io.Serializable;
import java.io.File;
import absynt.*;
import editor.Editor;

/**
 *
 * @author  Hans Theman and Ingo Schiller
 * @version 
 */
public class Project extends java.lang.Object implements Serializable {

    /** variable declarations */
    private static SFC sfc = null;
    private static Editor editor = null;
    public static String name;
    public static String fileName;
    public static boolean is_checked;
    public static boolean is_well_defined;
    public static boolean is_active;
    public static boolean has_changed;
    
    /** constructor */
    public Project () {
        sfc = new SFC(null, null, null, null, null);
        name = " unknown ";
        fileName = "";
        is_checked = false;
        is_well_defined = false;
        is_active = true;   // on creation Project is opened in Editor!
        has_changed = false;
    }
    
    public void setEditor(Editor _editor) {
        editor = _editor;
    }
    
    public Editor getEditor() {
        if (editor != null)
            return editor;
        else 
            return null;
    }

    public SFC getSFC() {
        return sfc;
    }
    
    public void saveSFC(File file) {
    // throws exception!
    // Exception.getMessage() is displayed in ErrorPopup
        System.out.print(" saving SFC ...");
    }
        
    public static Project openSFC(File file) {
    // throws exception!
    // Exception.getMessage() is displayed in ErrorPopup
       System.out.print(" opening SFC ...");
       return new Project();
    }
    

}