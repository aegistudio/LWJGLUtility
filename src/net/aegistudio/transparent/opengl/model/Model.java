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
	
	public Model(Map<EnumArrayPointer, VertexBufferObject> bufferedModel, int vertices_count, boolean scoping)
	{
		if(bufferedModel.get(EnumArrayPointer.VERTEX) == null) throw new IllegalArgumentException("The vertex buffer object of the vertices should be assigned!");
		
		this.scoping = scoping;
		this.vertices_count = vertices_count;
		
		Set<ArrayPointerEntry> usingArrayPointerSet = new TreeSet<ArrayPointerEntry>();
		Set<EnumArrayPointer> keys = bufferedModel.keySet(); VertexBufferObject vbo;
		for(EnumArrayPointer key : keys) if((vbo = bufferedModel.get(key)) != null)
			usingArrayPointerSet.add(new ArrayPointerEntry(key, vbo));
		
		this.usingArrayPointer = usingArrayPointerSet.toArray(new ArrayPointerEntry[0]);
	}
	
	public Model(ArrayPointerEntry[] entries, int vertices_count, boolean scoping)
	{
		this.scoping = scoping;
		this.vertices_count = vertices_count;
		boolean hasVerticesBuffer = false;
		Set<ArrayPointerEntry> usingArrayPointerSet = new TreeSet<ArrayPointerEntry>();
		for(ArrayPointerEntry entry : entries) if(entry != null)
		{
			if(entry.arrayPointer == EnumArrayPointer.VERTEX) hasVerticesBuffer = true;
			usingArrayPointerSet.add(entry);
		}
		if(!hasVerticesBuffer) throw new IllegalArgumentException("The vertex buffer object of the vertices should be assigned!"); 
		this.usingArrayPointer = usingArrayPointerSet.toArray(new ArrayPointerEntry[0]);
	}
	
	public Model(int vertices_count, boolean scoping, ArrayPointerEntry... entries)
	{
		this(entries, vertices_count, scoping);
	}
	
	public Model(int vertices_count, ArrayPointerEntry... entries)
	{
		this(entries, vertices_count, false);
	}
	
	public Model(Map<EnumArrayPointer, VertexBufferObject> bufferedModel, int vertices_count)
	{
		this(bufferedModel, vertices_count, false);
	}
	
	@Override
	public void onInit(Container container)
	{
		if(scoping) for(ArrayPointerEntry entry : usingArrayPointer) entry.vbo.create();
	}

	protected int primitiveMode = EnumPrimitive.QUADS.stateId;
	public void setPrimitive(EnumPrimitive primitive)
	{
		this.primitiveMode = primitive.stateId;
	}
	
	@Override
	public void onDraw(Container container)
	{
		for(ArrayPointerEntry entry : usingArrayPointer)
		{
			GL11.glEnableClientState(entry.arrayPointer.stateName);
			entry.vbo.bind();
			entry.arrayPointer.arrayPointer(entry.size, entry.vbo.getBufferType().inferGLType(), entry.stride, entry.offset);
			entry.vbo.unbind();
		}
		
		GL11.glDrawArrays(primitiveMode, 0, vertices_count);
		
		for(ArrayPointerEntry entry : usingArrayPointer)
			GL11.glDisableClientState(entry.arrayPointer.stateName);
	}

	@Override
	public void onDestroy(Container container)
	{
		if(scoping) for(ArrayPointerEntry entry : usingArrayPointer) entry.vbo.destroy();
	}
}
