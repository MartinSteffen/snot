package absynt;

import utils.PrettyPrint;
import java.util.LinkedList;

/**
 * 
 * The class offers an example for a program int abstract syntax.
 * @author Initially provided by Martin Steffen.
 * @version $Id: Example.java,v 1.12 2001-05-23 14:56:33 swprakt Exp $	
 */


public class Example{
  /**
   * The following method gives back an example program, containing 
   * most constructs of the abstract syntax. The example is not
   * really meaningful.
   */
    public static LinkedList getExampleStepActionList1(){
	LinkedList sal1 = new LinkedList();
	sal1.addLast(new StepAction(new Nqual(), new String("act1")));
	return sal1;
    };

    public static SFC getExample1(){
  /*
   * For enhanced readability, the example is built step-by-step.
   */
      BoolType btype = new BoolType();
      Variable v_x = new Variable ("x", btype);
      Variable v_y = new Variable ("y", btype);
      Variable v_z = new Variable ("z", btype);
      Constval c5  = new Constval (5);
      Constval cfalse = new Constval (false);
      Skip     stmt_skip = new Skip();

      //   ------- Variable-Declarations
      Declaration dec_x = new Declaration(v_x, btype, cfalse);
      Declaration dec_y = new Declaration(v_y, btype, cfalse);
      Declaration dec_z = new Declaration(v_z, btype, cfalse);

      //   ------- Step 1
      Assign stmt1 =                 
	new Assign(v_x,                    // x := false
		   new Constval(false));
      LinkedList sl1 = new LinkedList();
      sl1.addLast(stmt1);
      Action a1 = 
	new Action (new String("act1"),
		    sl1);
      StepAction sa1 = 
	new StepAction (new Nqual(),
			new String("act1"));
      LinkedList sal1 = new LinkedList();
      sal1.addLast(sa1);
      Step s1      = new Step("S1",sal1);   // Step s1

      //   ------- Step 2
      Assign stmt2 =                 
	new Assign(v_y, v_x);                   // y := x
      LinkedList sl2 = new LinkedList();
      sl2.addLast(stmt2);
      Action a2 = 
	new Action (new String("act2"),
		    sl2);
      StepAction sa2 =
	new StepAction (new Nqual(),
			new String("act2"));
      LinkedList sal2 = new LinkedList();
      sal2.addLast(sa2);
      Step s2      = new Step("S2",sal2);   // Step s2

      //   ------- Step 3
      Assign stmt3_1 =                 
	new Assign(v_x,                    // x := not x
		   new U_expr(Expr.NEG, v_x));
      Assign stmt3_2 =                 
	new Assign(v_y, v_x);             // y :=  x
      LinkedList sl3 = new LinkedList();
      sl3.addLast(stmt3_1);
      sl3.addLast(stmt3_2);
      Action a3 = 
	new Action (new String("act3"),
		    sl3);
      StepAction sa3 = 
	new StepAction (new Nqual(),
			new String("act3"));
      LinkedList sal3 = new LinkedList();
      sal3.addLast(sa3);
      Step s3      = new Step("S3",sal3);   // Step s3
      //   ------- Step 4
      Step s4      = new Step("S4");   // Step s4
      Step s5      = new Step("S5");   // Step s5
      Step s6      = new Step("S6");   // Step s6
      Step s7      = new Step("S7");   // Step s7
      Step s8      = new Step("S8");   // Step s8

      // s1 ---(true)--> {s2,s3}
      LinkedList src1 = new LinkedList();
      src1.addLast(s1);
      LinkedList target1 = new LinkedList();
      target1.addLast(s2);
      target1.addLast(s3);
      Transition t1 =
	new Transition (src1,
			new Constval (true), 
      			target1);

      // s2 ---(x and y)--> s4
      LinkedList src2 = new LinkedList();
      src2.addLast(s2);
      LinkedList target2 = new LinkedList();
      target2.addLast(s4);
      Transition t2 =
	  new Transition (src2,
			  new B_expr(v_x,Expr.AND,v_y),
			  target2);

      // s3 --(y)--> s5
      LinkedList src3 = new LinkedList();
      src3.addLast(s3);
      LinkedList target3 = new LinkedList();
      target3.addLast(s5);
      Transition t3 =
	new Transition (src3, v_y, target3);

      // s3 --(not y)--> s6
      LinkedList src4 = new LinkedList();
      src4.addLast(s3);
      LinkedList target4 = new LinkedList();
      target4.addLast(s6);
      Transition t4 =
	new Transition (src4,
			new U_expr(Expr.NEG,v_y),
			target4);

      // s5 ----> s7
      LinkedList src5 = new LinkedList();
      src5.addLast(s5);
      LinkedList target5 = new LinkedList();
      target5.addLast(s7);
      Transition t5 =
	new Transition (src5, target5);

      // s6 ----> s7
      LinkedList src6 = new LinkedList();
      src6.addLast(s6);
      LinkedList target6 = new LinkedList();
      target6.addLast(s7);
      Transition t6 =
	new Transition (src6, target6);

      // s4,s7 ----> s8
      LinkedList src7 = new LinkedList();
      src7.addLast(s4);
      src7.addLast(s7);
      LinkedList target7 = new LinkedList();
      target7.addLast(s8);
      Transition t7  =
	new Transition (src7, target7);

      // s8 ----> s1
      LinkedList src8 = new LinkedList();
      src8.addLast(s8);
      LinkedList target8 = new LinkedList();
      target8.addLast(s1);
      Transition t8  =
	new Transition (src8, target8);

      LinkedList steplist = new LinkedList();
      steplist.addLast(s1);
      steplist.addLast(s2);
      steplist.addLast(s3);
      steplist.addLast(s4);
      steplist.addLast(s5);
      steplist.addLast(s6);
      steplist.addLast(s7);
      steplist.addLast(s8);

      LinkedList transitionlist = new LinkedList();
      transitionlist.addLast(t1);
      transitionlist.addLast(t2);
      transitionlist.addLast(t3);
      transitionlist.addLast(t4);
      transitionlist.addLast(t5);
      transitionlist.addLast(t6);
      transitionlist.addLast(t7);
      transitionlist.addLast(t8);

      LinkedList actionlist = new LinkedList();
      actionlist.addLast(a1);
      actionlist.addLast(a2);
      actionlist.addLast(a3);

      LinkedList declarationlist = new LinkedList();
      declarationlist.addLast(dec_x);
      declarationlist.addLast(dec_y);
      declarationlist.addLast(dec_z);

      SFC sfc1 = 
	new SFC (s1,  //initial step
		 steplist,
		 transitionlist,
		 actionlist,
                 declarationlist);  
      
      return sfc1;
    };
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Example.java,v 1.12 2001-05-23 14:56:33 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.11  2001/05/10 06:59:52  swprakt
//	added a declaration list to the SFC's
//	
//	Revision 1.10  2001/05/08 08:22:11  swprakt
//	Extended the example, such that there is a non-trivial list
//	of statements associated with an action.
//	
//	Added the names of the teams to the packages
//	
//	[Steffen]
//	
//	Revision 1.9  2001/05/04 15:10:19  swprakt
//	changed actions in steps
//	
//	Revision 1.8  2001/05/04 09:14:13  swprakt
//	Example compiles, action list of the
//	SFC is empty still.
//	
//	[Steffen]
//	
//	Revision 1.7  2001/05/03 13:46:37  swprakt
//	A little extension, semi finished
//	
//	Revision 1.6  2001/05/02 07:37:15  swprakt
//	*** empty log message ***
//	
//	Revision 1.5  2001/05/02 07:37:02  swprakt
//	Example still empty
//	
//	Revision 1.4  2001/05/02 07:24:51  swprakt
//	Example compiles (but so far is empty)
//	
//	
//---------------------------------------------------------------------



