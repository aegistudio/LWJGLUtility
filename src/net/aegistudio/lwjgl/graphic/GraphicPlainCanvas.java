package net.aegistudio.lwjgl.graphic;

import org.lwjgl.opengl.GL11;

public class GraphicPlainCanvas extends Canvas
{
	private final int canvaswidth, canvasheight;
	private final float refreshcolorred, refreshcolorgreen, refreshcolorblue, refreshcoloralpha;
	
	public GraphicPlainCanvas(int canvaswidth, int canvasheight)
	{
		this(canvaswidth, canvasheight, 0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public GraphicPlainCanvas(int canvaswidth, int canvasheight, float refreshcolorred, float refreshcolorblue, float refreshcolorgreen, float refreshcoloralpha)
	{
		super();
		
		this.canvaswidth = canvaswidth;
		this.canvasheight = canvasheight;
		
		this.refreshcolorred = refreshcolorred;
		this.refreshcolorgreen = refreshcolorgreen;
		this.refreshcolorblue = refreshcolorblue;
		this.refreshcoloralpha = refreshcoloralpha;
	}
	
	@Override
	public void onInitialize(Canvas canvas) throws GraphicIllegalStateException
	{
		super.onInitialize(canvas);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, this.canvaswidth, 0, this.canvasheight, -1, 1);
	}
	
	@Override
	public void onRefresh(Canvas canvas) throws GraphicIllegalStateException
	{
		GL11.glClearColor(this.refreshcolorred, this.refreshcolorblue, this.refreshcolorgreen, this.refreshcoloralpha);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		super.onRefresh(canvas);
	}
}