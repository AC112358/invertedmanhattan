package library;


public class Axis {
	protected Label name;
	protected float x, y, maxX, maxY; //assume all Axes are horizontal: will be rotated if not
	private float[] ticks;
	protected Label[] tickNames;
	protected float angle;
	protected float tickLen;
	protected float marginX, marginY;
	protected boolean isXAxis;
	protected float xScale, yScale;
	protected boolean isVisible;
	protected boolean uniformTicks;
	protected boolean showTicks;
	protected float major1, major2;
	protected int constant;
	
	//protected static float axisLabelMargin = 4;
	protected static float xAxisTickNameMargin = -10;
	protected static float yAxisTickNameMargin = 10;

	public Axis(float marginX, float marginY, float x1, float y1, float x2, float y2, float angle, int c){
		constant = c;
		isVisible = true;
		showTicks = true;
		x = x1;
		y = y1;
		maxX = x2;
		maxY = y2;
		name = new Label("", (x1 + x2 + marginX)/2, (y1 + y2 + marginY)/2, constant);
		this.angle = angle;
		//name.angle = angle;	
		isXAxis = y1 == y2;
		major1 = y;
		major2 = maxY;
		if (isXAxis){
			major1 = x;
			major2 = maxX;
		}
	}
	
	public boolean setNTicks(int n){
		if (n < 0){
			return false;
		}
		uniformTicks = true;
		
		if (n == 0){
			ticks = new float[0];
		}
		else if (n == 1){
			ticks = new float[1];
			ticks[0] = (major2 + major1)/2;
		}
		else{
			ticks = new float[n];
			float increments = (major2 - major1)/(n-1);
			for (int i = 0; i < n; i++){
				ticks[i] = major1 + increments * i;
			}
		}
		if (tickNames == null){
			setTickNamesToTickVal();
		}
		return true;
	}
	
	public void setTickNamesToTickVal(){
		tickNames = new Label[ticks.length];
		for (int i = 0; i < ticks.length; i++){
			tickNames[i] = makeTickName(i, customRound(ticks[i] - major1));
		}
	}
	
	public void setNonuniformTickNamesToTickVal(){
		tickNames = new Label[ticks.length];
		for (int i = 0; i < ticks.length; i++){
			tickNames[i] = makeTickName(i, customRound(ticks[i]));
		}
	}
	
	private Label makeTickName(int i, String n){
		//System.out.println(i + " " + tickNames[i]);
		return new Label(n, getXVal(ticks[i]), getYVal(ticks[i]), constant);
	}
	
	protected static String customRound(float f){
		return "" + (int)Math.floor(f + 0.05);
	}
	
	private float getXVal(float tickVal){
		float ans = tickVal;
		if (!isXAxis){
			ans = x - Axis.yAxisTickNameMargin;
		}
		return ans;
	}
	
	private float getYVal(float tickVal){
		float ans = tickVal;
		if (isXAxis){
			ans = y - Axis.xAxisTickNameMargin;
		}
		return ans;
	}
	
	protected float setIntervals(float pVal, float maxVal){
		uniformTicks = false;
		//uniformTicks = true;
		int i = 0;
		float total = 0;
		while (total < maxVal){
			i++;
			total += pVal;
		}
		ticks = new float[i+1];
		total = 0;
		i = 0;
		while (pVal > 0 && total < maxVal){
			//System.out.println(total);
			ticks[i] = total;
			i++;
			total += pVal;
		}
		ticks[i] = total;
		//System.out.println(total);
		
		if (tickNames == null){
			setNonuniformTickNamesToTickVal();
		}
		return total;
	}
	
	public void setTickNames(String[] names){
		int i = 0;
		while (i < names.length && i < ticks.length){
			i++;
		}
		tickNames = new Label[i];
		
		int j = 0;
		while (j < i){
			tickNames[j] = makeTickName(j, names[j]);
			j++;
		}
	}
	
	public void setTicks(float[] t){
		uniformTicks = false;
		ticks = new float[t.length];
		for (int i = 0; i < t.length; i++){
			ticks[i] = t[i];
		}
	}
	
	public float[] getTicks(){
		float[] newTicks = new float[ticks.length];
		for (int i = 0; i < newTicks.length; i++){
			newTicks[i] = ticks[i];
		}
		return newTicks;
	}
}
