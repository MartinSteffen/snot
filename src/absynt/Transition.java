package absynt;
import java.io.Serializable;


/**
 * A transition of an SFC connects source and target step
 * and is labelled with a guard.
 * @author Initially provided by Martin Steffen.
 * @version $Id: Transition.java,v 1.1 2001-05-02 05:56:06 swprakt Exp $	
 */


public class Transition extends Absynt implements Serializable { 
  StepList source;
  Expr     guard;
  StepList target;
  //------------------
  public Transition (StepList _s; Expr _g, StepList _t) {
    source = _s;
    guard  = _g;
    target = _t;
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Transition.java,v 1.1 2001-05-02 05:56:06 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//---------------------------------------------------------------------

