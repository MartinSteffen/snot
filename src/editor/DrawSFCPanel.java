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
     private SFC sfc;     
             
     private void paintTrans(Transition Trans, Graphics2D G2D) {       
      LinkedList stepLL = Trans.source; 
      Step SourceStep = (Step)Trans.source.get(0), DestStep = (Step)Trans.target.get(0);
      Rectangle2D SourceRect = ((StepPosition)(SourceStep.pos)).Bounds, DestRect = ((StepPosition)(DestStep.pos)).Bounds;      
      double deltaX, deltaY;
      GeneralPath TransLine = new GeneralPath();
      int i; 
 
      deltaX = SourceRect.getCenterX() - DestRect.getCenterX();
      deltaY = SourceRect.getCenterY() - DestRect.getCenterY();
      
      G2D.setClip(null);
      Area ClipArea = new Area(getBounds(null));
      Rectangle SourceUnion = new Rectangle(), DestUnion = new Rectangle();      
      
      for (i = 0; i < Trans.source.size(); i++) {
        Step step = (Step)Trans.source.get(i);
        Rectangle2D StepRect = ((StepPosition)(step.pos)).Bounds;
        ClipArea.subtract(new Area(StepRect));
        //SourceUnion = SourceUnion.createUnion(StepRect.getBounds());
      }
      for (i = 0; i < Trans.target.size(); i++) {
        Step step = (Step)Trans.target.get(i);
        Rectangle2D StepRect = ((StepPosition)(step.pos)).Bounds;
        ClipArea.subtract(new Area(StepRect));
        //DestUnion = DestUnion.createUnion(StepRect.getBounds());
      }
        
      G2D.clip(ClipArea);
      /*
      int Sects = 4;  Direction = 1;
      if (Trans.source.size() = 1) Sects--;
      if (Trans.target.size() = 1) Sects--;
      if (DestUnion.getMaxY() < SourceUnion.getMinY()) Direction = -1;
      for (i = 0; i < Trans.source.size(); i++) {
        Step step = (Step)Trans.source.get(i);
        Rectangle2D StepRect = ((StepPosition)(step.pos)).Bounds;
        TransLine.moveTo((new Double(StepRect.getCenterX())).floatValue(), (new Double(StepRect.getCenterY())).floatValue());      
        TransLine.lineTo((new Double(StepRect.getCenterX())).floatValue(), (new Double(StepRect.getCenterY() - deltaY/Sects)).floatValue());
        TransLine.lineTo((new Double(DestRect.getCenterX())).floatValue(), (new Double(StepRect.getCenterY() - deltaY/Sects)).floatValue());
        TransLine.lineTo((new Double(DestRect.getCenterX())).floatValue(),(new Double(DestRect.getCenterY())).floatValue());
      }
      for (i = 0; i < Trans.target.size(); i++) {
        Step step = (Step)Trans.target.get(i);
        Rectangle2D StepRect = ((StepPosition)(step.pos)).Bounds;
        ClipArea.subtract(new Area(StepRect));
        DestUnion = DestUnion.createUnion(StepRect);
	}
      
      
      
      TransLine.moveTo((new Double(SourceRect.getCenterX())).floatValue(), (new Double(SourceRect.getCenterY())).floatValue());
      TransLine.lineTo((new Double(SourceRect.getCenterX())).floatValue(), (new Double(SourceRect.getCenterY() - deltaY/2)).floatValue());
      TransLine.lineTo((new Double(DestRect.getCenterX())).floatValue(), (new Double(SourceRect.getCenterY() - deltaY/2)).floatValue());
      TransLine.lineTo((new Double(DestRect.getCenterX())).floatValue(),(new Double(DestRect.getCenterY())).floatValue());
      */
      
      //SourceRect = ((TransPosition)Trans.pos).sourceBounds;
      //DestRect = ((TransPosition)Trans.pos).targetBounds;
      TransPosition transPos = (TransPosition)Trans.pos;
      if (transPos.autoAlign && transPos.transAlignInfo != null) {      	
        deltaY = transPos.transAlignInfo.bendPos;
      
	      TransLine.moveTo((new Double(SourceRect.getCenterX())).floatValue(), (new Double(SourceRect.getCenterY())).floatValue());
	      TransLine.lineTo((new Double(SourceRect.getCenterX())).floatValue(), (new Double(deltaY)).floatValue());
	      TransLine.lineTo((new Double(DestRect.getCenterX())).floatValue(), (new Double(deltaY)).floatValue());
	      TransLine.lineTo((new Double(DestRect.getCenterX())).floatValue(),(new Double(DestRect.getCenterY())).floatValue());
	      	      
	      G2D.draw(TransLine);
	      G2D.setClip(null);
	    }
      
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
 
    double StepBorder = 8.0f;
    
    public void paintStep(Step step, Graphics2D G2D, boolean selected) {
      StepPosition StepPos = (StepPosition)(step.pos);
      Rectangle2D StrBounds = G2D.getFontMetrics().getStringBounds(step.name, G2D);
      LineMetrics LnMetrics = G2D.getFontMetrics().getLineMetrics(step.name, G2D);
      StepPos.Bounds.setFrame(StepPos.Bounds.getX(), StepPos.Bounds.getY(), StrBounds.getWidth() + 2*StepBorder, StrBounds.getHeight() + 2*StepBorder);
      /* Rectangle ClipRect = G2D.getClipBounds();
      if (ClipRect != null && ClipRect.intersects(StepPos.Bounds)) { */
        if (selected) G2D.setColor(Color.blue); else G2D.setColor(Color.black);
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
      if (sfc != null) paintSFC(G2D); 
      //G2D.draw(Path); 
    } 

    public DrawSFCPanel(Editor _editor) {
      editor = _editor;  sfc = editor.getSFC();
      enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }
} 
