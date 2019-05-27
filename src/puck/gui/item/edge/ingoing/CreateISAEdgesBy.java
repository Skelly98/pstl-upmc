package puck.gui.item.edge.ingoing;



import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import org.piccolo2d.PNode;
import org.piccolo2d.extras.pswing.PSwingCanvas;

import puck.controleur.conversion.XmlToStructure;
import puck.gui.item.arrow.ArrowNodesHolder;
import puck.modele.*;
import puck.gui.item.arrow.ParrowExtends;
import puck.gui.item.node.PiccoloCustomNode;
import puck.gui.menu.Menu;

public class CreateISAEdgesBy extends JMenuItem {
	private HashMap<String, PiccoloCustomNode> allPNodes;
	private Map<String, Node> listNodes ;
	private PiccoloCustomNode pnode;
	private PSwingCanvas canvas;
	private Menu menu;
	private ArrowNodesHolder ANH;

	public CreateISAEdgesBy(PiccoloCustomNode pnode, PSwingCanvas canvas, HashMap<String, PiccoloCustomNode> allPNodes,
			Menu menu, ArrowNodesHolder ANH, Map<String, Node> listNodes) {
		super("show extends ingoing",new ImageIcon("left-arrow.png"));
		//this.setText();
		this.allPNodes = allPNodes;
		this.pnode = pnode;
		this.canvas = canvas;
		this.menu = menu;
		this.ANH = ANH;
		this.listNodes = listNodes;
		addActionListener();
		System.out.println("ISA");
	}

	public void drawExtendsEdges(PiccoloCustomNode target, PSwingCanvas canvas) {
		// Node node = listNodes.get(target.getidNode());
		// if (node != null && node.getRelation() != null) {

		HashMap<String, Edge> relation = getRelationInGoing();
		for (Entry<String, Edge> edgeEntry : relation.entrySet()) {
			Edge e = edgeEntry.getValue();
				PNode to = target;
				PNode from = (allPNodes.get(e.getFrom()));
				if (from.getParent() instanceof PiccoloCustomNode && !((PiccoloCustomNode) from.getParent()).isHidden()) {
					Parrow arow = new ParrowExtends(from, to, from, to);
					ANH.addArrow(arow);
				} else {
					for (PiccoloCustomNode pnode : ((PiccoloCustomNode) from).getAscendency()) {
						if (!pnode.isHidden()) {
							ANH.addArrow(new ParrowExtends(from, to, pnode, to));
							break;
						}
					}
				}
			}
		// }
		this.menu.hideMenu();
	}

	public void addActionListener() {
		this.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent arg0) {
				drawExtendsEdges(pnode, canvas);
			}
		});
	}

	public HashMap<String, Edge> getRelationInGoing() {
		Node currentNode = listNodes.get(pnode.getidNode());
		HashMap<String, Edge> relationInGoing = new HashMap<>();
		if (currentNode != null) {

			for (Entry<String, Node> nodeEntry : listNodes.entrySet()) {
				Node n = nodeEntry.getValue();
				HashMap<String, Edge> relation = n.getRelation();
				for (Entry<String, Edge> edgeEntry : relation.entrySet()) {
					Edge e = edgeEntry.getValue();
					if (e.getType().equals("isa") && (e.getTo().equals(currentNode.getId()))) {
						relationInGoing.put(e.getId(), e);
					}
				}
			}

		}
		return relationInGoing;
	}
}
