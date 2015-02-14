package net.aegistudio.lwjgl.opengl.texture;

import net.aegistudio.lwjgl.opengl.util.BindingFailureException;
import net.aegistudio.lwjgl.opengl.util.FrameBufferObject;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;

public class FBOTexture extends Texture
{
	private final FrameBufferObject fbo;
	private final int attachment;
	
	/**
	 * @param fbo - The frame buffer object to attach.
	 * @param attachment - The attachment point of the FBO, like GL_COLOR_ATTACHMENTn, GL_DEPTH_ATTACHMENT, etc.
	 */
	
	public FBOTexture(FrameBufferObject fbo, int pixelFormat, int pixelType, int width, int height, int texTarget, int attachment)
	{
		if(fbo == null) throw new IllegalArgumentException("The rendering FBO for this texture should not be null!");
		this.fbo = fbo;
		
		this.texTarget = texTarget;
		this.pixelFormat = pixelFormat;
		this.pixelType = pixelType;
		this.width = width;
		this.height = height;
		this.attachment = attachment;
	}
	
	public int create(int innerFormat, int mipmapLevels)
	{
		if(this.textureId == 0)
		{
			this.textureId = GL11.glGenTextures();
			if(this.textureId == 0) throw new BindingFailureException("Unable to create space for the given texture!");
			
			GL11.glBindTexture(texTarget, this.textureId);
			this.settingTextureParameters();
			GL11.glTexImage2D(texTarget, mipmapLevels, innerFormat, this.width, this.height, 0, this.pixelFormat, this.pixelType, 0);
			GL11.glBindTexture(texTarget, 0);
			
			int fboId = this.fbo.create();
			ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, fboId);
			ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_FRAMEBUFFER, attachment, texTarget, textureId, mipmapLevels);
			ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
		}
		return this.textureId;
	}
}
