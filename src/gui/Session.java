/*
 * Session.java
 *
 * Created on 26. Mai 2001, 14:06
 */

package gui;

import java.util.Hashtable;
import java.lang.*;
import java.io.Serializable;

import editor.Editor;
/**
 *
 * @author  Hans Theman and Ingo Schiller
 * @version 
 */
public class Session extends java.lang.Object implements Serializable {

    /** variable declarations */
    private Hashtable table = null;
    public String name;
    public String fileName;
    public boolean is_saved;
    public boolean is_modified;
    
    /** Creates new Session */
    public Session() {
        table = new Hashtable();
        name = "unknown";
        fileName = "";
        is_saved = false;
        is_modified = false;
    }
    
    public Session(String _name, String _fileName, boolean _is_saved, boolean _is_modified) {
        table = new Hashtable();
        name = _name;
        fileName = _fileName;
        is_saved = _is_saved;
        is_modified = _is_modified;
    }

    public int noOfProjects() {
        return table.size();
    }
    
    public Project getProject(Object key) {
        return (Project)table.get(key); // returns null, if key not found 
    }
    
    /**
     *  Function addProject
     *  @param _project Specifies the Object to be added to the hashtable 
     */
    
    public void addProject(Project _project) {
//        if (_project == null)
//            return;
        
        Object object = table.put((Object)_project.getEditor(), (Object)_project); // throws NullPointerExeption !!!!
        if (object != null)
            System.out.print("\nAttention: The added Project was allready mapped in the hashtable!");
    }
    
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
    
    public void save(){}
    
    /**
     *Function toString
     *Returns a stringrepresention of the session object readable for humans
     *@ret String 
     */
/*    public void printToStdout() {
        
        String text = "\nSESSION INFO:\nName: "+name+"; Filename: "+fileName+"; No of projects: "+this.noOfProjects()+"; Is saved: "+is_saved+"; Has changed: "+is_modified+"\n";
        System.out.print(text);
        for (int i=0; i<this.noOfProjects(); i++) {
            System.out.print(i+" - ");
            this.getProject(i).printToStdout();
        }
    }
*/    
}
