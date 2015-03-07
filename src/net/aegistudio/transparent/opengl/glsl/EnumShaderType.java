package net.aegistudio.transparent.opengl.glsl;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBVertexShader;

public enum EnumShaderType
{
	VERTEX(ARBVertexShader.GL_VERTEX_SHADER_ARB),
	FRAGMENT(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
	
	public final int stateId;
	
	private EnumShaderType(int stateId)
	{
		this.stateId = stateId;
	}
}
