package puck.gui.item.arrow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.piccolo2d.PNode;

import puck.modele.Parrow;

import puck.gui.item.node.PiccoloCustomNode;

public class ArrowNodesHolder extends PNode {
	public void setHiddenArrows(Collection<Parrow> hiddenArrows) {
		this.hiddenArrows = hiddenArrows;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Collection<Parrow> hiddenArrows;

	public ArrowNodesHolder() {
		hiddenArrows = new HashSet<>();
	}

	public void addArrow(Parrow arrow) {
		for (Parrow arr : getAllArrows()) {
			if (arr.equals(arrow))
				removeArrow(arr);
		}
		addChild(arrow);
		// updatePosition(arrow);
	}

	public void removeArrow(Parrow arrow) {
		removeChild(arrow);
	}

	public void hideArrow(Parrow arrow) {
		hiddenArrows.add(arrow);
		removeArrow(arrow);
	}

	public void showArrow(Parrow arrow) {
		addArrow(arrow);
		hiddenArrows.remove(arrow);
	}

	public boolean isHidden(Parrow arrow) {
		return hiddenArrows.contains(arrow);
	}

	@SuppressWarnings("unchecked")
	public Collection<Parrow> getVisibleArrows() {
		Collection<Parrow> set = new HashSet<>();
		for (Iterator<Parrow> iterator = getChildrenIterator(); iterator.hasNext();) {
			PNode n = iterator.next();
			if (n instanceof Parrow)
				set.add((Parrow) n);
		}
		return set;
	}

	public Collection<Parrow> getHiddenArrows() {
		Collection<Parrow> set = new HashSet<>();
		for (Iterator<Parrow> iterator = hiddenArrows.iterator(); iterator.hasNext();)
			set.add(iterator.next());
		return set;
	}

	public Collection<Parrow> getAllArrows() {
		Collection<Parrow> set = new HashSet<>();
		for (Iterator<PNode> iterator = getChildrenIterator(); iterator.hasNext();) {
			PNode n = iterator.next();
			if (n instanceof Parrow)
				set.add((Parrow) n);
		}
		for (Iterator<Parrow> iterator = hiddenArrows.iterator(); iterator.hasNext();)
			set.add(iterator.next());
		return set;
	}

	public void updatePosition(Parrow arrow) {
		PNode from = arrow.getFrom();
		PNode to = arrow.getTo();
		PNode virtualFrom = arrow.getVirtualFrom();
		PNode virtualTo = arrow.getVirtualto();
		// Parrow ar2 = null ;
		ArrayList<PiccoloCustomNode> fromAscendency = ((PiccoloCustomNode) from).getAscendency();
		ArrayList<PiccoloCustomNode> toAscendency = ((PiccoloCustomNode) to).getAscendency();
		for (PiccoloCustomNode pnode : fromAscendency) {
			if (!pnode.isHidden()) {
				if (pnode.equals((PiccoloCustomNode) from)) {
					Parrow ar2 = arrow.redraw();
					removeArrow(arrow);
					addArrow(ar2);
					virtualFrom = ar2.getVirtualFrom();
					arrow = ar2;
					break;
				} else {
					Parrow ar2 = arrow.redraw(pnode);
					removeArrow(arrow);
					addArrow(ar2);
					virtualFrom = ar2.getVirtualFrom();
					arrow = ar2;
					break;
				}
			}
		}
		for (PiccoloCustomNode pnode : toAscendency) {
			if (!pnode.isHidden()) {
				if (pnode.equals((PiccoloCustomNode) to) && from.equals(virtualFrom)) {
					Parrow ar2 = arrow.redraw();
					removeArrow(arrow);
					addArrow(ar2);
					arrow = ar2;
					break;
				} else if (pnode.equals((PiccoloCustomNode) to)) {
					Parrow ar2 = arrow.redrawTo(to);
					removeArrow(arrow);
					addArrow(ar2);
					virtualTo = ar2.getVirtualto();
					arrow = ar2;
					break;
				} else {
					System.out.println("pnode" + pnode.getContent().getType());
					Parrow ar2 = arrow.redrawTo(pnode);
					removeArrow(arrow);
					addArrow(ar2);
					virtualTo = ar2.getVirtualto();
					arrow = ar2;
					break;
				}
			}
		}
		// Pour afficher les cardinalit�s
		// if (virtualFrom != null &&
		// ((PiccoloCustomNode)virtualFrom).getContent().getType().equals("package")) {
		// if (virtualTo != null &&
		// ((PiccoloCustomNode)virtualTo).getContent().getType().equals("package")) {
		// System.out.println("package");
		// int [] tab = addCard(virtualFrom,virtualTo);
		// if(arrow instanceof ParrowUses) {
		// ((ParrowUses)arrow).addCard(tab[0], tab[1]);
		// }
		// }
		// }

	}

	public void hide_show_arrows(PiccoloCustomNode node) {
		Collection<PiccoloCustomNode> hierarchy = node.getHierarchy();
		for (PiccoloCustomNode PCN : hierarchy) {
			if (PCN.isHidden())
				for (Parrow arrow : getVisibleArrows()) {

					PiccoloCustomNode PCNF = (PiccoloCustomNode) arrow.getFrom();
					PiccoloCustomNode PCNT = (PiccoloCustomNode) arrow.getTo();

					if (PCN == PCNF || PCN == PCNT) {
						hideArrow(arrow);
						// System.out.println("hide
						// "+getVisibleArrows().size()+"-"+getHiddenArrows().size());
					}
				}
			else {
				for (Parrow arrow : getHiddenArrows()) {
					PiccoloCustomNode PCNF = (PiccoloCustomNode) arrow.getFrom();
					PiccoloCustomNode PCNT = (PiccoloCustomNode) arrow.getTo();
					if ((!PCNT.isHidden() || !PCNF.isHidden()) && (PCN == PCNF || PCN == PCNT)) {
						showArrow(arrow);
						// System.out.println("show
						// "+getVisibleArrows().size()+"-"+getHiddenArrows().size());
					}
				}
			}
		}
	}

	@SuppressWarnings("unckecked")
	public void clearCounters() {
		Collection<PNode> out = new HashSet<>();
		for (Iterator<PNode> pNodeIterator = getChildrenIterator(); pNodeIterator.hasNext();) {
			PNode node = pNodeIterator.next();
			if (node instanceof ArrowCounter) {
				out.add(node);
			}
		}
		for (PNode node : out) {
			removeChild(node);
		}
	}

	public void updateAllPosition() {
		for (Parrow parrow : getVisibleArrows()) {
			updatePosition(parrow);
		}
	}

	private int margin = 10;

	public int[] addCard(PNode from, PNode to) {
		int[] tab = new int[2];
		int a = 0;
		int b = 0;
		for (Parrow parrow : this.getAllArrows()) {
			if (parrow.getVirtualFrom().equals(from) && parrow.getVirtualto().equals(to)) {
				a++;
			}
			if (parrow.getVirtualFrom().equals(to) && parrow.getVirtualto().equals(from)) {
				b++;
			}
		}
		tab[0] = a;
		tab[1] = b;
		System.err.println(a);
		System.err.println(b);
		return tab;

	}
	// public void updateCount(ParrowDottedFat parrow) {
	// int i = 0;
	// for (Parrow parrow1 : getVisibleArrows())
	// if (parrow1 instanceof ParrowDottedFat && parrow.getFrom() ==
	// parrow1.getFrom()
	// && parrow.getTo() == parrow1.getTo())
	// i++;
	// ArrowCounter AC = new ArrowCounter(String.valueOf(i));
	// addChild(AC);
	// PBounds bounds = parrow.getUnionOfChildrenBounds(null);
	// parrow.setBounds(bounds);
	// AC.translate(parrow.getBounds().getCenterX() + margin,
	// parrow.getBounds().getCenterY() + margin);
	//
	// }
}
