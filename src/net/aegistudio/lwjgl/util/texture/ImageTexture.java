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

public class ImageTexture extends Texture
{
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
}
