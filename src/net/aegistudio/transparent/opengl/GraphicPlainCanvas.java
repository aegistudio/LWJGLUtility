package net.aegistudio.transparent.opengl;

import net.aegistudio.transparent.opengl.util.DisplayList;

import org.lwjgl.opengl.GL11;

/**
 * @Deprecated Please use ortho camera in the camera class instead of this canvas.
 * @author aegistudio
 */

@Deprecated
public class GraphicPlainCanvas extends Canvas
{
	private DisplayList graphicPlainCanvasCallback;
	
	public GraphicPlainCanvas(int canvaswidth, int canvasheight)
	{
		this(canvaswidth, canvasheight, 0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public GraphicPlainCanvas(final int canvaswidth, final int canvasheight, final float refreshcolorred, final float refreshcolorblue, final float refreshcolorgreen, final float refreshcoloralpha)
	{
		super();
		
		this.graphicPlainCanvasCallback = new DisplayList()
		{
			@Override
			protected void display(int displayIndex)
			{
				GL11.glClearColor(refreshcolorred, refreshcolorblue, refreshcolorgreen, refreshcoloralpha);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0, canvaswidth, 0, canvasheight, -1, 1);
			}
		};
	}
	
	@Override
	public void onInit(Container canvas)
	{
		this.graphicPlainCanvasCallback.create();
	}
	
	@Override
	public void onUpdate(Container canvas)
	{
		this.graphicPlainCanvasCallback.call(0);
		super.onUpdate(canvas);
	}
	
	@Override
	public void onDestroy(Container canvas)
	{
		this.graphicPlainCanvasCallback.destroy();
	}
}