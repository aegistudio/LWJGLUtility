package net.aegistudio.transparent.opengl.glsl;

public class Uniform
{
	protected final int uniformLocation;
	protected final EnumShaderData dataType;
	
	public Uniform(int vertexAttributeLocation, EnumShaderData dataType)
	{
		this.uniformLocation = vertexAttributeLocation;
		this.dataType = dataType;
	}
	
	public void set(Object... objects)
	{
		this.dataType.uniform(uniformLocation, objects);
	}
}
