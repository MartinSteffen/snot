package editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import absynt.*;

/**
 * Editor - Klasse
 * 
 * @author Natalja Froidenberg, Andreas Lukosch
 * @version 1.0
 */

public class Editor extends JFrame {

  private String strName;
  private boolean boolModified;
  private SFC sfc;
  
  final public boolean HILIGHT_OFF = false;
  final public boolean HILIGHT_ON = true;
  
  private void setFilename(String Filename) {
    strName = new String(Filename);  
    setTitle("Editor - " + strName);
    boolModified = false;
  }

  private void setSFC(SFC anSFC) {
    sfc = anSFC;
    // hier noch neuzeichen
  }

  public Editor(SFC anSFC) {
    setFilename("NoName");
    setSize(300, 320);
    setSFC(anSFC);
    //getContentPane().add
  }

  public boolean saveToFile(String Filename) {
    System.out.println("Speichern unter: " + Filename);
    setFilename(Filename);
    return(true);
  }

  public boolean loadFromFile(String Filename) {
    System.out.println("Lade: " + Filename);
    setFilename(Filename);
    return(true);
  }

  public void highlight_state(Absynt Element, boolean Highlight) {  
  }

  public boolean isModified() {
    return(boolModified);
  }
}
