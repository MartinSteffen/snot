package checks;
import java.lang.*;

    
public abstract class CheckException extends Exception{
protected
    String nachricht;
public
    String get_Nachricht(){return nachricht;}
}
