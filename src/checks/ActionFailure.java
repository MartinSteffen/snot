package checks;

import java.lang.*;
import absynt.*;


public class ActionFailure extends CheckException{
    private
	Action act;
    
    public ActionFailure(Action a, String aString){
	nachricht = aString;
	act = a;
    };

    public Action get_Action(){
	return act;
    };
}
