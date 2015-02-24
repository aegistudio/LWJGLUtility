package net.aegistudio.transparent.opengl.util;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public enum EnumBufferTarget
{
	ARRAY(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB),
	ELEMENT_ARRAY(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB),
	PIXEL_UNPACK(GL21.GL_PIXEL_UNPACK_BUFFER),
	PIXEL_PACK(GL21.GL_PIXEL_PACK_BUFFER),
	COPY_READ(GL31.GL_COPY_READ_BUFFER),
	COPY_WRITE(GL31.GL_COPY_WRITE_BUFFER),
	TRANSFORM_FEEDBACK(GL30.GL_TRANSFORM_FEEDBACK_BUFFER),
	UNIFORM(GL31.GL_UNIFORM_BUFFER);
	
	public final int stateId;
	
	private EnumBufferTarget(int stateId)
	{
		this.stateId = stateId;
	}
}
