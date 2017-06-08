package library;

public class Point {
	protected float xRel, yRel;
	protected int color;
	private float radius;
	private Label label;
	protected int constant;
	protected int chromosome;
	
	public Point(float x, float y){
		xRel = x;
		yRel = y;
		radius = 3;
		constant = 1;
	}
	
	public Point(float x, float y, int chr, int c){
		this(x, y);
		chromosome = chr;
		constant = c;
	}
	
	public Point(float x, float y, int clr, int size, int c){
		this(x, y);
		color = clr;
		constant = c;
	}
	
	public Point(float x, float y, int clr, int size, int c, Label name){
		this(x, y, clr, size, c);
		label = name;
	}
	
	public int getColor(){
		return color;
	}
	
	public void setColor(int clr){
		color = clr;
	}
	
	public float getRadius(){
		return radius;
	}
	public void setRadius (float r){
		radius = r;
	}
}
