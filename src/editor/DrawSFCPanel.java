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
import editor.PosRect;

public class DrawSFCPanel extends JPanel { 

     private Editor editor;
     public SFC sfc;     
             
     private void paintTrans(Transition Trans, Graphics2D G2D) {       
      LinkedList stepLL = Trans.source; 
      Step SourceStep = (Step)Trans.source.get(0), DestStep = (Step)Trans.target.get(0);
      PosRect SourceRect = ((StepPosition)(SourceStep.pos)).Bounds, DestRect = ((StepPosition)(DestStep.pos)).Bounds;      
      double deltaX, deltaY;
      GeneralPath TransLine = new GeneralPath();
      int i, j; 
 
      deltaX = SourceRect.getMidX() - DestRect.getMidX();
      deltaY = SourceRect.getMidY() - DestRect.getMidY();
      
      G2D.setClip(null);
      Area ClipArea = new Area(getBounds(null));
      PosRect sourceUnion = null, targetUnion = null;
      
      // SourceSteps nicht übermalen:
      for (i = 0; i < Trans.source.size(); i++) {
        Step step = (Step)Trans.source.get(i);
        PosRect StepRect = ((StepPosition)(step.pos)).Bounds;
        if (sourceUnion == null) 
          sourceUnion = new PosRect(StepRect); 
        else 
          sourceUnion.unionRect(StepRect);
        ClipArea.subtract(new Area(StepRect.createRect2D()));        
      }
      // TargetSteps nicht übermalen:
      for (i = 0; i < Trans.target.size(); i++) {
        Step step = (Step)Trans.target.get(i);
        PosRect StepRect = ((StepPosition)(step.pos)).Bounds;
        if (targetUnion == null) 
          targetUnion = new PosRect(StepRect); 
        else 
          targetUnion.unionRect(StepRect);
        ClipArea.subtract(new Area(StepRect.createRect2D()));        
      }
        
      G2D.clip(ClipArea);
      
      if (Trans.target.size() == 1 && Trans.source.size() == 1) {
        // einfache Transitionen:     
        TransPosition transPos = (TransPosition)Trans.pos;
        if (transPos != null && transPos.autoAlign && transPos.transAlignInfo != null) {      	
          float dy = (new Double(transPos.transAlignInfo.bendPos)).floatValue();
          float sx = SourceRect.getMidXAsFloat();
          float sy = SourceRect.getMidYAsFloat();
          float tx = DestRect.getMidXAsFloat();
          float ty = DestRect.getMidYAsFloat();
        
          TransLine.moveTo(sx, sy);
          if (ty <= sy ) {  // Transition von unten nach oben          
            sy = SourceRect.getMaxYAsFloat() + (new Double(StepBorder)).floatValue();
            TransLine.lineTo(sx, sy);
            sx = SourceRect.getMaxXAsFloat() + (new Double(StepBorder)).floatValue();
            TransLine.lineTo(sx, sy);
            TransLine.lineTo(sx, dy);
            float tx1 = DestRect.getMaxXAsFloat() + (new Double(StepBorder)).floatValue();
            TransLine.lineTo(tx1, dy);
            float ty1 = DestRect.getMinYAsFloat() - (new Double(StepBorder)).floatValue();
            TransLine.lineTo(tx1, ty1);
            TransLine.lineTo(tx, ty1);
            TransLine.lineTo(tx, ty);                      
          } else { // Transition von oben nach unten:
            TransLine.lineTo(sx, dy);
            TransLine.lineTo(tx, dy);
            TransLine.lineTo(tx, ty);
          }
          
          G2D.setColor(Color.black);	      
          G2D.draw(TransLine);
          
        }
      } else {
        // Paralle Transitionen:
        deltaY=sourceUnion.getMinY() + ((targetUnion.getMaxY() - sourceUnion.getMinY()) / 2.0);
        for (i=0; i < Trans.source.size(); i++) {
          PosRect sRect = ((StepPosition)((Step)Trans.source.get(i)).pos).Bounds;
          for (j=0; j < Trans.target.size(); j++) {
            PosRect tRect = ((StepPosition)((Step)Trans.target.get(j)).pos).Bounds;

            TransLine.moveTo(sRect.getMidXAsFloat(), sRect.getMidYAsFloat());
            TransLine.lineTo(sRect.getMidXAsFloat(), (new Double(deltaY)).floatValue());
            TransLine.lineTo(tRect.getMidXAsFloat(), (new Double(deltaY)).floatValue());
            TransLine.lineTo(tRect.getMidXAsFloat(), tRect.getMidYAsFloat());
            	                              
          }
        }
        G2D.setColor(Color.green);
        G2D.setStroke(new BasicStroke(3));
        G2D.draw(TransLine);
        G2D.setStroke(new BasicStroke());
      }
      G2D.setClip(null);      
    } 
 
    double StepBorder = 8.0f;
    
    public void paintStep(Step step, Graphics2D G2D, boolean selected, boolean isSrcStep, boolean isTrgtStep) {
      StepPosition StepPos = (StepPosition)(step.pos);
      Rectangle2D StrBounds = G2D.getFontMetrics().getStringBounds(step.name, G2D);
      LineMetrics LnMetrics = G2D.getFontMetrics().getLineMetrics(step.name, G2D);
      StepPos.Bounds.setRectWH(StepPos.Bounds.getMinX(), StepPos.Bounds.getMinY(), StrBounds.getWidth() + 2*StepBorder, StrBounds.getHeight() + 2*StepBorder);
      /* Rectangle ClipRect = G2D.getClipBounds();
      if (ClipRect != null && ClipRect.intersects(StepPos.Bounds)) { */
        
        if (step == sfc.istep) { 
          G2D.setPaint(Color.lightGray);
          G2D.fill(StepPos.Bounds.createRect2D());
        }
        if (isSrcStep) { 
          G2D.setPaint(Color.green);
          G2D.fill(StepPos.Bounds.createRect2D());
        } else
        if (isTrgtStep) { 
          G2D.setPaint(Color.red);
          G2D.fill(StepPos.Bounds.createRect2D());
        }
        G2D.setPaint(Color.black);
        if (selected) G2D.setColor(Color.blue); else G2D.setColor(Color.black);                
        G2D.draw(StepPos.Bounds.createRect2D());        
        G2D.drawString(step.name, 
          (new Double(StepPos.Bounds.getMinX() + StepBorder)).floatValue(), (new Double(StepPos.Bounds.getMaxY() - LnMetrics.getDescent() - StepBorder)).floatValue()); 
      /* }  */
      if (!step.actions.isEmpty()) {
        String actname = ((absynt.StepAction)step.actions.getFirst()).a_name;
        StrBounds = G2D.getFontMetrics().getStringBounds(actname, G2D);
        LnMetrics = G2D.getFontMetrics().getLineMetrics(actname, G2D);
        PosRect posRect = new PosRect(StepPos.Bounds);
        posRect.setLocation(posRect.getMaxX() + StepBorder, posRect.getMinY());
        posRect.setWidth(StrBounds.getWidth() + 2*StepBorder);
        G2D.draw(posRect.createRect2D());
        G2D.drawString(actname, 
            (new Double(posRect.getMinX() + StepBorder)).floatValue(), (new Double(posRect.getMaxY() - LnMetrics.getDescent() - StepBorder)).floatValue());       
      }
    }
    
    public void paintStep(Step step, Graphics2D G2D) { 
      paintStep(step, G2D, 
        editor.SelectedLL.contains(step), editor.sourceSteps.contains(step), editor.targetSteps.contains(step));
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
      if (sfc != null && !editor.aligningSFC) 
        paintSFC(G2D); 
      else {
        System.out.print("Can't paint SFC - ");
        if (editor.aligningSFC) System.out.println("SFC in aligningMode"); else System.out.println("SFC = null");        
      }
      //G2D.draw(Path); 
    } 

    public DrawSFCPanel(Editor _editor) {
      editor = _editor;  sfc = editor.getSFC();
      enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }
} 
