package checks;

import java.lang.*;
import absynt.*;


public class DecListFailure extends CheckException{
    private
	Declaration decl;
    
    public DecListFailure(Declaration d, String aString){
	nachricht = aString;
	decl = d;
    };

    public Declaration get_Declaration(){
	return decl;
    };
}
