/*
 * SMVTranslator.java
 *
 * Created on 14. Mai 2001, 22:08
 */

package smv;

import absynt.*;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.LinkedList;


/**
 *
 * @author  Kevin Koeser / Tobias Kloss
 * @version 0.3
 */
public class SMVTranslator extends java.lang.Object {
    private SFC sfc;
    private StringBuffer Output;
    private Hashtable hashedActions;
    private LinkedList ActionVariableExtensions;
    
    /**
     *
     * Creates new SMVTranslator 
     * @param sfc SFC-object to work on
     *
     */
    public SMVTranslator(SFC sfc) {
        this.sfc = sfc;
    }
    
    /**
     *
     * Prints SMV commands in Stream
     * @return Bytestream with SMV commands
     * @throws SMVException occurs if SFC is not welldefined or not boolean
 */
    public ByteArrayOutputStream toStream() throws SMVException {
        if (this.Output == null) translate();

        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        ba.write((new String(this.Output)).getBytes(), 0, this.Output.length());
        return ba;
    }

    /**
     *
     * Translates the SFC to SMV
     * @throws SMVException
     * @throws SMVException occurs if SFC is not welldefined or not boolean
 */
    private void translate() throws SMVException{
      this.Output = new StringBuffer();
      this.hashedActions = new Hashtable();
      this.ActionVariableExtensions = new LinkedList();
      
      hashActions();
      fillActionVariableExtensionsList();
      
      this.Output.append("#define true  1\n");
      this.Output.append("#define false 0\n");
      defineTransitions();
      this.Output.append("\n");
      this.Output.append("module main() {\n");
      this.Output.append("\t/* Declaration */\n");
      declareSFCVariables();
      declareSteps();
      declareStepActions();
      declareTransitions();
      this.Output.append("\t/* System variables */\n");
      this.Output.append("\tstate : {actions, transitions};\n");
      declareActiveAction();
      declareStepActionStates();
      declareActiveTransition();
      this.Output.append("\n\n");
      this.Output.append("\t/* Initialization */\n");
      initSFCVariables();
      initSteps();
      initStepActions();
      initTransitions();
      this.Output.append("\t/* System varibales */\n");
      this.Output.append("\tinit(state) := actions;\n");
      initActiveAction();
      initStepActionStates();
      this.Output.append("\tinit(activeTransition) := none;\n");
      this.Output.append("\n\n");
      this.Output.append("\t/* Stepwise Assignment*/\n");
      this.Output.append("\tdefault {\n");
      setDefaultSFCVariables();
      setDefaultSteps();
      setDefaultStepActions();
      setDefaultTransitions();
      this.Output.append("\t\tnext(state) := state;\n");
      this.Output.append("\t\tnext(activeAction) := activeAction;\n");
      setDefaultStepActionStates();
      this.Output.append("\t}\n");
      this.Output.append("\tin {\n");
      this.Output.append("\t\tswitch(state) {\n");
      this.Output.append("\t\t\tactions: {\n");
      this.Output.append("\t\t\t\tswitch(activeAction) {\n");
      makeActionAssignments();
      this.Output.append("\t\t\t\t}\n");
      this.Output.append("\t\t\ttransitions: {\n");
      this.Output.append("\t\t\t\tswitch(activeTransition) {\n");
      makeTransitionAssignments();
      this.Output.append("\t\t\t\t}\n");
      this.Output.append("\t\t\t}\n");
      this.Output.append("\t\t}\n");
      this.Output.append("\t}\n");
      this.Output.append("}\n");
    }
    
    /**
     *
     * defines the Transitions
     * @throws SMVException occurs if SFC is not welldefined or not boolean
 */
    private void defineTransitions() throws SMVException {
        int i,j;
        Transition t;
        
        for(i=0; i<sfc.transs.size(); i++) {
            t = (Transition) sfc.transs.get(i);
            this.Output.append("#define TRANSITION_");
            this.Output.append(i);
            this.Output.append("\t((");
            this.Output.append("Step_");
            this.Output.append(((Step)t.source.getFirst()).name);
            for(j=1; j<t.source.size(); j++) {
                this.Output.append(" & Step_");
                this.Output.append(((Step)t.source.get(j)).name);
            }
            this.Output.append(") & ");
            this.Output.append(ExprToString(t.guard));
            this.Output.append(")\n");
        }
        
        this.Output.append("\n");

        for(i=0; i<sfc.transs.size(); i++) {
            this.Output.append("#define NEXT_TRANSITION_");
            this.Output.append(i);
            this.Output.append("\t Transition_");
            this.Output.append(i);
            this.Output.append(" & TRANSITION_");
            this.Output.append(i);
            this.Output.append("\n");
        }
        
        this.Output.append("\n");
    }
    
    /**
     *
     * declares the SFC variables
     * @throws SMVException occurs if SFC is not welldefined or not boolean
 */
    private void declareSFCVariables() throws SMVException{
        int i;
        Declaration d;
        
        this.Output.append("\t/* SFC variables */\n");
        for(i=0; i<sfc.declist.size(); i++) {
            d = (Declaration) (sfc.declist.get(i));
            this.Output.append("\tVar_");
            this.Output.append(d.var.name);
            this.Output.append(" : ");
            if (d.type instanceof BoolType)
                this.Output.append("boolean;\n");
            else
                throw new SMVException("SFC is not boolean.");
        }
        this.Output.append("\n");
    }
    
    /**
     *
     * declares the SFC Steps
     *
     */
    private void declareSteps() {
        int i;
        Step s;
        
        this.Output.append("\t/* Steps */\n");
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            this.Output.append("\tStep_");
            this.Output.append(s.name);
            this.Output.append(" : boolean;\n");
        }
        this.Output.append("\n");
    }
    
    /**
     *
     * declares the SFC Actions
     *
     */
    private void declareStepActions() {
        int i,j;
        StepAction a;
        Step s;
        
        this.Output.append("\t/* Actions */\n");
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            for(j=0; j<s.actions.size(); j++) {
                a = (StepAction) s.actions.get(j);
                this.Output.append("\tStepAction_");
                this.Output.append(s.name);
                this.Output.append("_");
                this.Output.append(a.a_name);
                this.Output.append(" : boolean;\n");
            }
        }
        this.Output.append("\n");
    }
    
    /**
     *
     * declares the Transitions
     *
     */
    private void declareTransitions() {
        int i;
        
        this.Output.append("\t/* Transitions */\n");
        for(i=0; i<sfc.transs.size(); i++) {
            this.Output.append("\tTransition_");
            this.Output.append(i);
            this.Output.append(" : boolean;\n");
        }
        
        this.Output.append("\n");
    }
    
    /**
     *
     * declares the system variable activeAction
     *
     */
    private void declareActiveAction () {
        int i,j;
        StepAction a;
        Step s;
        
        this.Output.append("\tactiveAction : {");
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            for(j=0; j<s.actions.size(); j++) {
                a = (StepAction) s.actions.get(j);
                this.Output.append("Act_");
                this.Output.append(s.name);
                this.Output.append("_");
                this.Output.append(a.a_name);
                this.Output.append(", ");
            }
        }
        this.Output.append("none};\n");
    }
    
    /**
     *
     * declares a system variable for each ActionState
     *
     */
    private void declareStepActionStates() {
        int i, j, k;
        StepAction sa;
        Step s;
        Action a;
        
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            for(j=0; j<s.actions.size(); j++) {
                sa = (StepAction) s.actions.get(j);
                this.Output.append("\tActionState_");
                this.Output.append(s.name);
                this.Output.append("_");
                this.Output.append(sa.a_name);
                this.Output.append(" : {sub0");
                a = getActionByName(sa.a_name);
                for(k=1; k<a.sap.size(); k++) {
                    this.Output.append(", sub");
                    this.Output.append(k);
                }
                this.Output.append("};\n");
            }
        }
    }
    
    /**
     *
     * declares the system variable activeTransition
     *
     */
    private void declareActiveTransition() {
        int i;
        
        this.Output.append("\tactiveTransition : {");
        for(i=0; i<sfc.transs.size(); i++) {
            this.Output.append("Trans_");
            this.Output.append(i);
            this.Output.append(", ");
        }
        this.Output.append("none};\n");
    }
    
    /**
     *
     * initialzies the SFC variables
     *
     */
    private void initSFCVariables() {
        int i;
        Declaration d;
        
        this.Output.append("\t/* SFC variables */\n");
        for(i=0; i<sfc.declist.size(); i++) {
            d = (Declaration) sfc.declist.get(i);
            this.Output.append("\tinit(Var_"+d.var.name+") := "
                +d.val.val.toString()+";\n");
        }
        this.Output.append("\n");
    }
    
    /**
     *
     * initialzies the SFC Steps
     *
     */
    private void initSteps() {
        int i;
        Step s;
        
        this.Output.append("\t/* Steps */\n");
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            this.Output.append("\tinit(Step_");
            this.Output.append(s.name);
            this.Output.append(") := ");
            if (s == sfc.istep)
                this.Output.append("1;\n");
            else
                this.Output.append("0;\n");
        }
        this.Output.append("\n");
    }
    
    /**
     *
     * initialzies the SFC Actions
     *
     */
    private void initStepActions() {
        int i,j;
        StepAction a;
        Step s;
        
        this.Output.append("\t/* Actions */\n");
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            for(j=0; j<s.actions.size(); j++) {
                a = (StepAction) s.actions.get(j);
                this.Output.append("\tinit(StepAction_");
                this.Output.append(s.name);
                this.Output.append("_");
                this.Output.append(a.a_name);
                this.Output.append(") := ");
                if(s == sfc.istep) 
                    this.Output.append("1;\n");
                else
                    this.Output.append("0;\n");
            }
        }
        this.Output.append("\n");
    }
    
    /**
     *
     * initialzies the Transitions
     *
     */
    private void initTransitions() {
        int i;
        
        this.Output.append("\t/* Transitions */\n");
        for(i=0; i<sfc.transs.size(); i++) {
            this.Output.append("\tinit(Transition_");
            this.Output.append(i);
            this.Output.append(") := 1;\n");
        }
        
        this.Output.append("\n");
    }
    
    /**
     *
     * initialzies the system variable activeAction
     *
     */
    private void initActiveAction () {
        int i;
        StepAction sa;
        
        this.Output.append("\tinit(activeAction) := ");
        if(sfc.istep.actions.size()==0)
            this.Output.append("none;\n");
        else {
            sa = (StepAction) sfc.istep.actions.get(0);
            this.Output.append("{Act_");
            this.Output.append(sfc.istep.name);
            this.Output.append("_");
            this.Output.append(sa.a_name);
            for(i=1; i<sfc.istep.actions.size(); i++) {
                sa = (StepAction) sfc.istep.actions.get(i);
                this.Output.append(", Act_");
                this.Output.append(sfc.istep.name);
                this.Output.append("_");
                this.Output.append(sa.a_name);
            }
            this.Output.append("};\n");
        }
    }
    
    /**
     *
     * initializes the StepActionStates
     *
     */
    private void initStepActionStates() {
        int i, j;
        StepAction sa;
        Step s;
        
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            for(j=0; j<s.actions.size(); j++) {
                sa = (StepAction) s.actions.get(j);
                this.Output.append("\tinit(ActionState_");
                this.Output.append(s.name);
                this.Output.append("_");
                this.Output.append(sa.a_name);
                this.Output.append(") := sub0;\n");
            }
        }
    }
    
    /**
     *
     * sets the default Value for the SFC variables
     *
     */
    private void setDefaultSFCVariables() {
        int i;
        Declaration d;
        
        this.Output.append("\t\t/* SFC varibales */\n");
        for(i=0; i< sfc.declist.size(); i++) {
            d = (Declaration) sfc.declist.get(i);
            this.Output.append("\t\tnext(Var_");
            this.Output.append(d.var.name);
            this.Output.append(") := Var_");
            this.Output.append(d.var.name);
            this.Output.append(";\n");
        }
        
        this.Output.append("\n");
    }
    
    /**
     *
     * sets the default Value for the Steps
     *
     */
    private void setDefaultSteps() {
        int i;
        Step s;
        
        this.Output.append("\t\t/* Steps */\n");
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            this.Output.append("\t\tnext(Step_");
            this.Output.append(s.name);
            this.Output.append(") := Step_");
            this.Output.append(s.name);
            this.Output.append(";\n");
        }
        
        this.Output.append("\n");
    }
    
    /**
     *
     * sets the default Value for the StepActions
     *
     */
    private void setDefaultStepActions() {
        int i,j;
        StepAction a;
        Step s;
        
        this.Output.append("\t\t/* Actions */\n");
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            for(j=0; j<s.actions.size(); j++) {
                a = (StepAction) s.actions.get(j);
                this.Output.append("\t\tnext(StepAction_");
                this.Output.append(s.name);
                this.Output.append("_");
                this.Output.append(a.a_name);
                this.Output.append(") := StepAction_");
                this.Output.append(s.name);
                this.Output.append("_");
                this.Output.append(a.a_name);
                this.Output.append(";\n");
            }
        }
        this.Output.append("\n");
    }
    
    /**
     *
     * sets the default Value for the Transitions
     *
     */
    private void setDefaultTransitions() {
        int i;
        
        this.Output.append("\t\t/* Transitions */\n");
        for(i=0; i<sfc.transs.size(); i++) {
            this.Output.append("\t\tnext(Transition_");
            this.Output.append(i);
            this.Output.append(") := NEXT_TRANSITION_");
            this.Output.append(i);
            this.Output.append(";\n");
        }
        
        this.Output.append("\n");
    }
    
    /**
     *
     * sets the default Value for the StepActionStates
     *
     */
    private void setDefaultStepActionStates() {
        int i, j;
        StepAction sa;
        Step s;
        
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            for(j=0; j<s.actions.size(); j++) {
                sa = (StepAction) s.actions.get(j);
                this.Output.append("\t\tnext(ActionState_");
                this.Output.append(s.name);
                this.Output.append("_");
                this.Output.append(sa.a_name);
                this.Output.append(") := sub0;\n");
            }
        }
    }
    
    /**
     *
     * makes the assignments for the transitions state
     *
     */
    private void makeTransitionAssignments() {
        int i,j,k;
        Transition t;
        Step s;
        StepAction sa;
        int EntryCount;
        
        for(i=0; i<sfc.transs.size(); i++) {
            t = (Transition) sfc.transs.get(i);
            this.Output.append("\t\t\t\t\tTrans_");
            this.Output.append(i);
            this.Output.append(": {\n");
            // deactivate Source Steps
            // decativate Source StepActions
            this.Output.append("\t\t\t\t\t\t/* deactivate Source Steps and StepActions */\n");
            for(j=0; j<t.source.size(); j++) {
                s = (Step) t.source.get(j);
                this.Output.append("\t\t\t\t\t\tnext(Step_");
                this.Output.append(s.name);
                this.Output.append(") := 0;\n");
                for(k=0; k<s.actions.size(); k++) {
                    sa = (StepAction) s.actions.get(k);
                    this.Output.append("\t\t\t\t\t\tnext(StepAction_");
                    this.Output.append(s.name);
                    this.Output.append("_");
                    this.Output.append(sa.a_name);
                    this.Output.append(") := 0;\n");
                }
            }
            // activate Target Steps
            // activate Target StepActions
            this.Output.append("\t\t\t\t\t\t/* activate Target Steps and StepActions */\n");
            for(j=0; j<t.target.size(); j++) {
                s = (Step) t.target.get(j);
                this.Output.append("\t\t\t\t\t\tnext(Step_");
                this.Output.append(s.name);
                this.Output.append(") := 1;\n");
                for(k=0; k<s.actions.size(); k++) {
                    sa = (StepAction) s.actions.get(k);
                    this.Output.append("\t\t\t\t\t\tnext(StepAction_");
                    this.Output.append(s.name);
                    this.Output.append("_");
                    this.Output.append(sa.a_name);
                    this.Output.append(") := 1;\n");
                }
            }
            // deactivate this Transition
            this.Output.append("\n\t\t\t\t\t\tnext(Transition_");
            this.Output.append(i);
            this.Output.append(") := 0;\n");
            // activate next Transition
            this.Output.append("\t\t\t\t\t\tnext(activeTransition) := ");
            if( sfc.transs.size()==1 )
                this.Output.append("none;\n");
            else {
                this.Output.append("(");
                EntryCount = 0;
                for(j=0; j<sfc.transs.size(); j++) {
                    if( i!=j ) {
                        if( EntryCount>0 )
                            this.Output.append(" | ");
                        this.Output.append("NEXT_TRANSITION_");
                        this.Output.append(j);
                        EntryCount++;
                    }
                }
                this.Output.append(") ? {");
                EntryCount = 0;
                for(j=0; j<sfc.transs.size(); j++) {
                    if( i!=j ) {
                        if( EntryCount>0 )
                            this.Output.append(", ");
                        this.Output.append("NEXT_TRANSITION_");
                        this.Output.append(j);
                        this.Output.append(" ? Trans_");
                        this.Output.append(j);
                        EntryCount++;
                    }
                }
                this.Output.append("} : none;\n");
            }
            
            this.Output.append("\t\t\t\t\t}\n");
        }
        
        this.Output.append("\t\t\t\t\tnone: {\n");
        for(i=0; i<sfc.transs.size(); i++) {
            this.Output.append("\t\t\t\t\t\tnext(Transition_");
            this.Output.append(i);
            this.Output.append(") := 1;\n");
        }
        this.Output.append("\t\t\t\t\t\tnext(state) := actions;\n");
        this.Output.append("\t\t\t\t\t\tnext(activeAction) := ");
        if( this.ActionVariableExtensions.size()==0 ) 
            this.Output.append("none;\n");
        else {
            this.Output.append("(");
            EntryCount = 0;
            for(i=0; i<this.ActionVariableExtensions.size(); i++) {
                if( EntryCount>0 )
                    this.Output.append(" | ");
                this.Output.append("StepAction_");
                this.Output.append((String) this.ActionVariableExtensions.get(i));
                EntryCount++;
            }
            this.Output.append(") ? {");
            EntryCount = 0;
            for(i=0; i<this.ActionVariableExtensions.size(); i++) {
            if( EntryCount>0 )
                this.Output.append(", ");
            this.Output.append("StepAction_");
            this.Output.append((String) this.ActionVariableExtensions.get(i));
            this.Output.append(" ? Act_");
            this.Output.append((String) this.ActionVariableExtensions.get(i));
            EntryCount++;
        }
        this.Output.append("} : none;\n");
    }
    this.Output.append("\t\t\t\t\t}\n");
}
    
    /**
     *
     * makes the assignment for the actions state
     *
     * @throws SMVException occurs if assigned Expr is not boolean or unknown
     */
    private void makeActionAssignments() throws SMVException {
        int i,j;
        StepAction a;
        Step s;
        
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            for(j=0; j<s.actions.size(); j++) {
                a = (StepAction) s.actions.get(j);
                this.Output.append("\t\t\t\t\tAct_");
                this.Output.append(s.name);
                this.Output.append("_");
                this.Output.append(a.a_name);
                this.Output.append(": {\n");
                this.Output.append("\t\t\t\t\t\tswitch(ActionState_");
                this.Output.append(s.name);
                this.Output.append("_");
                this.Output.append(a.a_name);
                this.Output.append(") {\n");
                makeSingleStepActionAssignment(a, s.name+"_"+a.a_name);
                this.Output.append("\t\t\t\t\t\t}\n");
                this.Output.append("\t\t\t\t\t}\n");
            }
        }
        
        this.Output.append("\t\t\t\t\tnone: {\n");
        this.Output.append("\t\t\t\t\t\tnext(state) := transitions;\n");
        this.Output.append("\t\t\t\t\t\tnext(activeTransition) := ");
        if( sfc.transs.size()==0 )
            this.Output.append("none;\n");
        else {
            this.Output.append("(NEXT_TRANSITION_0");
            for(i=1; i<sfc.transs.size(); i++) {
                this.Output.append(" | NEXT_TRANSITION_");
                this.Output.append(i);
            }
            this.Output.append(") ? {NEXT_TRANSITION_0 ? Trans_0");
            for(i=1; i<sfc.transs.size(); i++) {
                this.Output.append(", NEXT_TRANSITION_");
                this.Output.append(i);
                this.Output.append(" ? Trans_");
                this.Output.append(i);
            }
            this.Output.append("} : none;\n");
        }

        this.Output.append("\t\t\t\t\t}\n");
        this.Output.append("\t\t\t\t}\n");
        
        this.Output.append("\n");
    }
    
    /**
     *
     * converts an Expression into a String
     *
     * @param e specifies the Expr to be converted
     * @return the String the Expr was converted into
     * @throws SMVException occurs if Expr not boolean
 */
    private String ExprToString(Expr e) throws SMVException {
        String s;
        
        if( e instanceof B_expr) {
            s = "(" + ExprToString(((B_expr)e).left_expr);
            switch(((B_expr)e).op) {
                case Expr.AND:
                    s += " & ";
                    break;
                case Expr.OR:
                    s += " | ";
                    break;
                default:
                    throw new SMVException("Nonboolean Expression found.");
            }
            s += ExprToString(((B_expr)e).right_expr) + ")";
        } else if( e instanceof U_expr) {
            switch(((U_expr)e).op) {
                case Expr.NEG:
                    s = "~("+ExprToString(((U_expr)e).sub_expr)+")";
                    break;
                default:
                    throw new SMVException("Nonboolean Expression found.");
            }
        } else if( e instanceof Constval) {
            s = ((Constval)e).val.toString();
        } else if( e instanceof Variable) {
            s = "Var_" + ((Variable)e).name;
        } else throw new SMVException("Unknown Expression specified.");
        
        return s;
    }
    
    /**
     *
     * creates Hashtable of Actionlist
     *
     */
    private void hashActions() {
        int i;
        Action a;
        
        for(i=0; i<sfc.actions.size(); i++) {
            a = (Action) sfc.actions.get(i);
            this.hashedActions.put(a.a_name, a);
        }
    }
    
    /**
     *
     * returns the Action specified by its name
     *
     * @param name specifies the Action
     * @return the Action specified by its name
 */
    private Action getActionByName(String name) {
        return (Action) this.hashedActions.get(name);
    }
    
    /**
     *
     * makes the assignment for a single StepAction
     *
     * @param sa specifies the StepAction
     * @param name specifies the variable extension of StepAction
     * @throws SMVException occurs if assigned Expression is not boolean or unknown
 */
    private void makeSingleStepActionAssignment(StepAction sa, String name) throws SMVException {
        int i, j;
        Action a = getActionByName(sa.a_name);
        Assign ass;
        Step s;
        int EntryCount = 0;
        
        for(i=0; i<a.sap.size(); i++) {
            ass = (Assign) a.sap.get(i);
            this.Output.append("\t\t\t\t\t\t\tsub");
            this.Output.append(i);
            this.Output.append(": {\n");
            this.Output.append("\t\t\t\t\t\t\t\tnext(Var_");
            this.Output.append(ass.var.name);
            this.Output.append(") := ");
            this.Output.append(ExprToString(ass.val));
            this.Output.append(";\n");
            if( (i+1)==a.sap.size() ){
                // last assign for this StepAction
                this.Output.append("\t\t\t\t\t\t\t\tnext(activeAction) := ");
                if( this.ActionVariableExtensions.size()==1 )
                    this.Output.append("none;\n");
                else {
                    this.Output.append("(");
                    for(j=0; j<this.ActionVariableExtensions.size(); j++) {
                        if( ((String)this.ActionVariableExtensions.get(j)).compareTo(name)!=0 ) {
                            if( EntryCount>0 )
                                this.Output.append(" | ");
                            this.Output.append("StepAction_");
                            this.Output.append((String) this.ActionVariableExtensions.get(j));
                            EntryCount++;
                        }
                    }
                    this.Output.append(") ? {");
                    EntryCount = 0;
                    for(j=0; j<this.ActionVariableExtensions.size(); j++) {
                        if( ((String)this.ActionVariableExtensions.get(j)).compareTo(name)!=0 ) {
                            if( EntryCount>0 )
                                this.Output.append(", ");
                            this.Output.append("StepAction_");
                            this.Output.append((String) this.ActionVariableExtensions.get(j));
                            this.Output.append(" ? Act_");
                            this.Output.append((String) this.ActionVariableExtensions.get(j));
                            EntryCount++;
                        }
                    }
                    this.Output.append("} : none;\n");
                }
                this.Output.append("\t\t\t\t\t\t\t\tnext(StepAction_");
                this.Output.append(name);
                this.Output.append(") := 0;\n");
            } else {
                this.Output.append("\t\t\t\t\t\t\t\tnext(ActionState_");
                this.Output.append(name);
                this.Output.append(") := sub");
                this.Output.append(i+1);
                this.Output.append(";\n");
            }
            this.Output.append("\t\t\t\t\t\t\t}\n");
        }
    }
    
    /**
     *
     * fills in List with all Extensions
     *
     */
    private void fillActionVariableExtensionsList() {
        int i,j;
        Step s;
        StepAction sa;
        
        for(i=0; i<sfc.steps.size(); i++) {
            s = (Step) sfc.steps.get(i);
            for(j=0; j<s.actions.size(); j++) {
                sa = (StepAction) s.actions.get(j);
                this.ActionVariableExtensions.addLast(s.name+"_"+sa.a_name);
            }
        }
    }
}
