package net.aegistudio.lwjgl.opengl.lighting;

import org.lwjgl.opengl.GL11;

import net.aegistudio.lwjgl.util.Bindable;
import net.aegistudio.lwjgl.util.Scoped;

public class Material implements Scoped, Bindable
{
	protected final Face face;
	
	public Material(Face face)
	{
		this.face = face;
	}
	
	public enum Face
	{
		FRONT(GL11.GL_FRONT),
		BACK(GL11.GL_BACK),
		BOTH(GL11.GL_FRONT_AND_BACK);
		
		int faceParameter;
		
		private Face(int faceParameter)
		{
			this.faceParameter = faceParameter;
		}
	}
	
	@Override
	public void bind()
	{
		
	}

	@Override
	public void unbind()
	{

	}
	
	@Override
	public int create()
	{
		return 0;
	}

	@Override
	public void destroy()
	{
		
	}
	
}
