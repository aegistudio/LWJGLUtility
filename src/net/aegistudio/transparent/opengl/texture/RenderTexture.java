package net.aegistudio.transparent.opengl.texture;

import java.nio.ByteBuffer;

import net.aegistudio.transparent.opengl.util.EnumDataType;
import net.aegistudio.transparent.opengl.util.EnumPixelFormat;
import net.aegistudio.transparent.opengl.util.FrameBufferObject;
import net.aegistudio.transparent.util.BindingFailureException;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;

public class RenderTexture extends Texture
{
	private final FrameBufferObject fbo;
	private final int[] attachments;
	
	/**
	 * @param fbo - The frame buffer object to attach.
	 * @param attachments - The attachment point of the FBO, like GL_COLOR_ATTACHMENTn, GL_DEPTH_ATTACHMENT, etc.
	 */
	public RenderTexture(FrameBufferObject fbo, int width, int height, int... attachments)
	{
		this(fbo, EnumPixelFormat.RGBA, EnumDataType.BYTE, width, height, GL11.GL_TEXTURE_2D, attachments);
	}
	
	public RenderTexture(FrameBufferObject fbo, EnumPixelFormat pixelFormat, EnumDataType pixelType, int width, int height, int texTarget, int... attachments)
	{
		if(fbo == null) throw new IllegalArgumentException("The rendering FBO for this texture should not be null!");
		this.fbo = fbo;
		
		this.texTarget = texTarget;
		this.pixelFormat = pixelFormat.stateId;
		this.pixelType = pixelType.inferGLType();
		this.width = width;
		this.height = height;
		this.attachments = attachments;
	}
	
	public void create(int innerFormat, int mipmapLevels)
	{
		if(this.textureId == 0)
		{
			this.textureId = GL11.glGenTextures();
			if(this.textureId == 0) throw new BindingFailureException("Unable to create space for the given texture!");
			
			GL11.glBindTexture(texTarget, this.textureId);
			this.settingTextureParameters();
			GL11.glTexImage2D(texTarget, mipmapLevels, innerFormat, this.width, this.height, 0, this.pixelFormat, this.pixelType, (ByteBuffer)null);
			GL11.glBindTexture(texTarget, 0);
			
			this.fbo.create();
			int fboId = this.fbo.getBufferObjectId();
			ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, fboId);
			for(int attachment : this.attachments) ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_FRAMEBUFFER, attachment, texTarget, textureId, mipmapLevels);
			ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
		}
	}
}
