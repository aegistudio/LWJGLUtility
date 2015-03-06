package net.aegistudio.transparent.opengl.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.aegistudio.transparent.opengl.texture.ImageTexture;
import net.aegistudio.transparent.opengl.util.EnumBufferTarget;
import net.aegistudio.transparent.opengl.util.EnumBufferUsage;
import net.aegistudio.transparent.opengl.util.VertexBufferObject;

public final class ImageUtils
{
	private ImageUtils(){}
	
	public static ImageTexture createImageTexture(Image image)
	{
		return new ImageTexture(image.getRasterData(), image.getPixelFormat(), image.getPixelType(), image.getWidth(), image.getHeight(), GL11.GL_TEXTURE_2D);
	}
	
	public static VertexBufferObject createImageVBO(Image image)
	{
		return new VertexBufferObject(EnumBufferTarget.PIXEL_UNPACK, EnumBufferUsage.STATIC_DRAW, image.getRasterData());
	}
	
	public static ImageTexture createImageTexture(InputStream image) throws IOException
	{
		BufferedImage bufferedImage = ImageIO.read(image);
		ImageRGBA img = new ImageRGBA(bufferedImage);
		ImageTexture imgTex = createImageTexture(img);
		bufferedImage.flush();
		return imgTex;
	}
}
