package puck.gui.item.arrow;

import java.awt.Color;

import org.piccolo2d.PNode;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;

public class TriangleHollow extends PNode{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TriangleHollow(){
        PPath t = new PPath.Float();
        t.moveTo(0,0);
        t.lineTo(-10,10);
        t.moveTo(0,0);
        t.lineTo(10,10);
        this.setBounds(0,0,0,0);
        addChild(t);
    }
}
