package editor;
import absynt.*;
import java.awt.geom.*;
import java.util.LinkedList;
import editor.TransPosition;

// TransAlignInfo fasst Steps zusammen, deren (nicht-parallele) Transitionen sich eine waagerechte
// Linie teilen:
public class TransAlignInfo {
	public LinkedList sSteps = new LinkedList();    // source-Steps
	public LinkedList tSteps = new LinkedList(); 		// target-Steps
	public LinkedList transs = new LinkedList(); 		// zugehörige Trans.
	public Rectangle2D sBounds = new Rectangle2D.Double(); // alle sSteps umfassendes Rechteck
	public Rectangle2D tBounds = new Rectangle2D.Double(); // alle tSteps umfassendes Rechteck
	public double bendPos;
	
	private boolean haveSameElements(LinkedList list1, LinkedList list2) {
    if (list1.size() != list2.size()) return(false);
    for (int i = 0; i < list1.size(); i++) {
      if (!list2.contains(list1.get(i))) return(false);
    }
    return(true);
  }
	
	// TransAlignInfo-Instanz wird nur erstellt mit mind. einer (nicht-parallelen) Transition:
	public TransAlignInfo(Transition trans) {
		Step sStep = (Step)trans.source.getFirst();
		Step tStep = (Step)trans.target.getFirst();
		sSteps.add(sStep);   tSteps.add(tStep);  
		sBounds.setRect(((StepPosition)sStep.pos).Bounds);
		tBounds.setRect(((StepPosition)tStep.pos).Bounds);
		transs.add(trans);
		trans.pos = new TransPosition(this);
		updateBendPos();
	}
	
	// berechnet die Position für den "Knick" in den Transitionen:
	public void updateBendPos() {
		bendPos = ((tBounds.getCenterY() - sBounds.getCenterY()) / 2.0f) + sBounds.getCenterY();
	}
	
	public void updateBounds() {
		sBounds.setRect(((StepPosition)((Step)sSteps.getFirst()).pos).Bounds);
		for (int i=0; i < sSteps.size(); i++) {
			Step step = (Step)sSteps.get(i);
			sBounds = sBounds.createUnion(((StepPosition)step.pos).Bounds);
		}
		tBounds.setRect(((StepPosition)((Step)tSteps.getFirst()).pos).Bounds);
		for (int i=0; i < tSteps.size(); i++) {
			Step step = (Step)tSteps.get(i);
			tBounds = tBounds.createUnion(((StepPosition)step.pos).Bounds);
		}
		updateBendPos();
	}
	
	// wenn seine tStep passen werden sie mit Trans eingefügt:
	public boolean insertSourceStep(Step step, LinkedList stepLL, LinkedList transLL) {
		if (haveSameElements(tSteps, stepLL)) {
			sSteps.add(step);  
			sBounds = sBounds.createUnion(((StepPosition)step.pos).Bounds);
			transs.addAll(transLL);
			updateBendPos();
			for (int i=0; i < transLL.size(); i++) {
				((Transition)transLL.get(i)).pos = new TransPosition(this);				
			};
			return(true);
		} else
		  return(false);
	}
}