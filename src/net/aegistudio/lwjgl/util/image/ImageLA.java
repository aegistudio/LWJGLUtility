package net.aegistudio.lwjgl.util.image;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

public class ImageLA implements Image
{
	private final int height;
	private final int width;
	private byte[] imageBW;
	
	public ImageLA(BufferedImage bufferedImage, float r, float g, float b)
	{
		if(r + g + b == 0) throw new IllegalArgumentException("The sum of r factor, g factor, b factor should not be zero!");
		this.height = bufferedImage.getHeight();
		this.width = bufferedImage.getWidth();
		this.imageBW = new byte[(this.height * this.width) << 1];
		
		int[] pixels = new int[this.height * this.width];
		bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);
		
		for(int y = 0, lineFactor = 0; y < this.height; y ++)
		{
			for(int x = 0; x < this.width; x ++)
			{
				int base = (lineFactor + x)<<1;
				int rgb = pixels[x + (this.height - y - 1) * this.width];
				
				float red = (rgb & 0x00FF) / 256.0f;
				rgb = rgb >> 8;
				float green = (rgb & 0x00FF) / 256.0f;
				rgb = rgb >> 8;
				float blue = (rgb & 0x00FF) / 256.0f;
				
				float color = (red * r + green * g + blue * b)/(r + g + b);
				
				imageBW[base + 0] = (byte) (((byte)(color * 256))&0x00FF);
				
				rgb = rgb >> 8;
				imageBW[base + 1] = (byte)(rgb & 0x00FF);
			}
			lineFactor += this.width;
		}
	}
	
	@Override
	public byte[] getRasterData()
	{
		return this.imageBW;
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
		return GL11.GL_LUMINANCE_ALPHA;
	}

	@Override
	public int getPixelType()
	{
		return GL11.GL_UNSIGNED_BYTE;
	}
}
