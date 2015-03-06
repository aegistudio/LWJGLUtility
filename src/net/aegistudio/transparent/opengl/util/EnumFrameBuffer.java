package net.aegistudio.transparent.opengl.util;

import org.lwjgl.opengl.GL11;

public enum EnumFrameBuffer
{
	COLOR(GL11.GL_COLOR_BUFFER_BIT),
	DEPTH(GL11.GL_DEPTH_BUFFER_BIT),
	STENCIL(GL11.GL_STENCIL_BUFFER_BIT),
	ACCUM(GL11.GL_ACCUM_BUFFER_BIT);
	
	public final int bufferBit;
	
	private EnumFrameBuffer(int bufferBit)
	{
		this.bufferBit = bufferBit;
	}
}
