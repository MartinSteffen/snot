/*
 * Session.java
 *
 * Created on 26. Mai 2001, 14:06
 */

package gui;

import java.util.Hashtable;
import java.lang.*;
import java.io.Serializable;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Vector;
import editor.Editor;

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
 * @version $Revision: 1.8 $
 */

public class Session extends java.lang.Object implements Serializable {

    /** variable declarations */
    private Hashtable table = null;
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
    public File getFileName() {
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
            object = table.put((Object)_project.getEditor(), (Object)_project); // throws NullPointerExeption !!!!
        }
        catch (NullPointerException nullex) {
            throw new Exception(nullex.getMessage());
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
        Editor editor = _project.getEditor();

        // remove Project from hashtable
        if (table.remove((Object)editor) == null) {
            System.out.print("\nAttention: The removed Project was not found in the hashtable!");
            return;
        }
        // remove editor from screen
        if (editor != null)
            editor.dispose();
    }
    
    public boolean isEmpty() {
        return table.isEmpty();
    }

    /** Shuts down all pending editor frames.
     *  It is called when closing the session in order to clean up the desktop.
     */
    public void disposeEditors() {
        for (Enumeration e = this.table.elements() ; e.hasMoreElements() ;) {
            ((Project)e.nextElement()).getEditor().dispose();
        }
    }
    
    /** Saves the whole Session inclusive all Projects.
     *  The passed File parameter is stored in the Session object. Once stored, the is_saved flag is set.
     *  When saving the Session the next time, the File parameter may be null.
     *  @param _file    Specifies the file in which to store the entire Session.
     */
    public void save(File _file) throws Exception {
        if (_file != null) {
            this.setFile(_file);
        }
        // ... else use the old file
        
        ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(file));
        try {
            outStream.writeObject((Object)new Project()); // throws IOException!!!!
        }
        finally {
            outStream.close(); // throws IOException!!!!
        }
        outStream.flush(); // throws IOException!!!!
        outStream.close(); // throws IOException!!!!
        
        this.is_saved = true;
        this.is_modified = false;

        // tell all the editors that the session is saved
        for (Enumeration e = this.table.elements() ; e.hasMoreElements() ;) {
            ((Project)e.nextElement()).clearModified();
        }
    }
    
    public static Session read(File _file) throws Exception {
        Project p = null;
        Session s = null;
        
        ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(_file));
        try {
           s = (Session)inStream.readObject(); 
        }
        finally {
            inStream.close();
        }
        inStream.close();
        
        for (Enumeration e = s.table.elements() ; e.hasMoreElements() ;) {
            p = (Project)e.nextElement();
            p.clearModified();
            if (p.isActive()) {
                p.setEditor(new Editor(p.getSFC()));
                p.getEditor().show();
            }
        }
        
        s.is_saved = true;
        s.is_modified = false;

        return s;
    }
    
    
    /**
     * to get the projectvector to display the list
     */    
    
    public Vector getprojectlist(){
        Vector Projects = new Vector(); // vector to save the projects to displaz in the jList
        Enumeration e = table.elements();
    
        while(e.hasMoreElements()){
            Projects.add(e.nextElement());
            }
    return Projects;        
    }
    
    
    
/*    public void showlist(Vector Projects){
       JList list;
       JButton ShowButton;
   
        //Create the list and put it in a scroll pane
        list = new JList(Projects);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        //list.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(list);

        ShowButton = new JButton("Show");
        ShowButton.setActionCommand("Show");
        //ShowButton.addActionListener(new ShowListener());

        
        //Create a panel that uses FlowLayout (the default).
        JFrame ProjectPane = new JFrame();
        ProjectPane.add(ShowButton);
        Container projectcontentPane = getContentPane();
        projectcontentPane.add(listScrollPane, BorderLayout.CENTER);
        projectcontentPane.add(ProjectPane, BorderLayout.SOUTH);
        
        ProjectPane.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose(0);
            }
        }

        ProjectPane.pack();
        ProjectPane.setVisible(true);
       
        
    }

    class ShowListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            System.out.println(index);

            int size = list.getSize();

            if (size == 0) {
            //Nobody's left, disable firing.
                ShowButton.setEnabled(false);

            } else {
            //Adjust the selection.
                if (index == listModel.getSize())//removed item in last position
                    index--;
                list.setSelectedIndex(index);   //otherwise select same index
            }
        }
    }*/
 }  

