
package checks;

import java.util.*;
import java.awt.*;
import java.lang.*;
import absynt.*;

/**
 *Diese Klasse macht eine ganze Menge, n"amlich zum Beispiel:
 *checken von SFCs
 *@author Dimitri Schultheis, Tobias Pugatschov
 *@version: $Id: Snotcheck.java,v 1.46 2001-07-11 12:05:25 swprakt Exp $
 *
 */

public class Snotcheck{

    private static boolean isBExprOk(B_expr aBExpr){
	/////////////////////////////////////////////////////////////////////////
	//in dieser methode wird geprueft, ob der binaere ausdruck korrekt ist,
	//und es wird ermittelt, welchen typ dieser hat
	/////////////////////////////////////////////////////////////////////////
	Expr lExpr,rExpr;
	boolean allesOk = true;
	
	lExpr = aBExpr.left_expr;
	rExpr = aBExpr.right_expr;

	//sind beide expressions vorhanden und ist der Operator ein moeglicher Operator?
	if ((lExpr == null)||(rExpr == null)||(aBExpr.op < 0)||(aBExpr.op > 12)){return false;}

	//pruefen der linken expression:
	String className = (lExpr.getClass()).getName();
	if (className == "B_expr"){
	    B_expr lBExpr = (B_expr) lExpr;
	    allesOk = isBExprOk(lBExpr);
	    if (allesOk == false){return false;}
	}
	if (className == "U_expr"){
	    U_expr lUExpr = (U_expr) lExpr;
	    allesOk = isUExprOk(lUExpr);
	    if (allesOk == false){return false;}
	}
	if (className == "Constval"){
	    String nameOfConstvalClass = (((Constval) lExpr).val.getClass()).getName();
	    if (nameOfConstvalClass == "Boolean"){
		lExpr.type = new BoolType();
	    } else {
		lExpr.type = new IntType();
	    }
	}

	if (className == "Variable"){
	    String nameOfVariableType = (((Variable) lExpr).type.getClass()).getName();
	    if (nameOfVariableType == "Boolean"){
		lExpr.type = new BoolType();
	    } else {
		lExpr.type = new IntType();
	    }
	}


	//pruefen der rechten expression:
	className = (rExpr.getClass()).getName();
	if (className == "B_expr"){
	    B_expr rBExpr = (B_expr) rExpr;
	    allesOk = isBExprOk(rBExpr);
	    if (allesOk == false){return false;}
	}
	if (className == "U_expr"){
	    U_expr rUExpr = (U_expr) rExpr;
	    allesOk = isUExprOk(rUExpr);
	    if (allesOk == false){return false;}
	}
	if (className == "Constval"){
	    String nameOfConstvalClass = (((Constval) rExpr).val.getClass()).getName();
	    if (nameOfConstvalClass == "Boolean"){
		rExpr.type = new BoolType();
	    } else {
		rExpr.type = new IntType();
	    }
	}

	if (className == "Variable"){
	    String nameOfVariableType = (((Variable) rExpr).type.getClass()).getName();
	    if (nameOfVariableType == "Boolean"){
		rExpr.type = new BoolType();
	    } else {
		rExpr.type = new IntType();
	    }
	}



	//"berechnen" des Typs von dieser Expression aus dem Operator und den Typen der linken/rechten expression
	if (((lExpr.type.getClass()).getName()) != ((rExpr.type.getClass()).getName())){
	    //abfrage, ob beide Operanden den gleichen Typ haben. Wenn nicht, dann gebe ein false zurueck
	    return false;
	}

	String typeName = (lExpr.type.getClass()).getName();


	if ((aBExpr.op == aBExpr.AND)||(aBExpr.op == aBExpr.OR)){
	    //AND und OR sind nur auf boolschen Werten definiert:
	    if (typeName == "BoolType"){
		aBExpr.type = new BoolType();
		return true;
	    } else {
		return false;
	    }
	}


	if ((aBExpr.op == aBExpr.DIV)||(aBExpr.op == aBExpr.MINUS)||(aBExpr.op == aBExpr.PLUS)||(aBExpr.op == aBExpr.TIMES)){
	    //DIV MINUS PLUS und TIMES sind Operationen, die nur auf Integers erlaubt sind:
	    if (typeName == "IntType"){
		aBExpr.type = new IntType();
		return true;
	    } else {
		return false;
	    }
	}


	if((aBExpr.op == aBExpr.GREATER)||(aBExpr.op == aBExpr.GEQ)||(aBExpr.op == aBExpr.LESS)||(aBExpr.op == aBExpr.LEQ)){
	    //GREATER GEQ LESS und LEQ sind auch Operationen, die nur auf Integers erlaubt sind:
	    if (typeName == "IntType"){
		aBExpr.type = new IntType();
		return true;
	    } else {
		return false;
	    }
	}

	if (aBExpr.op == aBExpr.EQ){
	    //EQ ist sowohl fuer boolsche Variablen als auch fuer integer Variablen definiert
	    if (typeName == "IntType"){
		//bei Operation auf Integers:
		aBExpr.type = new IntType();
		return true;
	    } else {
		//bei OPeration auf Booleans:
		aBExpr.type = new BoolType();
		return true;
	    }
	}

	//wenn keiner der ganzen Faelle eintrat, dann ist die Expression ungueltig:
	return false;
    }



    private static boolean isUExprOk(U_expr anUExpr){
	/////////////////////////////////////////////////////////////////////////
	//in dieser methode wird geprueft, ob der unaere ausdruck korrekt ist,
	//und es wird ermittelt, welchen typ dieser hat
	/////////////////////////////////////////////////////////////////////////
	Expr sExpr;
	String typeName;
	boolean allesOk = true;

	sExpr = anUExpr.sub_expr;

	//ist die Sub-Expression vorhanden und ist der Operator gueltig (d.h. MINUS oder NEG)
	if ((sExpr == null)||((anUExpr.op != anUExpr.MINUS)&&(anUExpr.op != anUExpr.NEG))){return false;}

	//pruefen der Sub-Expression:
	String className = (sExpr.getClass()).getName();
	if (className == "B_expr"){
	    B_expr sBExpr = (B_expr) sExpr;
	    allesOk = isBExprOk(sBExpr);
	    if (allesOk == false){return false;}
	}
	if (className == "U_expr"){
	    U_expr sUExpr = (U_expr) sExpr;
	    allesOk = isUExprOk(sUExpr);
	    if (allesOk == false){return false;}
	}
	if (className == "Constval"){
	    String nameOfConstvalClass = (((Constval) sExpr).val.getClass()).getName();
	    if (nameOfConstvalClass == "Boolean"){
		sExpr.type = new BoolType();
	    } else {
		sExpr.type = new IntType();
	    }
	}
	if (className == "Variable"){
	    String nameOfVariableType = (((Variable) sExpr).type.getClass()).getName();
	    if (nameOfVariableType == "Boolean"){
		sExpr.type = new BoolType();
	    } else {
		sExpr.type = new IntType();
	    }
	}

	
	//pruefen, ob der Operator den Type der Sub-Expression verarbeiten kann und setzen des Types von dieser Expression
	typeName = (sExpr.type.getClass()).getName();   //
	if (anUExpr.op == anUExpr.MINUS){
	    //das MINUS kann nur auf Integerwerte angewendet werden
	    if (typeName == "IntType"){
		anUExpr.type = new IntType();
		return true;
	    } else {return false;}
	}

	if (anUExpr.op == anUExpr.NEG){
	    //das NEG ist nur auf boolschen Expressions definiert
	    if (typeName == "BoolType"){
		anUExpr.type = new BoolType();
		return true;
	    } else {return false;}
	}


	//wenn keiner dieser beiden Faelle auftritt, dann ist die Expression ungueltig:
	return false;
    }




    private static boolean isGuardOk(Expr anExpr){
	//Diese Methode "uberpr"uft, ob der angegebene Ausdruck ein korrekter Ausdruck fuer eine Guard ist.

	//Folgende Dinge sollen gepr"uft werden:
	//   -Typ der Expr muss Boolean sein


	//1. Schritt: feststellen, was fuer eine Expression vorliegt:
	String nameOfExprClass,nameOfConstvalClass;
	Class exprClass,constvalClass;
	boolean allesOk = true;
	
	//wurde keine Expression uebergeben, dann gebe ein false zurueck:
	if (anExpr == null){return false;}

	exprClass = anExpr.getClass();
	nameOfExprClass = exprClass.getName();

	if (nameOfExprClass == "B_expr"){
	    //Aufruf der Methode zum Pruefen einer binaeren Expression:
	    B_expr bExpr = (B_expr) anExpr;
	    allesOk = isBExprOk(bExpr);
	    if (allesOk == false){return false;}
	}
	if (nameOfExprClass == "U_expr"){
	    //Aufruf der Methode zum Pruefen einer unaeren Expression:
	    U_expr uExpr = (U_expr) anExpr;
	    allesOk = isUExprOk(uExpr);
	    if (allesOk == false){return false;}
	}

	if (nameOfExprClass == "Constval"){
	    //wenn die Expression ein konstanter Wert ist, so sind nur boolsche Werte erlaubt
	    Constval constant = (Constval) anExpr;
	    constvalClass = constant.val.getClass();
	    nameOfConstvalClass = constvalClass.getName();
	    if (nameOfConstvalClass != "Boolean"){
		anExpr.type = new IntType();
		//wenn der konstante Wert nicht zu den Booleans gehoert, dann gebe ein false zurueck:
		return false;
	    } else {
		anExpr.type = new BoolType();
		return true;
	    }
	}

	if (nameOfExprClass == "Variable"){
	    //wenn die Expression eine Variable ist, so muss diese vom Type BoolType sein:
	    if (((anExpr.type.getClass()).getName()) == "BoolType"){
		anExpr.type = new BoolType();
		return true;
	    } else {
		anExpr.type = new IntType();
		return false;
	    }
	}


	//wenn die Expression OK ist, dann pruefe noch, ob der Type der Expression BoolType ist, sonst gebe ein false zurueck:
	if (((anExpr.type.getClass()).getName()) == "BoolType"){
	    //alles ist OK
	    return true;
	} else {
	    return false;
	}

    }











    /**Diese Methode prueft die Deklarationsliste
     */


    private static boolean isDeclarationOk(SFC aSFCObject) throws DecListFailure {
	//Diese Methode prueft, ob in den Dekarationen keine NULL-Werte stehen, ob keine Variable doppelt deklariert ist und ob der in der Deklaration angegebene Typ gleich dem Typ der Variable ist.


	//Pruefung auf NULL-Werte:

	LinkedList decList = aSFCObject.declist;
	Declaration decl,decl2;
	Variable var,var2;
	String className1, className2;
	Class typeClass1, typeClass2;



	if (decList != null){
	    for (int i=0; i < decList.size(); i++){
		decl = (Declaration)decList.get(i);
		if (decl.var == null || decl.type == null || decl.val == null){
		    //Wenn einer dieser Eintr"age fehlt, dann werfe eine Exception (DeclarationFailure) mit der entsprechenden Declaration als Argument
		    throw new DecListFailure(decl, "Missing argument(s) in declaration.");
			}
	    }
	}





	//Pr"ufung, ob Variable doppelt vorkommt:

	if (decList != null){
	    for (int i=0; i < decList.size(); i++){
		decl = (Declaration)decList.get(i);                          //durchgehen aller Deklarationen
		var = decl.var;                                 //auslesen der entsprechenden Variable
		if (var != null){
		    for (int j=i+1; j < decList.size(); j++){
			decl2 = (Declaration)decList.get(j);                 //durchgehen aller nachfolgenden Deklarationen
			var2 = decl2.var;
			if (var.name == null || var2.name == null){
			    throw new DecListFailure(decl, "One or more variables have no name!");}
			if (var.name == var2.name){            //vergleich aller Variablennamen
			    throw new DecListFailure(decl, "This variable is declared twice!");
			}
		    }
		}
	    }
	}





	//Pr"ufung auf Typgleichheit:

	if (decList != null){
	    for (int i=1; i < decList.size(); i++){
		decl = (Declaration)decList.get(i);
		var = decl.var;
		typeClass1 = decl.type.getClass();
		className1 = typeClass1.getName();
		if (var != null){
		    if (var.type == null){
			throw new DecListFailure(decl, "Variable has no Type!");}
		    typeClass2 = var.type.getClass();
		    className2 = typeClass2.getName();
		    if (className1 != className2){
			throw new DecListFailure(decl, "Type Error!");}   //Der Typ der Variable stimmt nicht mit dem Typ der Deklaration "uberein
		}
	    }
	}
	return true;
	
    }
    








    /**Diese Funktion prueft, ob alle Actions vollstaendig und korrekt sind
     */
    private static boolean isAllActionOk(SFC aSFCObject) throws ActionFailure {
	//Diese Methode pr"uft zuerst, ob bei den actions aus der actionList keine Null-Werte stehen. Anschliessend wird noch geprueft, ob die Namen eindeutig, d.h. nicht doppelt, sind.

	LinkedList actionList = aSFCObject.actions;
	String name1,name2;
	Action action,action2;

	if (actionList == null){return true;}    //wenn in der actionList keine Eintraege vorhanden sind, kann auch kein Eintrag fehlerhaft sein, also sind alle Eintraege korrekt

	//Pruefung auf Null-Werte
	for (int i=0; i < actionList.size(); i++){
	    action = (Action)actionList.get(i);
	    if (action.sap == null){throw new ActionFailure(action, "Missing sap in this Action.");}
	    if (action.a_name == null){throw new ActionFailure(action, "Action without a name.");}
	}

	//Namenspruefung
	for (int i=0; i < actionList.size(); i++){
	    action = (Action)actionList.get(i);
	    name1 = action.a_name;
	    for (int j=i+1; j < actionList.size(); j++){
		action2 = (Action)actionList.get(j);
		name2 = action2.a_name;
		if (name1 == name2){throw new ActionFailure(action, "Action with a name, which is already used.");}
	    }

	}

	LinkedList stmtList;
	Stmt stmt1;
	Class class1,valClass,varClass,varTypeClass,valTypeClass;
	String class1Name,valClassName,varTypeName,valTypeName;
	Assign ass;

	//nun muessen nur noch die einzelnen sap`s ueberprueft werden:
	for (int i=0; i < actionList.size(); i++){
	    action = (Action)actionList.get(i);
	    stmtList = action.sap;                           //auslesen der i-ten sap
	    for (int j=0; j < stmtList.size(); j++){
		stmt1 = (Stmt)stmtList.get(j);               //auslesen des j-ten Statements aus der sap

		class1 = stmt1.getClass();
		class1Name = class1.getName();
		//	if (stmt1 == null){throw new ActionFailure(action, "neuer fehler.");}
		if (class1Name == "absynt.Assign" || class1Name == "absynt.Skip"){
		    if (class1Name == "Assign"){             //wenn das statement eine Zuweisung ist, dann muessen noch einige Dinge geprueft werden, naehmlich:
			// - sind val und var ungleich null
			// - ist val eine gueltige Expression
			// - ist val ein gueltiger Wert fuer var (richtiger Typ?)
			ass = (Assign)stmt1;

			//pruefen, ob var und val vernuenftig sind (d.h. ungleich null)
			if (ass.var == null){throw new ActionFailure(action, "This statement has no var.");}
			if (ass.val == null){throw new ActionFailure(action, "This statement has no val.");}
			

			/////////////////////////////////////////////////////////////////////
			/////////////////////////////////////////////////////////////////////

			//pruefen, ob val eine gueltige Expression ist, und setzen des Typs von val:
			String nameOfValClass = ((ass.val).getClass()).getName();
			boolean allesOk;
			if (nameOfValClass == "B_expr"){
			    B_expr bExpr = (B_expr) ass.val;
			    allesOk = isBExprOk(bExpr);
			    if (allesOk == false){throw new ActionFailure(action, "This val is no correct expression.");}
			}
			if (nameOfValClass == "U_expr"){
			    U_expr uExpr = (U_expr) ass.val;
			    allesOk = isUExprOk(uExpr);
			    if (allesOk == false){throw new ActionFailure(action, "This val is no correct expression.");} 
			}
			if (nameOfValClass == "Constval"){
			    //wenn val ein konstanter Wert ist, dann muss erstmal nur der Typ von val geaendert werden
			    Constval constant = (Constval) ass.val;
			    String nameOfConstvalClass = (constant.val.getClass()).getName();  //name des Typs der Konstanten
			    if (nameOfConstvalClass == "Boolean"){
				ass.val.type = new BoolType();
			    } else {
				ass.val.type = new IntType();
			    }
			}
			if (nameOfValClass == "Variable"){
			    //wenn val eine Variable ist, dann muss erstmal nur der Typ von val geaendert werden
			    String nameOfVariableType = (((Variable) ass.val).type.getClass()).getName();
			    if (nameOfVariableType == "Boolean"){
				ass.val.type = new BoolType();
			    } else {
				ass.val.type = new IntType();
			    }
			}


			//////////////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////


			//pruefen, ob val den gleichen Typ wie var hat:
			valTypeClass = ass.val.type.getClass();
			valTypeName = valTypeClass.getName();
			varTypeClass = ass.var.type.getClass();
			varTypeName = varTypeClass.getName();

			if (varTypeName != valTypeName){
			    throw new ActionFailure(action, "This statement tries to assign a Boolean to an Interger or an Interger to a Boolean.");
			}
		    } else {
			//bei einem Skip wird nichts weiter geprueft, da in diesem Falle alles in Ordnung ist
		    }
		} else {throw new ActionFailure(null, "Statements MUST be an Assign or a Skip!");}
	    }        //Ende der inneren for-Schleife
	}            //Ende der ausseren for-Schleife
       	return true;
    }







    /**Diese Funktion prueft alle Steps(vollstaendig, korrekt)
     */
private static boolean isAllStepOk(SFC aSFCObject) throws StepFailure {

    LinkedList stepList = aSFCObject.steps;
    LinkedList actionList;
    int stepListSize = stepList.size();
    String stepName;
    Step aStep, bStep;
    StepAction anAction, bAction;

    if (stepList != null){

        //Pruefung auf Null-Werte von Stepparameter
	for (int i=0; i < stepListSize; i++){
	    aStep = (Step)stepList.get(i);
	    if (aStep.name == null){throw new StepFailure(aStep, "Stepname not found!");}
	    if (aStep.actions == null){throw new StepFailure(aStep, "No actions in step!");}
	    actionList = aStep.actions;
	    for (int j=0; j < actionList.size(); j++){
		anAction = (StepAction)actionList.get(j);//ohne cast
		for (int k=(j+1); k < actionList.size(); k++){
		    bAction = (StepAction)actionList.get(k);//ohne cast
		    if (anAction.a_name == bAction.a_name){throw new StepFailure(aStep,"The Actionname in Step is not unique!");}
		}
	    }
	}
	//Pruefung auf Doppeltvorkommene Namen von Steps
	for (int i=0; i < stepListSize; i++){
	    aStep = (Step)stepList.get(i);
	    
	    for (int j=(i+1); j < stepListSize; j++){
		bStep = (Step)stepList.get(j);
		if (aStep.name == bStep.name){throw new StepFailure(aStep,"The Stepname is not unique!");}
	    }
	}

    }else throw new StepFailure(null,"Where are the steps?!!");
    System.out.println("Steps are OK!");

    return true;
}






    /**Diese Funktion prueft, ob der istep vorhanden ist
     */
    private static boolean isThereAnIStep(SFC aSFCObject) throws IStepException{
	if (aSFCObject.istep == null) {throw new IStepException("There is no I-Step!!");}
	return true;
    }







    /**Diese Funktion prueft, ob alle Transitionen vollstaendig und korrekt sind
     *
     */
    private static boolean isAllTransitionOk(SFC aSFCObject) throws TransitionFailure{
	//Bevor diese Methode aufgerufen wird, ist zu pruefen, ob die Transitionsliste=null ist.
	//Wenn die Liste nicht existiert, arbeitet diese Methode m"oglicherweise nicht korrekt.

	LinkedList translist = aSFCObject.transs;
	Transition trans;
	Expr expr;

	if (translist == null){throw new TransitionFailure(null, "There are no Transitions!!!");}


	for (int i=0; i < translist.size(); i++){
	    trans = (Transition)translist.get(i);
	    if (trans.source == null || trans.target == null || trans.guard == null){
		//Wenn einer der Werte fehlt, dann werfe eine Exception (TransitionFailure) mit der entsprechenden Transition als Argument
		throw new TransitionFailure(trans, "Missing argument(s) in transition.");
	    }
	    //Pruefen der Guards:
	    if (isGuardOk(trans.guard) == false){
		throw new TransitionFailure(trans, "Failure in Expression.");
	    }
	    
	}
	
	
	

	return true;

    }





    /**Diese Funktion prueft, ob nur boolsche Variablen vorkommen.
     */
    public static boolean isOnlyBool(SFC aSFCObject){

	LinkedList decList = aSFCObject.declist;
	Declaration decl;
	String nameOfClass;
	Class typeClass;
	boolean nurBool = true;

	for (int i=0; i < decList.size(); i++){
	    decl = (Declaration)decList.get(i);
	    typeClass = decl.type.getClass();
	    nameOfClass = typeClass.getName();
	    if(nameOfClass != "BoolType"){
		nurBool = false;
		i = decList.size();
	    }
	}
	return nurBool;
    }





    /**Diese Funktion prueft, ob der angegebene SFC korrekt ist

     */

    public static boolean isWellDefined(SFC aSFCObject) throws CheckException {

	if (aSFCObject == null){throw new IStepException("No SFC selected.");}

	boolean test;

	test = isThereAnIStep(aSFCObject);
	System.out.println(test);
	test = isDeclarationOk(aSFCObject);
	System.out.println(test);
	test = isAllActionOk(aSFCObject);
	System.out.println(test);
	test = isAllStepOk(aSFCObject);
	System.out.println(test);
	test = isAllTransitionOk(aSFCObject);
	System.out.println(test);
	return true;
    }


}


//----------------------------------------------------------------------
//	package checks for Snot programs
//	------------------------------------
//
//	$Id: Snotcheck.java,v 1.46 2001-07-11 12:05:25 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.45  2001/07/11 09:25:18  swprakt
//	*** empty log message ***
//	
//	Revision 1.44  2001/07/11 08:41:20  swprakt
//	jetzt werden die guards gecheckt
//	
//	Revision 1.43  2001/07/11 07:14:49  swprakt
//	*** empty log message ***
//	
//	Revision 1.42  2001/07/10 14:25:34  swprakt
//	*** empty log message ***
//	
//	Revision 1.41  2001/07/10 14:10:35  swprakt
//	*** empty log message ***
//	
//	Revision 1.40  2001/07/10 14:07:13  swprakt
//	*** empty log message ***
//	
//	Revision 1.39  2001/07/10 14:00:01  swprakt
//	*** empty log message ***
//	
//	Revision 1.38  2001/07/03 15:06:30  swprakt
//	*** empty log message ***
//	
//	Revision 1.37  2001/07/03 14:57:34  swprakt
//	guards werden gecheckt (zumindest ein wenig)
//	
//	Revision 1.36  2001/07/03 14:47:00  swprakt
//	*** empty log message ***
//	
//	Revision 1.35  2001/07/03 14:40:04  swprakt
//	*** empty log message ***
//	
//	Revision 1.34  2001/07/03 14:36:33  swprakt
//	*** empty log message ***
//	
//	Revision 1.33  2001/06/27 13:12:42  swprakt
//	*** empty log message ***
//	
//	Revision 1.32  2001/06/27 13:08:18  swprakt
//	*** empty log message ***
//	
//	Revision 1.31  2001/06/27 11:33:22  swprakt
//	*** empty log message ***
//	
//	Revision 1.30  2001/06/19 15:04:39  swprakt
//	*** empty log message ***
//	
//	Revision 1.29  2001/06/19 14:34:55  swprakt
//	*** empty log message ***
//	
//	Revision 1.28  2001/06/19 13:30:35  swprakt
//	nix
//	
//	Revision 1.27  2001/06/14 11:52:23  swprakt
//	*** empty log message ***
//
//	Revision 1.26  2001/06/13 13:34:30  swprakt
//	g
//
//	Revision 1.25  2001/06/12 15:14:25  swprakt
//	I removed a file
//
//	Revision 1.23  2001/06/12 14:55:02  swprakt
//	*** empty log message ***
//
//	Revision 1.21  2001/06/08 09:00:56  swprakt
//	*** empty log message ***
//
//	Revision 1.20  2001/06/08 08:56:17  swprakt
//	*** empty log message ***
//
//	Revision 1.19  2001/06/07 12:04:25  swprakt
//	*** empty log message ***
//
//	Revision 1.18  2001/06/07 11:24:00  swprakt
//	*** empty log message ***
//
//	Revision 1.17  2001/06/07 11:00:12  swprakt
//	*** empty log message ***
//
//	Revision 1.16  2001/06/05 14:42:17  swprakt
//	*** empty log message ***
//
//	Revision 1.15  2001/06/05 14:33:52  swprakt
//	*** empty log message ***
//
//	Revision 1.14  2001/06/05 14:17:34  swprakt
//	*** empty log message ***
//
//	Revision 1.13  2001/06/05 13:21:31  swprakt
//	*** empty log message ***
//
//	Revision 1.12  2001/05/30 14:01:58  swprakt
//	*** empty log message ***
//
//	Revision 1.11  2001/05/29 15:08:01  swprakt
//	*** empty log message ***
//
//	Revision 1.10  2001/05/22 14:54:59  swprakt
//	*** empty log message ***
//
//	Revision 1.9  2001/05/22 14:50:36  swprakt
//	*** empty log message ***
//
//	Revision 1.8  2001/05/22 14:49:31  swprakt
//	alles ok
//
//	Revision 1.7  2001/05/22 14:41:53  swprakt
//	*** empty log message ***
//
//	Revision 1.6  2001/05/22 14:31:47  swprakt
//	*** empty log message ***
//
//	Revision 1.5  2001/05/22 14:28:04  swprakt
//	*** empty log message ***
//
//	Revision 1.4  2001/05/22 14:25:23  swprakt
//	ok
//
//
//--------------------------------------------------------------------
