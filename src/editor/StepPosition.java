package editor;

import absynt.Position;
import editor.PosRect;

public class StepPosition extends Position implements java.io.Serializable {
  public PosRect Bounds;

  public StepPosition() {
    super();
    Bounds = new PosRect();
  }

  public StepPosition(double x, double y, double w, double h) {
    super();
    Bounds = new PosRect(x, y, x+w, y+h);
  }

  public StepPosition(PosRect _Bounds) {
    super();
    Bounds = _Bounds;
  }
}