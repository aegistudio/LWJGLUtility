package net.aegistudio.transparent.opengl.glsl;

import org.lwjgl.opengl.GL20;

import net.aegistudio.transparent.opengl.model.ArrayPointer;

public class VertexAttributePointer implements ArrayPointer
{
	public VertexAttribute vertexAttribute;
	public int index = 0, size;
	public boolean normalized = false;
	
	public VertexAttributePointer(VertexAttribute attribute)
	{
		this.vertexAttribute = attribute;
		this.size = vertexAttribute.dataType.size;
	}
	
	@Override
	public void enable()
	{
		GL20.glEnableVertexAttribArray(index);
	}

	@Override
	public void arrayPointer(int size, int type, int stride, long offset)
	{
		GL20.glVertexAttribPointer(index, size, type, normalized, stride, offset);
	}

	@Override
	public void disable()
	{
		GL20.glDisableVertexAttribArray(index);
	}

	@Override
	public int getDefaultSize()
	{
		return this.size;
	}

	@Override
	public int identify()
	{
		return 100 + this.vertexAttribute.vertexAttributeLocation;
	}
}
