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
    private Vector projects = null;
    public String name;
    public String fileName;
    public boolean is_saved;
    public boolean has_changed;
    
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
        if (projects.isEmpty()) {
System.out.print("\n fatal error!! session.projects is empty!!!");
            return null;
        }
        
        return (Project)projects.elementAt(index);
    }
    
    /**
     *  Function addProject
     *  @param project Specifies the Object to be added to the project-vector
     *  @return index The index of the added object in the project-vector
     */
    
    public int addProject(Project project) {
        projects.add(project);
        return projects.size()-1; 
    }
    
    public void removeProjectAt(int index) {
        projects.removeElementAt(index);
        for (int i=index; i<projects.size(); i++) {
            ((Project)projects.elementAt(i)).setIndex(i);
        }
    }
    
    public boolean noProjects() {
        return projects.isEmpty();
    }
    
    public void save(){}
    
    /**
     *Function toString
     *Returns a stringrepresention of the session object readable for humans
     *@ret String 
     */
    public void printToStdout() {
        
        String text = "\nSESSION INFO:\nName: "+name+"; Filename: "+fileName+"; No of projects: "+this.noOfProjects()+"; Is saved: "+is_saved+"; Has changed: "+has_changed+"\n";
        System.out.print(text);
        for (int i=0; i<this.noOfProjects(); i++) {
            System.out.print(i+" - ");
            this.getProject(i).printToStdout();
        }
    }
    
}
