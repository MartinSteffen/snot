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
    private SFC sfc = null;
    private Editor editor = null;
    private String name;

    public boolean is_checked;   // indicates if it is checked
    public boolean is_well_defined;  
    public boolean is_active;    // indicates whether the Project is opened in the Editor
    
    /** constructor */
    public Project () {
        sfc = new SFC(null, null, null, null, null);
        name = "unknown";
        is_checked = false;
        is_well_defined = false;
        is_active = true;   // on creation Project is opened in Editor!
    }
    
    public Project(SFC _sfc, Editor _editor, String _name, 
                    boolean _is_checked, boolean _is_well_defined, boolean _is_active) {
        sfc = _sfc;
        editor = _editor;
        name = _name;
        is_checked = _is_checked;
        is_well_defined = _is_well_defined;
        is_active = _is_active;
    }
    
    public void setName(String _name) {
        name = _name;
        if (editor != null)
            editor.setFilename(name);
    }
    
    public String getName() {
        return name;
    }

    public void setEditor(Editor _editor) {
        editor = _editor;
        editor.setFilename(name);
    }
    
    public Editor getEditor() {
        return editor;
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
    
/*    public void printToStdout() {
        String text = "PROJECT INFO\nName: "+name+"; Is checked: "+is_checked+"; Is well defined: "+is_well_defined+"; Is active: "+is_active+"; Has changed: "+is_modified+"\n";
        System.out.print(text);
    }
*/    

}