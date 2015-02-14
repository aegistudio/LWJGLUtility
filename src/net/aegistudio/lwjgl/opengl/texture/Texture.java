package net.aegistudio.lwjgl.opengl.texture;

import net.aegistudio.lwjgl.opengl.util.BindingFailureException;

import org.lwjgl.opengl.GL11;

public abstract class Texture
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
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
	}
	
	public void bind()
	{
		if(this.textureId == 0) throw new BindingFailureException("You must create the texture before binding it!");
		GL11.glEnable(this.texTarget);
		this.settingTextureEnvironments();
		GL11.glBindTexture(this.texTarget, this.textureId);
	}
	
	public void unbind()
	{
		if(this.textureId == 0) throw new BindingFailureException("You must create the texture before unbinding it!");
		GL11.glBindTexture(this.texTarget, 0);
		GL11.glDisable(this.texTarget);
	}
	
	public void addVertexWithST(double x, double y, double z, double s, double t)
	{
		GL11.glTexCoord2d(s, t);
		GL11.glVertex3d(x, y, z);
	}
	
	public void addVertexWithST(double x, double y, double s, double t)
	{
		GL11.glTexCoord2d(s, t);
		GL11.glVertex2d(x, y);
	}
	
	public void addVertexWithSTR(double x, double y, double z, double s, double t, double r)
	{
		GL11.glTexCoord3d(s, t, r);
		GL11.glVertex3d(x, y, z);
	}
	
	public void addVertexWithSTRQ(double x, double y, double z, double s, double t, double r, double q)
	{
		GL11.glTexCoord4d(s, t, r, q);
		GL11.glVertex3d(x, y, z);
	}
}
