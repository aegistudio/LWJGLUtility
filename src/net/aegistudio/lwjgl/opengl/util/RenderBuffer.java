package net.aegistudio.lwjgl.opengl.util;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;

public class RenderBuffer
{
	private final FrameBufferObject fbo;
	private final int internalFormat;
	private final int width;
	private final int height;
	private final int[] attachments;
	
	private int rboId = 0;
	
	public RenderBuffer(FrameBufferObject fbo, int internalFormat, int width, int height, int... attachments)
	{
		if(fbo == null) throw new IllegalArgumentException("The rendering FBO for this texture should not be null!");
		this.fbo = fbo;
		
		this.internalFormat = internalFormat;
		this.width = width;
		this.height = height;
		this.attachments = attachments;
	}
	
	public RenderBuffer(FrameBufferObject fbo, int width, int height, int... attachments)
	{
		this(fbo, GL11.GL_RGBA, width, height, attachments);
	}
	
	public int create(int samples)
	{
		if(this.rboId == 0)
		{
			this.rboId = ARBFramebufferObject.glGenRenderbuffers();
			if(this.rboId == 0) throw new BindingFailureException("Unable to create space for the given texture!");
			
			ARBFramebufferObject.glBindRenderbuffer(ARBFramebufferObject.GL_RENDERBUFFER, this.rboId);
			if(samples <= 0) ARBFramebufferObject.glRenderbufferStorage(ARBFramebufferObject.GL_RENDERBUFFER, internalFormat, width, height);
			else ARBFramebufferObject.glRenderbufferStorageMultisample(ARBFramebufferObject.GL_RENDERBUFFER, samples, internalFormat, width, height);
			
			int fboId = this.fbo.create();
			ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, fboId);
			for(int attachment : this.attachments) ARBFramebufferObject.glFramebufferRenderbuffer(ARBFramebufferObject.GL_FRAMEBUFFER, attachment, ARBFramebufferObject.GL_RENDERBUFFER, rboId);
			ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
		}
		return this.rboId;
	}
	
	public int create()
	{
		return this.create(0);
	}
	
	public void destroy()
	{
		if(this.rboId != 0)
		{
			ARBFramebufferObject.glDeleteRenderbuffers(this.rboId);
			this.rboId = 0;
		}
	}
}
