package net.aegistudio.lwjgl.input.cursor;

public interface CursorBoundingEventMonitor
{
	public CursorBoundingEvent getCursorBoundingEvent();
	public void startInputEventMonitor();
	public void stopInputEventMonitor();
	
	public enum CursorBoundingEvent
	{
		ON_LOST_FOCUS,
		ON_FOCUS,
		ON_CLICK,
		ON_RELEASE,
	}
}