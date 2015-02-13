package net.aegistudio.lwjgl.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import net.aegistudio.lwjgl.graphic.Container;
import net.aegistudio.lwjgl.graphic.Drawable;
import net.aegistudio.lwjgl.util.VertexBufferObject;

public class VertexCoordPool implements Drawable
{

	private final VertexBufferObject vboPool;
	
	public VertexCoordPool(Object[] vertices)
	{
		this.vboPool = new VertexBufferObject(GL11.GL_VERTEX_ARRAY, GL15.GL_STATIC_DRAW, vertices);
	}
	
	@Override
	public void onInit(Container container)
	{
		this.vboPool.create();
	}

	@Override
	public void onDraw(Container container)
	{
		
	}

	@Override
	public void onDestroy(Container container)
	{
		this.vboPool.destroy();
	}
	
}
