package net.aegistudio.lwjgl.graphic;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;

@SuppressWarnings("serial")
public class WrappedAWTGLCanvas extends AWTGLCanvas implements Drawable
{
	private final Canvas canvas;
	private boolean shouldInitialize;
	private long refreshInterval = 60;
	private Thread refreshThread = new Thread()
	{
		public void run()
		{
			while(true) try
			{
				if(WrappedAWTGLCanvas.this.isVisible()) WrappedAWTGLCanvas.this.repaint();
				Thread.sleep(WrappedAWTGLCanvas.this.refreshInterval);
			}
			catch(Exception exception)
			{
				WrappedAWTGLCanvas.this.processException(exception);
			}
		}
	};
	
	public void setRefreshInterval(long interval)
	{
		this.refreshInterval = interval;
	}
	
	public WrappedAWTGLCanvas(Canvas theCanvas) throws LWJGLException
	{
		super();
		if(theCanvas == null) throw new IllegalArgumentException("The canvas should not be null!");
		this.canvas = theCanvas;
		this.shouldInitialize = true;
		this.refreshThread.start();
	}
	
	protected void paintGL()
	{
		try
		{
			if(this.shouldInitialize)
			{
				this.onInit(null);
				this.shouldInitialize = false;
			}
			this.onDraw(null);
			swapBuffers();
		}
		catch(Exception exception)
		{
			this.processException(exception);
		}
	}
	
	public void releaseContext() throws LWJGLException
	{
		super.releaseContext();
		this.onDestroy(null);
	}
	
	protected void exceptionOccurred(LWJGLException lwjgle)
	{
		super.exceptionOccurred(lwjgle);
		this.processException(lwjgle);
	}
	
	protected void processException(Exception exception)
	{
		exception.printStackTrace();
	}
	
	@Override
	public void onInit(Canvas canvas)
	{
		this.canvas.onInit(canvas);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		this.canvas.onDraw(canvas);
	}

	@Override
	public void onDestroy(Canvas canvas)
	{
		this.canvas.onDestroy(canvas);
	}
}
