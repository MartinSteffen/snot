package editor;

import java.util.LinkedList;
import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import javax.swing.*;
import absynt.*;
import editor.StepPosition;

 public class DrawSFCPanel extends JPanel { 

     private SFC sfc;

     public int editorAction;

     final static public int SELECT = 0;
     final static public int INSERT_STEP = 1;
     final static public int INSERT_TRANS = 2;
     final static public int DELETE = 3;

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

    public DrawSFCPanel(SFC anSFC) {
      sfc = anSFC;
      enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    } 

    protected void processMouseEvent(MouseEvent e) {
	//super.processMouseEvent(e);
      if (e.getID() == MouseEvent.MOUSE_PRESSED) {
        Step step = new Step("Neuer Step");
        step.Pos = new StepPosition(e.getX().doubleValue()/30, e.getY().doubleValue()/30, 1f, 1f);
        sfc.steps.add(step);
	repaint();
	System.out.println("Mouse");
      }
    } 
  } 
