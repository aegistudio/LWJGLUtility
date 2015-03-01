package net.aegistudio.transparent.opengl.model;

public interface ArrayPointer
{
	public void enable();
	
	public void arrayPointer(int size, int type, int stride, long offset);
	
	public void disable();
	
	public int getDefaultSize();
	
	public int identify();
}
