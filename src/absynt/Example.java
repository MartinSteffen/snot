package absynt;


/**
 * 
 * Die Klasse soll ein Beispiel fuer ein Programm
 * int abstrakter Syntax bereitstellen.
 * @author Initially provided by Martin Steffen.
 * @version $Id: Example.java,v 1.1 2001-04-20 17:29:11 swprakt Exp $	
 */

public class Example{
  /**
   * Methode, die ein Beispielprogramm konstruiert und liefert.  Das
   * Beispiel enthaelt so gut wie alle in der Absrakten Syntax vorkommenden
   * Konstrukte. Das Beispiel ist semantisch nicht sinnvoll. Auch
   * ist _nicht_ sichergestellt, dass das Beispiel gemaess der
   * Tests des Paketes ``check'' korrekt ist.
   */
  public static Program getExample1(){
  /*
   * Zur besseren Ubersichtlichkeit
   * wird das Beispiel  Schritt fuer Schritt aufgebaut, mit Variablen
   * zum Zwischenspeichern, nicht als einziger grosser Term.  
   * Werden spaeter Terme automatisch erzeugt (z.B. durch den 
   * Parser, wird die ``Termdarstellung'' evtl. vorzuziehen sein.
   *
   */
    Channel c1  = new Channel ("c1");
    Channel c2  = new Channel ("c2");
    Channel c3  = new Channel ("c3");

    Chandec cd1 = new Chandec(c1, new M_Chan(new M_Bool()));
    Chandec cd2 = new Chandec(c2, new M_Chan(new M_Int()));
    Chandec cd3 = new Chandec(c3, new M_Chan(new M_Bool()));

    ChandecList cl =   // drei Kommunikationskanaele
      new ChandecList 
	(cd1, new ChandecList 
	  (cd2, new ChandecList 
	    (cd3, null)));

    //---------------- Process 1 --------------------

    Expr empty_ass = (Expr)(new Constval(true));   // leere  Assertion = true
    Expr empty_guard = (Expr)(new Constval(true)); // leerer Guard = true
    Expr ass_1 =                           // 
      new B_expr(new Variable ("x"),       // x = 5
		 Expr.EQ,
		 new Constval(true));

    Expr ass_2 =                           // y > x + 5
      new B_expr(new Variable ("y"),
		 Expr.GREATER,
		 new B_expr(
			    new Variable ("x"),
			    Expr.PLUS,
			    new Constval(5)));

    // Variablen des Prozesses
    Variable x1  = new Variable ("x1");
    x1.type      = new M_Bool();
    Variable x2  = new Variable ("x2");
    x2.type      = new M_Int();    
    Variable x3  = new Variable ("x3");
    Variable y  = new Variable ("y3");

 
    Constval e_true   = new Constval (true);;
    Constval e_false  = new Constval (false);;
    Constval e_1   = new Constval (1);;
    

    VardecList vl_1 = 
      new VardecList
	(new Vardec(x1,e_true,new M_Bool()),
	 new VardecList 
	   (new Vardec (x2,e_1,new M_Bool()),  // Typfehler
	    new VardecList
	      (new Vardec(x3,e_1,new M_Int()),
	       new VardecList
		 (new Vardec (y,e_false,new M_Bool()), null))));
    
    // 4 Zustaende, s_4 ist nicht erreicht
    Initstate s1 = new Initstate("s_1", ass_1);
    Astate s2 = new State    ("s_2", empty_ass);
    Astate s3 = new State    ("s_3", empty_ass);
    Astate s4 = new State    ("s_4", ass_2);
    AstateList sl = new AstateList
      (s1,
       new AstateList 
	 (s2,
	  new AstateList
	    (s3,
	     new AstateList
	       (s4,
		null))));




    // 4 Transitionen
    Transition t1 =   // s1  -> s1
      new Transition(s1, 
		     s1,
		     new Label(empty_guard,
			       new Tau_action()));

    Transition t2 =   //   s1  -- x:= 5 --> s2
      new Transition
	(s1,
	 s2,
	 new Label
	   (empty_guard,
	    new Assign_action
	      (new Variable ("x"),
	       new Constval (5))));


    Transition t3 =   // s2  --> c1?x1  --> s3
      new Transition 
	(s2,
	 s3,
	 new Label
	   (empty_guard,
	    new Input_action (c1,x1))
	   );

    Transition t4 = // s4  -> c2!(x - 5)--> s4
      new Transition
	(s3,
	 s4,
	 new Label
	   (empty_guard,
	    new Output_action 
	      (c2, 
	       new B_expr
		 (new Variable ("x"),
		  Expr.MINUS,
		  new Constval(5)))));		 

    TransitionList tl =   // Body
      new TransitionList 
	(t1,
	 new TransitionList
	   (t2,
	    new TransitionList
	      (t3,
	       new TransitionList 
		 (t4,
		  null))));
    
    Process p1 =  new Process("P1",vl_1, tl,sl, s1);
    ProcessList procl = new ProcessList (p1, null);

    Program p = new Program("Prog p",cl, procl);
    return p;
  };
};





//----------------------------------------------------------------------
//	Abstract Syntax for Mist Programs
//	------------------------------------
//
//	$Id: Example.java,v 1.1 2001-04-20 17:29:11 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.7  2000/07/14 10:00:36  unix01
//	ok
//	
//	Revision 1.6  2000/07/12 05:23:49  unix01
//	Das Beispiel an die Aenderung mit den Typen angepasst [Steffen]
//	
//	Revision 1.5  2000/07/10 14:41:36  unix01
//	ok
//	
//	Revision 1.4  2000/07/03 16:25:05  unix01
//	Program/Process;  Name als String hinzugefuegt
//	Entsprechend auch das Beispiel angepa"st.
//	
//	[Steffen]
//	
//	Revision 1.3  2000/06/26 16:49:38  unix01
//	Einen expliziten ``Initstate'' fuer jeden Prozess hinzugef"ugt (wie
//	vor einer Woche besprochen und im Pflichtenheft festgehalten). [Steffen]
//	Das Beispiel nachgezogen.
//	
//	Revision 1.2  2000/06/19 17:06:44  unix01
//	Die abstrakte Syntax angepa"st (gem"a"s den heutigen Entscheidungen)
//	
//	
//		1) Positionen f"ur Zust"ande eingef"uhrt
//		2) Zustandsliste + Initialer Zustand in Prozessen extra gespeichert.
//	
//	Revision 1.1  2000/06/04 10:27:35  unix01
//	Ein Beispiel mit einem Prozess (fuer den Anfang) [Steffen]
//	
//	
//---------------------------------------------------------------------



