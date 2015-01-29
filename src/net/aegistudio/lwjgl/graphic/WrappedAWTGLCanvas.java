package net.aegistudio.lwjgl.graphic;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("serial")
public class WrappedAWTGLCanvas extends AWTGLCanvas implements Drawable
{
	private final Canvas canvas;
	private boolean shouldInitialize;
	private long refreshInterval = 60;
	
	public boolean lockedRatio = false;
	public double ratio;
	
	private Thread refreshThread = new Thread()
	{
		public void run()
		{
			while(WrappedAWTGLCanvas.this.refreshInterval > 0) try
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
		if(interval < 0) throw new IllegalArgumentException("The interval should not be less than zero");
		else
		{
			long previewInterval = this.refreshInterval;
			this.refreshInterval = interval;
			if(previewInterval == 0) this.refreshThread.start();
		}
	}
	
	public WrappedAWTGLCanvas(Canvas theCanvas) throws LWJGLException
	{
		super();
		if(theCanvas == null) throw new IllegalArgumentException("The canvas should not be null!");
		this.canvas = theCanvas;
		this.shouldInitialize = true;
		this.refreshThread.start();
	}
	
	public int width = 0;
	public int height = 0;
	
	protected void paintGL()
	{
		try
		{
			if(this.shouldInitialize)
			{
				this.onInit(null);
				this.ratio = (1.0 * this.getHeight()) / ((this.getWidth() == 0)? 1 : this.getWidth());
				this.shouldInitialize = false;
			}
			
			if(this.getWidth() != width || this.getHeight() != height)
			{
				this.width = this.getWidth();
				this.height = this.getHeight();
				if(this.lockedRatio)
				{
					int viewHeight = (int)(this.ratio * this.width);
					int viewWidth = (int)(this.height / this.ratio);
					if(viewHeight > this.height) GL11.glViewport((this.width - viewWidth) / 2, 0, viewWidth, this.height);
					else GL11.glViewport(0, (this.height - viewHeight) / 2, this.width, viewHeight);
				}
				else GL11.glViewport(0, 0, width, height);
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
	
	public static void main(String[] args) throws Exception
	{	
		java.awt.Frame frame = new java.awt.Frame();
		Canvas subcanvas = new Canvas()
		{
			@Override
			public void onInit(Canvas canvas)
			{
				GL11.glClearColor(1.0f, 0, 0, 1.0f);
			}
			
			public void onDraw(Canvas canvas)
			{
				GL11.glColor3d(0, 1, 0);
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex2d(0, 0);
					GL11.glVertex2d(0, 1);
					GL11.glVertex2d(1, 1);
					GL11.glVertex2d(1, 0);
				GL11.glEnd();
			}
			
		};
		Canvas topCanvas = new Canvas()
		{

			@Override
			public void onInit(Canvas canvas)
			{
				
			}
			
		};
		topCanvas.registerDrawable(subcanvas);
		WrappedAWTGLCanvas canvas = new WrappedAWTGLCanvas(topCanvas);
		canvas.lockedRatio = true;
		frame.setSize(600, 480);
		frame.add(canvas);
		frame.setVisible(true);
		
		frame.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent event)
			{
				System.exit(1);
			}
		});
	}
}
