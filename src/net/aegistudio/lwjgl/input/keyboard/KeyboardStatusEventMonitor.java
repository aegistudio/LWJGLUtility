package net.aegistudio.lwjgl.input.keyboard;

import org.lwjgl.input.Keyboard;

import net.aegistudio.lwjgl.input.*;

public class KeyboardStatusEventMonitor extends InputEventMonitor
{
	
	private final int keyboardstatusindex;
	private KeyboardStatus keyboardstatus;
	
	public KeyboardStatusEventMonitor(InputEventListener inputeventlistener, int keyboardstatusindex)
	{
		super(inputeventlistener);
		this.keyboardstatusindex = keyboardstatusindex;
		this.keyboardstatus = KeyboardStatus.KEY_RELEASED;
	}
	
	@Override
	public boolean onCheckInputEvent()
	{
		if(Keyboard.isKeyDown(keyboardstatusindex))
		{
			this.keyboardstatus = KeyboardStatus.KEY_DOWN;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onResumeInputEvent()
	{
		if(!Keyboard.isKeyDown(keyboardstatusindex))
		{
			this.keyboardstatus = KeyboardStatus.KEY_RELEASED;
			return true;
		}
		return false;
	}
	
	public KeyboardStatus getKeyboardStatus()
	{
		return this.keyboardstatus;
	}
	
}