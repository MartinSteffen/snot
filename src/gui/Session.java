/*
 * Session.java
 *
 * Created on 26. Mai 2001, 14:06
 */

package gui;

import java.util.Hashtable;
import java.lang.*;
//import java.lang.Class;
import java.io.Serializable;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Vector;
import editor.Editor;
import absynt.SFC;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


/**
 *  Class Session.
 *  The Session object keeps all information of a program execution. It keeps track of all SFCs and their status,
 *  which are included in the current program run. The Session manages the Projects and offers the ability to
 *  create new, import or export Projects.
 *
 * @author  Hans Theman and Ingo Schiller
 * @version $Revision: 1.18 $
 */

public class Session extends java.lang.Object implements Serializable {

    /** variable declarations */
    private transient Hashtable table = null;
    /** The name of the current session.
     *  It is taken form the filename excluding path and filename extension.
     */
    private String name;
    /** The file is set by saving the current session.
     */
    private transient File file = null;
    /** Once saved this flag is set to true. It is also set when a session is read from disk.
     *  It indicats that a valid filename is set and the SaveSessionbutton can be invoked.
     */
    private boolean is_saved;
    /** The is_modified flag indicates that the session has changed and needs to be saved.
     */
    private boolean is_modified;

    /** The standard constructor.
     *  Creates new Session without any SFCs.
     */
    public Session() {
        table = new Hashtable();
        name = "unknown";
        is_saved = false;
        is_modified = false;
    }

    /** The constructor.
     *  Creates new Session with the specified parameters. There are no SFCs.
     */
    public Session(File _file) {
        table = new Hashtable();
        file = _file;
        name = file.getName().replace('.','\0');
        is_saved = true;
        is_modified = false;
    }

    /**
     *  Returns the session name.
     *  @retrun name    The name of the session.
     */
    public String getName() {
        return name;
    }

//    /**
//     *  Sets the name of the session.
//     *  @param _name    The prefered session name.
//     */
//    public void setName(String _name) {
//        name = _name;
//    }
//
    /**
     *  Returns the session filename.
     *  @return fileName    The filename of the session.
     */
    public File getFile() {
        return file;
    }

    /**
     *  Sets the session filename.
     *  The session name is extracted from the filename excluding the filename extension and set.
     *  The is_saved flag is also set.
     *  @param _file The defined filename with full pathname.
     */
    public void setFile(File _file) throws Exception {
        boolean status;
        try {
            status = _file.isFile();
        }
        catch (SecurityException ex) {
            throw new Exception(ex.getMessage());
        }
        if (!status)
            throw new Exception("File \""+_file.getName()+"\" is not valid or does not exist!");

        file = _file;
        name = _file.getName().replace('.','\0');
        is_saved = true;
    }

    /**
     *  Sets the saved status.
     *  If saved, status = true, else status = false.
     *  @param status   The actual condition of the saved flag.
     */
    public void setSaved(boolean status) {
        is_saved = status;
    }

    /**
     *  Returns the saved flag of a session.
     *  @return is_saved    The satus of the saved flag. If saved, status = true, else false.
     */
    public boolean isSaved() {
        return is_saved;
    }

    /**
     *  Sets the modified flag.
     *  @param status   The condition of the modified flag. If modified, status = true, else false.
     */
    public void setModified(boolean status) {
        is_modified = status;
    }

    /**
     *  Returns the modified flag.
     *  @return is_modified The status of the modified flag. If modified, status = true, else false.
     */
    public boolean isModified() {
        return is_modified;
    }


    /**
     *  Returns the number of Projects in the session.
     *  @return size   The number of Project objects included in the session.
     */
    public int noOfProjects() {
        return table.size();
    }
    
    /**
     *  Returns the corresponding Project object.
     *  @param key  Specifies the object to search for.
     *  @return Project    The Project object that belongs to the passed key object. If the key is not found, null is retruned.
     */
    public Project getProject(Object key) {
        return (Project)table.get(key); // returns null, if key not found
    }

    /**
     *  Adds a Project object to the session.
     *  By the way the modified flag of the session is set.
     *  @param _project Specifies the Object to be added to the session.
     *  @throws Exception if parameter is null or the added Object allready was part of the session.
     */
    public void addProject(Project _project) throws Exception {
        Object object;
        try {
            object = table.put((Object)_project.getSFC(), (Object)_project); // throws NullPointerExeption !!!!
        }
        catch (NullPointerException nullex) {
            throw new Exception("An error occured while adding a Project to the Session!");
        }
        if (object != null)
            throw new Exception("Attention: The added Project was allready mapped in the Session!\nIt is now mapped to the new once.\n");

        is_modified = true;
    }

    /**
     *  Removes a Project from the session.
     *  If the Project has active Editorframes they are disposed.
     *  @param _project Specifies the object to be removed from the current session.
     */
    public void removeProject(Project _project) {
        SFC sfc = _project.getSFC();

	// remove editor from screen
        if (_project.getEditor() != null)
            (_project.getEditor()).dispose();        

	// remove Project from hashtable
        if (table.remove((Object)sfc) == null) {
            System.out.print("\nAttention: The removed Project was not found in the hashtable!");
            return;
        }
      
    }

    public boolean isEmpty() {
        return table.isEmpty();
    }

    /** Shuts down all pending editor frames.
     *  It is called when closing the session in order to clean up the desktop.
     */
    public void disposeEditors() {
        Project p = null;
        for (Enumeration e = this.table.elements() ; e.hasMoreElements() ;) {
            p = (Project)e.nextElement();
//            p.setEnvironment();
	    if (p.getEditor() != null)
		p.getEditor().dispose();
        }
    }

    /** Saves the whole Session inclusive all Projects.
     *  The passed File parameter is stored in the Session object. Once stored, the is_saved flag is set.
     *  When saving the Session the next time, the File parameter may be null.
     *  @param _file    Specifies the file in which to store the entire Session.
     */
    public void save(File _file) throws Exception {
        Enumeration e = null;
        Project p = null;
	Editor editor = null;
        
        if (_file != null) {
            this.setFile(_file); // ... else use the old file
        }

	Hashtable h = this.table;
        e = this.table.elements();
	this.table = null;
        ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(this.file));
        try {
            outStream.writeObject(this); // throws IOException!!!!
            outStream.writeInt(h.size()); // throws IOException!!!!
            while (e.hasMoreElements()) {
                p = (Project)e.nextElement();
                p.setEnvironment();
                p.clearModified();
		editor = p.getEditor();
		p.setEditor(null);	       
                outStream.writeObject(p); // throws IOException!!!!
		p.setEditor(editor);
            }
        }
        catch (Exception ex){
	    p.setEditor(editor);
	    throw ex;
	}
	finally {
	    this.table = h;
	    outStream.flush(); // throws IOException!!!!
	    outStream.close(); // throws IOException!!!!
        }
        
        this.is_saved = true;
        this.is_modified = false;
    }

    public static Session read(File _file) throws Exception {
        Project p = null;
        Session s = null;
        Editor e = null;
        int i = 0;

        ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(_file));
        // read session
        try {
           s = (Session)inStream.readObject();
           s.table = new Hashtable();
           i = inStream.readInt();
System.out.print("\n there are "+i+" projects in the session to read");           
           while ((i--)>0) {
System.out.print("\n reading project "+i);              
               p = (Project)inStream.readObject();
               p.restoreEnvironment();
               s.addProject(p);
           }
System.out.print("\n finished reading projects");
        }
        finally {
            inStream.close();
        }
        inStream.close();

        // session settings
        s.setFile(_file);
//        s.is_saved = true; // is set by setFile()
        s.is_modified = false;

        return s;
    }


    /**
     * to get the projectvector to display the list
     */

    public Vector getProjectList(){
        Vector projects = new Vector(); // vector to save the projects to displaz in the jList

	Enumeration e = table.elements();

        while(e.hasMoreElements()){
            projects.add(e.nextElement());
            }


    return projects;
    }

    public Vector getNamesList(){
        Vector names = new Vector();// vector to save the names of the projects to displaz in the jList
	Enumeration e = table.elements();

        while(e.hasMoreElements()){
            names.add(((Project)e.nextElement()).getName());
	    }


    return names;
    }



}

