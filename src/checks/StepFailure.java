package checks;

import java.lang.*;
import absynt.*;


public class StepFailure extends CheckException{
    private
	Step s;
    
    public StepFailure(Step st, String aString){
	nachricht = aString;
	s = st;
    };

    public Step get_Step(){
	return s;
    };
}
