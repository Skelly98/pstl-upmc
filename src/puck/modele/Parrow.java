package puck.modele;

import java.awt.geom.Point2D;

import org.piccolo2d.PNode;

public abstract class Parrow extends PNode{
    public Parrow(Point2D from, Point2D to,Point2D virtuaFrom,Point2D virtualTo){
    }
    protected PNode virtualFrom;
    protected PNode virtualto;
    protected PNode from;
    protected PNode to;
    protected boolean isAllowed;

    public PNode getFrom() {
        return from;
    }

    public PNode getTo() {
        return to;
    }

    public void setFrom(PNode from){
        this.from=from;
    }

    public void setTo(PNode to){
        this.to=to;
    }

    public Parrow(PNode from,PNode to,PNode virtuaFrom,PNode virtualto){
        this.from=from;
        this.to=to;
        this.virtualFrom = virtuaFrom;
        this.virtualto = virtualto;
        this.isAllowed = true;
    }

    public abstract Parrow redraw();
    public abstract Parrow redraw(PNode virtuaFrom);
    public abstract Parrow redrawTo(PNode virtuaFrom);

    @Override
    public boolean equals(Object arrow){
        if(!(arrow instanceof Parrow))
            return false;
        return this.from==((Parrow) arrow).getFrom()
                &&this.to==((Parrow) arrow).getTo()
                &&this.virtualFrom==((Parrow) arrow).getVirtualFrom()
                &&this.virtualto==((Parrow) arrow).getVirtualto();
    }

	public PNode getVirtualFrom() {
		return virtualFrom;
	}

	public void setVirtualFrom(PNode virtualFrom) {
		this.virtualFrom = virtualFrom;
	}

	public PNode getVirtualto() {
		return virtualto;
	}

	public void setVirtualto(PNode virtualto) {
		this.virtualto = virtualto;
	}

	public boolean isAllowed() {
		return isAllowed;
	}

	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}
	
    
}
