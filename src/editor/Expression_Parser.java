package editor;

import java.util.*; 
import java.util.List.*;
import java.awt.*; 
import java.awt.event.*; 
import java.awt.geom.*;
import java.lang.*;
import javax.swing.*; 
import javax.swing.event.*;

import absynt.*;

/** 
 * Editor - Klasse 
 * 
 * @author Natalja Froidenberg, Andreas Lukosch 
 * @version 1.0 
 */ 

// --------------------------------------------------------------------------------------------------------------
// Datenobjekt 
  class drawObject{
    private String content; // Auszugebendes Symbol
    private int    x;       // X-Koordinate, wo Symbol ausgegeben werden soll
    
    public drawObject(String cont, int _x){
        content = cont;
        x       = _x;
    }
    
    public String get_Content(){return content;}
    public int    get_x(){return x;}   
 }

// --------------------------------------------------------------------------------------------------------------
// Dialog zur Auswahl einer Variable
 class VariableDialog extends Dialog implements ActionListener{
     
     final static public int _NULL     = 0;
     final static public int _INTTYPE  = 1;
     final static public int _BOOLTYPE = 2;
     
     private int  selectedNum = 0;
     private SFC _sfc;
     private int  numBut;
     
     Expression_Parser  ExpressionParser;
     
     public VariableDialog(JDialog parent, String title, Expression_Parser  ExpParser,
                           SFC sfc){
         
         super(parent, title, true);
         setLayout(new BorderLayout());

        _sfc = sfc;
         ExpressionParser = ExpParser;
         
         JPanel panel_South = new JPanel();
         panel_South.add(getJButton("OK"));
         panel_South.add(getJButton("Cancel"));
         
         if (_sfc != null)
         { ButtonGroup g          = new ButtonGroup();
           JPanel panel_Center    = new JPanel();
           
           LinkedList decl_lList = new LinkedList();                  
           Iterator   decl_List = (_sfc.declist).iterator();
           Object     element;
          
           numBut = 0; 
       
           while (decl_List.hasNext()){
                  element = decl_List.next();
                  if (!(((Variable)(((Declaration)element).var)).name).equals("<UNDEF>"))
                  { JRadioButton but = getButton(g, panel_Center,((Variable)(((Declaration)element).var)).name);
                    numBut += 1;
                    if (numBut == 1){ but.setSelected(true); selectedNum = 1;}
                  }
            };
            if (numBut != 0 ) add(panel_Center, BorderLayout.CENTER);                
        };
        
        add(panel_South, BorderLayout.SOUTH);

        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);            
        }
        
         private JRadioButton getButton(ButtonGroup g, JPanel p, String str){
         JRadioButton button = new JRadioButton(str);
         g.add(button);
         p.add(button);
         button.addActionListener(this);
         return button;
       }
            
       private JButton getJButton(String s){
	    JButton button = new JButton(s);
          button.setFont(new Font("Monospaced", Font.PLAIN, 12));
          button.addActionListener(this);
          return button;
       }

     public int get_numBut(){ return numBut;}
       
       public void actionPerformed(ActionEvent e)
       { ExpressionParser.set_Variable(null);
         Variable var = null;
         if ((e.getActionCommand()).equals("OK")){
            try
	    { if ( numBut == 0) throw new ExpressionParserException(6);
              else
		  {  LinkedList  decl_lList = new LinkedList();                  
                     Iterator decl_List = (_sfc.declist).iterator();
                     Object   element = null;                     
                     int numElement = 0;
            
                     while (decl_List.hasNext() & (numElement != selectedNum)){                    	 
                          element = decl_List.next();
                          numElement += 1;
                      };
                     selectedNum = 0;
                     var = ((Variable)((Declaration)element).var);
                     ExpressionParser.set_Variable(var); dispose();
		  }
	    }
            catch (ExpressionParserException _e){dispose();};   	       	         	
	 }   
         else{ if ((e.getActionCommand()).equals("Cancel")) dispose();              
               else { // finde, welches Button gedrueckt war
                      LinkedList  decl_lList = new LinkedList();                  
                      Iterator    decl_List = (_sfc.declist).iterator();
                      Object      element;                     
                      boolean     isFound = false;
                      selectedNum = 1;
                      
                      while (decl_List.hasNext() & !isFound){                    	 
                         element = decl_List.next();               
                         if ( e.getActionCommand() == (((Variable)(((Declaration)element).var)).name))
                             isFound = true;
                         else selectedNum += 1;
                       }
                   }
             }
       }

 }

// --------------------------------------------------------------------------------------------------------------
// Dialog zur Erstellung einer Konstante
  class ConstanteDialog extends Dialog implements ActionListener{
     
     final static public int _INTTYPE  = 0;
     final static public int _BOOLTYPE = 1;
     int selectedType = 0;
     JTextField textField;
     
     Expression_Parser  ExpressionParser;
     
     
     public ConstanteDialog(JDialog parent, String title, Expression_Parser  ExpParser){
         
         super(parent, title, true);
         setLayout(new BorderLayout());
         setSize(300,120);

         ExpressionParser = ExpParser;
         
         textField = new JTextField();
         add(textField,BorderLayout.NORTH);
         
         ButtonGroup g = new ButtonGroup();
         JPanel panel_Center = new JPanel();
         JRadioButton _inttype  = getButton(g, panel_Center,"inttype");
         JRadioButton _booltype = getButton(g, panel_Center,"booltype");
         _inttype.setSelected(true);         
         add(panel_Center, BorderLayout.CENTER);
         
         JPanel panel_South = new JPanel();
         panel_South.add(getJButton("OK"));
         panel_South.add(getJButton("Cancel"));
         add(panel_South, BorderLayout.SOUTH);
         
         Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
         setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);
        
         enableEvents(AWTEvent.MOUSE_EVENT_MASK);
     }         
     
     private JRadioButton getButton(ButtonGroup g, JPanel p, String str){
         JRadioButton button = new JRadioButton(str);
         g.add(button);
         p.add(button);
         button.addActionListener(this);
         return button;
       }
                  
       private JButton getJButton(String s){
	  JButton button = new JButton(s);
          button.setFont(new Font("Monospaced", Font.PLAIN, 12));
          button.addActionListener(this);
          return button;
       }
       
       public void actionPerformed(ActionEvent e)
      { Constval cons = null;
        
        if ((e.getActionCommand()).equals("OK")){
             String str= textField.getText();
             switch (selectedType){
                 case 0 : try
                          { cons = new Constval(Integer.parseInt(str));}
                          catch (NumberFormatException _e){new ExpressionParserException(2);}
                          break;
                 case 1 : try
                          { if (!( str.equals("true") | str.equals("false"))){
                                     throw new ExpressionParserException(1);}
                            else cons = new Constval((Boolean.valueOf(str)).booleanValue());
                          }
                          catch (ExpressionParserException _e){};
                          break;
             };
             ExpressionParser.set_Constante(cons);
             dispose();}
         else if ((e.getActionCommand()).equals("inttype")) { selectedType = _INTTYPE;}
         else if ((e.getActionCommand()).equals("booltype")){ selectedType = _BOOLTYPE;}
         else if ((e.getActionCommand()).equals("Cancel"))
	 { ExpressionParser.set_Constante(null); dispose();}
      }
 }

// --------------------------------------------------------------------------------------------------------------
// Fehlerbehandlung
 class ExpressionParserException extends Exception{
     public ExpressionParserException(int bed){
         String msg = "";
         switch (bed){
             case 1: msg = "The Constante must be TRUE or FALSE !\n";
                     break;
             case 2: msg = "The Constante must be INTEGER ! \n";
                     break;
             case 3: msg = "The Statement is not full !\n";
                     break;
             case 4: msg = "The Variablelist is empty !\n";
                     break;
             case 5: msg = "The SFC is empty !\n";
                     break;
             case 6: msg = "The Variablelist is empty !\n";
                     break;                                                                                  
         };
         (new JOptionPane()).showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
     }
 }

// --------------------------------------------------------------------------------------------------------------
// Bearbeitungspanel fuer den Ausdruck      
 class Expression_Panel extends JPanel{

    private Expression_Parser ExprParser;
    
    public Expression_Panel(Expression_Parser ExpressionParser){
       setBackground(Color.white);
//       setFont(new Font("Monospaced",Font.PLAIN,12));
       setPreferredSize(new Dimension(400,20));
       ExprParser = ExpressionParser;
       enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    public void paint(Graphics g) { 
        Graphics2D G2D = (Graphics2D)g; 
        super.paint(G2D);
        G2D.setColor(Color.white);
        
        if ( ExprParser.get_expr() == null) G2D.setBackground(Color.white);
        else ExprParser.paint_Expression_Panel(ExprParser.get_expr(),g); 
    }
  }

// --------------------------------------------------------------------------------------------------------------
// Beziehungspanel (X=...)
 class AssignVarPanel extends JPanel{

    private Expression_Parser ExprParser;
    
    public AssignVarPanel(Expression_Parser ExpressionParser){
       setBackground(Color.white);
//       setFont(new Font("Monospaced",Font.PLAIN,12));
       setPreferredSize(new Dimension(200,15));
       ExprParser = ExpressionParser;
    }

    public void paint(Graphics g) { 
        Graphics2D G2D = (Graphics2D)g; 
        super.paint(G2D);
        G2D.setColor(Color.white);
        
        if (ExprParser.get_VarAssign() != null){ G2D.setColor(Color.black);
            G2D.drawString(ExprParser.get_VarAssignName(),2,10);
        }
  }
 }

// --------------------------------------------------------------------------------------------------------------
// Hauptklasse, welche alle Steuerobjekte enthaelt
public class Expression_Parser extends JDialog implements ActionListener{

    private Expression_Panel ExpressionPanel;
    private AssignVarPanel   AssVarPanel;
    private int      mouseX, mouseY, index;

    private Expr    _expr;
    private Assign  _assign;                  // Speicherort fuer Statement
    private Expr     predecessor;

    private boolean  root , var , cons;
    private int      x_fort , kk , pred_index;
    private int      y_a ,letter_width ,letter_high;
    
    private  SFC sfc;
    
    private Variable vari, varAssign;
    private Constval consi;
    private boolean  canceled;
    
    public Expression_Parser(JFrame parentFrame, SFC _sfc){
    	
	    super(parentFrame,"  Statement erstellen",true);
	    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        sfc = _sfc;	    	  
//-------------------------------------------------------------------------	  
        Container c = getContentPane();
        c.setLayout(new BorderLayout(2,5));
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(4,5,5,5));

        JButton _Var = getJButton("Var");
        p.add(_Var);  if (sfc == null) _Var.setEnabled(false);
        p.add(getJButton("Const"));
        p.add(getJButton("+"));
        p.add(getJButton("-"));
        p.add(getJButton("*"));
        p.add(getJButton("/"));
        p.add(getJButton("&"));
        p.add(getJButton("|"));
        p.add(getJButton("~"));
        p.add(getJButton("<"));
        p.add(getJButton(">"));
        p.add(getJButton("<="));
        p.add(getJButton(">="));
        p.add(getJButton("="));
        p.add(getJButton("-U"));
        p.add(getJButton("CLEAR"));
        p.add(getJButton("OK"));
        p.add(getJButton("Cancel"));
        p.setPreferredSize(new Dimension(400,150));
        
        c.add(p, BorderLayout.CENTER);
        pack();

        JPanel VarPanel = new JPanel();
        VarPanel.setLayout(new GridLayout(1,2,5,5));        
        
        AssVarPanel = new AssignVarPanel(this);
        VarPanel.add(AssVarPanel);
        VarPanel.add(getJButton("Variable"));
        _assign = null;
        varAssign = null;

        JPanel Panel_North = new JPanel();
        Panel_North.setLayout(new BorderLayout(2,5));
        
        ExpressionPanel = new Expression_Panel(this);
        Panel_North.add(ExpressionPanel, BorderLayout.CENTER);
        Panel_North.add(VarPanel, BorderLayout.NORTH);
        
        c.add(Panel_North, BorderLayout.NORTH);
    
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);
 
        set_Fonts();
        set_Variable();
       }


    private JButton getJButton(String s){
	  JButton button = new JButton(s);
      button.setFont(new Font("Monospaced", Font.PLAIN, 12));
      button.addActionListener(this);
      return button;
       }

// ------------------------------------------ return - Methoden  ------------------------------------------------ 
  
    public Assign   get_assign()       { return _assign;}                // gebe zurueck Assign
    public Variable get_VarAssign()    { return  varAssign;}             // gebe zurueck Variable in Assign
    public Expr     get_expr()         { return _expr;}                  // gebe zurueck Expression in Assign
    public boolean  get_canceled()     { // true - OK, false - Cancel    // bestimme, welches Button gedrueck war
                                         return canceled;}
// --------------------------------------------------------------------------------------------------------------

    public int      get_Index()        { return index;}                  // gebe an, welches Symbol in ParserPanel
                                                                         // gewaehlt ist
    public String   get_VarAssignName(){ return varAssign.name;}         // gebe zurueck VariablenNamen in Assign

// --------------------------------------------------------------------------------------------------------------

      private boolean isSelected(){ return (index != 0);}

      private  void set_Fonts(){
           y_a = 2;
           letter_width = 12;
           letter_high  = 8;
	   }

      private void set_Variable(){
           root     = true;
           var      = false;
           cons     = false;
           canceled = false;
           index    = 0;
	       x_fort   = 2;
           kk       = 0;
           pred_index = 0;
       }
       
      public void set_Variable (Variable v){vari  = v;}
      public void set_Constante(Constval c){consi = c;}
     
// --------------------------------------------------------------------------------------------------------------    
       
    private void setExprTreeRoot(String str)
    { // setze BaumWurzelExpression
      if ((str == "+")|(str == "-")|(str == "*")|(str == "/")|(str == "&")|(str == "|")|(str == "=")
          |(str == "<") |(str == ">")|(str == "<=")|(str == "=>")|(str == "!="))
          _expr = new B_expr(null,op_string(str),null);

       else{ if ((str == "~")|(str == "-U"))   { if (str == "-U") str = "-";_expr = new U_expr(op_string(str),null);}
            else{ if (var)                     {  _expr = vari;  var  = false;}
                  else { if  (cons)            {  _expr = consi; cons = false;}
		  }
            }
      }             
      predecessor = _expr;
      root = false;    
    }

    private void set_Expression(String str) // der ganze Baum
    { if (root){ setExprTreeRoot(str);}
      else{ if (isSelected()){
                predecessor = _expr;
                definePredecessor(_expr);    
       
                if ((str == "+")|(str == "-")|(str == "/")|(str == "*")|(str == "&")|(str == "|")|(str == "=") |
                    (str == "<")|(str == ">")|(str == "<=")|(str == ">=")|(str == "!="))
		    { if (classType(className(predecessor)) == 4){
		        ((U_expr)predecessor).sub_expr = new B_expr(null,op_string(str),null);}
                    
                      else{ if (this.kk < this.pred_index)
                             ((B_expr)predecessor).left_expr = new B_expr(null,op_string(str),null);
                            else
                             ((B_expr)predecessor).right_expr = new B_expr(null,op_string(str),null);
	             }
                   }
                else if ((var == true) |(cons == true))
                 { if (classType(className(predecessor)) == 4){
		               if (var  == true)((U_expr)predecessor).sub_expr = vari;
		               if (cons == true)((U_expr)predecessor).sub_expr = consi;
		           }
		           else{
                      if (this.kk < this.pred_index)
                       { if (var  == true) ((B_expr)predecessor).left_expr = vari;
                         if (cons == true) ((B_expr)predecessor).left_expr = consi;
                       }
                      else
                       { if (var  == true)((B_expr)predecessor).right_expr = vari;
                         if (cons == true)((B_expr)predecessor).right_expr = consi;
                       }
                      }
                    var  = false;
                    cons = false;
                    }
             else if ((str == "~") | (str == "-U"))
                 { if (str == "-U") str = "-";
                   if (classType(className(predecessor)) == 4){
		     ((U_expr)predecessor).sub_expr = new U_expr(op_string(str),null);}

                   else{ if (this.kk < this.pred_index)
                          ((B_expr)predecessor).left_expr = new U_expr(op_string(str),null);
                         else
                          ((B_expr)predecessor).right_expr = new U_expr(op_string(str),null);
	          }
             }
         } 
             };
         index      = 0;
         kk         = 0;
         pred_index = 0;              
         predecessor = _expr;
    }

// --------------------------------------------------------------------------------------------------------------
// Wandelt Expression in Linked List
    private void expression_to_linkedList(Expr _expr, LinkedList expr_lList)
    { if (_expr == null){ expr_lList.addLast(""); }
      else{ switch (classType(className(_expr))) 
            {  case 1 : if (((Constval)_expr).val instanceof Integer)     
                        { expr_lList.addLast((((Constval)_expr).val).toString());}; 
                        if (((Constval)_expr).val instanceof Boolean)     
                        { expr_lList.addLast((((Constval)_expr).val).toString()); };
                        break;                      
              case  2 : expr_lList.addLast((((Variable)_expr).name).toString());
                        break;      
              case  3 : expr_lList.addLast("(");
                        expression_to_linkedList(((B_expr)_expr).left_expr,expr_lList);
                        expr_lList.addLast(op_add(((B_expr)_expr).op));
                        expression_to_linkedList(((B_expr)_expr).right_expr,expr_lList);                         
                        expr_lList.addLast(")");
                        break;
              case  4 : expr_lList.addLast(op_add(((U_expr)_expr).op));
                        expression_to_linkedList(((U_expr)_expr).sub_expr,expr_lList);
                        break;
      } // end-of-switch*/
      }
    }
    
    private void selectedElement_inLinkedList(){
       LinkedList       expr_lList      = new LinkedList();
       LinkedList       expr_drawObject = new LinkedList();        
       expression_to_linkedList(_expr,expr_lList);              
       drawObject_List(expr_lList,expr_drawObject,letter_width);
       
       Iterator   expr_List = expr_drawObject.iterator();
       Object     element;
       boolean    isFound = false;
       int        i = 0;

       if ((mouseY >= y_a) & (mouseY <= y_a + letter_high)){
           while (expr_List.hasNext() & (!isFound)){
               element = expr_List.next();
               if ((((((drawObject)element).get_Content()).equals(String.valueOf('('))) | 
                   ((((drawObject)element).get_Content()).equals(String.valueOf(')')))) != true)
               { i += 1;
                 if ( (((drawObject)element).get_Content() == "" ) &
                      (((drawObject)element).get_x()                   <= mouseX ) &
                      (( ((drawObject)element).get_x() + letter_width) >= mouseX) 
                    )
                {isFound = true; index = i;}
               }
           }// end-of-while
        }
      }

      private void definePredecessor(Expr _exp){
         if ((_exp != null) & (this.kk != this.index)){
           switch (classType(className(_exp)))
           { case 1 : if (this.kk != this.index){ this.kk += 1; this.pred_index += 1;}; break;
             case 2 : if (this.kk != this.index){ this.kk += 1; this.pred_index += 1;}; break;
             case 3 : if ((((B_expr)_exp).left_expr) != null) { definePredecessor(((B_expr)_exp).left_expr);}
                      else { if (this.kk != this.index){ this.pred_index += 1; this.kk += 1; 
                             if (this.kk == this.index){ this.pred_index += 1;this.predecessor = _exp;}} };
                                           
                      if (this.kk != this.index){ this.kk += 1; this.pred_index += 1; this.predecessor = _exp;
                                         if (this.kk == this.index) this.pred_index += 1;
                      };                     
                       
                      if ((((B_expr)_exp).right_expr) != null) {definePredecessor(((B_expr)_exp).right_expr);}
                      else { if (this.kk != this.index) {
                             if ((this.kk+1) == this.index)this.kk += 1;
                             if (this.kk != this.index) {this.pred_index += 1; this.kk += 1;}}                      
                      };
                     break;
	   case 4 :  if (((U_expr)_exp).sub_expr == null) this.kk += 1;
                     this.index -=1; this.predecessor = _exp;
                     if ((((U_expr)_exp).sub_expr) != null) { definePredecessor(((U_expr)_exp).sub_expr); };
                     break;
	   }
       } // end-of-if
      }

// --------------------------------------------------------------------------------------------------------------

    private int op_string(String str)
    {  int op = 0;
       if      (str == "+")  op = Expr.PLUS;
       else if (str == "-")  op = Expr.MINUS;
       else if (str == "*")  op = Expr.TIMES;
       else if (str == "/")  op = Expr.DIV;
       else if (str == "&")  op = Expr.AND;
       else if (str == "|")  op = Expr.OR;
       else if (str == "~")  op = Expr.NEG;
       else if (str == "=")  op = Expr.EQ;
       else if (str == "<")  op = Expr.LESS;
       else if (str == ">")  op = Expr.GREATER;
       else if (str == "<=") op = Expr.LEQ;
       else if (str == ">=") op = Expr.GEQ;
       else if (str == "!=") op = Expr.NEQ;
       return op;
    }

     private  String op_add(int op)
    {   String str = "";
        switch(op){
          case  0 : str = "+";  break;
          case  1 : str = "-";  break;
          case  2 : str = "*";  break;
          case  3 : str = "/";  break;
          case  4 : str = "&";  break;
          case  5 : str = "|";  break;
          case  6 : str = "~";  break;
          case  7 : str = "=";  break;
          case  8 : str = "<";  break;
          case  9 : str = ">";  break;
          case 10 : str = "<="; break;
          case 11 : str = ">="; break;
          case 12 : str = "!="; break;
      };
      return str;
    }
    
    private String className(Expr _expr)
    { return _expr.getClass().getName();} // bestimme KlassenNamen

    private int classType(String className)
    { int int_className = 0;
      if      (className == "absynt.Constval") int_className = 1;
      else if (className == "absynt.Variable") int_className = 2;
      else if (className == "absynt.B_expr")   int_className = 3;
      else if (className == "absynt.U_expr")   int_className = 4;

      return int_className;
     }

// --------------------------------------------------------------------------------------------------------------
// Erstelle DrawObjektList von Linked List 
    private void drawObject_List(LinkedList expr_lList, LinkedList expr_drawObject,int width)
    { Iterator exp_List = expr_lList.iterator();
      Object   element;
      int      x_fort = 2;
      
      while (exp_List.hasNext()){
            element = exp_List.next();
             expr_drawObject.addLast(new drawObject(element.toString(),x_fort));
             if ((element.toString()).length() == 0) x_fort += width;
             else  x_fort += ((element.toString()).length()) * width;
       }
    }
// --------------------------------------------------------------------------------------------------------------

    public void paint_Expression_Panel(Expr _expr, Graphics expPan){

/**    Gebe Expression in GraphicsObject expPan aus.  */
  	
       Graphics2D G2D = (Graphics2D)expPan;
       super.paint(G2D);
        
       LinkedList    expr_lList      = new LinkedList();
       LinkedList    expr_drawObject = new LinkedList();        
             
       Object   element;
       int      i  = 0;
       int      bright = 12 ,high = 8;       

       expression_to_linkedList(_expr,expr_lList);              
       drawObject_List(expr_lList,expr_drawObject,bright);
       Iterator expr_List = expr_drawObject.iterator();       
       
       while (expr_List.hasNext()){
          element = expr_List.next();
          if ((((((drawObject)element).get_Content()).equals(String.valueOf('('))) | 
               ((((drawObject)element).get_Content()).equals(String.valueOf(')')))) != true)  i += 1;          
          if (((drawObject)element).get_Content() == "" )
            { if ((index != 0) & (i == index)) {G2D.setColor(Color.gray);}
              else G2D.setColor(Color.cyan);
              G2D.fill3DRect(((drawObject)element).get_x(), y_a,bright-2,y_a+high,true);
            }
            else
            { G2D.setColor(Color.black);
              G2D.drawString(((drawObject)element).get_Content(),((drawObject)element).get_x(),y_a+letter_high);
            }
        }
    }

    public void paint_Statement(Assign _ass, Graphics expPan){
       Graphics2D G2D = (Graphics2D)expPan;
       super.paint(G2D);
        
       LinkedList    expr_lList      = new LinkedList();
       LinkedList    expr_drawObject = new LinkedList();        
             
       Object   element;
       int      i  = 0;
       int      y_a = 2, bright = 12 ,high = 8;
       Expr     _expr = _ass.val;

       expression_to_linkedList(_expr,expr_lList);              
       drawObject_List(expr_lList,expr_drawObject,bright);
       Iterator expr_List = expr_drawObject.iterator();       
       

       G2D.drawString(_ass.var.name,2,y_a+letter_high);
       G2D.drawString("=",16,y_a+letter_high);

       while (expr_List.hasNext()){
          element = expr_List.next();
          if ((((((drawObject)element).get_Content()).equals(String.valueOf('('))) | 
               ((((drawObject)element).get_Content()).equals(String.valueOf(')')))) != true)  i += 1;          
          if (((drawObject)element).get_Content() == "" )
            { if ((index != 0) & (i == index)) {G2D.setColor(Color.gray);}
              else G2D.setColor(Color.cyan);
              G2D.fill3DRect(((drawObject)element).get_x()+26, y_a,bright-2,y_a+high,true);
            }
            else
            { G2D.setColor(Color.black);
              G2D.drawString(((drawObject)element).get_Content(),((drawObject)element).get_x()+26,y_a+letter_high);
            }
       }
    }

    
// --------------------------------------------------------------------------------------------------------------

    private class MouseEventHandleAdapter extends MouseInputAdapter{
        public void mousePressed(MouseEvent e){
             mouseX = e.getX();
             mouseY = e.getY();
                
             selectedElement_inLinkedList();                
             if (isSelected()){ ExpressionPanel.repaint();}
        }
    }
                    
    private void setExpressionPanelAction(){
        ExpressionPanel.addMouseListener(new MouseEventHandleAdapter());
    }	

    public void show(){
        super.show();
       _expr = null; root = true; varAssign = null;
     }


    public void actionPerformed(ActionEvent e){
         String command = ((JButton)e.getSource()).getText();
         setExpressionPanelAction();

         if ((command.equals("Var")) & (root | isSelected()))
            {    try{ if(sfc != null){
	                if (!((sfc.declist).isEmpty())) new VariableDialog(this, "Variable", this, sfc).show();
                         else throw new ExpressionParserException(4);}
                      else throw new ExpressionParserException(5);
                 }
                 catch (ExpressionParserException _e){};
                 if (vari != null){var = true; set_Expression(""); ExpressionPanel.repaint();}
            }
	   else if ((command.equals("Const")) & (root | isSelected()))
                  { new ConstanteDialog(this, "new Constante", this).show();
                  if (consi != null){cons = true; set_Expression("");  ExpressionPanel.repaint();}}
	   else if (command.equals("+")) { set_Expression("+");  ExpressionPanel.repaint();}
           else if (command.equals("-")) { set_Expression("-");  ExpressionPanel.repaint();}
           else if (command.equals("*")) { set_Expression("*");  ExpressionPanel.repaint();}
           else if (command.equals("/")) { set_Expression("/");  ExpressionPanel.repaint();}
	   else if (command.equals("&")) { set_Expression("&");  ExpressionPanel.repaint();}
           else if (command.equals("|")) { set_Expression("|");  ExpressionPanel.repaint();}
           else if (command.equals("~")) { set_Expression("~");  ExpressionPanel.repaint();}
           else if (command.equals("=")) { set_Expression("=");  ExpressionPanel.repaint();}
	   else if (command.equals("<")) { set_Expression("<");  ExpressionPanel.repaint();}
           else if (command.equals(">")) { set_Expression(">");  ExpressionPanel.repaint();}
           else if (command.equals("<=")){ set_Expression("<="); ExpressionPanel.repaint();}
           else if (command.equals(">=")){ set_Expression(">="); ExpressionPanel.repaint();}
           else if (command.equals("!=")){ set_Expression("!="); ExpressionPanel.repaint();}
           else if (command.equals("-U")){ set_Expression("-U"); ExpressionPanel.repaint();}
           else if (command.equals("CLEAR")){ _expr = null; root = true; varAssign = null;
                                              ExpressionPanel.repaint(); AssVarPanel.repaint();}
           else if (command.equals("Variable")){
                        try{ if(sfc != null){
           	               if (!(sfc.declist.isEmpty())) new VariableDialog(this, "Variable", this, sfc).show();
                               else throw new ExpressionParserException(4);}
                              else throw new ExpressionParserException(5);
                        }
                        catch (ExpressionParserException _e){};
                        if (vari != null){ varAssign = vari; AssVarPanel.repaint();}
                    }
           else if (command.equals("OK")){
           	        try {
           	              if ((varAssign == null) | (_expr == null)) throw new ExpressionParserException(3);
           	              else {_assign = new Assign(varAssign,_expr); 
           	                     canceled = false; hide();                   
                                 dispose();
                            }
                         }
                         catch (ExpressionParserException _e){};
                  }
           else if (command.equals("Cancel"))
                {  canceled = true; hide();
           	  _expr = null; _assign = null; varAssign = null; dispose();
                 }
	 }
      }
