package editor;

import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
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
      super("InsertStep"); 
    }

    public void actionPerformed(ActionEvent e) {
      repaint();
    }
  }
  
  private class ActInsertTrans extends AbstractAction {
    public ActInsertTrans() {
      super("InsertTrans"); 
    }

    public void actionPerformed(ActionEvent e) {
      repaint();
    }
  }
  
  private class ActDeleteAbsynt extends AbstractAction {
    public ActDeleteAbsynt() {
      super("Delete"); 
    }

    public void actionPerformed(ActionEvent e) {
      repaint();
    }
  }

  private class DrawSFCPanel extends JPanel {

    private void paintTrans(Transition Trans, Graphics2D G2D) {
      Step step;
      LinkedList stepLL = Trans.source;
      float minX = 10000.0f, maxX = 0.0f;
      int i;

      if (stepLL != null) {
        for (i=0; i < stepLL.size(); i++) {
          step = (Step)stepLL.get(i);
          G2D.draw(new Rectangle2D.Float(step.pos.x*30f, step.pos.y*30f, 30.0f, 30.0f));
          G2D.drawString(step.name, step.pos.x*30f, (step.pos.y+1)*30f);
          if (step.pos.x < minX) minX=step.pos.x;
          if (step.pos.x > maxX) maxX=step.pos.x;
        }
      }

      stepLL = Trans.target;
      if (stepLL != null) {
        for (i=0; i < stepLL.size(); i++) {
          step = (Step)stepLL.get(i);
          G2D.draw(new Rectangle2D.Float(step.pos.x*30f, step.pos.y*30f, 30.0f, 30.0f));
          G2D.drawString(step.name, step.pos.x*30f, (step.pos.y+1)*30f);
        }
      }
    }

    private void paintSFC(Graphics2D G2D) {
      Transition Trans;
      LinkedList transLL = sfc.transs;

      if (transLL != null) {
        for (int i=0; i < transLL.size(); i++) {
          Trans = (Transition)transLL.get(i);
          paintTrans(Trans, G2D);
        }
      }

    }

    public void paint(Graphics g) {
      Graphics2D G2D = (Graphics2D)g;
      //GeneralPath Path = new GeneralPath();
      //Font font = new Font("

      super.paint(g);
      //G2D.scale(10.0f, 10.0f);
      G2D.setColor(Color.black);
      if (sfc != null) paintSFC(G2D);
      //G2D.draw(Path);
    }
  }

  private JToolBar ToolBar;
  private JScrollPane DrawScrPane;
  private DrawSFCPanel DrawPanel;
  private JScrollPane DataScrPane;
  private JPanel DataPanel, DataDeclPanel, DataActPanel;
  private JTable DataDeclTable, DataActTable;

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
   * (nur zur Anzeige in der Titelleise, Datei-IO macht ansonsten die GUI-Gruppe)
   */
  private void setFilename(String Filename) {
    strName = new String(Filename);
    setTitle("Editor - " + strName);
    boolModified = false;
  }

  private void setSFC(SFC anSFC) throws EditorException {
    if (anSFC != null) 
	sfc = anSFC;
    else       
      throw new EditorException("Anwendungsfehler: SFC = null");
  }

  private void proccessTrans(Transition Trans, int PosY) {
    Step step;
    LinkedList stepLL = Trans.source;
    int numStep = 0, i = 0;

    System.out.println("  " + "SourceSteps:");
    if (stepLL != null) {
      for (i=0; i < stepLL.size(); i++) {
        step = (Step)stepLL.get(i);
        System.out.print("    " + "Step " + step.name + " : PosY=" + PosY + " - ");
        if (step.pos == null || step.pos.x == -1)
          step.pos = new Position((float)numStep*2f, (float)PosY);
        numStep+=1;
      }
    }
    System.out.println("");

    stepLL = Trans.target;  PosY+=1;  numStep=0;
    System.out.println("  " + "TargetSteps:");
    if (stepLL != null) {
      for (i=0; i < stepLL.size(); i++) {
        step = (Step)stepLL.get(i);
        System.out.print("    " + "Step " + step.name + " : PosY=" + PosY + " - ");
        if (step.pos == null || step.pos.x == -1)
          step.pos = new Position(numStep, PosY);
        numStep+=1;
      }
    }
    System.out.println("");
  }

  public void realignSFC() {
    Transition Trans;
    LinkedList transLL = sfc.transs;
    int PosY = 0, TransNum = 1, i= 0;

    if (transLL != null) {
      for (i = 0; i < transLL.size();  i++) {
        Trans = (Transition)transLL.get(i);
        System.out.println("Transition-Nr.: " + TransNum);
        proccessTrans(Trans, PosY);
        PosY+=3;  TransNum+=1;
      }
    }

  }

  public void repaintSFC() {
    realignSFC();
    repaint();
  }

  /**
   * baut ein Editor-Fenster (momentan JFrame-Ableitung) auf.
   * Übergeben werden muss ein SFC<>null
   */
  public Editor(SFC anSFC) throws EditorException {
    setFilename("NoName");
    setSize(600, 420);
    setSFC(anSFC);

    // ToolBar:
    ToolBar = new JToolBar();
    ToolBar.add(new ActInsertStep());
    ToolBar.add(new ActInsertTrans());
    ToolBar.add(new ActDeleteAbsynt());
    
    ToolBar.setSize(400, 80);
    getContentPane().add(ToolBar, BorderLayout.NORTH);    

    // Panel für Variablen- / Aktionen-Deklaration:
    DataPanel = new JPanel();
    DataPanel.setSize(200, 0);
    DataPanel.setLayout(new GridLayout(0, 1));
    DataDeclPanel = new JPanel();
    DataDeclPanel.setLayout(new BorderLayout());
    DataDeclPanel.add(new JLabel("Deklarationen:"), BorderLayout.NORTH);
    DataDeclTable = new JTable(1, 3);
    // DataDeclTable.setTableHeader(new JTableHeader());
    DataDeclPanel.add(DataDeclTable, BorderLayout.CENTER);
    DataDeclPanel.add(new JButton("Neu"), BorderLayout.SOUTH);
    DataDeclPanel.add(new JButton("Entf"), BorderLayout.SOUTH);
    DataPanel.add(DataDeclPanel);
    DataActPanel = new JPanel();
    DataActPanel.setLayout(new BorderLayout());
    DataActPanel.add(new JLabel("Aktionen:"), BorderLayout.NORTH);
    DataActTable = new JTable(1, 2);
    // DataActTable.setTableHeader(new JTableHeader());
    DataActPanel.add(DataActTable, BorderLayout.CENTER);
    DataActPanel.add(new JButton("Neu"), BorderLayout.SOUTH);
    DataActPanel.add(new JButton("Entf"), BorderLayout.SOUTH);
    DataPanel.add(DataActPanel);
    
    // DataPanel.add(new JTable, BorderLayout.NORTH);
    DataScrPane = new JScrollPane(DataPanel);
    DataScrPane.setSize(200, 0);
    getContentPane().add(DataScrPane, BorderLayout.WEST);

    // Zeichenfläche für SFC:
    DrawPanel = new DrawSFCPanel();
    DrawPanel.setBackground(Color.white);

    DrawScrPane = new JScrollPane(DrawPanel);
    getContentPane().add(DrawScrPane, BorderLayout.CENTER);

    repaintSFC();

  }

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
