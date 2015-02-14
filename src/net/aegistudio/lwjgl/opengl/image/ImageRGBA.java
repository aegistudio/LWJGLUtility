package net.aegistudio.lwjgl.opengl.image;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

public class ImageRGBA implements Image
{
	private final int height;
	private final int width;
	private byte[] imageRGBA;
	
	public ImageRGBA(IntBuffer intbuffer, int width, int height)
	{
		this.width = width;
		this.height = height;
		
		int[] pixels = intbuffer.array();
		if(pixels.length < this.width * this.height)
			throw new IllegalArgumentException("The image provided in the buffer should not be smaller than the product of width and height!");
		
		this.convertImage(pixels);
	}
	
	public ImageRGBA(BufferedImage bufferedImage)
	{
		this.height = bufferedImage.getHeight();
		this.width = bufferedImage.getWidth();
		
		int[] pixels = new int[this.height * this.width];
		bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);
		
		this.convertImage(pixels);
	}
	
	private void convertImage(int[] pixels)
	{
		this.imageRGBA = new byte[(this.height * this.width) << 2];
		
		for(int y = 0, lineFactor = 0; y < this.height; y ++)
		{
			for(int x = 0; x < this.width; x ++)
			{
				int rgb = pixels[x + (this.height - y - 1) * this.width];
				int base = (lineFactor + x) << 2;
				imageRGBA[base + 2] = (byte)(rgb & 0x00FF);
				rgb = rgb >> 8;
				imageRGBA[base + 1] = (byte)(rgb & 0x00FF);
				rgb = rgb >> 8;
				imageRGBA[base + 0] = (byte)(rgb & 0x00FF);
				
				rgb = rgb >> 8;
				imageRGBA[base + 3] = (byte)(rgb & 0x00FF);
			}
			lineFactor += this.width;
		}
	}
	
	@Override
	public byte[] getRasterData()
	{
		return this.imageRGBA;
	}

	@Override
	public int getWidth()
	{
		return this.width;
	}

	@Override
	public int getHeight()
	{
		return this.height;
	}

	@Override
	public int getPixelFormat()
	{
		return GL11.GL_RGBA;
	}

	@Override
	public int getPixelType()
	{
		return GL11.GL_UNSIGNED_BYTE;
	}
}
