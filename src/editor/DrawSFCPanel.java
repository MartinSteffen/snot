package editor;

import java.util.Enumeration;
import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import javax.swing.*;
import absynt.*;
import editor.StepPosition;

public class DrawSFCPanel extends JPanel {

  private Font sfcFont;
  private float zoomFac = 1.0f;
  private SFC sfc;

  private void paintTrans(Transition Trans, Graphics2D G2D) {
    Step step;
    StepList SL = Trans.source;
    float minX = 10000.0f, maxX = 0.0f;

    while (SL != null && SL.head != null) {
      step = (Step)SL.head;
      G2D.draw(new Rectangle2D.Float(step.pos.x*30f, step.pos.y*30f, 30.0f, 30.0f));
      G2D.drawString(step.name, step.pos.x*30f, (step.pos.y+1)*30f);
      if (step.pos.x < minX) minX=step.pos.x;
      if (step.pos.x > maxX) maxX=step.pos.x;
      SL = (StepList)SL.nextElement();
    }

    SL = Trans.target;
    while (SL != null && SL.head != null) {
      step = (Step)SL.head;
      G2D.draw(new Rectangle2D.Float(step.pos.x*30f, step.pos.y*30f, 30.0f, 30.0f));
      G2D.drawString(step.name, step.pos.x*30f, (step.pos.y+1)*30f);
      SL = (StepList)SL.nextElement();
    }
  }

  private void paintSFC(Graphics2D G2D) {
    Transition Trans;
    TransitionList TS = sfc.transs;

    while (TS != null && TS.head != null) {
      Trans = (Transition)TS.head;
      paintTrans(Trans, G2D);
      TS = (TransitionList)TS.nextElement();
    }

  }

  public void paint(Graphics g) {
    Graphics2D G2D = (Graphics2D)g;
    if (sfcFont == null) sfcFont = new Font("Helvetica", Font.PLAIN, 10);
    //GeneralPath Path = new GeneralPath();
    //Font font = new Font("

    super.paint(g);
    //G2D.scale(10.0f, 10.0f);
    G2D.setColor(Color.black);
    if (sfc != null) paintSFC(G2D);
    //G2D.draw(Path);
  }

  public void setSFCFont(Font _font) {
    sfcFont = _font.deriveFont(_font.getSize2D());
    repaint();
  }

  public Font getSFCFont() {
    return (sfcFont.deriveFont(sfcFont.getSize2D()));
  }

  public DrawSFCPanel(SFC _sfc) {
    super();
    sfcFont = new Font("Helvetica", Font.PLAIN, 10);
    sfc = _sfc;
  }

}