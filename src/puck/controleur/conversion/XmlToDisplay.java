package puck.controleur.conversion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.piccolo2d.PNode;
import org.piccolo2d.extras.pswing.PSwingCanvas;

import puck.gui.item.arrow.ArrowNodesHolder;
import puck.gui.item.edge.ingoing.CreateEgdesHierarchyBy;
import puck.gui.item.edge.outgoing.CreateEgdesHierarchyOf;
import puck.gui.item.node.PiccoloCustomNode;
import puck.gui.menu.Menu;
import puck.modele.*;

public class XmlToDisplay {
	
	private StringBuilder xmlString;
	private PiccoloCustomNode root;
	private HashMap<String, Edge> addedEdges = new HashMap<String, Edge>();
	private HashMap<String, PiccoloCustomNode> addedPnodes = new HashMap<String, PiccoloCustomNode>();
	private HashMap<String, PiccoloCustomNode> allPNodes = new HashMap<>();
	private Map<String, Node> listNodes;
	private BufferedWriter br;
	private CreateEgdesHierarchyBy createEdgesHierarchyBy;
	private CreateEgdesHierarchyOf createEdgesHierarchyOf;
	private PSwingCanvas canvas;
	private Menu menu;
	private ArrowNodesHolder ANH;

	public XmlToDisplay() {
		xmlString = new StringBuilder();
		xmlString.append("<?xml version=\"1.0\"?>\n");
		xmlString.append("<DG>\n");
		xmlString.append("</DG>");
		this.listNodes = new XmlToStructure("DependecyGraph.xml").parseNode();
		this.root = new NodeToPnodeParser(allPNodes, listNodes).getPackageNodes();
		this.canvas = new PSwingCanvas();
		this.menu = new Menu();
		this.ANH = new ArrowNodesHolder();
		root.setParent(new PNode());
		root.expandAll();
		createEdgesHierarchyBy = new CreateEgdesHierarchyBy(root, canvas, allPNodes, menu, ANH, listNodes);
		createEdgesHierarchyOf = new CreateEgdesHierarchyOf(root, canvas, allPNodes, menu, ANH, listNodes);
		addPnodes(root);
		createEdgesHierarchyBy.drawOutgoingdges(root, canvas);
		createEdgesHierarchyOf.drawOutgoingdges(root, canvas);
		
		for (Entry<String, PiccoloCustomNode> entry : addedPnodes.entrySet()) {
			String key = entry.getKey();
			PiccoloCustomNode p = entry.getValue();
			addEdgesToXml(p);
		}
		addUsesIsaToXml();
		System.err.println(addedEdges.size());
	
		try {
			writeTo();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void addPnodes(PiccoloCustomNode pnode) {
		for (PiccoloCustomNode child : pnode.getHierarchy()) {
			addPnodeToXml(child);
			addPnodes(child);
		}
	}

	private void addPnodeToXml(PiccoloCustomNode pnode) {
		if (!addedPnodes.containsValue(pnode)) {
			addedPnodes.put(pnode.getidNode(), pnode);
			String s = nodeToString(pnode);
			xmlString.insert(xmlString.length()-5,s , 0,s.length());
		}
	}

	private String nodeToString(PiccoloCustomNode node) {
		String formatString = "\t<node type=\"%s\" id=\"%s\" name=\"%s\"/>\n";
		String type = node.getContent().getType().toLowerCase();
		String id = node.getidNode();
		String name = node.getName();
		return String.format(formatString, type, id, name);
	}

	private String edgeToString(Edge node) {
		String formatString = "\t<edge type=\"%s\" src=\"%s\" dest=\"%s\" id=\"%s\"/>\n";
		String type = node.getType();
		String id = node.getId();
		String from = node.getFrom();
		String to = node.getTo();
		return String.format(formatString,type,from,to,id);
	}

	public void writeTo() throws IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter(new File("xmlFromDisplay.xml")));
		br.write(xmlString.toString());
		br.close();
	}
	
	public void addUsesIsaToXml() {
		for (Entry<String, Edge> entry : addedEdges.entrySet()) {
			Edge e = entry.getValue();
			String s = edgeToString(e);
			xmlString.insert(xmlString.length()-5,s , 0,s.length());
		}
	}
	
	private void addEdgesToXml(PiccoloCustomNode pnode) {
		Node node = listNodes.get(pnode.getidNode());
		for (Entry<String, PiccoloCustomNode> entry : addedPnodes.entrySet()) {
			String key = entry.getKey();
			PiccoloCustomNode p = entry.getValue();
			for (PiccoloCustomNode child : p.getChildren()) {
				if (node != null) {
					Edge e = getCointainsEdge(node, listNodes.get(child.getidNode()));
					if (e != null && !addedEdges.containsKey(e)) {		
						String s = edgeToString(e);
						xmlString.insert(xmlString.length()-5,s , 0,s.length());
					}
				}
			}
		}
	}

	private Edge getCointainsEdge(Node node, Node child) {
		HashMap<String, Edge> edges = node.getRelation();
		for (Entry<String, Edge> entry : edges.entrySet()) {
			Edge e = entry.getValue();
			if (e.getType().equals("contains") && e.getTo().equals(child.getId())) {
				return e;
			}
			if(!e.getType().equals("contains")){
				addedEdges.put(e.getId(), e);
			}
		}
		return null;
	}

	public HashMap<String, PiccoloCustomNode> getAddedPnodes() {
		return addedPnodes;
	}

	public void setAddedPnodes(HashMap<String, PiccoloCustomNode> addedPnodes) {
		this.addedPnodes = addedPnodes;
	}

	public Map<String, Node> getListNodes() {
		return listNodes;
	}

	public void setListNodes(Map<String, Node> listNodes) {
		this.listNodes = listNodes;
	}
	
	public StringBuilder getXmlString() {
		return xmlString;
	}



}
