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

import absynt.*;
import editor.Editor;

/**
 *  The Project keeps the SFC and a reference to the connected editor frame.
 *  It also keeps the name and status flags of the SFC.
 *
 * @author  Hans Theman and Ingo Schiller
 *  <<<<<<< Project.java
 * @version $Revision: 1.11 $
 * =======
 * @version $Revision: 1.11 $
 *  >>>>>>> 1.10
 */
public class Project extends java.lang.Object implements Serializable {

    /** variable declarations */
    private SFC sfc = null;
    private transient Editor editor = null;
    private String name;

    private boolean is_checked;   // indicates if it is checked
    private boolean is_well_defined;  // ?????????????????????????
    private boolean is_active;    // indicates whether the Project is opened in the Editor

    /** The standard constructor.
     *  It generates an empty SFC. All parameters of the SFC are null!
     *  The name is set to "unknown".
     *  Its active flag is set to true by default, because it is opened in the Editor.
     */
    public Project () {
        sfc = new SFC(null, null, null, null, null);
        name = "unknown";
        is_checked = false;
        is_well_defined = false;
        is_active = true;   // on creation Project is opened in Editor!
    }

    /** The constructor.
     *  Creates a new Project and initializes it whith these parameters.
     */
    public Project(SFC _sfc, Editor _editor, String _name,
                    boolean _is_checked, boolean _is_well_defined, boolean _is_active) {
        sfc = _sfc;
        editor = _editor;
        name = _name;
        is_checked = _is_checked;
        is_well_defined = _is_well_defined;
        is_active = _is_active;
    }

    /** Return the projet checked flag.
     *  @return is_checked  Indicates whether the project SFC is checked.
     */
    public boolean isChecked() {
        return is_checked;
    }

    /** Sets the project checked flag.
     *  @param status   The checked status of the SFC in the project.
     */
    public void setChecked(boolean status) {
        is_checked = status;
    }

    /** Returns the project welldefined flag.
     *  @return is_well_defined The status of the SFC of the project.
     */
    public boolean isWellDefined() {
        return is_well_defined;
    }

    /** Sets the project welldefined flag.
     *  @param status   The specified status of the SFC of the project.
     */
    public void setWellDefined(boolean status) {
        is_well_defined = status;
    }

    /** Returns the project active flag.
     *  @return is_active   Indicates whether the SFC is currently displayed in an editor window.
     */
    public boolean isActive() {
        return is_active;
    }

    /** Sets the project active flag.
     *  @param status   Indicates whether the SFC is currently displayed in an editor window.
     */
    public void setActive(boolean status) {
        is_active = status;
    }


    /** Retruns the project name.
     *  @return name    The name of the project.
     */
    public String getName() {
        return name;
    }

    /** Retruns the project editor.
     *  If no editor is specified in the project, null is returned.
     *  @return editor  The connected editor.
     */
    public Editor getEditor() {
        return editor;
    }

    /** Retruns the project SFC.
     *  @return sfc The SFC that belongs to the project.
     */
    public SFC getSFC() {
        return sfc;
    }

    public void saveSFC(File _file) throws Exception {
        if (_file == null)
            return;

        ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(_file));
        try {
            outStream.writeObject(new Project());//this); // throws IOException!!!!
        }
        finally {
            outStream.close(); // throws IOException!!!!
        }
        outStream.flush(); // throws IOException!!!!
        outStream.close(); // throws IOException!!!!

System.out.print("\nSFC exported");
    }

    public static Project readSFC(File _file) throws Exception {
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
	p.setEditor(new Editor(p.sfc));
	p.editor.show();

System.out.print("\nSFC imported");
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
        if (editor != null)
          editor.setSFCName(name);
    }

    /** Sets the editor in the project.
     *  While connecting an editor to the project, the project name is set in it.
     *  @param _editor  The editor in which the project is opened.
     */
    public void setEditor(Editor _editor) {
        editor = _editor;
        editor.setSFCName(this.name);
    }

    /** Returns the editor modified flag.
     *  This function is part of the editor interface.
     *  @return isModified  The editor's modified status
     */
    public boolean isModified() {
        return editor.isModified();
    }

    /** Sets the editor modified flag.
     *  This function is part of the editor interface.
     */
    public void clearModified() {
        editor.clearModified();
    }

}