package editor; 
 
import java.util.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.awt.geom.*; 
import javax.swing.*; 
import javax.swing.event.*; 
import absynt.*; 
import editor.DrawSFCPanel;
 
/** 
 * Editor - Klasse 
 * 
 * @author Natalja Froidenberg, Andreas Lukosch 
 * @version 1.0 
 */ 
 
public class Editor extends JFrame { 

  // ----------------------- visuelle Komponenten ------------------------------
  
  private JToolBar ToolBar; 
  private JScrollPane DrawScrPane; 
  private DrawSFCPanel DrawPanel; 
  private JScrollPane DataScrPane; 
  private JPanel DataPanel, DataDeclPanel, DataActPanel; 
  private JTable DataDeclTable, DataActTable;
  private JToggleButton SelectToggleBtn; 
  
  // --------------------------- Editor-Action ---------------------------------
  // gibt an, mit welcher Aktion der Editor bei folgenden MausEvents im SFCPabnel
  // reagiert (wird geändert durch die Buttons in der ToolBar):
  
  private int editorAction;

  final static public int SELECT = 0;
  final static public int INSERT_STEP = 1;
  final static public int INSERT_TRANS = 2;
  final static public int DELETE = 3;
   
  private MouseInputAdapter SFCPanelsMouseAdapter;
   
  private void setEditorAction(int newEditAct) {
    // Wenn Button gedrückt wird, der schon aktiv ist, dann wird das wie "Ausschalten"
    // behandelt und der "Grundzustand" SELECT wird aktiviert:
    if (newEditAct != SELECT && newEditAct == editorAction) 
      SelectToggleBtn.doClick();
    else
      editorAction = newEditAct;
    DrawPanel.removeMouseListener(SFCPanelsMouseAdapter);
    DrawPanel.removeMouseMotionListener(SFCPanelsMouseAdapter);
    switch (editorAction) {
      case SELECT       : SFCPanelsMouseAdapter = new MASelect();  break;
      case INSERT_STEP  : SFCPanelsMouseAdapter = new MAInsertStep();  break;
      case INSERT_TRANS : SFCPanelsMouseAdapter = new MAInsertTrans();  break;
      case DELETE       : SFCPanelsMouseAdapter = new MADelete();  break;
    }
    DrawPanel.addMouseListener(SFCPanelsMouseAdapter);
    DrawPanel.addMouseMotionListener(SFCPanelsMouseAdapter);
  }
  
  // ----------------- Actions für Buttons in der ToolBar ----------------------
       
  private class ActSelect extends AbstractAction { 
    public ActSelect() { 
      super("Select");  
    } 
 
    public void actionPerformed(ActionEvent e) { 
      setEditorAction(SELECT);
    } 
  } 
 
  private class ActInsertStep extends AbstractAction { 
    public ActInsertStep() { 
      super("InsertStep");  
    } 
 
    public void actionPerformed(ActionEvent e) { 
      setEditorAction(INSERT_STEP);
    } 
  } 
   
  private class ActInsertTrans extends AbstractAction { 
    public ActInsertTrans() { 
      super("InsertTrans");  
    } 
 
    public void actionPerformed(ActionEvent e) { 
      setEditorAction(INSERT_TRANS);
    } 
  } 
   
  private class ActDeleteAbsynt extends AbstractAction { 
    public ActDeleteAbsynt() { 
      super("Delete");  
    } 
 
    public void actionPerformed(ActionEvent e) { 
      System.out.println("Delete"); 
      setEditorAction(DELETE);
    } 
  } 
 
  // ----------- MouseAdapter für SFCPanel abhängig von editAction -------------
  private Step checkStepHit(double PosX, double PosY) {
    LinkedList stepsLL = sfc.steps;
    Step step;
    StepPosition StepPos;
       
    if (stepsLL != null) {
      for (int i=0; i < stepsLL.size(); i++) { 
        step = (Step)stepsLL.get(i); 
        StepPos = (StepPosition)(step.pos);
        if (StepPos.Bounds.contains(new Point2D.Double(PosX, PosY))) return(step);          
      }   
    }
    return(null);                
  }
    
  protected boolean isStepNameHit(Step step, double PosX, double PosY) {
    StepPosition StepPos = (StepPosition)(step.pos);
    return (StepPos.Bounds.contains(PosX - DrawPanel.StepBorder, PosY - DrawPanel.StepBorder, 2*DrawPanel.StepBorder, 2*DrawPanel.StepBorder));
  }
  
  // (MA für _M_ouse_A_dapter)

  // MouseAdapter für den "editorAction-Modus" SELECT
  private class MASelect extends MouseInputAdapter {
    Rectangle SelectedRect;
    boolean MouseDragging = false;
    public void mousePressed(MouseEvent e) {
      double mouseX = (new Integer(e.getX())).doubleValue();
      double mouseY = (new Integer(e.getY())).doubleValue();
      Step step = checkStepHit(mouseX, mouseY);
      if (step != null) {
        if (e.isShiftDown()) 
          switchSelected(step); 
        else {
          setSelected(step);         
          if (isStepNameHit(step, mouseX, mouseY)) editStepName(step);
        }
      } else 
        setSelected(null);  // kein Step selektiert
      // Selektionsrechteck fuer Verschiebung erstellen (als Union der StepPosition-Rechtecke):
      SelectedRect = new Rectangle();
      if (!SelectedLL.isEmpty()) {
        StepPosition StepPos = (StepPosition)(((Step)(SelectedLL.getFirst())).pos);
        SelectedRect.setBounds(StepPos.Bounds.getBounds());
      }
      for (int i=0; i < SelectedLL.size(); i++) {
        StepPosition StepPos = (StepPosition)(((Step)(SelectedLL.get(i))).pos);
        SelectedRect = SelectedRect.union(StepPos.Bounds.getBounds());
      }
      SelectedRect.setLocation(0, 0);
      
    }
    
    public void mouseDragged(MouseEvent e) {
      if (SelectedRect != null) {        
        double mouseX = (new Integer(e.getX())).doubleValue();
        double mouseY = (new Integer(e.getY())).doubleValue();
        Graphics2D G2D = (Graphics2D)(((JComponent)(e.getSource())).getGraphics());
        G2D.setXORMode(Color.green);
        if (MouseDragging) G2D.draw(SelectedRect); 
        if (SelectedLL.isEmpty()) {
          // Rechteck zum selektieren aufziehen:
          
        } else {
          // Selektion verschieben;
          SelectedRect.setLocation(e.getX(), e.getY());
          G2D.draw(SelectedRect);
        }        
        MouseDragging = true;
      }
    }
    
    public void mouseReleased(MouseEvent e) {
      if (MouseDragging) {
        Graphics2D G2D = (Graphics2D)(((JComponent)(e.getSource())).getGraphics());
        G2D.setXORMode(Color.green);  G2D.draw(SelectedRect);
        G2D.setPaintMode();  
      }
      MouseDragging = false;  SelectedRect = null;
    }
  }
  
  
  // MouseAdapter für den "editorAction"-Modus INSERT_STEP
  private class MAInsertStep extends MouseInputAdapter {
    public void mousePressed(MouseEvent e) {
      double mouseX = (new Integer(e.getX())).doubleValue();
      double mouseY = (new Integer(e.getY())).doubleValue();
      Step step = new Step("Neuer Step");
      step.pos = new StepPosition(mouseX, mouseY, 100f, 100f);
      sfc.steps.add(step);  DrawPanel.repaint();
    }        
  }
  
  // MouseAdapter für den "editorAction"-Modus INSERT_TRANS
  private class MAInsertTrans extends MouseInputAdapter {
    Step SourceStep;
    
    public void mousePressed(MouseEvent e) {
      double mouseX = (new Integer(e.getX())).doubleValue();
      double mouseY = (new Integer(e.getY())).doubleValue();
      SourceStep = checkStepHit(mouseX, mouseY);
    }
    
    public void mouseReleased(MouseEvent e) {
      double mouseX = (new Integer(e.getX())).doubleValue();
      double mouseY = (new Integer(e.getY())).doubleValue();
      if (SourceStep != null) {
        Step step = checkStepHit(mouseX, mouseY);
        if (step == null) {
          SourceStep = null;
          System.out.println("DestStep = null");
        } else { 
          LinkedList SourceSteps = new LinkedList();
          SourceSteps.add(SourceStep);
          LinkedList DestSteps = new LinkedList();
          DestSteps.add(step);
          Transition Trans = new Transition(SourceSteps, null, DestSteps);
          sfc.transs.add(Trans);  DrawPanel.repaint();
        }                    
      } // if (SourceStep ... 
    else 
      System.out.println("SourceStep = null");       
    }
  }
  
  // MouseAdapter für den "editorAction"-Modus DELETE
  private class MADelete extends MouseInputAdapter {
    public void mousePressed(MouseEvent e) {
      double mouseX = (new Integer(e.getX())).doubleValue();
      double mouseY = (new Integer(e.getY())).doubleValue();
      Step step = checkStepHit(mouseX, mouseY);
      // if (step != null) sfc.steps.delete(step);  // *** Transitions müssen auch noch raus
    }
  }
  
  public LinkedList SelectedLL;  // Liste der momentan selektierten Elemente (für Verschieben, Löschen usw.)
 
  private String strName; 
  private boolean boolModified;   
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
   * setzt den Namen 
   * (nur zur Anzeige in der Titelleise, Datei-IO macht ansonsten die GUI-Gruppe) 
   */ 
  public void setSFCName(String Filename) { 
    strName = new String(Filename); 
    setTitle("Editor - " + strName);     
  } 
 
  private void setSFC(SFC anSFC) throws EditorException { 
    if (anSFC != null)  
	sfc = anSFC; 
    else        
      throw new EditorException("Application-Error: SFC = null"); 
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
    setSFCName("NoName"); 
    setSize(600, 420); 
    setSFC(anSFC); 
    SelectedLL = new LinkedList();
 
    // -- TOOL-BAR: ------------------------------------------------------------
    // beinhaltet die Buttons, mit denen der User eine Aktion wählt
    ToolBar = new JToolBar();
    SelectToggleBtn = new JToggleButton(new ActSelect());
    // Die Buttons InsertStep, InsertTrans, Select, usw. geben die momentan 
    // gewählte Aktion an, d.h. die Buttons schließen sich gegenseitig aus.
    // Also: Buttons in eine ButtonGroup ...
    ButtonGroup BtnGroup = new ButtonGroup();
    BtnGroup.add(SelectToggleBtn);
    BtnGroup.add(new JToggleButton(new ActInsertStep()));
    BtnGroup.add(new JToggleButton(new ActInsertTrans()));
    BtnGroup.add(new JToggleButton(new ActDeleteAbsynt()));    
    // ... UND natürlich in die ToolBar:
    Enumeration Enum = BtnGroup.getElements();
    while (Enum.hasMoreElements()) 
      ToolBar.add((JToggleButton)Enum.nextElement()); 
    // Jetzt ToolBar ins Fenster:      
    //ToolBar.setSize(400, 80); 
    getContentPane().add(ToolBar, BorderLayout.NORTH);     
    
    // -- DATA-PANEL: ----------------------------------------------------------
    // Panel für Variablen- / Aktionen-Deklaration: 
    DataPanel = new JPanel(); 
    DataPanel.setSize(200, 0); 
    DataPanel.setLayout(new GridLayout(0, 1));
    // 1. DataDeclPanel beinhaltet die Deklarationen (obere Hälfte des DataPanels):
    DataDeclPanel = new JPanel(); 
    DataDeclPanel.setLayout(new BorderLayout());
    //   a) "Titelleiste":
    DataDeclPanel.add(new JLabel("Deklarationen:"), BorderLayout.NORTH); 
    //   b) Tabelle mit Dekl.:
    String[] Header = {"Name", "Typ", "Wert"};
    String[][] Values = {{"x", "bool", "false"}};
    DataDeclTable = new JTable(Values, Header);     
    DataDeclPanel.add(new JScrollPane(DataDeclTable), BorderLayout.CENTER); 
    //   c) Buttons zum Löschen/Hinzufügen von Dekl.:
    DataDeclPanel.add(new JButton("Neu"), BorderLayout.SOUTH); 
    DataDeclPanel.add(new JButton("Entf"), BorderLayout.SOUTH);
    //   d) nun zum DataPanel hinzufügen:
    DataPanel.add(DataDeclPanel);
    // 2. DataActPanel beinhaltet die Aktionen (unter Hälfte des DataPanels):
    DataActPanel = new JPanel(); 
    DataActPanel.setLayout(new BorderLayout()); 
    //   a) "Titelleiste":
    DataActPanel.add(new JLabel("Aktionen:"), BorderLayout.NORTH); 
    //   b) Tabelle mit Aktionen:
    DataActTable = new JTable(1, 2);     
    DataActPanel.add(new JScrollPane(DataActTable), BorderLayout.CENTER); 
    //   c) Buttons zum Löschen/Hinzufügen von Aktionen:
    DataActPanel.add(new JButton("Neu"), BorderLayout.SOUTH); 
    DataActPanel.add(new JButton("Entf"), BorderLayout.SOUTH); 
    //   d) nun zum DataPanel hinzufügen:
    DataPanel.add(DataActPanel); 
     
    
    //DataScrPane = new JScrollPane(DataPanel); 
    //DataScrPane.setSize(200, 0); 
    getContentPane().add(DataPanel, BorderLayout.WEST); 
 
    // Zeichenfläche für SFC: 
    DrawPanel = new DrawSFCPanel(this); 
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
 
  private void setSelected(Absynt Element) {
    // *** momentan nur für Steps:
    // 1. alle vorher selektierten Elemente aus SelectedLL raus und neu (unselektiert) zeichnen:
    while (SelectedLL.size() > 0) 
      DrawPanel.paintStep((Step)SelectedLL.removeFirst(), (Graphics2D)DrawPanel.getGraphics(), false);         
    // 2. Element in SelectedLL rein und neu (selektiert) zeichnen:
    SelectedLL.add(Element);
    DrawPanel.paintStep((Step)(Element), (Graphics2D)DrawPanel.getGraphics(), true); 
  } 
 
  private void switchSelected(Absynt Element) {
    // *** momentan nur für Steps:
    if (SelectedLL.contains(Element)) {
      SelectedLL.remove(Element);
      DrawPanel.paintStep((Step)(Element), (Graphics2D)DrawPanel.getGraphics(), false); 
    } else {
      SelectedLL.add(Element);
      DrawPanel.paintStep((Step)(Element), (Graphics2D)DrawPanel.getGraphics(), true); 
    }    
  }
  
  public void editStepName(Step step) {    
    String newStepName = (new JOptionPane()).showInputDialog("Please input a new step-name");
    if (newStepName != null && newStepName != "") {
      step.name = newStepName;
      /*  ***
      Rectangle Rect = ((StepPosition)(step.pos)).Bounds.getBounds();
      Rect.setSize((new Double(Rect.getWidth())).intValue() + 1, (new Double(Rect.getHeight())).intValue() + 1);
      DrawPanel.repaint(Rect);      
      DrawPanel.paintStep(step, (Graphics2D)DrawPanel.getGraphics());
      */
      DrawPanel.repaint();  // *** immer alles neuzeichnen???
    }
  }
  
  public void deleteStep(Step step) {
    // 1. Step aus der Selectedliste:
    if (SelectedLL.contains(step)) SelectedLL.remove(step);
    // 2. Step aus Stepliste des SFC:
    sfc.steps.remove(step);
    // 3. Alle Transitionen mit diesem Step (als Start oder Ziel) raus:
    Transition Trans;
    int i=0; 
    while (i < sfc.transs.size()) {
      Trans = (Transition)(sfc.transs.get(i));
      if (Trans.source.contains(step) || Trans.target.contains(step)) sfc.transs.remove(i); else i++;
    }
    DrawPanel.repaint();
  }
  
  public void deleteSelection() {
    while (SelectedLL.size() > 0) deleteStep((Step)SelectedLL.getFirst());      
  }
  
  /** 
   * gibt an, ob der SFC (seit dem letzten Speichern/Laden) geändert wurde 
   */ 
  public boolean isModified() { 
    return(boolModified); 
  } 
 
  private void setModified(boolean Value) { 
    boolModified = Value;     
  } 
  
  /** 
   * hiermit besteht die Möglichkeit, das Modified-Flag zurückzusetzen
   */   
  public void clearModified() {
    setModified(false);
  }
  
  public SFC getSFC() {
      return(sfc);
  }
  
  protected void processKeyEvent(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_DELETE) deleteSelection();
  }
 
} 
