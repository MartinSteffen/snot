package editor; 
 
import java.util.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.awt.geom.*; 
import javax.swing.*; 
import javax.swing.event.*; 
import javax.swing.table.*;
import absynt.*; 
import editor.*;
 
/** 
 * Editor - Klasse 
 * 
 * @author Natalja Froidenberg, Andreas Lukosch 
 * @version 1.0 
 */ 

public class Editor extends JFrame implements ActionListener { 

  // ----------------------- visuelle Komponenten ------------------------------
  
  private JToolBar ToolBar; 
  private JScrollPane DrawScrPane; 
  private DrawSFCPanel DrawPanel; 
  private JScrollPane DataScrPane; 
  private JPanel DataPanel, DataDeclPanel, DataActPanel; 
  private JTable DataDeclTable, DataActTable;
  private JToggleButton SelectToggleBtn; 

  // ---------------- Klassen zum Manipulieren des SFC's -----------------------

  public class StepLL extends LinkedList {
    public StepLL() { super(); }
    public StepLL(LinkedList stepLL) { super(stepLL); }
    public Step getStep(int i) { return((Step)(super.get(i))); }
    public Step getFirstStep() { return((Step)(super.getFirst())); }      
  }

  public class TransLL extends LinkedList {
    public TransLL() { super(); }
    public TransLL(LinkedList transLL) { super(transLL); }
    public Transition getTrans(int i) { return((Transition)(super.get(i))); }
    public Transition getFirstTrans() { return((Transition)(super.getFirst())); }      
  }
  
  public class TransAlignInfoLL extends LinkedList {
    public TransAlignInfoLL() { super(); }
    public TransAlignInfoLL(LinkedList transAlignInfoLL) { super(transAlignInfoLL); }
  	public TransAlignInfo getTAInfo(int i) { return((TransAlignInfo)(super.get(i))); }
    public TransAlignInfo getFirstTAInfo() { return((TransAlignInfo)(super.getFirst())); }      
  }

  public class StepIterator {   
    public StepIterator(LinkedList stepLL) {
    	int i=0;
      while (i < stepLL.size()) 
				if ( withEachStepDo((Step)stepLL.get(i)) ) i++;
    }
    public boolean withEachStepDo(Step step) { return(true); }                    // muss ueberschrieben werden
  }

  public class TransIterator {
    public TransIterator(LinkedList transLL) {
    	int i=0;
      while (i < transLL.size()) 
				if ( withEachTransDo((Transition)transLL.get(i)) ) i++;
    }
    public boolean withEachTransDo(Transition trans) { return(true); }               // muss ueberschrieben werden
  }
  
  public class TAInfoIterator {
    public TAInfoIterator(LinkedList transAlignInfoLL) {
    	int i=0;
      while (i < transAlignInfoLL.size()) 
				if ( withEachTAInfoDo((TransAlignInfo)transAlignInfoLL.get(i)) ) i++;
    }
    public boolean withEachTAInfoDo(TransAlignInfo transAlignInfo) { return(true); }  // muss ueberschrieben werden
  }
  
  // --------------------------- Editor-Action ---------------------------------
  // gibt an, mit welcher Aktion der Editor bei folgenden MausEvents im SFCPabnel
  // reagiert (wird geändert durch die Buttons in der ToolBar):
  
  private int editorAction;

  final static public int SELECT = 0;
  final static public int INSERT_STEP = 1;
  final static public int INSERT_TRANS1 = 2;
  final static public int INSERT_TRANS2 = 3;
   
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
      case SELECT        : SFCPanelsMouseAdapter = new MASelect();  break;
      case INSERT_STEP   : SFCPanelsMouseAdapter = new MAInsertStep();  break;
      case INSERT_TRANS1 : SFCPanelsMouseAdapter = new MAInsSrcTrans();  break;
      case INSERT_TRANS2 : SFCPanelsMouseAdapter = new MAInsTrgtTrans();  break;      
    }
    DrawPanel.addMouseListener(SFCPanelsMouseAdapter);
    DrawPanel.addMouseMotionListener(SFCPanelsMouseAdapter);
  }
  
  // ----------------- Actions für Buttons in der ToolBar ----------------------
       
  public StepLL sourceSteps = new StepLL();  // Steps aus denen eine Transition entstehen soll
  public StepLL targetSteps = new StepLL();
         
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
   
  private class ActInsSrcTrans extends AbstractAction { 
    public ActInsSrcTrans() { 
      super("InsSrcTrans");  
    } 
 
    public void actionPerformed(ActionEvent e) { 
      setEditorAction(INSERT_TRANS1);
      sourceSteps = new StepLL();
      repaint();     
    } 
  } 
  
  private class ActInsTrgtTrans extends AbstractAction { 
    public ActInsTrgtTrans() { 
      super("InsTrgtTrans");  
    } 
 
    public void actionPerformed(ActionEvent e) { 
      setEditorAction(INSERT_TRANS2);
      targetSteps = new StepLL();
      repaint();
    } 
  } 
  
  private class ActInsertTrans extends AbstractAction { 
    public ActInsertTrans() { 
      super("InsertTrans");  
    } 
 
    public void actionPerformed(ActionEvent e) {       
      setEditorAction(SELECT);
      if (!sourceSteps.isEmpty() && !targetSteps.isEmpty()) {
        Transition trans = new Transition(sourceSteps, targetSteps);
        insertTrans(trans);  sourceSteps = new StepLL();  targetSteps = new StepLL();
        repaint();
      }      
    } 
  }   
     
  private class ActTestSFC extends AbstractAction { 
    public ActTestSFC() { 
      super("Test-SFC");  
    } 
 
    public void actionPerformed(ActionEvent e) {
      setEditorAction(SELECT);
      aligningSFC = true; 
      System.out.println("Demo SFC");       
      Step s1 = new Step("S1");
      Step s2 = new Step("S2");
      Step s3 = new Step("S3");
      Step s4 = new Step("S4");
      Step s5 = new Step("S5");
      Step s6 = new Step("S6");
      Step s7 = new Step("S7");
      StepLL stepLL = new StepLL();
      stepLL.add(s1);  stepLL.add(s2);  stepLL.add(s3);
      stepLL.add(s4);  stepLL.add(s5);  stepLL.add(s6);  stepLL.add(s7);
      
      LinkedList sLL, tLL;
      LinkedList transLL = new LinkedList();
      // t1: s1->s2
      sLL = new LinkedList();  sLL.add(s1);
      tLL = new LinkedList();  tLL.add(s2);
      transLL.add(new Transition(sLL, tLL));
      // t2: s1->s3
      sLL = new LinkedList();  sLL.add(s1);
      tLL = new LinkedList();  tLL.add(s3);
      transLL.add(new Transition(sLL, tLL));
      // t3: s2->s4
      sLL = new LinkedList();  sLL.add(s2);
      tLL = new LinkedList();  tLL.add(s4);
      transLL.add(new Transition(sLL, tLL));
      // t4: s2->s5
      sLL = new LinkedList();  sLL.add(s2);
      tLL = new LinkedList();  tLL.add(s5);
      transLL.add(new Transition(sLL, tLL));
      // t5: s3->s6
      sLL = new LinkedList();  sLL.add(s3);
      tLL = new LinkedList();  tLL.add(s6);
      transLL.add(new Transition(sLL, tLL));
      // t6: s4->s7
      sLL = new LinkedList();  sLL.add(s4);
      tLL = new LinkedList();  tLL.add(s7);
      transLL.add(new Transition(sLL, tLL));
      // t7: s5->s7
      sLL = new LinkedList();  sLL.add(s5);
      tLL = new LinkedList();  tLL.add(s7);
      transLL.add(new Transition(sLL, tLL));
      
      /*
      // t6: s4->s1
      sLL = new LinkedList();  sLL.add(s4);
      tLL = new LinkedList();  tLL.add(s1);
      transLL.add(new Transition(sLL, tLL));
      // t7: s5->s1
      sLL = new LinkedList();  sLL.add(s5);
      tLL = new LinkedList();  tLL.add(s1);
      transLL.add(new Transition(sLL, tLL));
      // t8: s6->s1
      sLL = new LinkedList();  sLL.add(s6);
      tLL = new LinkedList();  tLL.add(s1);
      transLL.add(new Transition(sLL, tLL));      
      */
      
      sfc = new SFC(s1, stepLL, transLL, null, null);
      DrawPanel.sfc = sfc;
      repaintSFC();
    } 
  } 
 
  // ----------- MouseAdapter für SFCPanel abhängig von editAction -------------
  private Step checkStepHit(double PosX, double PosY) {
    StepLL stepLL = new StepLL(sfc.steps);
           
    if (!stepLL.isEmpty()) {
      for (int i=0; i < stepLL.size(); i++) {                 
        StepPosition stepPos = (StepPosition)stepLL.getStep(i).pos;
        if (stepPos.Bounds.contains(new Point2D.Double(PosX, PosY))) return(stepLL.getStep(i));
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
    int selectedOldX, selectedOldY;
    int selectedMouseX, selectedMouseY;
    boolean MouseDragging = false;
    public void mousePressed(MouseEvent e) {
      double mouseX = (new Integer(e.getX())).doubleValue();
      double mouseY = (new Integer(e.getY())).doubleValue();
      Step step = checkStepHit(mouseX, mouseY);
      if (step != null) {
        if (e.isShiftDown()) 
          switchSelected(step); 
        else if (!SelectedLL.contains(step) || SelectedLL.size() == 1) {
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
      selectedOldX = (new Double(SelectedRect.getX())).intValue();
      selectedOldY = (new Double(SelectedRect.getY())).intValue();
      selectedMouseX = e.getX();  selectedMouseY = e.getY();
      
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
          SelectedRect.setLocation(e.getX() - selectedMouseX + selectedOldX, e.getY() - selectedMouseY + selectedOldY);
          G2D.draw(SelectedRect);
        }        
        MouseDragging = true;
      }
    }
    
    public void mouseReleased(MouseEvent e) {
      if (MouseDragging) {
        // Graphic-Objekt vom DrawSFCPanel holen:
        Graphics2D G2D = (Graphics2D)(((JComponent)(e.getSource())).getGraphics());
        // letztes Rechteck löschen:
        G2D.setXORMode(Color.green);  G2D.draw(SelectedRect);   G2D.setPaintMode();
        // Selection verschieben:
        double deltaX = (new Integer(e.getX() - selectedMouseX)).doubleValue();
        double deltaY = (new Integer(e.getY() - selectedMouseY)).doubleValue();
        moveSelection(deltaX, deltaY);
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
    
  
  // MouseAdapter für den "editorAction"-Modus INSERT_TRANS1 (Erstellen einer SrcStep-Liste für Trans):
  private class MAInsSrcTrans extends MouseInputAdapter {        
    public void mousePressed(MouseEvent e) {
      Step sourceStep = null;
      double mouseX = (new Integer(e.getX())).doubleValue();
      double mouseY = (new Integer(e.getY())).doubleValue();
      sourceStep = checkStepHit(mouseX, mouseY);
      if (sourceStep != null) {
        sourceSteps.add(sourceStep);
        DrawPanel.paintStep(sourceStep, (Graphics2D)DrawPanel.getGraphics());
      }
    }    
  }
  
  // MouseAdapter für den "editorAction"-Modus INSERT_TRANS1 (Erstellen einer SrcStep-Liste für Trans):
  private class MAInsTrgtTrans extends MouseInputAdapter {        
    public void mousePressed(MouseEvent e) {
      Step targetStep = null;
      double mouseX = (new Integer(e.getX())).doubleValue();
      double mouseY = (new Integer(e.getY())).doubleValue();
      targetStep = checkStepHit(mouseX, mouseY);
      if (targetStep != null) {
        targetSteps.add(targetStep);
        DrawPanel.paintStep(targetStep, (Graphics2D)DrawPanel.getGraphics());
      }
    }    
  }    
  
  // Liste der momentan selektierten Elemente (für Verschieben, Löschen usw.):
  public LinkedList       SelectedLL;
  // Liste der TransAlignInfos (TransPosition-Objekte referenzieren nur auf diese Elemente):
  public TransAlignInfoLL transAlignInfoLL = new TransAlignInfoLL();  
 
  private String  strName; 
  private boolean boolModified;
  private int     intTransitionAlign = 0;
  private SFC     sfc;
  public  boolean aligningSFC = false;

  

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
 
  StepLL     alignedStepLL = new StepLL();  // Steps die bereits eine Platzierung erhalten haben  
  LinkedList stepRowsLL    = new LinkedList();  // enthält Zeilen mit StepLL
  int        widestRow     = 0;
  
  protected void updateStepSize(Step step) {    
    StepPosition stepPos = new StepPosition();
    Integer txtHeight = new Integer(getToolkit().getFontMetrics(getFont()).getHeight());
    Integer txtWidth  = new Integer(getToolkit().getFontMetrics(getFont()).stringWidth(step.name));
    if (step.pos == null) stepPos = new StepPosition(); else stepPos = (StepPosition)step.pos;
    stepPos.Bounds.setFrame(stepPos.Bounds.getY(), stepPos.Bounds.getY(), 
      txtWidth.doubleValue() + 2*DrawPanel.StepBorder, txtHeight.doubleValue() + 2*DrawPanel.StepBorder);
    step.pos = stepPos;
  }  
 
  private void createStepRows(int row, Step step) {
    if (!alignedStepLL.contains(step)) {
      updateStepSize(step);
      System.out.print(" (" + row + ", " + step.name + ") ");
      while (stepRowsLL.size() <= row) stepRowsLL.add(new StepLL());
      double width = getRowWidth((StepLL)stepRowsLL.get(row));
      if (width > 0) width+=2*DrawPanel.StepBorder;
      ((StepLL)stepRowsLL.get(row)).add(step);
      Rectangle2D rect = ((StepPosition)step.pos).Bounds;      
      rect.setRect(width, 2*row*rect.getHeight(), rect.getWidth(), rect.getHeight());
      alignedStepLL.add(step);
    
      StepLL stepsSuccLL = new StepLL();
      getSuccLL(step, stepsSuccLL);
      for (int i=0; i < stepsSuccLL.size(); i++)
        createStepRows(row+1, stepsSuccLL.getStep(i));
    }
  }
   
  private double getRowWidth(StepLL stepLL) {
    double width = 0;
    // Step-Breiten addieren:
    for (int i=0; i < stepLL.size(); i++) {
      StepPosition stepPos = (StepPosition)stepLL.getStep(i).pos;
      Rectangle2D stepRect = stepPos.Bounds;
      width+=stepRect.getWidth();
    }
    // ggf. Abstände zwischen Steps:
    if (stepLL.size() > 1) width = width + (stepLL.size()-1)*2*DrawPanel.StepBorder; // *** sinnvoller Abstand?
    return(width);
  }
  
  private double getRowMid(StepLL stepLL) {
    double width = 0, minX = 1000, maxX = -1000;
    // Step-Breiten addieren:    
    for (int i=0; i < stepLL.size(); i++) {
      StepPosition stepPos = (StepPosition)stepLL.getStep(i).pos;      
      Rectangle2D stepRect = stepPos.Bounds;
      if (stepRect.getMinX() < minX) minX=stepRect.getMinX();
      if (stepRect.getMaxX() > maxX) maxX=stepRect.getMaxX();      
    }        
    return( minX + ((maxX-minX) / 2.0) );
  }
   
  private void calcStepPos(int row, Step step) {
    // alle Steps richten sich nach breitester Zeile
    if (alignedStepLL.contains(step)) return;
    alignedStepLL.add(step);
    if (row < widestRow) {
      StepLL stepsSuccLL = new StepLL();
      StepLL nextRowLL   = (StepLL)stepRowsLL.get(row+1);
      int    i           = 0;
      // Liste der Nachfolger holen, die in der nächsten Reihe sitzen:
      getSuccLL(step, stepsSuccLL);
      while (i < stepsSuccLL.size())
        if (!nextRowLL.contains(stepsSuccLL.getStep(i))) stepsSuccLL.remove(i); else i++;
      // die Position dieser Nachfolger berechnen:
      for (i=0; i < stepsSuccLL.size(); i++)
        calcStepPos(row+1, stepsSuccLL.getStep(i));
      // anhand derer Positionen die eigene Pos berechnen:
      double mid = getRowMid(stepsSuccLL);
      Rectangle2D rect = (Rectangle2D)((StepPosition)step.pos).Bounds;
      rect.setRect(mid - (rect.getWidth() / 2.0), 2*row*rect.getHeight(), rect.getWidth(), rect.getHeight());       
    } else {            
      if (row > widestRow) {
        StepLL stepsPredLL = new StepLL();
        StepLL prevRowLL   = (StepLL)stepRowsLL.get(row-1);
        int    i           = 0;
        // Liste der Vorgänger holen, die in der vorigen Reihe sitzen:
        System.out.println("Pred");      
        getPredLL(step, stepsPredLL);
        while (i < stepsPredLL.size()) 
          if (!prevRowLL.contains(stepsPredLL.getStep(i))) stepsPredLL.remove(i); else i++;      
        // anhand derer Positionen die eigene Pos berechnen:      
        double mid = getRowMid(stepsPredLL);
        Rectangle2D rect = (Rectangle2D)((StepPosition)step.pos).Bounds;
        rect.setRect(mid - (rect.getWidth() / 2.0), 2*row*rect.getHeight(), rect.getWidth(), rect.getHeight());      
      } 
      if (row < stepRowsLL.size()-1) {
       StepLL stepsSuccLL = new StepLL();
       StepLL nextRowLL   = (StepLL)stepRowsLL.get(row+1);
       int    i           = 0;
       // Liste der Nachfolger holen, die in der nächsten Reihe sitzen:
       getSuccLL(step, stepsSuccLL);
       while (i < stepsSuccLL.size())
         if (!nextRowLL.contains(stepsSuccLL.getStep(i))) stepsSuccLL.remove(i); else i++;
       // die Position dieser Nachfolger berechnen:
       for (i=0; i < stepsSuccLL.size(); i++)
         calcStepPos(row+1, stepsSuccLL.getStep(i));
      }
    }
  }
   

    /****************************************************************************


	hi Editors!
	ich musste auf Verlangen von Karsten etwas unternehmen.
	ja, ich habe in eurem kot gefummelt, weil es schnell
	gehen musste! hab nur die untenstehende realignSFC() editiert.
	folegende Veraenderungen:
	     die try funktion samt ihrem catch rumpf musste ich 
	     rausnehmen. Die Exception fangen wir, die Gui!
	     
        das ist alles sehr dirty. Wir muessen uns auf eine 
	elegantere art einigen. Mehr dazu per emil ...

	cheers

	hans




************************************************************************************/




  public void realignSFC() { 
    Step step;
    aligningSFC=true;
    //try {
     if (sfc.steps.isEmpty()) return;
     // 1. Die Steps-Reihen erstellen (ggf. mit iStep anfangen):
     System.out.println("Reihen erstellen");
     if (sfc.istep != null) step = sfc.istep; else step = (Step)sfc.steps.getFirst();
     alignedStepLL.clear();   stepRowsLL.clear();
     createStepRows(0, step);      
     // 2. breiteste Zeile rausfinden:
     System.out.println("Breiteste Reihe");
     double rowWidth = 0, maxRowWidth = 0;
     widestRow = 0;
     for (int i=0; i < stepRowsLL.size(); i++) {
       rowWidth = getRowWidth((StepLL)stepRowsLL.get(i));
       if (rowWidth > maxRowWidth) { 
         maxRowWidth = rowWidth;  widestRow = i;
       }
     }
     System.out.println("Steps positionieren");
     // 3. Steps positionieren:
     alignedStepLL.clear();
     calcStepPos(0, step);
     // 5. Transitionen einfügen:
     TransLL transLL = new TransLL(sfc.transs);
     sfc.transs.clear();
     for (int i=0; i < transLL.size(); i++) insertTrans(transLL.getTrans(i));

     //} 
    //catch(Exception e) {
	//e.printStackTrace();
	//System.out.println("\nATTENTION!\nThe editor failed to print the SFC!");     
	//}
 
    aligningSFC=false;
  } 
 
  public void repaintSFC() { 
    realignSFC(); 
    repaint(); 
  } 
  
  private void disableAlign() {
  	intTransitionAlign++;
  } 
  
  private void enableAlign() {
  	if (intTransitionAlign > 0) intTransitionAlign--;
  	if (intTransitionAlign == 0) {
  		/*
  		for (int i=0; i < transAlignInfoLL.size(); i++) {
  			TransAlignInfo alignInfo = (TransAlignInfo)transAlignInfoLL.get(i);
  			for (int j=0; j < alignInfo.transs.size(); j++) {
  				Transition trans = (Transition)alignInfo.transs.get(j);
  				if (!sfc.transs.contains(trans)) alignInfo.removeTrans(trans);
  			}
  			alignInfo.updateBounds();
  		}
  		*/
  		repaint();
  	}
  }
  
  Expression_Parser ExprEditor = new Expression_Parser(this, sfc);  
  // DataActTable's Statement-Editor:
  class ActionEditor extends DefaultCellEditor {        
    int column=0, row=0;
    public ActionEditor(JButton b) {
      //Unfortunately, the constructor expects a check box, combo box, or text field:
      super(new JCheckBox()); 
      editorComponent = b;
      setClickCountToStart(1); //This is usually 1 or 2.
      b.setText("<EDITING>");
      
      ExprEditor.addWindowListener(new WindowAdapter() {
        public void windowClosed(WindowEvent e) {
          editorComponent.hide();
          if (ExprEditor.get_canceled()) fireEditingCanceled(); else fireEditingStopped();
        }
      });
    }

    protected void fireEditingStopped() {      
      super.fireEditingStopped();
      
    }

    public Object getCellEditorValue() {
      // *** dazu muss Editor modal sein:
      LinkedList ll = new LinkedList();
      ll.add(new Assign(null, ExprEditor.get_expr()));
      return (ll); 
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int arow, int acolumn) {
      row=arow;  column=acolumn;
      ((JButton)editorComponent).setText(value.toString());
      //ExprEditor.expr_lList=(LinkedList.value); //***
      return editorComponent;
    }
  }
    
  /** 
   * baut ein Editor-Fenster (momentan JFrame-Ableitung) auf. 
   * Übergeben werden muss ein SFC<>null 
   */ 
  public Editor(SFC anSFC) throws EditorException { 
    super();    
    setSFCName("NoName"); 
    pack();
    //setSize(600,420);
    setVisible(true);
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
    BtnGroup.add(new JToggleButton(new ActInsSrcTrans()));
    BtnGroup.add(new JToggleButton(new ActInsTrgtTrans()));
    BtnGroup.add(new JToggleButton(new ActInsertTrans()));
    BtnGroup.add(new JToggleButton(new ActTestSFC()));    
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
    DataDeclPanel.add(new JLabel("Declaration-List:"), BorderLayout.NORTH); 
    //   b) Tabelle mit Dekl.:    
    DataDeclTable = new JTable(new AbstractTableModel() {
      public int getRowCount() {
        return(sfc.declist.size());
      }
      
      public int getColumnCount() {
        return(3);  // VarName + Type + Value
      }
      
      public Object getValueAt(int row, int column) {
        Declaration decl = (absynt.Declaration)sfc.declist.get(row);
        if (column == 0) 
          return(decl.var.name);
        else if (column == 1) {
          if (decl.type instanceof IntType) return("Integer"); else return("Boolean");
        } else
           return(decl.val.val.toString());        
      }
      
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Declaration decl = (absynt.Declaration)sfc.declist.get(rowIndex);
        if (columnIndex == 0) 
          decl.var.name=(String)aValue;
        else if (columnIndex == 1) {
          if (((String)aValue) == "Integer") {
            decl.type=new IntType();
            decl.val=new Constval(0);  
          } else {            
            decl.type=new BoolType();
            decl.val=new Constval(false); 
          }
          fireTableCellUpdated(rowIndex, 2);
        } else {
          if (decl.type instanceof IntType) {
            try {
              decl.val.val=((Integer)decl.val.val).valueOf((String)aValue);
            } catch(Exception e) {
              decl.val.val=new Integer(0);
            }
          } else
            decl.val.val=((Boolean)decl.val.val).valueOf((String)aValue);
        }
      }
      
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return(true);
      }
      
      public Class getColumnClass(int columnIndex) {
        return(String.class);
      }
      
      public String getColumnName(int column) {
        if (column == 0) return("Name"); 
        else if (column == 1) return("Type"); 
        else return("Value");
      }

    });
    JComboBox CmbBox = new JComboBox();
    CmbBox.addItem("Integer");  CmbBox.addItem("Boolean");
    
    DataDeclTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(CmbBox));
    DataDeclTable.setPreferredScrollableViewportSize(new Dimension(250, 70)); //***
    DataDeclPanel.add(new JScrollPane(DataDeclTable), BorderLayout.CENTER); 
    //   c) Buttons zum Löschen/Hinzufügen von Dekl.:
    JPanel BtnPanel = new JPanel(new FlowLayout());    
    JButton Btn = new JButton("Add");
    Btn.setActionCommand("AddSFCDeclaration");
    Btn.addActionListener(this);    
    BtnPanel.add(Btn); 
    Btn = new JButton("Delete");
    Btn.setActionCommand("DeleteSFCDeclaration");
    Btn.addActionListener(this);    
    BtnPanel.add(Btn);
    DataDeclPanel.add(BtnPanel, BorderLayout.SOUTH);
    //   d) nun zum DataPanel hinzufügen:
    DataPanel.add(DataDeclPanel);
    // 2. DataActPanel beinhaltet die Aktionen (unter Hälfte des DataPanels):
    DataActPanel = new JPanel(); 
    DataActPanel.setLayout(new BorderLayout()); 
    //   a) "Titelleiste":
    DataActPanel.add(new JLabel("Action-List:"), BorderLayout.NORTH); 
    //   b) Tabelle mit Aktionen:    
    // DataActTable mit SFC-Datenmodell:
    DataActTable = new JTable(new AbstractTableModel() {
      public int getRowCount() {
        return(sfc.actions.size());
      }
      
      public int getColumnCount() {
        return(2);  // Actionname + Expression
      }
      
      public Object getValueAt(int row, int column) {
        if (column == 0) 
          return(((absynt.Action)sfc.actions.get(row)).a_name);
        else
          return(((absynt.Action)sfc.actions.get(row)).sap);
      }
      
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) 
          ((absynt.Action)sfc.actions.get(rowIndex)).a_name=(String)aValue;
        else 
          ((absynt.Action)sfc.actions.get(rowIndex)).sap=(LinkedList)aValue;
      }
      
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return(true);
      }
      
      public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0) return(String.class); else return(LinkedList.class);
      }
      
      public String getColumnName(int column) {
        if (column == 0) return("Name"); else return("Statement");
      }

    });
    // DataActTable-Statement-Renderer:
    DataActTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
      Stmt stmt = null;
      public void setPaintData(Stmt _stmt) {
        stmt = _stmt;
        if (stmt == null) 
          setText("<UNDEF>");
        else if (stmt instanceof Skip)
          setText("SKIP");
        else
          setText("");      
      }
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {                    
          Stmt stmt = null;
          if (value != null && !((LinkedList)value).isEmpty()) stmt = (Stmt)((LinkedList)value).getFirst();
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          setPaintData(stmt);
          return(this);
      }
      public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        if (stmt == null || stmt instanceof Skip)
          super.paint(g2d);
        else {          
          super.paint(g2d);
          ExprEditor.paint_Expression_Panel(((Assign)stmt).val, g2d);	    
        }
      }    

    });
    Btn = new JButton("<EDITING>");
    Btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {                
	      //ExprEditor.setModal(true);
	      ExprEditor.show();
      }
    });
    DataActTable.getColumnModel().getColumn(1).setCellEditor(new ActionEditor(Btn));
    DataActTable.setPreferredScrollableViewportSize(new Dimension(250, 70));
    DataActPanel.add(new JScrollPane(DataActTable), BorderLayout.CENTER); 
    //   c) Buttons zum Löschen/Hinzufügen von Aktionen:
    BtnPanel = new JPanel(new FlowLayout());
    Btn = new JButton("Add");
    Btn.setActionCommand("AddSFCAction");
    Btn.addActionListener(this);    
    BtnPanel.add(Btn); 
    Btn = new JButton("Delete");
    Btn.setActionCommand("DeleteSFCAction");
    Btn.addActionListener(this);    
    BtnPanel.add(Btn);     
    DataActPanel.add(BtnPanel, BorderLayout.SOUTH); 
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
 
  private boolean isSimpleTrans(Transition trans) {
    return((trans.source.size() == 1) && (trans.target.size() == 1));
  }
  
  // liefert LinkedList aller Steps, die Vorgaenger von Step sind:
  private void getPredLL(Step step, StepLL stepLL) {
    stepLL.clear();
    for (int i=0; i < sfc.transs.size(); i++) {
      Transition trans = (Transition)(sfc.transs.get(i));
      if (trans.target.contains(step)) stepLL.addAll(trans.source);
    }    
  }  
  
  // liefert LinkedList aller Steps, die Nachfolger von step sind:
  private void getSuccLL(Step step, StepLL succLL) {        
    for (int i=0; i < sfc.transs.size(); i++) {
      Transition trans = (Transition)(sfc.transs.get(i));
      if (trans.source.contains(step)) {
      	succLL.addAll(trans.target);      	
      }
    }    
  }
  
  // liefert LinkedList aller Steps, die Nachfolger von step sind (nur nicht-paral. Trans werden berücksichtig):
  private void getSimpleSuccLL(Step step, StepLL succLL, TransLL transLL) {
    if (succLL != null) succLL.clear();    
    if (transLL != null) transLL.clear();    
    for (int i=0; i < sfc.transs.size(); i++) {
      Transition trans = (Transition)(sfc.transs.get(i));
      if (isSimpleTrans(trans) && trans.source.contains(step)) {
      	if (succLL != null) succLL.add(trans.target.getFirst());
      	if (transLL != null) transLL.add(trans);
      }
    }    
  }
   
  private TransLL getTranss(Step step) {
  	TransLL resultLL = new TransLL();
  	LinkedList transPosLL = new LinkedList();
  	// alle Transitionen durchsuchen:
  	for (int i=0; i < sfc.transs.size(); i++) {
      Transition trans = (Transition)(sfc.transs.get(i));
      // ist Transition nicht-parallel und hat irgendwas mit unserem step zu tun?:
      if (isSimpleTrans(trans) && (trans.source.contains(step) || trans.target.contains(step))) {
      	TransPosition transPos=(TransPosition)(trans.pos);
      	// wenn zugehörige TransPosition noch nicht berücksichtigt wurde
      	if (!transPosLL.contains(transPos)) {
      		transPosLL.add(transPos);  resultLL.add(trans);
      	}
      }
    }
    return(resultLL);
  }
  
  private void realignTrans(Transition trans) {
  	TransPosition transPos = (TransPosition)trans.pos;
  	if (transPos.autoAlign) ((TransAlignInfo)transPos.transAlignInfo).updateBounds();
  }
  
  // Transition einfügen - BEDINGUNG: diese Transition hat genau einen source- und target-Step:
  private void insertTrans(Transition trans) {
    if (isSimpleTrans(trans)) {
      Step sStep = (Step)(trans.source.getFirst()),  tStep = (Step)(trans.target.getFirst());        
      StepLL succLL = new StepLL();    // alle Nachfolger vom sourceStep holen
      TransLL transLL = new TransLL();
      boolean TransLinked = false;     // wurde schon eine Stelle gefunden, wo die neue Trans sich einklinken kann?
      
      getSimpleSuccLL(sStep, succLL, transLL);  
      succLL.add(tStep);  transLL.add(trans);
      for (int i = 0; i < transAlignInfoLL.size(); i++) {    	
        if (((TransAlignInfo)transAlignInfoLL.get(i)).insertSourceStep(sStep, succLL, transLL)) {
        	System.out.println("TransPos gefunden");        
          TransLinked = true;  break;        
        }      
      }
      if (!TransLinked) transAlignInfoLL.add(new TransAlignInfo(trans));
    }
    sfc.transs.add(trans);
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
      DrawPanel.paintStep((Step)SelectedLL.removeFirst(), (Graphics2D)DrawPanel.getGraphics());         
    // 2. Falls Element <> null, Element in SelectedLL rein und neu (selektiert) zeichnen:
    if (Element != null) {
      SelectedLL.add(Element);
      DrawPanel.paintStep((Step)(Element), (Graphics2D)DrawPanel.getGraphics()); 
    }
  } 
 
  private void switchSelected(Absynt Element) {
    // *** momentan nur für Steps:
    if (SelectedLL.contains(Element)) {
      SelectedLL.remove(Element);
      DrawPanel.paintStep((Step)(Element), (Graphics2D)DrawPanel.getGraphics()); 
    } else {
      SelectedLL.add(Element);
      DrawPanel.paintStep((Step)(Element), (Graphics2D)DrawPanel.getGraphics()); 
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
      DrawPanel.repaint();  // *** intelligentes Zeichnen der geänderten Teile?
    }
  }
  
  public void deleteStep(Step step) {
    // 1. Step aus der Selectedliste:
    if (SelectedLL.contains(step)) SelectedLL.remove(step);
    // 2. Step aus Stepliste des SFC:
    sfc.steps.remove(step);
    // 3. Alle Transitionen mit diesem Step (als Start oder Ziel) raus:
    Transition trans = null;    
    int i=0; 
    Transition stepsTrans = null;
    while (i < sfc.transs.size()) {
      trans = (Transition)sfc.transs.get(i);
      if (trans.source.contains(step) || trans.target.contains(step)) {
        sfc.transs.remove(i); 
        stepsTrans = trans;
      } else 
        i++;
    }
    // Steps Transitionen aus ihrem TransAlignInfo:
    if (stepsTrans != null) {
    	TransAlignInfo transAlignInfo = ((TransPosition)stepsTrans.pos).transAlignInfo;    	
      transAlignInfo.removeTrans(stepsTrans);
      if (transAlignInfo.isEmpty()) transAlignInfoLL.remove(transAlignInfo);
    }
    DrawPanel.repaint();  // *** intelligentes Zeichnen der geänderten Teile?
  }
  
  public void deleteSelection() {
    while (SelectedLL.size() > 0) deleteStep((Step)SelectedLL.getFirst());      
  }
  
  public void moveStep(Step step, double deltaX, double deltaY) {
  	// Step verschieben:
    StepPosition StepPos = (StepPosition)(step.pos);
    StepPos.Bounds.setRect(StepPos.Bounds.getX() + deltaX, StepPos.Bounds.getY() + deltaY, StepPos.Bounds.getWidth(), StepPos.Bounds.getHeight());        
    // TransPosition updaten:
    for (int i=0; i < sfc.transs.size(); i++) {
    	Transition trans = (Transition)sfc.transs.get(i);
    	if (trans.source.getFirst() == step) {
    		TransAlignInfo transAlignInfo = ((TransPosition)trans.pos).transAlignInfo;
    		System.out.println("Move-update");
    		transAlignInfo.updateBounds();
    		Graphics2D gr = (Graphics2D)DrawPanel.getGraphics();
    		gr.setColor(Color.red);
    		gr.draw(transAlignInfo.sBounds);
    		gr.setColor(Color.green);
    		gr.draw(transAlignInfo.tBounds);
    		break;
    	}
    }
    DrawPanel.repaint();  // *** intelligentes Zeichnen der geänderten Teile?
  }
  
  public void moveSelection(double deltaX, double deltaY) {
    for (int i=0; i < SelectedLL.size(); i++) {
    	moveStep((Step)SelectedLL.get(i), deltaX, deltaY);
    	
    }
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
  
  public void actionPerformed(java.awt.event.ActionEvent e) {
    if (e.getActionCommand().equals("AddSFCAction")) {
      sfc.actions.add(new absynt.Action("<UNDEF>", new LinkedList()));      
      ((AbstractTableModel)DataActTable.getModel()).fireTableRowsInserted(sfc.actions.size()-1, sfc.actions.size()-1);
    } else if (e.getActionCommand().equals("DeleteSFCAction")) {
      int row = DataActTable.getSelectedRow();
      if (row >= 0 && row < sfc.actions.size()) {
        sfc.actions.remove(row);
        ((AbstractTableModel)DataActTable.getModel()).fireTableRowsInserted(0, sfc.actions.size()-1);
      }
    } else if (e.getActionCommand().equals("AddSFCDeclaration")) {
      sfc.declist.add(new Declaration(new Variable("<UNDEF>", new BoolType()), new BoolType(), new Constval(false)));
      ((AbstractTableModel)DataDeclTable.getModel()).fireTableRowsInserted(sfc.declist.size()-1, sfc.declist.size()-1);
    } else if (e.getActionCommand().equals("DeleteSFCDeclaration")) {
      int row = DataDeclTable.getSelectedRow();
      if (row >= 0 && row < sfc.declist.size()) {
        sfc.declist.remove(row);
        ((AbstractTableModel)DataDeclTable.getModel()).fireTableRowsInserted(0, sfc.actions.size()-1);
      }
    }
  }
 
} 
