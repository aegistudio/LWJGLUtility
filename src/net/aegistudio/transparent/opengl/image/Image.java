package net.aegistudio.transparent.opengl.image;

import net.aegistudio.transparent.opengl.util.EnumPixelFormat;

public interface Image
{	
	public Object getRasterData();
	
	public int getWidth();
	
	public int getHeight();
	
	public EnumPixelFormat getPixelFormat();
	
	public int getPixelType();
}
