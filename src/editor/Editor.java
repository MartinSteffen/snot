package editor; 
 
import java.util.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.awt.geom.*; 
import javax.swing.*; 
import absynt.*; 
import editor.DrawSFCPanel;
 
/** 
 * Editor - Klasse 
 * 
 * @author Natalja Froidenberg, Andreas Lukosch 
 * @version 1.0 
 */ 
 
public class Editor extends JFrame { 

  private class ActSelect extends AbstractAction { 
    public ActSelect() { 
      super("Select");  
    } 
 
    public void actionPerformed(ActionEvent e) { 
      System.out.println("Select");  
      DrawPanel.editorAction = DrawSFCPanel.SELECT;
    } 
  } 
 
  private class ActInsertStep extends AbstractAction { 
    public ActInsertStep() { 
      super("InsertStep");  
    } 
 
    public void actionPerformed(ActionEvent e) { 
      System.out.println("InsertStep");
      if (DrawPanel.editorAction == DrawSFCPanel.INSERT_STEP)
        SelectToggleBtn.doClick();
      else
        DrawPanel.editorAction = DrawSFCPanel.INSERT_STEP;
    } 
  } 
   
  private class ActInsertTrans extends AbstractAction { 
    public ActInsertTrans() { 
      super("InsertTrans");  
    } 
 
    public void actionPerformed(ActionEvent e) { 
      System.out.println("InsertTrans");
      if (DrawPanel.editorAction == DrawSFCPanel.INSERT_TRANS)
        SelectToggleBtn.doClick();
      else
        DrawPanel.editorAction = DrawSFCPanel.INSERT_TRANS;
    } 
  } 
   
  private class ActDeleteAbsynt extends AbstractAction { 
    public ActDeleteAbsynt() { 
      super("Delete");  
    } 
 
    public void actionPerformed(ActionEvent e) { 
      System.out.println("Delete"); 
      if (DrawPanel.editorAction == DrawSFCPanel.DELETE)
        SelectToggleBtn.doClick();
      else
        DrawPanel.editorAction = DrawSFCPanel.DELETE;
    } 
  } 
 
 
 
  private JToolBar ToolBar; 
  private JScrollPane DrawScrPane; 
  private DrawSFCPanel DrawPanel; 
  private JScrollPane DataScrPane; 
  private JPanel DataPanel, DataDeclPanel, DataActPanel; 
  private JTable DataDeclTable, DataActTable;
  private JToggleButton SelectToggleBtn; 
 
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
 
    // ToolBar: 
    ToolBar = new JToolBar();     
    ButtonGroup BtnGroup = new ButtonGroup();
    SelectToggleBtn = new JToggleButton(new ActSelect());
    BtnGroup.add(SelectToggleBtn);
    BtnGroup.add(new JToggleButton(new ActInsertStep()));
    BtnGroup.add(new JToggleButton(new ActInsertTrans()));
    BtnGroup.add(new JToggleButton(new ActDeleteAbsynt()));    
    
    Enumeration Enum = BtnGroup.getElements();
    while (Enum.hasMoreElements()) 
      ToolBar.add((JToggleButton)Enum.nextElement()); 
      
      /*
    ToolBar.add(new JToggleButton(new ActInsertTrans())); 
    ToolBar.add(new JToggleButton(new ActDeleteAbsynt())); 
      */
    ToolBar.setSize(400, 80); 
    getContentPane().add(ToolBar, BorderLayout.NORTH);     
    
 
    // Panel für Variablen- / Aktionen-Deklaration: 
    DataPanel = new JPanel(); 
    DataPanel.setSize(200, 0); 
    DataPanel.setLayout(new GridLayout(0, 1)); 
    DataDeclPanel = new JPanel(); 
    DataDeclPanel.setLayout(new BorderLayout()); 
    DataDeclPanel.add(new JLabel("Deklarationen:"), BorderLayout.NORTH); 
    String[] Header = {"Name", "Typ", "Wert"};
    String[][] Values = {{"x", "bool", "false"}};
    DataDeclTable = new JTable(Values, Header); 
    // DataDeclTable.setTableHeader(new JTableHeader()); 
    DataDeclPanel.add(new JScrollPane(DataDeclTable), BorderLayout.CENTER); 
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
    DrawPanel = new DrawSFCPanel(sfc); 
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
  
  public void clearModified() {
    setModified(false);
  }
  
  public SFC getSFC() {
      return(sfc);
  }
 
} 
