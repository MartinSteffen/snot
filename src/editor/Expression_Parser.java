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

  class drawObject{
    private String content;
    private int    x;
    
    public drawObject(String cont, int _x){
        content = cont;
        x       = _x;
    }
    
    public String get_Content(){return content;}
    public int    get_x(){return x;}   
 }

 class VariableDialog extends Dialog implements ActionListener{
     
     final static public int _NULL     = 0;
     final static public int _INTTYPE  = 1;
     final static public int _BOOLTYPE = 2;
     
     int selectedType = 0;
     JTextField textField;
     
     Expression_Parser  ExpressionParser;
     
     public void set_selected(int sel){ selectedType = sel;}
     
     public VariableDialog(JFrame parent, String title, Expression_Parser  ExpParser){
         
         super(parent, title, true);
         setLayout(new BorderLayout());
         setSize(300,120);

         ExpressionParser = ExpParser;
         
         textField = new JTextField();
         add(textField,BorderLayout.NORTH);
         
         ButtonGroup g = new ButtonGroup();
         JPanel panel_Center = new JPanel();
         JRadioButton _null     = getButton(g, panel_Center,_NULL);
         _null.setSelected(true);
         JRadioButton _inttype  = getButton(g, panel_Center,_INTTYPE);
         JRadioButton _booltype = getButton(g, panel_Center,_BOOLTYPE);         
         add(panel_Center, BorderLayout.CENTER);
         
         JPanel panel_South = new JPanel();
         panel_South.add(getJButton("OK"));
         panel_South.add(getJButton("Cancel"));
         add(panel_South, BorderLayout.SOUTH);
         
         Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
         setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);
        
         enableEvents(AWTEvent.MOUSE_EVENT_MASK);
     }         
     
     private JRadioButton getButton(ButtonGroup g, JPanel p, int select){
         JRadioButton button = null;
         switch (select){
             case 0: button = new JRadioButton(new _NULL_ActButtonSelect());     break;
             case 1: button = new JRadioButton(new _INTTYPE_ActButtonSelect());  break;
             case 2: button = new JRadioButton(new _BOOLTYPE_ActButtonSelect()); break;
         };
         g.add(button);
         p.add(button);
         return button;
       }
                  
       private JButton getJButton(String s){
	  JButton button = new JButton(s);
          button.setFont(new Font("Monospaced", Font.PLAIN, 12));
          button.addActionListener(this);
          return button;
       }
       
       public void actionPerformed(ActionEvent e)
       { String command = ((JButton)e.getSource()).getText();
         Variable var = null;
         if (command.equals("OK")){
             String str= textField.getText();
             switch (selectedType){
                 case 0 : var = new Variable(str);                break;
                 case 1 : var = new Variable(str,new IntType());  break;
                 case 2 : var = new Variable(str,new BoolType()); break;
             };
             ExpressionParser.set_Variable(var);
            System.out.println("text = " + str); 
             dispose();}
         else if (command.equals("Cancel")){ ExpressionParser.set_Variable(null); dispose();}
       }

       private MouseAdapter  ButtonSelect;
       
       private void setVariableDialogButton(int newSelectButton)
       { removeMouseListener(ButtonSelect);
         switch (newSelectButton){
           case _NULL     : ButtonSelect = new Select_NULL(this);    break;
           case _INTTYPE  : ButtonSelect = new Select_INTTYPE(this);  break;
           case _BOOLTYPE : ButtonSelect = new Select_BOOLTYPE(this); break;
       };
       addMouseListener(ButtonSelect);
       }
       
       private class Select_NULL extends MouseAdapter{
           VariableDialog _VD;
           public Select_NULL(VariableDialog vd){_VD = vd;_VD.set_selected(0);}           
           public void mousePressed(MouseEvent e){ // _VD.set_selected(0);
           }
           public void mouseDragged(MouseEvent e){}
           public void mouseReleased(MouseEvent e){}
       }
       private class Select_INTTYPE extends MouseAdapter{
           VariableDialog _VD;
           public Select_INTTYPE(VariableDialog vd){_VD = vd;_VD.set_selected(1);}           
           public void mousePressed(MouseEvent e){ //_VD.set_selected(1);
           }
           public void mouseDragged(MouseEvent e){}
           public void mouseReleased(MouseEvent e){}
       }
       private class Select_BOOLTYPE extends MouseAdapter{
           VariableDialog _VD;
           public Select_BOOLTYPE(VariableDialog vd){_VD = vd;_VD.set_selected(2);}           
           public void mousePressed(MouseEvent e){ // _VD.set_selected(2);
           }
           public void mouseDragged(MouseEvent e){}
           public void mouseReleased(MouseEvent e){}
       }
           
       private class _NULL_ActButtonSelect extends AbstractAction{
           public _NULL_ActButtonSelect(){super("null");}           
           public void actionPerformed(ActionEvent e){setVariableDialogButton(_NULL);}
       }
       
       private class _INTTYPE_ActButtonSelect extends AbstractAction{
           public _INTTYPE_ActButtonSelect(){super("inttype");}           
           public void actionPerformed(ActionEvent e){setVariableDialogButton(_INTTYPE);}
       }
       
       private class _BOOLTYPE_ActButtonSelect extends AbstractAction{
           public _BOOLTYPE_ActButtonSelect(){super("booltype");}           
           public void actionPerformed(ActionEvent e){setVariableDialogButton(_BOOLTYPE);}
       }
 }
 
  class ConstanteDialog extends Dialog implements ActionListener{
     
     final static public int _INTTYPE = 0;
     final static public int _BOOLTYPE = 1;
     int selectedType = 0;
     JTextField textField;
     
     Expression_Parser  ExpressionParser;
     
     
     public ConstanteDialog(JFrame parent, String title, Expression_Parser  ExpParser){
         
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
        System.out.println(e.getActionCommand());
	  if ((e.getActionCommand()).equals("OK")){
             String str= textField.getText(); System.out.println(str);
             switch (selectedType){
                 case 0 : cons = new Constval(Integer.parseInt(str));  break;
                 case 1 : cons = new Constval((Boolean.valueOf(str)).booleanValue()); break;

             };
             ExpressionParser.set_Constante(cons);
             dispose();}
         else if ((e.getActionCommand()).equals("inttype")) { selectedType = _INTTYPE;}
         else if ((e.getActionCommand()).equals("booltype")){ selectedType = _BOOLTYPE;}
         else if ((e.getActionCommand()).equals("Cancel"))
	 { ExpressionParser.set_Constante(null); dispose();}
      }
 }
 
 class Expression_Panel extends JPanel{

    private Expression_Parser ExprParser;

    public Expression_Panel(Expression_Parser ExpressionParser){
       setSize(100,100);
       ExprParser = ExpressionParser;
       enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    public void paint(Graphics g) { 
        Graphics2D G2D = (Graphics2D)g; 
        super.paint(G2D);
        
        if ( ExprParser.get_expr() == null) G2D.setBackground(Color.white);
        else ExprParser.paint_Expression_Panel(ExprParser.get_expr(),g); 
    }
  }

public class Expression_Parser extends JFrame implements ActionListener{

    private Expression_Panel ExpressionPanel = new Expression_Panel(this);
    private int      mouseX, mouseY, index;

    private Expr    _expr;
    private Expr     predecessor;

    private boolean  root , var , cons;
    private int      x_fort , kk , pred_index;   
    private int      y_a ,letter_width ,letter_high;

    
    Variable         vari;
    Constval         consi;
    public boolean canceled=false;
    
    public Expression_Parser(){
	super("Expression erstellen/editieren");
	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new BorderLayout(2,5));
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(4,5,5,5));

        p.add(getJButton("Var"));
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
        p.add(getJButton("->"));
        p.add(getJButton("<-"));
        p.add(getJButton("-U"));
        p.add(getJButton("CLEAR"));
        p.add(getJButton("OK"));
        p.add(getJButton("Cancel"));        

        c.add(p, BorderLayout.CENTER);
        pack();

        ExpressionPanel.setBackground(Color.white);
        ExpressionPanel.setFont(new Font("Monospaced",Font.PLAIN,16));
      //  ExpressionPanel.setpreferedSize(100,100);
        c.add(ExpressionPanel, BorderLayout.NORTH);      
    
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);
 
        set_Fonts();
        set_Variable();
       }

       private  void set_Fonts(){
           y_a = 2;
           letter_width = 12;
           letter_high  = 8;
       }
       private void set_Variable(){
           root   = true;
           var    = false;
           cons   = false;
           index  = 0;
           x_fort = 2;
           kk     = 0;
           pred_index = 0;
       }
       
       public void set_Variable(Variable v){vari = v;}
       public void set_Constante(Constval c){consi = c;}       
           
       private JButton getJButton(String s){
	  JButton button = new JButton(s);
          button.setFont(new Font("Monospaced", Font.PLAIN, 12));
          button.addActionListener(this);
          return button;
       }

    
    public int  get_Index()           { return index;}
    public Expr get_expr()            { return _expr;}
    public int  get_Font_width()      { return letter_width;}
    public int  get_Font_high()       { return letter_high;}
       
    private void setExprTreeRoot(String str)
    { if ((str == "+")|(str == "-")|(str == "*")|(str == "/")|(str == "&")|(str == "|")|(str == "=")
          |(str == "<") |(str == ">")|(str == "<=")|(str == "=>")|(str == "!="))
          _expr = new B_expr(null,op_string(str),null);

       else{ if ((str == "~")|(str == "-U"))   { _expr = new U_expr(op_string(str),null);}
            else{ if (var)                     {  _expr = vari;  var = false;}
                  else { if  (cons)            {  _expr = consi; cons = false;}
		  }
            }
      }        
      
      predecessor = _expr;
      root = false;    
    }

    private int op_string(String str)
    {  int op = 0;
       if (str == "+") op = Expr.PLUS;
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
    
    private void set_Expression(String str)
    { if (root){ setExprTreeRoot(str);}
      else{ if (isSelected()){ System.out.println("isSelected()");
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
                 { if (this.kk < this.pred_index)
                    { if (var  == true) ((B_expr)predecessor).left_expr = vari;
                      if (cons == true) ((B_expr)predecessor).left_expr = consi;
                    }
                   else
                    { 
                      if (var  == true)((B_expr)predecessor).right_expr = vari;
                      if (cons == true)((B_expr)predecessor).right_expr = consi;
                    }
                    var  = false;
                    cons = false;
                    }
             else if ((str == "~") | (str == "-U"))
                 { if (classType(className(predecessor)) == 4){
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

    private String className(Expr _expr)
    { return _expr.getClass().getName();}

    private int classType(String className)
    { int int_className = 0;
      if      (className == "absynt.Constval") int_className = 1;
      else if (className == "absynt.Variable") int_className = 2;
      else if (className == "absynt.B_expr")   int_className = 3;
      else if (className == "absynt.U_expr")   int_className = 4;

      return int_className;
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
    
    private void drawObject_List(LinkedList expr_lList, LinkedList expr_drawObject)
    { Iterator exp_List = expr_lList.iterator();
      Object   element;
      
      while (exp_List.hasNext()){
            element = exp_List.next();
            //if ((element.toString() != "(") | (element.toString() != ")"))
             expr_drawObject.addLast(new drawObject(element.toString(),x_fort));
             if ((element.toString()).length() == 0) x_fort += letter_width;
             else  x_fort += ((element.toString()).length()) * letter_width;
       };
       x_fort = 2;
    }
        
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
       drawObject_List(expr_lList,expr_drawObject);
       
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

      private boolean isSelected(){ return (index != 0);}
      
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


    public void paint_Expression_Panel(Expr _expr,Graphics expPan){
        Graphics2D G2D = (Graphics2D)expPan;
        super.paint(G2D);
        
       LinkedList       expr_lList      = new LinkedList();
       LinkedList       expr_drawObject = new LinkedList();        
       expression_to_linkedList(_expr,expr_lList);              
       drawObject_List(expr_lList,expr_drawObject);
             
       Iterator expr_List = expr_drawObject.iterator();
       Object   element;
       int      i  = 0;
       int      y_a = 2 ,letter_bright = 8 ,letter_high = 8;          

       while (expr_List.hasNext()){
          element = expr_List.next();
          if ((((((drawObject)element).get_Content()).equals(String.valueOf('('))) | 
               ((((drawObject)element).get_Content()).equals(String.valueOf(')')))) != true)  i += 1;          
          if (((drawObject)element).get_Content() == "" )
            { if ((index != 0) & (i == index)) {G2D.setColor(Color.gray);}
              else G2D.setColor(Color.cyan);
              G2D.fill3DRect(((drawObject)element).get_x(), y_a,letter_bright-2,y_a+letter_high,true);
            }
            else
            { G2D.setColor(Color.black);
              G2D.drawString(((drawObject)element).get_Content(),((drawObject)element).get_x(),y_a+letter_high);
            }
        }
    }   
	
    public static void main(String[] args){
            new Expression_Parser().show();
        }	

      public void actionPerformed(ActionEvent e){
         String command = ((JButton)e.getSource()).getText();
         setExpressionPanelAction();
           
	 if ((command.equals("Var")) & (root | isSelected())){ 
              new VariableDialog(this, "new Variable", this).show();
              if (vari != null){var = true; set_Expression(""); ExpressionPanel.repaint();
              }
           }
         else if ((command.equals("Const")) & (root | isSelected())){
              new ConstanteDialog(this, "new Constante", this).show();
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
           else if (command.equals("CLEAR")){ _expr = null; root = true; ExpressionPanel.repaint();}
           else if (command.equals("OK")){canceled=false;hide();}
           else if (command.equals("Cancel")){canceled=true;hide();}
      }
}
