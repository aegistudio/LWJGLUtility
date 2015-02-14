package net.aegistudio.lwjgl.opengl.image;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL21;

import net.aegistudio.lwjgl.opengl.texture.ImageTexture;
import net.aegistudio.lwjgl.opengl.util.VertexBufferObject;

public final class ImageUtils
{
	private ImageUtils(){}
	
	public static ImageTexture createImageTexture(Image image)
	{
		return new ImageTexture(image.getRasterData(), image.getPixelFormat(), image.getPixelType(), image.getWidth(), image.getHeight(), GL11.GL_TEXTURE_2D);
	}
	
	public static VertexBufferObject createImageVBO(Image image)
	{
		return new VertexBufferObject(GL21.GL_PIXEL_UNPACK_BUFFER, ARBVertexBufferObject.GL_STATIC_DRAW_ARB, image.getRasterData());
	}
}
