package absynt;
import java.io.Serializable;


/**
 * SFC.java
 * Class for sfc-programs, the top level syntactic construct,
 * i.e., the entry point.
 * @author Initially provided by Martin Steffen.
 * @version $Id: SFC.java,v 1.3 2001-05-02 07:03:37 swprakt Exp $
 */


public class SFC extends Absynt implements Serializable{ 
  public Step istep;
  public StepList steps;
  public TransitionList transs;
  public ActionList actions;
  // -------------------
  /** 
   * Constructor just stores the arguments into the fields
   **/
  public SFC (Step _istep,
	      StepList _steps, 
	      TransitionList _transs,
	      ActionList _actions) {
    istep  = _istep;
    steps  = _steps;
    transs = _transs;
    actions = _actions;
  }
}




//----------------------------------------------------------------------
//	Abstract Syntax for Snot Programs
//	------------------------------------
//
//	$Id: SFC.java,v 1.3 2001-05-02 07:03:37 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2001/05/02 04:40:19  swprakt
//	ok
//	
//	
//---------------------------------------------------------------------

