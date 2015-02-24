package net.aegistudio.transparent.opengl.util;

import org.lwjgl.opengl.ARBCopyBuffer;
import org.lwjgl.opengl.ARBPixelBufferObject;
import org.lwjgl.opengl.ARBUniformBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL30;

public enum EnumBufferTarget
{
	ARRAY(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB),
	ELEMENT_ARRAY(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB),
	PIXEL_UNPACK(ARBPixelBufferObject.GL_PIXEL_UNPACK_BUFFER_ARB),
	PIXEL_PACK(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB),
	COPY_READ(ARBCopyBuffer.GL_COPY_READ_BUFFER),
	COPY_WRITE(ARBCopyBuffer.GL_COPY_WRITE_BUFFER),
	TRANSFORM_FEEDBACK(GL30.GL_TRANSFORM_FEEDBACK_BUFFER),
	UNIFORM(ARBUniformBufferObject.GL_UNIFORM_BUFFER);
	
	public final int stateId;
	
	private EnumBufferTarget(int stateId)
	{
		this.stateId = stateId;
	}
}
