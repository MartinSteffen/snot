package simulator;

import java.io.PrintStream;
import java.lang.*;
import java.util.Iterator;
import java.util.LinkedList;
import absynt.*;
import editor.Editor;
import utils.*;



/**
 * Diese Klasse ermöglicht es, SFCs schrittweise auszuführen
 * 
 *@author Jörn Fiebelkorn
 *
 */


public class Simulator{


    private SFC    sfc;
    private Editor editor;
    private Configuration  config;

	private static final boolean DEBUG = false;


    /**
     *
     * Erzeugt einen neuen Simulator.
     * Das zu simulierende SFC wird vom Editor geliefert.
     * Der Editor wird verwendet, um den Zustand des SFCs darzustellen.
     *
     * @param Editor
     *
     */
    public Simulator(Editor _editor) {
		sfc    = _editor.getSFC();
		editor = _editor;
    }


    /**
     *
     * Erzeugt einen neuen Simulator.
     * Das zu simulierende SFC wird direkt übergeben.
     *
     * @param SFC 
     *
     */
    public Simulator(SFC _sfc) {
		sfc    = _sfc;
		editor = null;
    }
    
    /**
     *
     * Bringt den Simulator in den Anfangszustand
     *
     */
    public void Initialize() {
		config = new Configuration(sfc.istep,sfc.declist);
    }
    

    /**
     *
     * Ausgabe des aktuellen Zustands in Textform
     *
     * @param out PrintStream
     *
     */
    public void PrintConfiguration(PrintStream out) {
    	config.Print(out);
    }
    
    
    /**
     *
     * Simuliert einen SFC-Zyklus 
     *
     * @throws SimException 
     * 
     */
    public void SingleStep() throws SimException {
    	
		PrettyPrint pp = new PrettyPrint();
		
		if (DEBUG)
			System.out.println("enter SingleStep()");
			

		// Wir arbeiten mit einer Kopie des aktuellen Zustands

    	Configuration c = new Configuration(config);

    	// Inputs lesen
    	
    	// Reihenfolge festlegen, in der die aktiven Steps ausgeführt werden  (bestimmt Interleaving)
    	
    	for (Iterator i = sfc.steps.iterator(); i.hasNext(); ) {
    		Step s = (Step)i.next();
    		if (c.active_steps.remove(s)) 
    			c.active_steps.addLast(s);
    	}
    	
    	if (DEBUG) {
    		System.out.println("active Steps:");
    		for (Iterator i = c.active_steps.iterator(); i.hasNext(); ) {
    			Step s = (Step) i.next();
    			pp.print(s);
    		}
    		System.out.println("");
    	}

    	// Aktionen der aktiven Steps ausführen                               (Auswertung der Aktionslisten ist atomar)
    	
		for (Iterator i = c.active_steps.iterator(); i.hasNext(); ) {

    			Step s = (Step) i.next();

				if (DEBUG) 
					System.out.println("ExecNQualifiedStepActions("+s.name+")");

    			ExecNQualifiedStepActions(s,c.state);
    	}
        	
    	// Guards auswerten und ggfs. Transitionen nehmen                     (Die Liste der Transitionen gibt die Reihenfolge vor.)
    	
    	StepList activated_steps = new StepList();
    	
		for (Iterator i = sfc.transs.iterator(); i.hasNext(); ) {
			
    		Transition t = (Transition) i.next();

			if (DEBUG) {
				System.out.println("ExecTransition:");
//				pp.print(t);
			}
    			
    		StepList l = ExecTransition(t,c);
    		
    		for (Iterator j = l.iterator(); j.hasNext(); ) {
    			Step s = (Step) j.next();
    			if ( !(activated_steps.contains(s)) )
    				activated_steps.add(s);
    		}
    	}
    	
    	for (Iterator i = activated_steps.iterator(); i.hasNext(); ) {
    		Step s = (Step) i.next();
    		if ( !c.active_steps.contains(s) )
    			c.active_steps.add(s);
    	}
    	
    	// Outputs schreiben
    	
    	// aktuellen Zustand durch den soeben bestimmten ersetzen
    	
    	config = c;

    }
    
    
    /**
     *
     * Wertet einen Ausdruck aus, indem Variablen durch ihre aktuellen Werte ersetzt werden.
     *
     * @param  Expr
     * @param  State  Variablenbelegung
     *
     * @return Expr   ein konstanter Audruck gemaess der abstrakten Syntax
     *
     * @throws SimException 
     * 
     */
    private Expr EvaluateExpression(Expr e, State state) throws SimException {
    	
    	if (e instanceof Constval) {
    		
    		Constval c = (Constval) e;
    	
			if (c.val instanceof Integer) 
				return new Constval( ((Integer)c.val).intValue() );
					
			if (c.val instanceof Boolean) {
				return new Constval( ((Boolean)c.val).booleanValue() );
			}
			
            throw new SimException("Konstante unbekannten Typs");
    	}
    	
    	if (e instanceof Variable) {
    		
    		Variable v = (Variable) e;
    		
	   		return state.GetVar(v);
       	}
    	
    	if (e instanceof U_expr) {
    		
    		U_expr u = (U_expr) e;
    		
    		// Nun rufen wir "EvaluateExpression" auf, um den Teilausdruck auszuwerten (REKURSION).
    		
    		Expr sub_e = EvaluateExpression(u.sub_expr,state);  
    		
    		if (sub_e instanceof Constval) {

    			Constval c = (Constval) sub_e;

    			if ( c.val instanceof Integer ) {
    				
    				int i = ((Integer)c.val).intValue();
    				
    				switch (u.op) {
    				case Expr.PLUS  : return ( new Constval( i) );
    				case Expr.MINUS : return ( new Constval(-i) );
    				}
                    throw new SimException("unerlaubter Operator vor Integer-Ausdruck");
    			}
    			if ( c.val instanceof Boolean ) {
    				
    				boolean b = ((Boolean)c.val).booleanValue();
    				
    				switch (u.op) {
    				case Expr.NEG : return ( new Constval(!b) );
    				}
                    throw new SimException("unerlaubter Operator vor Boolean-Ausdruck");
    			}
    		}
            throw new SimException("Teilausdruck konnte nicht ausgewertet werden");
    	}

    	if (e instanceof B_expr) {

    		B_expr b = (B_expr) e;
    		
    		// Nun rufen wir "EvaluateExpression" auf, um die zwei Teilausdrücke auszuwerten (REKURSION).
    		
    		Expr left_expr  = EvaluateExpression(b.left_expr, state); 
    		Expr right_expr = EvaluateExpression(b.right_expr,state); 

    		if (left_expr  instanceof Constval && right_expr instanceof Constval   ) {
    			
    			Constval left_c  = (Constval) left_expr;
    			Constval right_c = (Constval) right_expr;
    			
    			if ( left_c.val instanceof Integer && right_c.val instanceof Integer) {
    				
    				int left_i  = ((Integer)left_c.val ).intValue(); 
    				int right_i = ((Integer)right_c.val).intValue(); 
    				
    				switch (b.op) {
    				case Expr.PLUS    : return ( new Constval(left_i +  right_i) );
    				case Expr.MINUS   : return ( new Constval(left_i -  right_i) );
    				case Expr.TIMES   : return ( new Constval(left_i *  right_i) );
    				case Expr.DIV     : return ( new Constval(left_i /  right_i) );
    				case Expr.EQ      : return ( new Constval(left_i == right_i) );
    				case Expr.NEQ     : return ( new Constval(left_i != right_i) );
    				case Expr.LESS    : return ( new Constval(left_i <  right_i) );
    				case Expr.GREATER : return ( new Constval(left_i >  right_i) );
    				case Expr.LEQ     : return ( new Constval(left_i <= right_i) );
    				case Expr.GEQ     : return ( new Constval(left_i >= right_i) );
    				}
                    throw new SimException("unerlaubter Operator");
    			}
    			if ( left_c.val instanceof Boolean && right_c.val instanceof Boolean) {
    				
    				boolean left_b  = ((Boolean)left_c.val ).booleanValue();
    				boolean right_b = ((Boolean)right_c.val).booleanValue();
    				
    				switch (b.op) {
    				case Expr.AND : return ( new Constval(left_b && right_b) );
    				case Expr.OR  : return ( new Constval(left_b || right_b) );
    				case Expr.EQ  : return ( new Constval(left_b == right_b) );
    				case Expr.NEQ : return ( new Constval(left_b != right_b) );
    				}
                    throw new SimException("unerlaubter Operator");
    			}
    		}
            throw new SimException("unzulaessige Kombination von Ausdruecken");
    	}
        throw new SimException("Syntax-Fehler");
    }

    
    
    /**
     *
     * Fuehrte eine StepAction aus.
     *
     * @param  Action  Aktion
     * @param  State   Variablenbelegung
     *
     * @throws SimException 
     * 
     */
    private void ExecAction(Action action, State state) throws SimException {
    
		for (Iterator i = action.sap.iterator(); i.hasNext();) {
	  	
	    	Stmt s = (Stmt) i.next();
	    
	    	if (s instanceof Assign) {
	    
	    		Assign a = (Assign) s;
	    		
				if (DEBUG)
					System.out.println("Assign("+a.var.name+")");

	    		// Audruck in "val" auswerten 
	    	
	    		Expr eval = EvaluateExpression(a.val,state);
	    	
	    		if (eval instanceof Constval ) {

					// Wert von "eval" der Variablen "var" zuweisen
				
					Constval c = (Constval) eval;
				
					state.SetVar(a.var,c);
	    		}
	    		else

                    throw new SimException("Ausdruck konnte nicht ausgewertet werden");
	    	
			}
    	}
    }
    
    
    /**
     *
     * Fuehrte saemtliche N-Aktionen eines Steps aus.
     *
     * @param  Step 
     * @param  State   Variablenbelegung
     *
     * @throws SimException 
     * 
     */
    private void ExecNQualifiedStepActions(Step step, State state) throws SimException {
    	
    	for (Iterator i = step.actions.iterator(); i.hasNext(); ) {
    		
    		StepAction sa = (StepAction) i.next();
    		
    		if (sa.qualifier instanceof Nqual) {   // ließe sich Nqual durch einen formalen Parameter ersetzen?
    		
		    	for (Iterator j = sfc.actions.iterator(); j.hasNext(); ) {
    			
    				Action a = (Action) j.next();
    				
    				if (a.a_name.equals(sa.a_name))
    					ExecAction(a,state);
    			}
    		}
    	}
    }
    
    /**
     *
     * Bearbeitet eine Transition. 
     * Ist diese aktiviert, so werden die Quell-Steps deaktiviert und 
     * die zu aktivierenden Ziel-Steps als Liste zurueckgegeben.
     * Ist sie nicht aktiviert, so wird eine leere Liste zurückgegeben.
     *
     * @param  Transition 
     * @param  Configuration  aktueller Zustand 
     *
     * @return StepList       aktivierte Steps
     *
     * @throws SimException 
     * 
     */
    private StepList ExecTransition(Transition t, Configuration cfg) throws SimException {
    	
		StepList l = new StepList();

    	// Sind sämtliche Quell-Steps aktiviert?
    	
    	boolean activated = true;
    	
    	for (Iterator i = t.source.iterator(); i.hasNext(); ) {
    		Step s = (Step) i.next();
    		if ( !cfg.active_steps.contains(s) ) {
    			activated = false;
    			break;
    		}
    	}
    	
    	if (activated) {
    		
    		if (DEBUG)
    			System.out.println("activated");
    		
    		// Guard auswerten
    		
    		Expr e = EvaluateExpression(t.guard,cfg.state);
	    	
    		if (e instanceof Constval ) {
		
				if (DEBUG)
	 				System.out.println("Constval");

				Constval c = (Constval) e;
				
				if (c.val instanceof Boolean)
				
					// erfuellt?
				
					if ( ((Boolean) c.val).booleanValue() ) {
						
						if (DEBUG)
							System.out.println("enabled");
						
						// Quell-Steps deaktivieren

				    	for (Iterator i = t.source.iterator(); i.hasNext(); ) {
    						Step s = (Step) i.next();
   							cfg.active_steps.remove(s);
    					}
						
						// Liste der Ziel-Steps erzeugen und zurückgeben
						
				    	for (Iterator i = t.target.iterator(); i.hasNext(); ) {
    						Step s = (Step) i.next();
    						l.add(s);
    					}
					}
			}
    	}
    	
    	return l;
    }
    


    /**
     *
     * Zustand des Simulators
     * StepList: aktive Steps
     * State:    Variablenbelegung
     *
     */
    private class Configuration {
    	
    	private StepList  active_steps;  // Liste der aktivierten Steps ("Step")
		private State     state;         // Liste der Variablen und ihrer Werte ("Declaration")
  
  
     	public Configuration(Configuration c){

			// Im Falle der Step-Liste werden nur die Verweise auf die Step-Objekte kopiert  	
			
			active_steps = new StepList();

			for (Iterator i = c.active_steps.iterator(); i.hasNext(); ) 
				active_steps.addLast(i.next());
  	
			// Bei der Variablenbelegung genügt das nicht, hier müssen wir eine "echte" Kopie erzeugen.
			
			state = new State(c.state);
  	
		}

     	public Configuration(Step istep, LinkedList declist){
  	
			active_steps = new StepList();
			active_steps.addLast(istep);    // Der von "istep" bestimmte "Step" wird nicht kopiert.
  	
			state = new State(declist);     // Es werden neue "Map"-Objekte erzeugt. Die Werte werden den "Declarations" entnommen.
		}

		
		public void Print(PrintStream out) {

			out.print("[");			
			boolean first = true;
	    	for (Iterator i = active_steps.iterator(); i.hasNext(); ) {
    			Step s = (Step)i.next();
    			if (!first)
    				out.print(" ");
    			first = false;
    			out.print(s.name);
    		}
			out.print("] ");
			
	    	for (Iterator i = state.iterator(); i.hasNext(); ) {
    			Map m = (Map)i.next();
    			out.print(" " + m.var.name + "=");
    			if (m.var.type instanceof BoolType) {
    				if ( ((Boolean)m.val).booleanValue() )
    					out.print("true");
    				else
    					out.print("false");
    			}
    			if (m.var.type instanceof IntType) 
    				out.print( ((Integer)m.val).intValue() );
    		}
			out.println();
		}
		
    } // class Configuration
    

    
    /**
     *
     * Variablenbelegung
     * 
     */
    public class State extends LinkedList {
    	
    	public State(LinkedList l) {

			for (Iterator i = l.iterator(); i.hasNext();) {

				Declaration d = (Declaration) i.next();
				
				if ( (d.var.type instanceof BoolType) &&
				     (d.type     instanceof BoolType)    ) 
					addLast(new Map(d.var, ((Boolean)d.val.val).booleanValue() ));
				if ( (d.var.type instanceof IntType) &&
				     (d.type     instanceof IntType)    ) 
					addLast(new Map(d.var, ((Integer)d.val.val).intValue() ));
			}
    	}
    	
    	public State(State s) {

			for (Iterator i = s.iterator(); i.hasNext();) {

				Map m = (Map) i.next();
				
				if ( (m.var.type instanceof BoolType) ) 
					addLast( new Map(m.var,((Boolean)m.val).booleanValue()) );
				if ( (m.var.type instanceof IntType)  ) 
					addLast( new Map(m.var,((Integer)m.val).intValue()) );
			}
    	}
    	
    	public void SetVar(Variable var, Constval c) {

			if (DEBUG) System.out.println("SetVar("+var.name+")");

			Map m_old = null, m_new = null;

			for (Iterator j = iterator(); j.hasNext();) {
					
				Map m = (Map) j.next();
					
				if ( var.name.equals(m.var.name) ) {
					
					m_old = m;
						
				    if (c.val instanceof Integer && var.type instanceof IntType  && m_old.var.type instanceof IntType) {
			     		m_new = new Map(var,((Integer)c.val).intValue());
				    	break;
				    }
				     		
				    if (c.val instanceof Boolean && var.type instanceof BoolType  && m_old.var.type instanceof BoolType) {
				    	m_new = new Map(var,((Boolean)c.val).booleanValue());
				    	break;
				    }
				     		
				}
			}
			
			if (m_old != null && m_new != null) {
				if (remove(m_old)) 
					addLast(m_new);
				else
					; // throw Execption
			}
			else
				; // throw Execption
    	}


    	public Constval GetVar(Variable var) {
    		
			if (DEBUG) System.out.println("GetVar("+var.name+")");

    		// Durchsuche die Liste nach einer gleichnamigen Variablen gleichen Typs
   		
    		for (Iterator i = iterator(); i.hasNext();) {

				Map m = (Map) i.next();

				if ( var.name.equals(m.var.name) ) {
					
					if (var.type instanceof IntType  && m.var.type instanceof IntType) 
						return ( new Constval(((Integer)m.val).intValue()) );
						
					if (var.type instanceof BoolType && m.var.type instanceof BoolType)  
				  		return ( new Constval(((Boolean)m.val).booleanValue()) );
				  		
				  	// throw Exception
				}
				
			}
    		// throw Exception
    		return (new Constval(false));  // wird nie erreicht, soll nur den Compiler beruhigen
    	}
    	
    }

    
    private class StepList extends LinkedList {}

    
    private class Map {
    	
    	Variable var;
    	Object   val;
    	
    	public Map(Variable _var, boolean b) {
    		var = _var;
    		val = new Boolean(b);
    	}
    	public Map(Variable _var, int i) {
    		var = _var;
    		val = new Integer(i);
    	}
    }

    

    

} // class Simulator


