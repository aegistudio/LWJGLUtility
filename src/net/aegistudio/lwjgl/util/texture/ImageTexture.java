package net.aegistudio.lwjgl.util.texture;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.aegistudio.lwjgl.util.BindingFailureException;
import net.aegistudio.lwjgl.util.BufferHelper;
import net.aegistudio.lwjgl.util.EnumDataType;
import net.aegistudio.lwjgl.util.BufferHelper.BufferProcessor;
import net.aegistudio.lwjgl.util.image.Image;

public class ImageTexture
{
	protected int textureId = 0;
	protected int pixelFormat;
	protected int pixelType;
	protected int width;
	protected int height;
	protected int texTarget;
	
	private ByteBuffer buffer;
	
	public ImageTexture(Image image, int texTarget)
	{
		this(image.getRasterData(), image.getPixelFormat(), image.getPixelType(), image.getWidth(), image.getHeight(), texTarget);
	}
	
	public ImageTexture(Image image)
	{
		this(image, GL11.GL_TEXTURE_2D);
	}
	
	public ImageTexture(Object texture, int pixelFormat, int pixelType, int width, int height, int texTarget)
	{
		if(texture == null) throw new IllegalArgumentException("Buffer should not be null!");
		this.texTarget = texTarget;
		this.pixelFormat = pixelFormat;
		this.pixelType = pixelType;
		this.width = width;
		this.height = height;
		
		Class<?> clz = texture.getClass().getComponentType();
		if(clz == null) throw new IllegalArgumentException("Unable to create buffer for not an array!");
		EnumDataType dataType = EnumDataType.getDataType(clz);
		if(dataType == null) throw new IllegalArgumentException("Unable to create buffer for given type!");
		
		int arrayLength = Array.getLength(texture);
		this.buffer = BufferUtils.createByteBuffer(arrayLength * dataType.getDataTypeSize());
		
		BufferProcessor processor = BufferHelper.getBufferProcessor(dataType);
		for(int i = 0; i < arrayLength; i ++) processor.putBuffer(this.buffer, texture, i);
		this.buffer.flip();
	}
	
	public int create(int innerFormat, int mipmapLevels)
	{
		if(this.textureId == 0)
		{
			this.textureId = GL11.glGenTextures();
			if(this.textureId == 0) throw new BindingFailureException("Unable to create space for the given texture!");
			
			GL11.glBindTexture(texTarget, this.textureId);
			this.settingTextureParameters();
			GL11.glTexImage2D(texTarget, mipmapLevels, innerFormat, this.width, this.height, 0, this.pixelFormat, this.pixelType, this.buffer);
		}
		return this.textureId;
	}
	
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
	
	public void delete()
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
}
