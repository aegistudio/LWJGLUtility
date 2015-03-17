package net.aegistudio.transparent.opengl.model;

import org.lwjgl.opengl.ARBVertexArrayObject;
import org.lwjgl.opengl.GLContext;

import net.aegistudio.transparent.opengl.util.FeatureNotSupportedException;
import net.aegistudio.transparent.util.Bindable;
import net.aegistudio.transparent.util.BindingFailureException;
import net.aegistudio.transparent.util.Resource;

public class VertexArrayObject implements Resource, Bindable
{
	protected int arrayObjectId = 0;
	
	public void create()
	{
		if(this.arrayObjectId == 0)
		{
			if(!GLContext.getCapabilities().GL_ARB_vertex_array_object) throw new FeatureNotSupportedException("vertex array object");
			this.arrayObjectId = ARBVertexArrayObject.glGenVertexArrays();
			if(this.arrayObjectId == 0) throw new BindingFailureException("Fail to create space for the vertex array object!");
		}
	}
	
	public int getArrayObjectId()
	{
		return this.arrayObjectId;
	}
	
	/**
	 * Set the current vertex array object in use.
	 */
	public void bind()
	{
		if(arrayObjectId == 0) throw new BindingFailureException("You must create the array before binding it!");
		ARBVertexArrayObject.glBindVertexArray(arrayObjectId);
	}
	
	/**
	 * Set the current vertex array object not in use.
	 */
	public void unbind()
	{
		if(arrayObjectId == 0) throw new BindingFailureException("You must create the array before unbinding it!");
		ARBVertexArrayObject.glBindVertexArray(0);
	}
	
	/**
	 * Remove / release the vertex array object.
	 */
	public void destroy()
	{
		if(this.arrayObjectId != 0)
		{
			ARBVertexArrayObject.glDeleteVertexArrays(arrayObjectId);
			this.arrayObjectId = 0;
		}
	}
}
