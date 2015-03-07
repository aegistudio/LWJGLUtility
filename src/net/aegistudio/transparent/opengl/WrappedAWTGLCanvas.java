package net.aegistudio.transparent.opengl;

import net.aegistudio.transparent.opengl.util.Scanvager;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("serial")
public class WrappedAWTGLCanvas extends AWTGLCanvas implements Container
{
	private final Container theContainer;
	private Scanvager scanvager;
	private boolean shouldInitialize;
	private long refreshInterval = 60;
	
	public boolean lockedRatio = false;
	public boolean useLWJGLDisplay = false;
	
	public double ratio;
	
	private Thread refreshThread = new Thread()
	{
		public void run()
		{
			while(WrappedAWTGLCanvas.this.refreshInterval > 0) try
			{
				if(useLWJGLDisplay)
				{
					Display.update();
					Display.sync((int)(1000 / refreshInterval));
				}
				else
				{
					if(WrappedAWTGLCanvas.this.isVisible()) WrappedAWTGLCanvas.this.repaint();
					Thread.sleep(WrappedAWTGLCanvas.this.refreshInterval);
				}
			}
			catch(RuntimeException runtimeException)
			{
				WrappedAWTGLCanvas.this.processRuntimeException(runtimeException);
			}
			catch(Exception exception)
			{
				WrappedAWTGLCanvas.this.processException(exception);
			}
		}
	};
	
	public boolean equals(Object obj)
	{
		return this.theContainer.equals(obj);
	}
	
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
	
	public WrappedAWTGLCanvas(Container container) throws LWJGLException
	{
		super();
		if(container == null) throw new IllegalArgumentException("The canvas should not be null!");
		this.theContainer = container;
		this.shouldInitialize = true;
		this.refreshThread.start();
		this.scanvager = new Scanvager();
	}
	
	public WrappedAWTGLCanvas() throws LWJGLException
	{
		this(new Canvas()
		{
			@Override
			public void onInit(Container container)
			{
			}
		});
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
	
	protected void processRuntimeException(RuntimeException runtimeException)
	{
		runtimeException.printStackTrace();
	}
	
	@Override
	public void onInit(Container canvas)
	{
		this.theContainer.onInit(canvas);
	}

	@Override
	public void onDraw(Container canvas)
	{
		if(this.scanvager != null) this.scanvager.scanvage();
		this.theContainer.onDraw(canvas);
	}

	@Override
	public void onDestroy(Container canvas)
	{
		this.theContainer.onDestroy(canvas);
	}
	
	@Override
	public boolean registerDrawable(Drawable drawable)
	{
		return theContainer.registerDrawable(drawable);
	}
	
	@Override
	public boolean unregisterDrawable(Drawable drawable)
	{
		return theContainer.unregisterDrawable(drawable);
	}
	
	@Override
	public boolean registerSementicDrawable(Object sementicDrawable)
	{
		return theContainer.registerSementicDrawable(sementicDrawable);
	}
	

	@Override
	public boolean unregisterSementicDrawable(Object sementicDrawable)
	{
		return theContainer.unregisterSementicDrawable(sementicDrawable);
	}
	
	public Scanvager getScanvager()
	{
		return this.scanvager;
	}
	
	public void setScanvager(Scanvager scanvager)
	{
		this.scanvager = scanvager;
	}
}
