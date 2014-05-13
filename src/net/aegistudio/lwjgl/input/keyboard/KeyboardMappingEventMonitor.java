package net.aegistudio.lwjgl.input.keyboard;

import org.lwjgl.input.Keyboard;

import net.aegistudio.lwjgl.input.*;

public class KeyboardMappingEventMonitor extends InputEventMonitor 
{
	
	private final boolean[] keyboardmapping = new boolean[Keyboard.KEYBOARD_SIZE];
	private int keyboardstatuschangedindex;
	
	public KeyboardMappingEventMonitor(InputEventListener inputeventlistener)
	{
		super(inputeventlistener);
		this.keyboardstatuschangedindex = 0;
	}
	
	public boolean onCheckInputEvent()
	{
		boolean shouldchangestate = Keyboard.next();
		if(shouldchangestate)
		{
			this.keyboardstatuschangedindex = Keyboard.getEventKey();
			this.keyboardmapping[this.keyboardstatuschangedindex] = !this.keyboardmapping[this.keyboardstatuschangedindex];
		}
		return shouldchangestate;
	}
	
	public boolean onResumeInputEvent()
	{
		this.keyboardstatuschangedindex = 0;
		return true;
	}
	
	public KeyboardStatus getKeyboardStatus(int keyindex)
	{
		return (this.keyboardmapping[keyindex])? KeyboardStatus.KEY_DOWN:KeyboardStatus.KEY_RELEASED;
	}
	
	public int getKeyboardStatusChangedIndex()
	{
		return this.keyboardstatuschangedindex;
	}
}