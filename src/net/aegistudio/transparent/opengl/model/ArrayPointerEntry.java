package net.aegistudio.transparent.opengl.model;

import net.aegistudio.transparent.opengl.util.VertexBufferObject;

public class ArrayPointerEntry implements Comparable<ArrayPointerEntry>
{
	EnumArrayPointer arrayPointer;
	int size, stride; long offset;
	VertexBufferObject vbo;
	
	public ArrayPointerEntry(EnumArrayPointer arrayPointer, int size, int stride, VertexBufferObject vbo, long offset)
	{
		this.arrayPointer = arrayPointer;
		this.size = size; this.stride = stride;
		this.vbo = vbo; this.offset = offset;
	}
	
	public ArrayPointerEntry(EnumArrayPointer arrayPointer, int size, VertexBufferObject vbo)
	{
		this(arrayPointer, size, 0, vbo, 0);
	}
	
	public ArrayPointerEntry(EnumArrayPointer arrayPointer, VertexBufferObject vbo)
	{
		this(arrayPointer, arrayPointer.defaultSize, vbo);
	}

	@Override
	public int compareTo(ArrayPointerEntry arg0)
	{
		return new Integer(this.arrayPointer.ordinal()).compareTo(new Integer(arg0.arrayPointer.ordinal()));
	}
}
