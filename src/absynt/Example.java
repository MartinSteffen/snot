package absynt;


/**
 * 
 * The class offers an example for a program int abstract syntax.
 * @author Initially provided by Martin Steffen.
 * @version $Id: Example.java,v 1.5 2001-05-02 07:37:02 swprakt Exp $	
 */


public class Example{
  /**
   * The following method gives back an example program, containing 
   * most constructs of the abstract syntax. The example is not
   * really meaningful.
   */
    public static SFC getExample1(){
  /*
   * For enhanced readability, the example is built stp-byte-step.
   */
      Variable v_x = new Variable ("x");   
      Constval c5  = new Constval (5);
      Assign stmt1 = new Assign(v_x, c5);  // x := 5
      Assign stmp2 = 
	new Assign(v_x,
		   new U_expr(Expr.NEG, v_x));
      return null;
    };
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Example.java,v 1.5 2001-05-02 07:37:02 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.4  2001/05/02 07:24:51  swprakt
//	Example compiles (but so far is empty)
//	
//	
//---------------------------------------------------------------------



