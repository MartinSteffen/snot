/*
 * Session.java
 *
 * Created on 26. Mai 2001, 14:06
 */

package gui;

import java.util.*;
import java.lang.*;
import java.io.Serializable;

/**
 *
 * @author  Hans Theman and Ingo Schiller
 * @version 
 */
public class Session extends java.lang.Object implements Serializable {

    /** variable declarations */
    private static Vector projects = null;
    public static String name;
    public static String fileName;
    public static boolean is_saved;
    public static boolean has_changed;
    
    /** Creates new Session */
    public Session() {
        projects = new Vector();
        name = "unknown";
        fileName = "";
        is_saved = false;
        has_changed = false;
    }
    
    public Session(String _name, String _fileName, boolean _is_saved, boolean _has_changed) {
        projects = new Vector();
        name = _name;
        fileName = _fileName;
        is_saved = _is_saved;
        has_changed = _has_changed;
    }

    public int getIndexOfLastProject() {
       return projects.size()-1; 
    }
    
    public int noOfProjects() {
        return projects.size();
    }
    
    public Project getProject(int index) {
        if (projects.isEmpty())
            return null;
        
        return (Project)projects.get(index);
    }
    
    /**
     *  Function addProject
     *  @param project Specifies the Object to be added to the project-vector
     *  @return index The index of the added object in the project-vector
     */
    
    public int addProject(Project project) {
        projects.add((Object)project);
        return projects.size()-1; 
    }
    
    public void removeProjectAt(int index) {
        projects.removeElementAt(index);
    }
    
    public boolean noProjects() {
        return projects.isEmpty();
    }
    
    public void save(){}
    
}
