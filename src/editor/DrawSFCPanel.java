package editor;

import java.util.LinkedList;
import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.awt.event.*;
import javax.swing.*;
import absynt.*;
import editor.StepPosition;
import editor.TransPosition;

public class DrawSFCPanel extends JPanel { 

     private Editor editor;
     public SFC sfc;     
             
     private void paintTrans(Transition Trans, Graphics2D G2D) {       
      LinkedList stepLL = Trans.source; 
      Step SourceStep = (Step)Trans.source.get(0), DestStep = (Step)Trans.target.get(0);
      Rectangle2D SourceRect = ((StepPosition)(SourceStep.pos)).Bounds, DestRect = ((StepPosition)(DestStep.pos)).Bounds;      
      double deltaX, deltaY;
      GeneralPath TransLine = new GeneralPath();
      int i, j; 
 
      deltaX = SourceRect.getCenterX() - DestRect.getCenterX();
      deltaY = SourceRect.getCenterY() - DestRect.getCenterY();
      
      G2D.setClip(null);
      Area ClipArea = new Area(getBounds(null));
      Rectangle2D sourceUnion = null, targetUnion = null;
      
      // SourceSteps nicht übermalen:
      for (i = 0; i < Trans.source.size(); i++) {
        Step step = (Step)Trans.source.get(i);
        Rectangle2D StepRect = ((StepPosition)(step.pos)).Bounds;
        if (sourceUnion == null) 
          sourceUnion = new Rectangle2D.Double(StepRect.getX(), StepRect.getY(), StepRect.getWidth(), StepRect.getHeight()); 
        else 
          sourceUnion=sourceUnion.createUnion(StepRect);
        ClipArea.subtract(new Area(StepRect));        
      }
      // TargetSteps nicht übermalen:
      for (i = 0; i < Trans.target.size(); i++) {
        Step step = (Step)Trans.target.get(i);
        Rectangle2D StepRect = ((StepPosition)(step.pos)).Bounds;
        if (targetUnion == null) 
          targetUnion = new Rectangle2D.Double(StepRect.getX(), StepRect.getY(), StepRect.getWidth(), StepRect.getHeight()); 
        else 
          targetUnion=targetUnion.createUnion(StepRect);
        ClipArea.subtract(new Area(StepRect));        
      }
        
      G2D.clip(ClipArea);
      
      if (Trans.target.size() == 1 && Trans.source.size() == 1) {
        // einfache Transitionen:     
        TransPosition transPos = (TransPosition)Trans.pos;
        if (transPos != null && transPos.autoAlign && transPos.transAlignInfo != null) {      	
          deltaY = transPos.transAlignInfo.bendPos;
        
          TransLine.moveTo((new Double(SourceRect.getCenterX())).floatValue(), (new Double(SourceRect.getCenterY())).floatValue());
          TransLine.lineTo((new Double(SourceRect.getCenterX())).floatValue(), (new Double(deltaY)).floatValue());
          TransLine.lineTo((new Double(DestRect.getCenterX())).floatValue(), (new Double(deltaY)).floatValue());
          TransLine.lineTo((new Double(DestRect.getCenterX())).floatValue(),(new Double(DestRect.getCenterY())).floatValue());
          
          G2D.setColor(Color.black);	      
          G2D.draw(TransLine);
          
        }
      } else {
        // Paralle Transitionen:
        deltaY=sourceUnion.getMinY() + ((targetUnion.getMaxY() - sourceUnion.getMinY()) / 2.0);
        for (i=0; i < Trans.source.size(); i++) {
          Rectangle2D sRect = ((StepPosition)((Step)Trans.source.get(i)).pos).Bounds;
          for (j=0; j < Trans.target.size(); j++) {
            Rectangle2D tRect = ((StepPosition)((Step)Trans.target.get(j)).pos).Bounds;

            TransLine.moveTo((new Double(sRect.getCenterX())).floatValue(), (new Double(sRect.getCenterY())).floatValue());
            TransLine.lineTo((new Double(sRect.getCenterX())).floatValue(), (new Double(deltaY)).floatValue());
            TransLine.lineTo((new Double(tRect.getCenterX())).floatValue(), (new Double(deltaY)).floatValue());
            TransLine.lineTo((new Double(tRect.getCenterX())).floatValue(),(new Double(tRect.getCenterY())).floatValue());
            	                              
          }
        }
        G2D.setColor(Color.green);
        G2D.draw(TransLine);
      }
      G2D.setClip(null);      
    } 
 
    double StepBorder = 8.0f;
    
    public void paintStep(Step step, Graphics2D G2D, boolean selected) {
      StepPosition StepPos = (StepPosition)(step.pos);
      Rectangle2D StrBounds = G2D.getFontMetrics().getStringBounds(step.name, G2D);
      LineMetrics LnMetrics = G2D.getFontMetrics().getLineMetrics(step.name, G2D);
      StepPos.Bounds.setFrame(StepPos.Bounds.getX(), StepPos.Bounds.getY(), StrBounds.getWidth() + 2*StepBorder, StrBounds.getHeight() + 2*StepBorder);
      /* Rectangle ClipRect = G2D.getClipBounds();
      if (ClipRect != null && ClipRect.intersects(StepPos.Bounds)) { */
        if (selected) G2D.setColor(Color.blue); else G2D.setColor(Color.black);        
        if (step == sfc.istep) { 
          G2D.setPaint(Color.lightGray);
          G2D.fill(StepPos.Bounds);
        }
        G2D.setPaint(Color.black);
        G2D.draw(StepPos.Bounds);        
        G2D.drawString(step.name, 
          (new Double(StepPos.Bounds.getX() + StepBorder)).floatValue(), (new Double(StepPos.Bounds.getMaxY() - LnMetrics.getDescent() - StepBorder)).floatValue()); 
      /* }  */
    }
    
    public void paintStep(Step step, Graphics2D G2D) { 
      paintStep(step, G2D, editor.SelectedLL.contains(step));
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
      if (sfc != null && !editor.aligningSFC) paintSFC(G2D); 
      //G2D.draw(Path); 
    } 

    public DrawSFCPanel(Editor _editor) {
      editor = _editor;  sfc = editor.getSFC();
      enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }
} 
