package absynt;


/**
 * 
 * The class offers an example for a program int abstract syntax.
 * @author Initially provided by Martin Steffen.
 * @version $Id: Example.java,v 1.8 2001-05-04 09:14:13 swprakt Exp $	
 */


public class Example{
  /**
   * The following method gives back an example program, containing 
   * most constructs of the abstract syntax. The example is not
   * really meaningful.
   */
    public static SFC getExample1(){
  /*
   * For enhanced readability, the example is built step-by-step.
   */
      Variable v_x = new Variable ("x");   
      Variable v_y = new Variable ("y");   
      Constval c5  = new Constval (5);
      Skip     stmt_skip = new Skip();
      Assign stmt1 =                 
	new Assign(v_x,                    // x := not x
		   new U_expr(Expr.NEG, v_x));
      StmtList sl1 = new StmtList(stmt1, null);
      Action a1 = 
	new Action (new Nqual(),
		    sl1);
      ActionList al1 = 
	new ActionList (a1, null);
      Step s1      = new Step("S1",al1);   // Step s1
      Assign stmt2 =                 
	new Assign(v_y, v_x);                   // y := x
      StmtList sl2 = new StmtList(stmt2, null);
      Action a2 = 
	new Action (new Nqual(),
		    sl2);
      ActionList al2 = 
	new ActionList (a2, null);
      Step s2      = new Step("S2",al2);   // Step s2
      //   ------- Step 3
      Assign stmt3 =                 
	new Assign(v_x,                    // x := not x
		   new U_expr(Expr.NEG, v_x));
      StmtList sl3 = new StmtList(stmt3, null);
      Action a3 = 
	new Action (new Nqual(),
		    sl3);
      ActionList al3 = 
	new ActionList (a3, null);
      Step s3      = new Step("S3",al3);   // Step s3
      //   ------- Step 4
      Step s4      = new Step("S4",null);   // Step s4
      Step s5      = new Step("S5",null);   // Step s5
      Step s6      = new Step("S6",null);   // Step s6
      Step s7      = new Step("S7",null);   // Step s7
      Step s8      = new Step("S8",null);   // Step s8

      Transition t1 =  // s1 ->true   -> {s2,s3}
	new Transition (new StepList(s1,null),
			new Constval (true), 
      			new StepList (s2,
      				      new StepList (s3, null)));
      Transition t2 =  // s2 ---(x and y)--> s4
	new Transition (new StepList(s2,null),
			new B_expr(v_x,Expr.AND,v_y),
			new StepList(s4,null));
      Transition t3 = // s3 --(y)--> s5
	new Transition (new StepList(s3,null),
			v_y,
			new StepList(s5,null));
      Transition t4 = // s3 --(not y)--> s6
	new Transition (new StepList(s3,null),
			new U_expr(Expr.NEG,v_y),
			new StepList(s6,null));
      Transition t5 = // s5 ----> s7
	new Transition (new StepList(s5,null),
			new StepList(s7,null));
      Transition t6 = // s6 ----> s7
	new Transition (new StepList(s6,null),
			new StepList(s7,null));

      Transition t7  = // s4,s7 ----> s8
	new Transition (new StepList(s4, new StepList(s7,null)),
			new StepList(s8,null));
      Transition t8  = // s8 ----> s1
	new Transition (new StepList(s8, null),
			new StepList(s1, null));
      SFC sfc1 = 
	new SFC (s1,  //initial step
		 new StepList(s1,
		 new StepList(s2,
		 new StepList(s3,
		 new StepList(s4,
		 new StepList(s5,
		 new StepList(s6,
		 new StepList(s7,
			      new StepList(s8,null)))))))),
		 new TransitionList(t1,
		 new TransitionList(t2,
		 new TransitionList(t3,
		 new TransitionList(t4,
		 new TransitionList(t5,
		 new TransitionList(t6,
		 new TransitionList(t7,
				    new TransitionList(t8,null)))))))),
		 null);
                 

      //new Constval(true)));
			
  
      
  return sfc1;
    };
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Example.java,v 1.8 2001-05-04 09:14:13 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
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



