package net.aegistudio.transparent.opengl.texture;

import java.nio.Buffer;

import org.lwjgl.opengl.GL11;

import net.aegistudio.transparent.opengl.image.Image;
import net.aegistudio.transparent.opengl.util.BufferHelper;
import net.aegistudio.transparent.opengl.util.BufferHelper.BufferProcessor;
import net.aegistudio.transparent.opengl.util.EnumDataType;
import net.aegistudio.transparent.opengl.util.EnumPixelFormat;
import net.aegistudio.transparent.util.BindingFailureException;

public class ImageTexture extends Texture
{
	private Buffer buffer;
	private BufferProcessor processor;
	
	public ImageTexture(Image image, EnumTextureTarget texTarget)
	{
		this(image.getRasterData(), image.getPixelFormat(), image.getPixelType(), image.getWidth(), image.getHeight(), texTarget);
	}
	
	public ImageTexture(Image image)
	{
		this(image, EnumTextureTarget.PLAIN);
	}
	
	public ImageTexture(Object texture, EnumPixelFormat pixelFormat, EnumDataType pixelType, int width, int height, EnumTextureTarget texTarget)
	{
		if(texture == null) throw new IllegalArgumentException("Buffer should not be null!");
		this.texTarget = texTarget.texTarget;
		this.pixelFormat = pixelFormat.stateId;
		this.pixelType = pixelType.inferGLType();
		this.width = width;
		this.height = height;
		
		texture = BufferHelper.convertToArrayIfNecessary(texture);
		Class<?> clz = BufferHelper.getCertainClass(texture);
		if(clz == null) throw new IllegalArgumentException("Unable to create buffer for not an array!");
		EnumDataType dataType = EnumDataType.getDataType(clz);
		if(dataType == null) throw new IllegalArgumentException("Unable to create buffer for given type!");
		
		this.processor = BufferHelper.getBufferProcessor(dataType);
		this.buffer = processor.makeBuffer(texture);
	}

	public void create(int innerFormat, int mipmapLevels)
	{
		if(this.textureId == 0)
		{
			this.textureId = GL11.glGenTextures();
			if(this.textureId == 0) throw new BindingFailureException("Unable to create space for the given texture!");
			
			GL11.glBindTexture(texTarget, this.textureId);
			this.settingTextureParameters();
			this.processor.texImage2D(texTarget, mipmapLevels, innerFormat, width, height, 0, pixelFormat, buffer);
			GL11.glBindTexture(texTarget, 0);
		}
	}
}
