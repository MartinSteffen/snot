/*
 * SMVTranslator.java
 *
 * Created on 14. Mai 2001, 22:08
 */

package smv;

import absynt.*;
import java.io.ByteArrayOutputStream;


/**
 *
 * @author  Kevin Koeser / Tobias Kloss
 * @version 0.2
 */
public class SMVTranslator extends java.lang.Object {
    private SFC sfc;
    private StringBuffer Output;
    
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
     *
     */
    public ByteArrayOutputStream toStream() throws SMVException {
        if (this.Output == null) throw(new SMVException("SFC has not been translated yet."));

        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        ba.write((new String(this.Output)).getBytes(), 0, this.Output.length());
        return ba;
    }

    /**
     *
     * Translates the SFC to SMV
     *
     */
    public void translate() throws SMVException{
      this.Output = new StringBuffer();

      this.Output.append("module main() {\n");
      this.Output.append("\t/* Declaration */\n");
      declareSFCVariables();
      declareSteps();
      declareActions();
      this.Output.append("\t/* System variables */\n");
      this.Output.append("\tstate : {initial, actions, transitions};\n");
      declareActiveAction();
      declareActionStates();
      this.Output.append("\n\n");
      this.Output.append("\t/* Initialization */\n");
      initSFCVariables();
      initSteps();
      initActions();
      this.Output.append("\t/* System varibales */\n");
      this.Output.append("\tinit(state) := initial;\n");
      initActiveAction();
      initActionStates();
      this.Output.append("\n\n");
      this.Output.append("\t/* Stepwise Assignment*/\n");
      this.Output.append("\tswitch(state) {\n");
      this.Output.append("\t\tinitial: {\n");
      makeInitialAssignments();
      this.Output.append("\t\t}\n");
      this.Output.append("\t\ttransitions: {\n");
      makeTransitionAssignments();
      this.Output.append("\t\t}\n");
      this.Output.append("\t\tactions: {\n");
      makeActionAssignments();
      this.Output.append("\t\t}\n");
      this.Output.append("\t}\n");
      this.Output.append("\n");
      this.Output.append("}\n");
    }
    
    /**
     *
     * declares the SFC variables
     *
     */
    private void declareSFCVariables() throws SMVException{
        int i;
        Declaration d;
        BoolType b = new BoolType();
        
        this.Output.append("\t/* SFC variables */\n");
        for(i=0; i<sfc.declist.size(); i++) {
            d = (Declaration) (sfc.declist.get(i));
            this.Output.append("\tVar_"+d.var.name+" : ");
            if (d.type.getClass() == b.getClass())
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
            this.Output.append("\tStep_"+s.name+" : boolean;\n");
        }
        this.Output.append("\n");
    }
    
    /**
     *
     * declares the SFC Actions
     *
     */
    private void declareActions() {
        int i;
        Action a;
        
        this.Output.append("\t/* Actions */\n");
        for(i=0; i<sfc.actions.size(); i++) {
            a = (Action) sfc.actions.get(i);
            this.Output.append("\tAction_"+a.a_name+" : boolean;\n");
        }
        this.Output.append("\n");
    }
    
    /**
     *
     * declares the system variable activeAction
     *
     */
    private void declareActiveAction () {
        int i;
        Action a;
        
        this.Output.append("\tactiveAction : {");
        for(i=0; i<sfc.actions.size(); i++) {
            a = (Action) sfc.actions.get(i);
            this.Output.append("ActiveAction_"+a.a_name+", ");
        }
        this.Output.append("none};\n");
    }
    
    /**
     *
     * declares a system variable for each ActionState
     *
     */
    private void declareActionStates() {
        int i, j;
        Action a;
        
        for(i=0; i<sfc.actions.size(); i++) {
            a = (Action) sfc.actions.get(i);
            this.Output.append("\tActionState_"+a.a_name+" : {");
            for(j=0; j<a.sap.size(); j++)
                this.Output.append("sub"+j+", ");
            this.Output.append("completed};\n");
        }
    }
    
    /**
     *
     * initialzies the SFC variables
     *
     */
    private void initSFCVariables() {
    }
    
    /**
     *
     * initialzies the SFC Steps
     *
     */
    private void initSteps() {
    }
    
    /**
     *
     * initialzies the SFC Actions
     *
     */
    private void initActions() {
    }
    
    /**
     *
     * initialzies the system variable activeAction
     *
     */
    private void initActiveAction () {
    }
    
    /**
     *
     * initialzies the system variable for each ActionState
     *
     */
    private void initActionStates() {
    }
    
    /**
     *
     * makes the assignments for the initial state
     *
     */
    private void makeInitialAssignments() {
    }
    
    /**
     *
     * makes the assignments for the transitions state
     *
     */
    private void makeTransitionAssignments() {
    }
    
    /**
     *
     * makes the assignment for the actions state
     *
     */
    private void makeActionAssignments() {
    }
}
