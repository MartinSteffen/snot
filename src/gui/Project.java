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
    public String name;

    public boolean is_checked;   // indicates if it is checked
    public boolean is_well_defined;  
    public boolean is_active;    // indicates whether the Project is opened in the Editor
    public boolean has_changed;  // indicates whether the Project has been edited and needs to be saved
    
    /** constructor */
    public Project () {
        sfc = new SFC(null, null, null, null, null);
        name = "unknown";
//        fileName = "";
        is_checked = false;
        is_well_defined = false;
        is_active = true;   // on creation Project is opened in Editor!
        has_changed = false;
    }
    
    public String getName() {
        return name;
    }
    
    public void setIndex(int i) {
        if (editor != null)
            editor.setName(""+i);
    }
    
    public void setName(String _name) {
        name = new String(_name);
        if (editor != null)
            editor.setTitle("Editor  "+name);
    }
    
    public void setEditor(Editor _editor) {
        editor = _editor;
        editor.setTitle("Editor  "+name);
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
    
    public void printToStdout() {
        String text = "PROJECT INFO\nName: "+name+"; Is checked: "+is_checked+"; Is well defined: "+is_well_defined+"; Is active: "+is_active+"; Has changed: "+has_changed+"\n";
        System.out.print(text);
    }
    

}