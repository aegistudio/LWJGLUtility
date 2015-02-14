package net.aegistudio.lwjgl.opengl.image;

public interface Image
{	
	public Object getRasterData();
	
	public int getWidth();
	
	public int getHeight();
	
	public int getPixelFormat();
	
	public int getPixelType();
}
