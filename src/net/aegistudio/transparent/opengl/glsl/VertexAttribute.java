package net.aegistudio.transparent.opengl.glsl;

public class VertexAttribute
{
	protected final int vertexAttributeLocation;
	protected final EnumShaderData dataType;
	
	public VertexAttribute(int vertexAttributeLocation, EnumShaderData dataType)
	{
		this.vertexAttributeLocation = vertexAttributeLocation;
		this.dataType = dataType;
	}
	
	public void set(Object... objects)
	{
		this.dataType.vertexAttribute(vertexAttributeLocation, objects);
	}
}
