package absynt;
import java.io.Serializable;

/**
 * Toplevel syntactic contruct for Mist programs.
 * @author Initially provided by Martin Steffen.
 * @version  $Id: Program.java,v 1.1 2001-04-20 17:29:12 swprakt Exp $
 */





public class Program extends Absyn implements Serializable {
  public ChandecList chans;
  public ProcessList procs;
  public String name;

 /**
  * Haupt-konstruktor, übernimmt schlicht die Argumente in die Felder
  */
  public Program (String _name, ChandecList cl, ProcessList pl) {
    name     = _name;
    chans    = cl;
    procs    = pl;
  };
  public Program (ChandecList cl, ProcessList pl) {
    name     = "";
    chans    = cl;
    procs    = pl;
  };
};


//----------------------------------------------------------------------
//	Abstract Syntax for Mist Programs
//	------------------------------------
//
//	$Id: Program.java,v 1.1 2001-04-20 17:29:12 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.5  2000/07/03 16:31:05  unix01
//	Die _alten_ Konstruktoren ohne den Namens-Parameter wieder
//	zus"atzlich hinzugef"ugt, damit alles zusammen noch kompiliert.
//	
//	[Steffen]
//	
//	Revision 1.4  2000/07/03 16:25:05  unix01
//	Program/Process;  Name als String hinzugefuegt
//	Entsprechend auch das Beispiel angepa"st.
//	
//	[Steffen]
//	
//	Revision 1.3  2000/05/28 12:57:12  unix01
//	Zwischenzustand, vor Reorganisation
//	
//	Revision 1.2  2000/05/28 08:51:48  unix01
//	ok
//	
//	Revision 1.1  2000/05/28 08:08:12  unix01
//	Initiale Revision, zum testen der cvs-log-Funktion
//	
//---------------------------------------------------------------------
