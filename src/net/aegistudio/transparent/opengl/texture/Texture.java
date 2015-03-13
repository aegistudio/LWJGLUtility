package net.aegistudio.transparent.opengl.texture;

import net.aegistudio.transparent.util.Bindable;
import net.aegistudio.transparent.util.BindingFailureException;
import net.aegistudio.transparent.util.Scoped;

import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public abstract class Texture implements Scoped, Bindable
{
	protected int textureId = 0;
	protected int pixelFormat;
	protected int pixelType;
	protected int width;
	protected int height;
	protected int texTarget;
	
	public abstract int create(int innerFormat, int mipmapLevels);
	
	public int create()
	{
		return this.create(this.pixelFormat, 0);
	}
	
	protected void settingTextureParameters()
	{
		GL11.glTexParameteri(texTarget, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(texTarget, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(texTarget, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(texTarget, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	}
	
	public void destroy()
	{
		if(this.textureId != 0)
		{
			GL11.glDeleteTextures(this.textureId);
			this.textureId = 0;
		}
	}
	
	protected void settingTextureEnvironments()
	{
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL13.GL_COMBINE);
	}
	
	public void bind()
	{
		if(this.textureId == 0) throw new BindingFailureException("You must create the texture before binding it!");
		allocateMultiTexture();
		ARBMultitexture.glActiveTextureARB(ARBMultitexture.GL_TEXTURE0_ARB + textureBias);
		GL11.glEnable(this.texTarget);
		this.settingTextureEnvironments();
		GL11.glBindTexture(this.texTarget, this.textureId);
	}
	
	public void unbind()
	{
		if(this.textureId == 0) throw new BindingFailureException("You must create the texture before unbinding it!");
		deallocateMultiTexture();
		GL11.glBindTexture(this.texTarget, 0);
		ARBMultitexture.glActiveTextureARB(ARBMultitexture.GL_TEXTURE0_ARB + textureBias);
		GL11.glDisable(this.texTarget);
	}
	
	public void addVertexWithST(double x, double y, double z, double s, double t)
	{
		ARBMultitexture.glMultiTexCoord2dARB(ARBMultitexture.GL_TEXTURE0_ARB + textureBias, s, t);
		GL11.glVertex3d(x, y, z);
	}
	
	public void addVertexWithST(double x, double y, double s, double t)
	{
		ARBMultitexture.glMultiTexCoord2dARB(ARBMultitexture.GL_TEXTURE0_ARB + textureBias, s, t);
		GL11.glVertex2d(x, y);
	}
	
	public void addVertexWithSTR(double x, double y, double z, double s, double t, double r)
	{
		ARBMultitexture.glMultiTexCoord3dARB(ARBMultitexture.GL_TEXTURE0_ARB + textureBias, s, t, r);
		GL11.glVertex3d(x, y, z);
	}
	
	public void addVertexWithSTRQ(double x, double y, double z, double s, double t, double r, double q)
	{
		ARBMultitexture.glMultiTexCoord4dARB(ARBMultitexture.GL_TEXTURE0_ARB + textureBias, s, t, r, q);
		GL11.glVertex3d(x, y, z);
	}
	
	public void finalize() throws Throwable
	{
		this.destroy();
		super.finalize();
	}
	
	protected int textureBias = -1;
	public static int maxMultiTextureUnit = -1;
	public static boolean[] texUnitAllocation;
	
	protected void allocateMultiTexture()
	{
		if(textureBias >= 0) return;
		if(maxMultiTextureUnit < 0)
		{
			maxMultiTextureUnit = GL11.glGetInteger(ARBMultitexture.GL_MAX_TEXTURE_UNITS_ARB);
			texUnitAllocation = new boolean[maxMultiTextureUnit];
		}
		for(int i = 0; i < maxMultiTextureUnit; i ++)
		{
			if(!texUnitAllocation[i])
			{
				texUnitAllocation[i] = true;
				textureBias = i;
				return;
			}
		}
		throw new BindingFailureException("All multi-texture unit has been allocated!");
	}
	
	protected void deallocateMultiTexture()
	{
		if(textureBias < 0) return;
		texUnitAllocation[textureBias] = false;
		textureBias = -1;
	}
	
	public int getTextureId()
	{
		return textureId;
	}
	
	public int getMultiTextureUnit()
	{
		return textureBias;
	}
}
