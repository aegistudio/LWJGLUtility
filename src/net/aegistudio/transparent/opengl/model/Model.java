package net.aegistudio.transparent.opengl.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.opengl.GL11;

import net.aegistudio.transparent.opengl.Container;
import net.aegistudio.transparent.opengl.Drawable;
import net.aegistudio.transparent.opengl.util.VertexBufferObject;

public class Model implements Drawable
{
	protected final ArrayPointerEntry[] usingArrayPointer;
	protected ArrayPointerEntry indexPointer;
	
	protected final boolean scoping;
	
	public Model(Map<EnumArrayPointer, VertexBufferObject> bufferedModel, int indices_count, boolean scoping)
	{
		if(bufferedModel.get(EnumArrayPointer.INDEX) == null) throw new IllegalArgumentException("The vertex buffer object of the indices should be assigned!");
		if(bufferedModel.get(EnumArrayPointer.VERTEX) == null) throw new IllegalArgumentException("The vertex buffer object of the vertices should be assigned!");
		
		this.scoping = scoping;
		Set<ArrayPointerEntry> usingArrayPointerSet = new TreeSet<ArrayPointerEntry>();
		Set<EnumArrayPointer> keys = bufferedModel.keySet(); VertexBufferObject vbo;
		for(EnumArrayPointer key : keys) if((vbo = bufferedModel.get(key)) != null)
		{
			if(key != EnumArrayPointer.INDEX) usingArrayPointerSet.add(new ArrayPointerEntry(key, vbo));
			else this.indexPointer = new ArrayPointerEntry(EnumArrayPointer.INDEX, indices_count, vbo);
		}
		
		this.usingArrayPointer = usingArrayPointerSet.toArray(new ArrayPointerEntry[0]);
	}
	
	public Model(ArrayPointerEntry[] entries, boolean scoping)
	{
		this.scoping = scoping;
		boolean hasVerticesBuffer = false;
		Set<ArrayPointerEntry> usingArrayPointerSet = new TreeSet<ArrayPointerEntry>();
		for(ArrayPointerEntry entry : entries) if(entry != null)
		{
			if(entry.arrayPointer == EnumArrayPointer.VERTEX) hasVerticesBuffer = true;
			else if(entry.arrayPointer == EnumArrayPointer.INDEX) this.indexPointer = entry;
			else usingArrayPointerSet.add(entry);
		}
		if(!hasVerticesBuffer) throw new IllegalArgumentException("The vertex buffer object of the vertices should be assigned!");
		if(this.indexPointer == null) throw new IllegalArgumentException("The vertex buffer object of the indices should be assigned!"); 
		this.usingArrayPointer = usingArrayPointerSet.toArray(new ArrayPointerEntry[0]);
	}
	
	public Model(Map<EnumArrayPointer, VertexBufferObject> bufferedModel, int indices_count)
	{
		this(bufferedModel, indices_count, false);
	}
	
	public Model(ArrayPointerEntry[] entries)
	{
		this(entries, false);
	}
	
	@Override
	public void onInit(Container container)
	{
		if(scoping)
		{
			this.indexPointer.vbo.create();
			for(ArrayPointerEntry entry : usingArrayPointer) entry.vbo.create();
		}
	}

	protected int mode = GL11.GL_POLYGON;
	public void setMode(int drawMode)
	{
		this.mode = drawMode;
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
		
		this.indexPointer.vbo.bind();
		GL11.glDrawElements(mode, indexPointer.size, this.indexPointer.vbo.getBufferType().inferGLType(), this.indexPointer.offset);
		this.indexPointer.vbo.unbind();
		
		for(ArrayPointerEntry entry : usingArrayPointer)
			GL11.glDisableClientState(entry.arrayPointer.stateName);
	}

	@Override
	public void onDestroy(Container container)
	{
		if(scoping)
		{
			this.indexPointer.vbo.destroy();
			for(ArrayPointerEntry entry : usingArrayPointer) entry.vbo.destroy();
		}
	}
}
