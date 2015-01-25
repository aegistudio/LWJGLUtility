package net.aegistudio.lwjgl.input;

public abstract class InputEventMonitor implements Runnable
{
	
	private final InputEventListener inputeventlistener;
	private final Thread inputeventmonitor;
	private final int inputeventrefreshrate;
	
	public InputEventMonitor(InputEventListener inputeventlistener)
	{
		this(inputeventlistener, 10);
	}
	
	public InputEventMonitor(InputEventListener inputeventlistener, int inputeventrefreshtime)
	{
		this.inputeventlistener = inputeventlistener;
		this.inputeventrefreshrate = inputeventrefreshtime;
		this.inputeventmonitor = new Thread(this);
	}
	
	public abstract boolean onCheckInputEvent();
	
	public boolean onResumeInputEvent()
	{
		return true;
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				while(!this.onCheckInputEvent()) Thread.sleep(this.inputeventrefreshrate);
				this.inputeventlistener.onInputEventResponse(this);
				while(!this.onResumeInputEvent()) Thread.sleep(this.inputeventrefreshrate);
				this.inputeventlistener.onInputEventResume(this);
			}
		}
		catch(Exception exception)
		{
			this.inputeventlistener.onInputEventException(new InputEventException("Error occurs while monitoring input events.", exception));
		}
	}
	
	@SuppressWarnings("deprecation")
	public void stopInputEventMonitor()
	{
		try
		{
			this.inputeventmonitor.stop();
		}
		catch(Exception exception)
		{
			this.inputeventlistener.onInputEventException(new InputEventException("Error occurs while stopping input event monitor.", exception));
		}
	}
	
	
	public void startInputEventMonitor()
	{
		try
		{
			this.inputeventmonitor.start();
		}
		catch(Exception exception)
		{
			this.inputeventlistener.onInputEventException(new InputEventException("Error occurs while starting input event monitor.", exception));
		}
	}
	
	protected Thread getInputEventMonitorThread()
	{
		return this.inputeventmonitor;
	}
	
}