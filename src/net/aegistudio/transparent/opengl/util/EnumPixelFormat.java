package net.aegistudio.transparent.opengl.util;

import org.lwjgl.opengl.GL11;

public enum EnumPixelFormat
{
	INDEX(GL11.GL_COLOR_INDEX),
	RED(GL11.GL_RED), GREEN(GL11.GL_GREEN), BLUE(GL11.GL_BLUE),
	RGBA(GL11.GL_RGBA), RGB(GL11.GL_RGB),
	ALPHA(GL11.GL_ALPHA), LUMINANCE(GL11.GL_LUMINANCE),
	LUMINALPHA(GL11.GL_LUMINANCE_ALPHA),
	STENCIL(GL11.GL_STENCIL_INDEX), DEPTH(GL11.GL_DEPTH_COMPONENT);
	
	public final int stateId;
	
	private EnumPixelFormat(int stateId)
	{
		this.stateId = stateId;
	}
}
