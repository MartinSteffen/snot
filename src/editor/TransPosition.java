package editor;
import absynt.*;
import java.awt.geom.*;
import editor.TransAlignInfo;

class TransPosition extends Position implements java.io.Serializable {
	public boolean autoAlign = true;
	public TransAlignInfo transAlignInfo = null;	
	
	public TransPosition(TransAlignInfo newTransAlignInfo) {
		super();
		transAlignInfo = newTransAlignInfo;
	}
}