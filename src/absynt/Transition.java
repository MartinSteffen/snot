package absynt;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * A transition of an SFC connects source and target step
 * and is labelled with a guard.
 * @author Initially provided by Martin Steffen.
 * @version $Id: Transition.java,v 1.6 2001-05-23 14:56:36 swprakt Exp $	
 */


public class Transition extends Absynt implements Serializable { 
  public LinkedList source;
  public Expr       guard;
  public LinkedList target;
  //------------------
  public Transition (LinkedList _s, Expr _g, LinkedList _t) {
    source = _s;
    guard  = _g;
    target = _t;
  };

  //overloading: empty guard = true
  public Transition (LinkedList _s, LinkedList _t) {
    source = _s;
    guard  = new Constval (true);
    target = _t;
};

}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Transition.java,v 1.6 2001-05-23 14:56:36 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.5  2001/05/04 09:03:45  swprakt
//	Typo removed
//	
//	Revision 1.4  2001/05/04 09:01:05  swprakt
//	I added an new constructure: a transition ``without''
//	a guard means: the guard = true
//	
//	[Steffen]
//	
//	Revision 1.3  2001/05/03 14:02:08  swprakt
//	Variablen sind jetzt public
//	
//	Revision 1.2  2001/05/02 07:03:37  swprakt
//	Abstract syntax compiles.
//	
//	Revision 1.1  2001/05/02 05:56:06  swprakt
//	First proposal according to the requirement spec.
//	
//	
//---------------------------------------------------------------------

