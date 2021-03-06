package puck.controleur.user_event;

import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;

import org.piccolo2d.PNode;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.event.PInputEventFilter;
import org.piccolo2d.extras.pswing.PSwingCanvas;

import puck.modele.Node;
import puck.modele.Parrow;
import puck.controleur.state.Changeable;
import puck.controleur.state.StateChanger;
import puck.gui.item.arrow.ArrowNodesHolder;
import puck.gui.item.edge.ingoing.CreateEdgesBy;
import puck.gui.item.edge.ingoing.CreateEgdesHierarchyBy;
import puck.gui.item.edge.outgoing.CreateEdgesOf;
import puck.gui.item.edge.outgoing.CreateEgdesHierarchyOf;
import puck.gui.item.edge.removing.RemoveEdgesOf;
import puck.gui.item.edge.removing.RemovesHierarchyEdgesOf;
import puck.gui.item.node.AddNode;
import puck.gui.item.node.CollapseAll;
import puck.gui.item.node.ExpandAll;
import puck.gui.item.node.FocusNode;
import puck.gui.item.node.HideNode;
import puck.gui.item.node.NodeType;
import puck.gui.item.node.PiccoloCustomNode;
import puck.gui.item.node.RenameNode;
import puck.gui.menu.Menu;

public class PCustomInputEventHandler extends PBasicInputEventHandler {
	
	private PiccoloCustomNode pnode;
	private PiccoloCustomNode root;
	private PSwingCanvas canvas;
	private HashMap<String, PiccoloCustomNode> allPNodes;
	private Menu menu;
	private JMenuItem createEdgesOf;
	private JMenuItem createEdgesBy;
	private JMenuItem removeEdgesOf;
	private JMenuItem createEgdesHierarchyBy;
	private JMenuItem createEgdesHierarchyOf;
	private JMenuItem removesHierarchyEdgesOf;
	private Map<String, Node> listNodes;
	private ArrowNodesHolder ANH;
	private HideNode hideNode ;
	private FocusNode focusNode;
	private ExpandAll expandAll;
	private CollapseAll collapseAll;
	private JMenuItem addClass;
	private JMenuItem addPackage;
	private JMenuItem renameNode;
	private Changeable state;
	
	public PCustomInputEventHandler(PiccoloCustomNode pnode, PiccoloCustomNode root, PSwingCanvas canvas,
			Map<String, PiccoloCustomNode> allPNodes, Menu menu, ArrowNodesHolder ANH, Map<String, Node> listNodes) {
		setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK & InputEvent.BUTTON2_MASK));
		this.pnode = pnode;
		this.canvas = canvas;
		this.root = root;
		this.allPNodes = (HashMap<String, PiccoloCustomNode>) allPNodes;
		this.menu = menu;
		this.ANH = ANH;
		this.listNodes = listNodes;
		createEdgesOf = new CreateEdgesOf(pnode, canvas, this.allPNodes, menu,ANH,this.listNodes);
		removeEdgesOf = new RemoveEdgesOf(pnode, canvas, this.allPNodes, menu,ANH,listNodes);
		createEdgesBy = new CreateEdgesBy(pnode, canvas, this.allPNodes, menu,ANH,listNodes);
		createEgdesHierarchyBy = new CreateEgdesHierarchyBy(pnode, canvas, this.allPNodes, menu,ANH,listNodes);
		createEgdesHierarchyOf = new CreateEgdesHierarchyOf(pnode, canvas, this.allPNodes, menu,ANH,listNodes);
		removesHierarchyEdgesOf = new RemovesHierarchyEdgesOf(pnode, canvas, this.allPNodes, menu, ANH, listNodes);
		hideNode = new HideNode(pnode, canvas, this.allPNodes, menu, ANH, listNodes);
		focusNode = new FocusNode(pnode, canvas, this.allPNodes, menu, ANH, listNodes);
		expandAll = new ExpandAll(pnode, canvas, this.allPNodes, menu, ANH, listNodes);
		collapseAll = new CollapseAll(pnode, canvas, this.allPNodes, menu, ANH, listNodes);
		StateChanger.getInstance().init(this.allPNodes, ANH, canvas);
		state = StateChanger.getInstance();
		addClass = new AddNode(pnode, canvas, this.allPNodes, menu, ANH, listNodes, NodeType.CLASS, state);
		addPackage =  new AddNode(pnode, canvas, this.allPNodes, menu, ANH, listNodes, NodeType.PACKAGE, state);
		renameNode = new RenameNode(pnode, canvas, this.allPNodes, menu, ANH, listNodes, state);
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
				//pnode.setPickable(true);
				root.setLayout();
				root.updateContentBoundingBoxes(false, canvas);
				System.out.println("root children count "+ root.getHiddenchildren().size());
				
				for(PNode p : root.getHiddenchildren()) {
					System.out.println("is children of root :"+ p.getName());
				}
				
				for (Parrow arrow : ANH.getVisibleArrows()) {
					ANH.updatePosition(arrow);
				}
				 aEvent.setHandled(true);
				
			}
			if (aEvent.isRightMouseButton()) {
					generateMenu(menu,aEvent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	 
	 
	 @Override
     public void mouseEntered(final PInputEvent event) {
         event.getPickedNode().setPaint(Color.YELLOW);
     }
 	
	 
	 @Override
     public void mouseExited(final PInputEvent event) {
         event.getPickedNode().setPaint(Color.WHITE);
     }
	
	public void generateMenu(Menu menu,PInputEvent aEvent) {
		menu.removeAll();
		menu.add(createEdgesOf);
		menu.add(createEgdesHierarchyOf);
		menu.addSeparator();
		menu.add(createEdgesBy);
		menu.add(createEgdesHierarchyBy);
		menu.addSeparator();
		menu.add(removeEdgesOf);
		menu.add(removesHierarchyEdgesOf);
		menu.addSeparator();
		menu.add(hideNode);
		menu.add(focusNode);
		menu.addSeparator();
		menu.add(expandAll);
		menu.add(collapseAll);
		menu.addSeparator();
		if(pnode.getContent().getType().equals("package")) {
			menu.add(addClass);
			menu.add(addPackage);
			menu.addSeparator();
		}
		menu.add(renameNode);
		menu.setPoint(aEvent.getPosition());
		menu.setCanvas(canvas);
	}

	 
}