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
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.awt.Point;
import java.awt.Dimension;

import absynt.*;
import editor.Editor;

/**
 *  The Project keeps the SFC and a reference to the connected editor frame.
 *  It also keeps the name and status flags of the SFC.
 *
 * @author  Hans Theman and Ingo Schiller
 * @version $Revision: 1.23 $
 */
public class Project extends java.lang.Object implements Serializable {

    /** variable declarations */
    private SFC sfc = null;
    private transient Editor editor = null;
    private String name;

    private boolean is_named;   // determines if a name is set
    private boolean is_checked;   // indicates if it is checked
    private boolean is_only_bool;  // for the isOnlyBool() test of the checker group
    private boolean has_editor;

    private Point location = null; // the position of the editor frame
    private Dimension size = null; // the size of the editor frame
    private int state;      // the state ... (iconified, normal, ...)

    /** The standard constructor.
     *  It generates an empty SFC. All parameters of the SFC are null!
     *  The name is set to "unknown".
     */
    public Project () {
        sfc = new SFC();
        name = "unknown";
        is_named = false;
        is_checked = false;
        is_only_bool = false;
	has_editor = false;
       }



    public void restoreEnvironment(Editor e) {
//        Editor e = this.getEditor();
        if (e!=null && location!=null && size!=null) {
            e.setSize(size);
            e.setLocation(location);
            e.setState(state);
        }
    }

    public void setEnvironment() {
        Editor e = this.getEditor();
        if (e!=null) {
            this.size = e.getSize();
            this.location = e.getLocation();
            this.state = e.getState();
        }
    }

    public boolean isNamed() {
        return this.is_named;
    }

    public boolean hasEditor() {
        return this.has_editor;
    }

    /** Return the projet checked flag.
     *  @return is_checked  Indicates whether the project SFC is checked.
     */
    public boolean isChecked() {
        return this.is_checked;
    }

    /** Sets the project checked flag.
     *  @param status   The checked status of the SFC in the project.
     */
    public void setChecked(boolean status) {
        this.is_checked = status;
    }

    /** Returns the project isOnlyBool flag.
     *  @return is_only_bool The status of the SFC of the project.
     */
    public boolean isOnlyBool() {
        return this.is_only_bool;
    }

    /** Sets the project welldefined flag.
     *  @param status   The specified status of the SFC of the project.
     */
    public void setOnlyBool(boolean status) {
        this.is_only_bool= status;
    }


    /** Retruns the project name.
     *  @return name    The name of the project.
     */
    public String getName() {
        return this.name;
    }

    /** Retruns the project editor.
     *  If no editor is specified in the project, null is returned.
     *  @return editor  The connected editor.
     */
    public Editor getEditor() {
        return this.editor;
    }

    /** Retruns the project SFC.
     *  @return sfc The SFC that belongs to the project.
     */
    public SFC getSFC() {
        return this.sfc;
    }

    public void setSFC(SFC _sfc){
       this.sfc = _sfc;
    }


    public void exportSFC(File _file) throws Exception {
        if (_file == null)
            return;

        ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(_file));
        try {
            
	    outStream.writeObject(this); // throws IOException!!!!
        }
        finally {
            outStream.close(); // throws IOException!!!!
        }
        outStream.flush(); // throws IOException!!!!
        outStream.close(); // throws IOException!!!!

    }

    /**
     * The import sfc routine
     */

    public static Project importSFC(File _file) throws Exception {
        Project p = null;

        if (_file == null)
            throw new Exception("NullPointer in function readSFC()!");

        ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(_file));
        try {
           p = (Project)inStream.readObject();
        }
        finally {
            inStream.close();
        }
        inStream.close();
        p.is_checked = false;
        p.is_only_bool = false;

        return p;
    }

//##############################################
//
//  The Editor Interface
//
//##############################################



    /** Sets the name of the project.
     *  So far a editor is present in the project, the name is also set in the editor.
     *  @param _name    The desired name of the project.
     */
    public void setName(String _name) {
        name = _name;
        is_named = true;
        if (editor != null)
          editor.setSFCName(name);
    }

    /** Sets the editor in the project.
     *  While connecting an editor to the project, the project name is set in it.
     *  @param _editor  The editor in which the project is opened.
     */
    public void setEditor(Editor editor) {
        this.editor = editor;
	if (editor!=null) {
	    this.editor.setSFCName(this.name);
	    this.has_editor = true;
	}
//	else
//	    this.has_editor = false;
    }

    /** Returns the editor modified flag.
     *  This function is part of the editor interface.
     *  @return isModified  The editor's modified status
     */
    public boolean isModified() {
        if(editor!=null)
	    return editor.isModified();
	return false;
    }

    /** Sets the editor modified flag.
     *  This function is part of the editor interface.
     */
    public void clearModified() {
        if(editor!=null)
	    editor.clearModified();
    }

}
