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

  private class ActInsertStep extends AbstractAction {
    public ActInsertStep() {
      super("InsertStep", new ImageIcon("Step.bmp"));
    }

    public void actionPerformed(ActionEvent e) {
    }
  }

  private JToolBar ToolBar;
  private JScrollPane ScrollPane;
  private JPanel DrawPanel;

  private String strName;
  private boolean boolModified;
  private boolean boolChecked;
  private SFC sfc;

  /**
   * Konstante zum Highlight ausschalten
   * @see #highlight_state(Absynt Element, boolean Value)
   */
  final public boolean HILIGHT_OFF = false;
  /**
   * Konstante zum Highlight einschalten
   * @see #highlight_state(Absynt Element, boolean Value)
  */
  final public boolean HILIGHT_ON = true;

  /**
   * setzt den Dateinamen
   * (nur zur Anzeige in der Titelleise, Datei-IO macht ansonsten die GUI-Gruppe
   */
  private void setFilename(String Filename) {
    strName = new String(Filename);
    setTitle("Editor - " + strName);
    boolModified = false;
  }

  private void setSFC(SFC anSFC) {
    sfc = anSFC;
    // hier noch neuzeichen
  }

  public void repaintSFC() {

  }

  /**
   * baut ein Editor-Fenster (momentan JFrame-Ableitung) auf.
   * Übergeben werden muss ein SFC<>nul
   */
  public Editor(SFC anSFC) {
    setFilename("NoName");
    setSize(300, 320);
    setSFC(anSFC);
    ToolBar = new JToolBar();
    ToolBar.add(new ActInsertStep());  ToolBar.setSize(300, 40);
    getContentPane().add(ToolBar, BorderLayout.NORTH);

    DrawPanel = new JPanel();
    DrawPanel.setBackground(Color.white);

    ScrollPane = new JScrollPane(DrawPanel);
    getContentPane().add(ScrollPane, BorderLayout.CENTER);

    repaintSFC();

  }

  /* saveToFile und loadFromFile fliegen raus, da Gui-Gruppe sich darum
     kümmert:

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

  */

  /**
   * setzt den Highlight-Status eines Absynt-Elements -
   * @see #HILIGHT_ON
   * @see #HILIGHT_OFF
   */
  public void highlight_state(Absynt Element, boolean Value) {
  }

  private void setSelected(Absynt Element, boolean Value) {
  }

  /**
   * gibt an, ob der SFC seit dem letzten Speichern/Laden geändert wurde
   */
  public boolean isModified() {
    return(boolModified);
  }

  private void setModified(boolean Value) {
    boolModified = Value;
    if (boolModified) boolChecked = false;
  }

  /**
   * gibt an, ob der SFC gecheckt ist
   * @see #setChecked()
   */
  public boolean isChecked() {
    return(boolChecked);
  }

  /**
   * teilt dem Editor mit, dass sein SFC gecheckt ist - sobald der SFC im Editor
   * geändert wird, ist der SFC für den Editor nicht mehr gecheckt
   * @see #isModified()
   */
  public void setChecked() {
    boolChecked =  true;
  }

}
