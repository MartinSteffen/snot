package editor;
import absynt.*;
import java.awt.geom.*;
import editor.TransAlignInfo;

class TransPosition extends Position {
	public boolean autoAlign = true;
	public TransAlignInfo transAlignInfo = null;	
	
	public TransPosition(TransAlignInfo newTransAlignInfo) {
		super();
		transAlignInfo = newTransAlignInfo;
	}
}