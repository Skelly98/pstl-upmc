package puck.controleur.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.piccolo2d.extras.pswing.PSwingCanvas;

import puck.controleur.refactoring.RefactoringCommands;
import puck.controleur.user_event.PCustomInputEventHandler;
import puck.gui.item.arrow.ArrowNodesHolder;
import puck.modele.Node;
import puck.gui.item.node.PiccoloCustomNode;
import puck.gui.menu.Menu;

public class StateChanger2 implements Changeable{
	private static HashMap<String, PiccoloCustomNode> allPNodes;
	private static ArrowNodesHolder ANH;
	private static PSwingCanvas canvas;
	private static Stack<State> editedState = new Stack<State>(); 
	private static int position = 0 ;
	private static PiccoloCustomNode root;
	private static Map<String, Node> listNodes;
	private static Menu menu;
	private static State currentState;
	private static StringBuilder refactoringCommands;
	
	public StateChanger2() {
		super();
	};
	
	private static StateChanger2 INSTANCE = new StateChanger2();
	
	public static StateChanger2 getInstance(){   
		return INSTANCE;
	}
	
	
	public void init(HashMap<String, PiccoloCustomNode> allPNodes,ArrowNodesHolder ANH,PSwingCanvas canvas,PiccoloCustomNode root,
			Map<String, Node> listNodes, Menu menu, StringBuilder refactoringCommands) {
		StateChanger2.allPNodes = allPNodes;
		StateChanger2.ANH = ANH;
		StateChanger2.canvas = canvas;
		StateChanger2.root = root;
		StateChanger2.listNodes = listNodes;
		StateChanger2.menu = menu;
		StateChanger2.refactoringCommands = refactoringCommands;
		//System.err.println("ref"+refactoringCommands.hashCode());
	}
	
	@Override
	public void undo() {
		if (editedState.size() > 0 && position > 0 && !currentState.equals(editedState.get(position))) {
			System.out.println("undo");
			State previousState = editedState.get(position);
			currentState = previousState;
			allPNodes.clear();
			allPNodes.putAll(previousState.getAllPNodes());
			
			ANH = previousState.getANH();
			RefactoringCommands.getInstance().setXmlString(previousState.getRefactoringCommands());
			root.removeAllChildren();
			root.getAllChildren().clear();
			root.getHiddenchildren().clear();
			Collection<PiccoloCustomNode> children = previousState.getRoot().getAllChildren();
			root.setChilldren(new ArrayList<>(children));
			//root.setHiddenchildren(previousState.getRoot().getHiddenchildren());	
			addEvent(root, root, canvas, menu, listNodes);
			System.out.println(allPNodes.hashCode());
			root.setLayout();
		}

	}


	@Override
	public void redo() {
		position++;
		if (position == editedState.size()) position--;
		
		if (editedState.size() > 0 && position < editedState.size() && !currentState.equals(editedState.get(position))) {
			System.out.println("redo");
			State nextState = editedState.get(position);
			currentState = nextState;
			allPNodes.clear();
			allPNodes.putAll(nextState.getAllPNodes());
			
			ANH = nextState.getANH();
			RefactoringCommands.getInstance().setXmlString(nextState.getRefactoringCommands());

			root.removeAllChildren();
			root.getAllChildren().clear();
			root.getHiddenchildren().clear();
			
			root.setChilldren(new ArrayList<>(nextState.getRoot().getAllChildren()));
			//root.setHiddenchildren(nextState.getRoot().getHiddenchildren());
			addEvent(root, root, canvas, menu, listNodes);

			root.setLayout();
		}

	}
	
	
	public Stack<State> getAddedPnodes() {
		return editedState;
	}



	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setAddedPnodes(Stack<State> editedState) {
		this.editedState = editedState;
		position = this.editedState.size()-1;
		currentState = editedState.get(position);
	}

	public HashMap<String, PiccoloCustomNode> getAllPNodes() {
		return allPNodes;
	}

	public void setAllPNodes(HashMap<String, PiccoloCustomNode> allPNodes) {
		this.allPNodes = allPNodes;
	}

	public ArrowNodesHolder getANH() {
		return ANH;
	}

	public void setANH(ArrowNodesHolder aNH) {
		ANH = aNH;
	}

	public PSwingCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(PSwingCanvas canvas) {
		this.canvas = canvas;
	}
	
	public StringBuilder getRefactoringCommands() {
		return refactoringCommands;
	}
	public void setRefactoringCommands(StringBuilder refactoringCommands) {
		this.refactoringCommands = refactoringCommands;
	}
	private void addEvent(PiccoloCustomNode node, PiccoloCustomNode tree,PSwingCanvas canvas,Menu menu,Map<String, Node> listNodes) {
		if (node.getidNode() != "r01") 
		node.getContent().getText().addInputEventListener(new PCustomInputEventHandler(node, tree, canvas, allPNodes,menu,ANH,listNodes));
		if (node.getAllChildren().size() != 0)
			for (PiccoloCustomNode PCN : node.getAllChildren()) {
				addEvent(PCN, tree,canvas,menu,listNodes);
			}
	}
	

}
