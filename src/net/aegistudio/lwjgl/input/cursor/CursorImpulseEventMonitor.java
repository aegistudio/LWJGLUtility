package net.aegistudio.lwjgl.input.cursor;

import org.lwjgl.input.Mouse;

import net.aegistudio.lwjgl.input.*;

public class CursorImpulseEventMonitor extends InputEventMonitor
{
	
	private final int cursorx, cursory, precision;
	private int impulsedeltax, impulsedeltay;
	private boolean issuspended;
	
	public CursorImpulseEventMonitor(InputEventListener inputeventlistener, int cursorx, int cursory, int precision, int inputeventrefreshtime) throws InputEventException
	{
		super(inputeventlistener, inputeventrefreshtime);
		if(precision <= 0) throw new InputEventException("Unable initialize new CursorImpulseEventMonitor instance, the precision must be greater than zero.");
		this.cursorx = cursorx;
		this.cursory = cursory;
		this.precision = precision;
		this.issuspended = false;
	}
	
	
	@Override
	public boolean onCheckInputEvent()
	{
		this.impulsedeltax = Mouse.getX() - this.cursorx;
		this.impulsedeltay = Mouse.getY() - this.cursory;
		
		if(this.impulsedeltax > this.precision) return true;
		if(this.impulsedeltax < - this.precision) return true;
		if(this.impulsedeltay > this.precision) return true;
		if(this.impulsedeltay < - this.precision) return true;
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public void setImpulseListenerSuspend()
	{
		if(this.issuspended)
		{
			super.getInputEventMonitorThread().resume();
			this.issuspended = false;
		}
		else
		{
			super.getInputEventMonitorThread().suspend();
			this.issuspended = true;
		}
	}
	
	public int getImpulseDeltaX()
	{
		return this.impulsedeltax;
	}
	
	public int getImpulseDeltaY()
	{
		return this.impulsedeltay;
	}
	
	@Override
	public boolean onResumeInputEvent()
	{
		Mouse.setCursorPosition(this.cursorx, this.cursory);
		return true;
	}
	
}