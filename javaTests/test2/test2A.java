package p;

public class A{
	private int x;

	public A(int x){
		this.x = x;
	}

	private void ma1(){
	}

	public int ma2(){
		ma1();
		return this.x;
	}
}