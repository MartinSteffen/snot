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
		trans.pos = new TransPosition(this);
		updateBendPos();
	}
	
	// berechnet die Position für den "Knick" in den Transitionen:
	public void updateBendPos() {
		bendPos = ((tBounds.getMinY() - sBounds.getMaxY()) / 2.0f) + sBounds.getMaxY();
	}
	
	public void updateBounds() {
		sBounds.setRect(((StepPosition)((Step)sSteps.getFirst()).pos).Bounds);
		for (int i=0; i < sSteps.size(); i++) {
			Step step = (Step)sSteps.get(i);
			System.out.print(" (" + step.name + ") ");
			sBounds = sBounds.createUnion(((StepPosition)step.pos).Bounds);
		}
		System.out.println();
		tBounds.setRect(((StepPosition)((Step)tSteps.getFirst()).pos).Bounds);
		for (int i=0; i < tSteps.size(); i++) {
			Step step = (Step)tSteps.get(i);
			tBounds = tBounds.createUnion(((StepPosition)step.pos).Bounds);
		}
		updateBendPos();
	}
	
	// wenn seine TargetStep passen werden sie mit Trans eingefügt:
	public boolean insertSourceStep(Step step, LinkedList stepLL, LinkedList transLL) {
		if (haveSameElements(tSteps, stepLL)) {
			// neuen SourceStep einfügen (seine TargetSteps sind ja schon in tSteps):
			sSteps.add(step);  
			// entspr. die "union-Box" erweitern:
			sBounds = sBounds.createUnion(((StepPosition)step.pos).Bounds);			
			// allen Transitionen von SourceStep diese TransPosition zuordnen:
			for (int i=0; i < transLL.size(); i++) {
				Transition trans = (Transition)transLL.get(i);
				trans.pos = new TransPosition(this);
			};
			// neue Pos für gemeinsame horiz. Linie der Transitionen berechnen:
			updateBendPos();
			return(true);
		} else
		  return(false);
	}
	
	// wenn eine Transition entfernt wird, dann passen alle Trans des zugehörigen sSteps nicht:
	public Step removeTrans(Transition trans) {				
		Step sStep = (Step)trans.source.getFirst();
		sSteps.remove(sStep);
		if (sSteps.size() > 0) updateBounds();
		System.out.println(sSteps.size());
		return(sStep);
	}
	
	public boolean isEmpty() {
		return(sSteps.size() == 0);
  }
}