package net.aegistudio.transparent.opengl.texture;

import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.GL11;

import net.aegistudio.transparent.opengl.model.ArrayPointer;
import net.aegistudio.transparent.opengl.model.EnumArrayPointer;
import net.aegistudio.transparent.opengl.util.FeatureNotSupportedException;

public class MultiTextureCoordPointer implements ArrayPointer
{
	protected int textureUnit = 0;
	
	public void setTextureUnit(int textureUnit)
	{
		this.textureUnit = textureUnit;
	}
	
	@Override
	public void enable()
	{
		int maxTextureUnit = GL11.glGetInteger(ARBMultitexture.GL_MAX_TEXTURE_UNITS_ARB);
		if(textureUnit < maxTextureUnit) ARBMultitexture.glClientActiveTextureARB(ARBMultitexture.GL_TEXTURE0_ARB + textureUnit);
		else throw new FeatureNotSupportedException("multitexture unit #" + textureUnit);
	}

	@Override
	public void arrayPointer(int size, int type, int stride, long offset)
	{
		EnumArrayPointer.TEXTURE.arrayPointer(size, type, stride, offset);
	}

	@Override
	public void disable()
	{
		ARBMultitexture.glClientActiveTextureARB(ARBMultitexture.GL_TEXTURE0_ARB);
	}

	@Override
	public int getDefaultSize()
	{
		return EnumArrayPointer.TEXTURE.getDefaultSize();
	}

	@Override
	public int identify()
	{
		return 200 + textureUnit;
	}
}
