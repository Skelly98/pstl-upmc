package com.puck.utilities.piccolo2d;




import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;

import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.event.PInputEventFilter;
import org.piccolo2d.extras.pswing.PSwingCanvas;

import com.puck.arrows.ArrowNodesHolder;
import com.puck.arrows.Parrow;
import com.puck.menu.CreateISAEdgesBy;
import com.puck.menu.CreateISAEdgesOf;
import com.puck.menu.CreateUsesEdgesBy;
import com.puck.menu.CreateUsesEdgesOf;
import com.puck.menu.Menu;
import com.puck.menu.RemoveExtendsEdgesOf;
import com.puck.menu.RemoveUsesEdgesOf;
import com.puck.nodes.piccolo2d.PiccoloCustomNode;

public class PCustomInputEventHandler extends PBasicInputEventHandler {
	private PiccoloCustomNode pnode;
	private PiccoloCustomNode root;
	private PSwingCanvas canvas;
	private HashMap<String, PiccoloCustomNode> allPNodes;
	private Menu menu;
	private JMenuItem createUsesEdgesOf;
	private JMenuItem createExtendsEdgesOf;
	private JMenuItem removeUsesEdgesOf;
	private JMenuItem removeExtendsEdgesOf;
	private JMenuItem createISAEdgesBy;
	private JMenuItem createUsesEdgesBy;

	private ArrowNodesHolder ANH;

	public PCustomInputEventHandler(PiccoloCustomNode pnode, PiccoloCustomNode root, PSwingCanvas canvas,
			Map<String, PiccoloCustomNode> allPNodes, Menu menu, ArrowNodesHolder ANH) {
		setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK & InputEvent.BUTTON2_MASK));
		this.pnode = pnode;
		this.canvas = canvas;
		this.root = root;
		this.allPNodes = new HashMap<>(allPNodes);
		this.menu = menu;
		this.ANH = ANH;
		createUsesEdgesOf = new CreateUsesEdgesOf(pnode, canvas, this.allPNodes, menu, ANH);
		createExtendsEdgesOf = new CreateISAEdgesOf(pnode, canvas, this.allPNodes, menu,ANH);
		removeUsesEdgesOf = new RemoveUsesEdgesOf(pnode, canvas, this.allPNodes, menu,ANH);
		removeExtendsEdgesOf = new RemoveExtendsEdgesOf(pnode, canvas, this.allPNodes, menu,ANH);
		createISAEdgesBy = new CreateISAEdgesBy(pnode, canvas, this.allPNodes, menu,ANH);
		createUsesEdgesBy = new CreateUsesEdgesBy(pnode, canvas, this.allPNodes, menu,ANH);
	}

	public PCustomInputEventHandler(PiccoloCustomNode pnode) {
		setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK & InputEvent.BUTTON2_MASK));
		this.pnode = pnode;

	}

	@Override
	public void mousePressed(PInputEvent aEvent) {

		try {
			if (aEvent.isLeftMouseButton()) {
				pnode.toggleChildren();
				root.setLayout();
				root.updateContentBoundingBoxes(false, canvas);
				for (Parrow arrow : ANH.getVisibleArrows()) {
					//System.out.println((PiccoloCustomNode) arrow.getTo());
					ANH.updatePosition(arrow);
				}
//				ANH.clearCounters();
//				for (Parrow ar : ANH.getVisibleArrows())
//					if (ar instanceof ParrowDottedFat)
//						ANH.updateCount((ParrowDottedFat) ar);
				//ANH.hide_show_arrows(pnode);
			}
			if (aEvent.isRightMouseButton()) {
				// menu.setVisible(true);
				generateMenu(menu,aEvent);
				canvas.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						// System.out.println((menu.getP().getGlobalFullBounds().contains(e.getPoint())));
						if (!menu.getP().getGlobalFullBounds().contains(e.getPoint()))
							menu.hideMenu();
					}
				});

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void generateMenu(Menu menu,PInputEvent aEvent) {
		menu.removeAll();
		menu.add(createUsesEdgesOf);
		menu.add(createExtendsEdgesOf);
		menu.addSeparator();
		menu.add(removeUsesEdgesOf);
		menu.add(removeExtendsEdgesOf);
		menu.addSeparator();
		menu.add(createISAEdgesBy);
		menu.add(createUsesEdgesBy);
		menu.setPoint(aEvent.getPosition());
		menu.setCanvas(canvas);
		menu.drawMenu();
	}
}