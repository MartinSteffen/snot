package editor;

import absynt.Position;
import java.awt.geom.*;

public class StepPosition extends Position {
  public Rectangle2D Bounds;

  public StepPosition() {
    super();
    Bounds = new Rectangle2D.Double();
  }

  public StepPosition(double x, double y, double w, double h) {
    super();
    Bounds = new Rectangle2D.Double(x, y, w, h);
  }

  public StepPosition(Rectangle2D _Bounds) {
    super();
    Bounds = _Bounds;
  }
}