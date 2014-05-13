package net.aegistudio.lwjgl.input;

public interface InputEventListener
{
	public void onInputEventResponse(InputEventMonitor inputeventmonitor);
	public void onInputEventResume(InputEventMonitor inputeventmonitor);
	public void onInputEventException(InputEventException inputeventexception);
}