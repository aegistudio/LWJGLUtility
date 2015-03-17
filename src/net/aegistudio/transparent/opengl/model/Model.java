package net.aegistudio.transparent.opengl.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.opengl.GL11;

import net.aegistudio.transparent.opengl.Container;
import net.aegistudio.transparent.opengl.Drawable;
import net.aegistudio.transparent.opengl.util.EnumPrimitive;
import net.aegistudio.transparent.opengl.util.VertexBufferObject;

public class Model implements Drawable
{
	protected final ArrayPointerEntry[] usingArrayPointer;
	protected final int vertices_count;
	protected final boolean scoping;
	protected VertexArrayObject vao = new VertexArrayObject();
	
	public Model(Map<ArrayPointer, VertexBufferObject> bufferedModel, boolean scoping)
	{
		VertexBufferObject vertexArray = bufferedModel.get(EnumArrayPointer.VERTEX);
		if(vertexArray == null) throw new IllegalArgumentException("The vertex buffer object of the vertices should be assigned!");
		
		this.scoping = scoping;
		this.vertices_count = vertexArray.getLength() / EnumArrayPointer.VERTEX.getDefaultSize();
		
		Set<ArrayPointerEntry> usingArrayPointerSet = new TreeSet<ArrayPointerEntry>();
		Set<ArrayPointer> keys = bufferedModel.keySet(); VertexBufferObject vbo;
		for(ArrayPointer key : keys) if((vbo = bufferedModel.get(key)) != null)
			usingArrayPointerSet.add(new ArrayPointerEntry(key, vbo));
		
		this.usingArrayPointer = usingArrayPointerSet.toArray(new ArrayPointerEntry[0]);
	}
	
	public Model(ArrayPointerEntry[] entries, boolean scoping)
	{
		this.scoping = scoping;
		int vertices_count = 0;
		
		boolean hasVerticesBuffer = false;
		Set<ArrayPointerEntry> usingArrayPointerSet = new TreeSet<ArrayPointerEntry>();
		for(ArrayPointerEntry entry : entries) if(entry != null)
		{
			if(entry.arrayPointer == EnumArrayPointer.VERTEX)
			{
				hasVerticesBuffer = true;
				vertices_count = entry.vbo.getLength() / entry.size;
			}
			usingArrayPointerSet.add(entry);
		}
		if(!hasVerticesBuffer) throw new IllegalArgumentException("The vertex buffer object of the vertices should be assigned!"); 
		this.usingArrayPointer = usingArrayPointerSet.toArray(new ArrayPointerEntry[0]);
		this.vertices_count = vertices_count;
	}
	
	public Model(boolean scoping, ArrayPointerEntry... entries)
	{
		this(entries, scoping);
	}
	
	public Model(ArrayPointerEntry... entries)
	{
		this(entries, false);
	}
	
	public Model(Map<ArrayPointer, VertexBufferObject> bufferedModel, int vertices_count)
	{
		this(bufferedModel, false);
	}
	
	@Override
	public void onInit(Container container)
	{
		vao.create();
		vao.bind();
		for(ArrayPointerEntry entry : usingArrayPointer)
		{
			entry.vbo.create();
			entry.arrayPointer.enable();
			entry.vbo.bind();
			entry.arrayPointer.arrayPointer(entry.size, entry.vbo.getBufferType().inferGLType(), entry.stride, entry.offset);
			entry.vbo.unbind();
		}
		vao.unbind();
		for(ArrayPointerEntry entry : usingArrayPointer) entry.arrayPointer.disable();
	}

	protected int primitiveMode = EnumPrimitive.QUADS.stateId;
	public void setPrimitive(EnumPrimitive primitive)
	{
		this.primitiveMode = primitive.stateId;
	}
	
	@Override
	public void onUpdate(Container container)
	{
		vao.bind();
		GL11.glDrawArrays(primitiveMode, 0, vertices_count);
		vao.unbind();
	}

	@Override
	public void onDestroy(Container container)
	{
		vao.destroy();
		if(scoping) for(ArrayPointerEntry entry : usingArrayPointer) entry.vbo.destroy();
	}
	
	public ArrayPointerEntry[] getArrayPointers()
	{
		return this.usingArrayPointer;
	}
}
