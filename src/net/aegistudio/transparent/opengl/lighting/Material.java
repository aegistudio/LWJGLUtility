package net.aegistudio.transparent.opengl.lighting;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.aegistudio.transparent.opengl.util.DisplayList;
import net.aegistudio.transparent.util.Bindable;

public class Material extends DisplayList implements Bindable
{
	protected int face;
	
	public Material()
	{
		super();
		this.setFace(Face.BOTH);
		this.ambient(0.2f, 0.2f, 0.2f, 1.0f);
		this.diffuse(0.8f, 0.8f, 0.8f, 1.0f);
		this.specular(0.8f, 0.8f, 0.8f, 1.0f);
		this.emission(0.1f, 0.1f, 0.1f, 1.0f);
		this.shiness(0.0f);
	}
	
	public void setFace(Face face)
	{
		this.face = face.faceParameter;
		this.isDirty = true;
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
	
	protected boolean isDirty = true;
	@Override
	public void bind()
	{
		if(isDirty)
		{
			this.create(true, true);
			isDirty = false;
		}
		else this.call(0);
	}

	@Override
	public void unbind()
	{

	}
	
	protected FloatBuffer ambient;
	
	public void ambient(float r, float g, float b, float a)
	{
		this.ambient = BufferUtils.createFloatBuffer(4).put(new float[]{r, g, b, a});
		this.ambient.flip();
		this.isDirty = true;
	}
	
	protected FloatBuffer diffuse;
	
	public void diffuse(float r, float g, float b, float a)
	{
		this.diffuse = BufferUtils.createFloatBuffer(4).put(new float[]{r, g, b, a});
		this.diffuse.flip();
		this.isDirty = true;
	}
	
	protected FloatBuffer specular;
	public void specular(float r, float g, float b, float a)
	{
		this.specular = BufferUtils.createFloatBuffer(4).put(new float[]{r, g, b, a});
		this.specular.flip();
		this.isDirty = true;
	}
	
	protected FloatBuffer emission;
	public void emission(float r, float g, float b, float a)
	{
		this.emission = BufferUtils.createFloatBuffer(4).put(new float[]{r, g, b, a});
		this.emission.flip();
		this.isDirty = true;
	}
	
	protected float shiness;
	public void shiness(float shiness)
	{
		this.shiness = shiness;
		this.isDirty = true;
	}

	@Override
	protected void display(int displayIndex)
	{
		GL11.glMaterial(face, GL11.GL_AMBIENT, ambient);
		GL11.glMaterial(face, GL11.GL_DIFFUSE, diffuse);
		GL11.glMaterial(face, GL11.GL_SPECULAR, specular);
		GL11.glMaterial(face, GL11.GL_EMISSION, emission);
		GL11.glMaterialf(face, GL11.GL_SHININESS, shiness);
	}
}
