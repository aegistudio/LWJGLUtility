package net.aegistudio.transparent.opengl.util;

import org.lwjgl.opengl.ARBBufferObject;

public enum EnumBufferUsage
{
	STATIC_DRAW(ARBBufferObject.GL_STATIC_DRAW_ARB),
	STATIC_READ(ARBBufferObject.GL_STATIC_READ_ARB),
	STATIC_COPY(ARBBufferObject.GL_STATIC_COPY_ARB),
	
	STREAM_DRAW(ARBBufferObject.GL_STREAM_DRAW_ARB),
	STREAM_READ(ARBBufferObject.GL_STREAM_READ_ARB),
	STREAM_COPY(ARBBufferObject.GL_STREAM_COPY_ARB),
	
	DYNAMIC_DRAW(ARBBufferObject.GL_DYNAMIC_DRAW_ARB),
	DYNAMIC_READ(ARBBufferObject.GL_DYNAMIC_READ_ARB),
	DYNAMIC_COPY(ARBBufferObject.GL_DYNAMIC_COPY_ARB);
	
	public final int stateId;
	
	private EnumBufferUsage(int stateId)
	{
		this.stateId = stateId;
	}
}
