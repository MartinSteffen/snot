package checks;
import java.lang.*;

    
public abstract class CheckException extends Exception{
protected
    String nachricht;
public
    String getNachricht(){return nachricht;}
}
