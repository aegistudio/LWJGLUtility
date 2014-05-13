package net.aegistudio.lwjgl.input.cursor;

import org.lwjgl.input.Mouse;

import net.aegistudio.lwjgl.input.*;

public class CursorBoundingFocusEventMonitor extends InputEventMonitor implements CursorBoundingEventMonitor
{
	
	private CursorBoundingEvent cursorboundingevent;
	private final int beginingcursorx, beginingcursory, endingcursorx, endingcursory;
	
	public CursorBoundingFocusEventMonitor(InputEventListener inputeventlistener, int begincursorx, int begincursory, int endingcursorx, int endingcursory) throws InputEventException
	{
		super(inputeventlistener);
		this.beginingcursorx = begincursorx;
		this.beginingcursory = begincursory;
		this.endingcursorx = endingcursorx;
		this.endingcursory = endingcursory;
		if(this.beginingcursorx > this.endingcursorx) throw new InputEventException("Unable initialize new CursorBoundingEventMonitor instance, because the bounding box left is on the right of the right.");
		if(this.beginingcursory > this.endingcursory) throw new InputEventException("Unable initialize new CursorBoundingEventMonitor instance, because the bounding box bottom is on the top of the top.");
		this.cursorboundingevent = CursorBoundingEvent.ON_LOST_FOCUS;
	}
	
	@Override
	public boolean onCheckInputEvent()
	{
		int cursorx = Mouse.getX();
		int cursory = Mouse.getY();
		if(this.beginingcursorx >= cursorx) return false;
		if(this.endingcursorx <= cursorx) return false;
		if(this.beginingcursory >= cursory) return false;
		if(this.endingcursory <= cursory) return false;
		this.cursorboundingevent = CursorBoundingEvent.ON_FOCUS;
		return true;
	}
	
	@Override
	public boolean onResumeInputEvent()
	{
		boolean onlostfocus = !this.onCheckInputEvent();
		if(onlostfocus) this.cursorboundingevent = CursorBoundingEvent.ON_LOST_FOCUS;
		return onlostfocus;
	}
	
	@Override
	public CursorBoundingEvent getCursorBoundingEvent()
	{
		return this.cursorboundingevent;
	}
}