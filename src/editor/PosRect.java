package editor;

import editor.*;
import java.awt.geom.Rectangle2D;

public class PosRect implements java.io.Serializable
{
  boolean boolEmpty = false;
	double x1=0.0, y1=0.0, x2=0.0, y2=0.0;
	
	public PosRect() { boolEmpty=true; }
	public PosRect(Rectangle2D rect) {
	 x1=rect.getMinX();  y1=rect.getMinY();
	 x2=rect.getMaxX();  y2=rect.getMaxY();	 
  }
  public PosRect(double _x1, double _y1, double _x2, double _y2) {
    x1=_x1;  y1=_y1;  x2=_x2;  y2=_y2; 
  }
  public PosRect(PosRect rect) {
	 x1=rect.getMinX();  y1=rect.getMinY();
	 x2=rect.getMaxX();  y2=rect.getMaxY();	 
  }
    
  public void setLocation(double x, double y) {
    double w=getWidth(), h=getHeight();
    x1=x;  y1=y;  x2=x + w;  y2=y + h;
  }
  public void setRectWH(double x, double y, double width, double height) {
    x1=x;  y1=y;  x2=x+width;  y2=y+height;
  }
  public void setRect(PosRect rect) {
    x1=rect.getMinX();  y1=rect.getMinY();  x2=rect.getMaxX();  y2=rect.getMaxY();
  }  
  public void setRect2D(Rectangle2D rect) {
    x1=rect.getMinX();  y1=rect.getMinY();  x2=rect.getMaxX();  y2=rect.getMaxY();
  }
  public void setWidth(double width) {
    x2 = x1 + width;
  }
  
  public double getMinX() { return(Math.min(x1, x2)); }
  public double getMinY() { return(Math.min(y1, y2)); }
  public double getMaxX() { return(Math.max(x1, x2)); }
  public double getMaxY() { return(Math.max(y1, y2)); }
  public double getWidth() { return(getMaxX() - getMinX()); }
  public double getHeight() { return(getMaxY() - getMinY()); }
  public double getMidX() { return( getMinX() + (getWidth()/2) ); }
  public double getMidY() { return( getMinY() + (getHeight()/2) ); }
  
  public float getMinXAsFloat() { return(new Double(Math.min(x1, x2)).floatValue()); }
  public float getMinYAsFloat() { return(new Double(Math.min(y1, y2)).floatValue()); }
  public float getMaxXAsFloat() { return(new Double(Math.max(x1, x2)).floatValue()); }
  public float getMaxYAsFloat() { return(new Double(Math.max(y1, y2)).floatValue()); }
  public float getWidthAsFloat() { return(new Double(getMaxX() - getMinX()).floatValue()); }
  public float getHeightAsFloat() { return(new Double(getMaxY() - getMinY()).floatValue()); }
  public float getMidXAsFloat() { return( new Double(getMidX()).floatValue() ); }
  public float getMidYAsFloat() { return( new Double(getMidY()).floatValue() ); }
  
  public int getMinXAsInt() { return(new Double(Math.min(x1, x2)).intValue()); }
  public int getMinYAsInt() { return(new Double(Math.min(y1, y2)).intValue()); }
  public int getMaxXAsInt() { return(new Double(Math.max(x1, x2)).intValue()); }
  public int getMaxYAsInt() { return(new Double(Math.max(y1, y2)).intValue()); }
  public int getWidthAsInt() { return(new Double(getMaxX() - getMinX()).intValue()); }
  public int getHeightAsInt() { return(new Double(getMaxY() - getMinY()).intValue()); }
  public double getMidXAsInt() { return( new Double(getMidX()).intValue() ); }
  public double getMidYAsInt() { return( new Double(getMidY()).intValue() ); }
  
  public boolean isEmpty() { return(boolEmpty); }
  public boolean contains(double x, double y) {
    return (x>=getMinX() && x<=getMaxX() && y>=getMinY() && y<=getMaxY());
  }
  public boolean contains(double x, double y, double width, double height) {
    return (x>=getMinX() && x+width<=getMaxX() && y>=getMinY() && y+height<=getMaxY());
  }
    
  public void unionRect(PosRect rect) {
    if (isEmpty()) {
      x1=rect.getMinX();  y1=rect.getMinY();  x2=rect.getMaxX();  y2=rect.getMaxY();
      boolEmpty=false;
    } else {
      double a, b, c, d;
      a=Math.min(getMinX(), rect.getMinX());  b=Math.min(getMinY(), rect.getMinY());  
      c=Math.max(getMaxX(), rect.getMaxX());  d=Math.max(getMaxY(), rect.getMaxY()); 
      x1=a;  y1=b;  x2=c;  y2=d;
    }
  }
  
  public Rectangle2D.Double createRect2D() {
    return(new Rectangle2D.Double(x1, y1, x2-x1, y2-y1));
  }
  
  
}
