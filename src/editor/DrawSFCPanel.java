package editor;

import java.util.LinkedList;
import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.awt.event.*;
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
     
     private Step SourceStep;

     private void paintTrans(Transition Trans, Graphics2D G2D) { 
      Step step; 
      LinkedList stepLL = Trans.source; 
      Step SourceStep = (Step)Trans.source.get(0), DestStep = (Step)Trans.target.get(0);
      Rectangle2D SourceRect = ((StepPosition)(SourceStep.pos)).Bounds, DestRect = ((StepPosition)(DestStep.pos)).Bounds;
      double deltaX, deltaY;
      GeneralPath TransLine = new GeneralPath();
      int i; 
 
      deltaX = SourceRect.getCenterX() - DestRect.getCenterX();
      deltaY = SourceRect.getCenterY() - DestRect.getCenterY();
      
      TransLine.moveTo((new Double(SourceRect.getCenterX())).floatValue(), (new Double(SourceRect.getCenterY())).floatValue());
      TransLine.lineTo((new Double(SourceRect.getCenterX())).floatValue(), (new Double(SourceRect.getCenterY() - deltaY/2)).floatValue());
      TransLine.lineTo((new Double(DestRect.getCenterX())).floatValue(), (new Double(SourceRect.getCenterY() - deltaY/2)).floatValue());
      TransLine.lineTo((new Double(DestRect.getCenterX())).floatValue(),(new Double(DestRect.getCenterY())).floatValue());
      G2D.draw(TransLine);
      
      /*
      if (stepLL != null) { 
        for (i=0; i < stepLL.size(); i++) { 
          step = (Step)stepLL.get(i);           
        } 
      } 
 
      stepLL = Trans.target; 
      if (stepLL != null) { 
        for (i=0; i < stepLL.size(); i++) {           
        } 
      } 
       */
    } 
 
    private void paintStep(Step step, Graphics2D G2D) { 
      StepPosition StepPos = (StepPosition)(step.pos);
      Rectangle2D StrBounds = G2D.getFontMetrics().getStringBounds(step.name, G2D);
      LineMetrics LnMetrics = G2D.getFontMetrics().getLineMetrics(step.name, G2D);
      StepPos.Bounds.setFrame(StepPos.Bounds.getX(), StepPos.Bounds.getY(), StrBounds.getWidth(), StrBounds.getHeight());
      G2D.draw(StepPos.Bounds); 
      G2D.drawString(step.name, 
        (new Double(StepPos.Bounds.getX())).floatValue(), (new Double(StepPos.Bounds.getMaxY() - LnMetrics.getDescent())).floatValue()); 
    }
      
    
    private void paintSFC(Graphics2D G2D) { 
      Transition Trans; 
      Step       step;
      LinkedList transLL = sfc.transs; 
      LinkedList stepsLL = sfc.steps;
 
      if (transLL != null) { 
        for (int i=0; i < transLL.size(); i++) { 
          Trans = (Transition)transLL.get(i); 
          paintTrans(Trans, G2D); 
        } 
      } 
      if (stepsLL != null) {
        for (int i=0; i < stepsLL.size(); i++) { 
          step = (Step)stepsLL.get(i); 
          paintStep(step, G2D); 
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
    
    protected void processMouseEvent(MouseEvent e) {      
      Step step;
	//super.processMouseEvent(e);
      if (e.getID() == MouseEvent.MOUSE_PRESSED) {
        switch (editorAction) {
          case INSERT_STEP : { 
            step = new Step("Neuer Step");        
            step.pos = new StepPosition((new Integer(e.getX())).doubleValue(), (new Integer(e.getY())).doubleValue(), 100f, 100f);
            sfc.steps.add(step);
          }
          case INSERT_TRANS : {
            SourceStep = checkStepHit((new Integer(e.getX())).doubleValue(), (new Integer(e.getY())).doubleValue());
          }  
        }
	repaint();	                                            
      }
      if (e.getID() == MouseEvent.MOUSE_RELEASED) {
        switch (editorAction) {
            case INSERT_TRANS : {
                if (SourceStep != null) {
                    step = checkStepHit((new Integer(e.getX())).doubleValue(), (new Integer(e.getY())).doubleValue());
                    if (step == null) {
                      SourceStep = null;
                      System.out.println("DestStep = null");
                    } else { 
                        LinkedList SourceSteps = new LinkedList();
                        SourceSteps.add(SourceStep);
                        LinkedList DestSteps = new LinkedList();
                        DestSteps.add(step);
                        Transition Trans = new Transition(SourceSteps, null, DestSteps);
                        sfc.transs.add(Trans);
                        repaint();
                    }                    
                } // if (SourceStep ... 
                else System.out.println("SourceStep = null");
            }
        } // switch     
      }  // if
    } 
  } 
