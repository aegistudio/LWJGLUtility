package net.aegistudio.transparent.opengl.glsl;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBTessellationShader;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.EXTGeometryShader4;
import org.lwjgl.opengl.GLContext;

public enum EnumShaderType
{
	VERTEX(ARBVertexShader.GL_VERTEX_SHADER_ARB, "vertex shader")
	{
		@Override
		public boolean checkCapability()
		{
			return GLContext.getCapabilities().GL_ARB_vertex_shader;
		}
	},
	GEOMETRY(EXTGeometryShader4.GL_GEOMETRY_SHADER_EXT, "geometry shader")
	{
		@Override
		public boolean checkCapability()
		{
			return GLContext.getCapabilities().GL_EXT_geometry_shader4;
		}
	},
	TESSELLATION_CONTROL(ARBTessellationShader.GL_TESS_CONTROL_SHADER, "tessellation shader")
	{
		@Override
		public boolean checkCapability()
		{
			return GLContext.getCapabilities().GL_ARB_tessellation_shader;
		}
	},
	TESSELLATION_EVALUATION(ARBTessellationShader.GL_TESS_EVALUATION_SHADER, "tessellation shader")
	{
		@Override
		public boolean checkCapability()
		{
			return GLContext.getCapabilities().GL_ARB_tessellation_shader;
		}
	},
	FRAGMENT(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB, "fragment shader")
	{
		@Override
		public boolean checkCapability()
		{
			return GLContext.getCapabilities().GL_ARB_fragment_shader;
		}
	};
	
	public final int stateId;
	public final String shaderName;
	
	private EnumShaderType(int stateId, String shaderName)
	{
		this.stateId = stateId;
		this.shaderName = shaderName;
	}
	
	public abstract boolean checkCapability();
}
