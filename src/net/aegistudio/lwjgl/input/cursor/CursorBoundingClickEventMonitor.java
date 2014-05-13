package net.aegistudio.lwjgl.input.cursor;

import org.lwjgl.input.Mouse;

import net.aegistudio.lwjgl.input.*;

public class CursorBoundingClickEventMonitor extends InputEventMonitor implements InputEventListener, CursorBoundingEventMonitor
{
	
	private final CursorBoundingFocusEventMonitor cursorboundingfocuseventmonitor;
	private final InputEventListener inputeventlistener;
	private final int clickbutton;
	private CursorBoundingEvent cursorboundingevent;
	
	public CursorBoundingClickEventMonitor(InputEventListener inputeventlistener, int begincursorx, int begincursory, int endingcursorx, int endingcursory, int clickbutton) throws InputEventException
	{
		super(inputeventlistener);
		this.inputeventlistener = inputeventlistener;
		this.clickbutton = clickbutton;
		this.cursorboundingevent = CursorBoundingEvent.ON_LOST_FOCUS;
		this.cursorboundingfocuseventmonitor = new CursorBoundingFocusEventMonitor(this, begincursorx, begincursory, endingcursorx, endingcursory);
	}
	
	public void onInputEventException(InputEventException inputeventexception)
	{
		this.inputeventlistener.onInputEventException(new InputEventException("Error occurs while monitoring cursor bounding click event.", inputeventexception));
	}
	
	@SuppressWarnings("deprecation")
	public void onInputEventResponse(InputEventMonitor inputeventmonitor)
	{
		this.cursorboundingevent = CursorBoundingEvent.ON_FOCUS;
		this.getInputEventMonitorThread().resume();
		this.inputeventlistener.onInputEventResponse(this);
	}
	
	@SuppressWarnings("deprecation")
	public void onInputEventResume(InputEventMonitor inputeventmonitor)
	{
		this.cursorboundingevent = CursorBoundingEvent.ON_LOST_FOCUS;
		this.getInputEventMonitorThread().suspend();
		this.inputeventlistener.onInputEventResume(this);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void startInputEventMonitor()
	{
		super.startInputEventMonitor();
		this.cursorboundingfocuseventmonitor.startInputEventMonitor();
		this.getInputEventMonitorThread().suspend();
	}
	
	@Override
	public void stopInputEventMonitor()
	{
		this.cursorboundingfocuseventmonitor.stopInputEventMonitor();
		super.stopInputEventMonitor();
	}
	
	@Override
	public boolean onCheckInputEvent()
	{
		boolean cursorstatus = Mouse.isButtonDown(this.clickbutton);
		if(cursorstatus) this.cursorboundingevent = CursorBoundingEvent.ON_CLICK;
		return cursorstatus;
	}
	
	@Override
	public boolean onResumeInputEvent()
	{
		boolean cursorstatus = !Mouse.isButtonDown(this.clickbutton);
		if(cursorstatus) this.cursorboundingevent = CursorBoundingEvent.ON_RELEASE;
		return cursorstatus;
	}
	
	public CursorBoundingEvent getCursorBoundingEvent()
	{
		return this.cursorboundingevent;
	}
	

}