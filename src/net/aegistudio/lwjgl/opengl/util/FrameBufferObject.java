package net.aegistudio.lwjgl.opengl.util;

import net.aegistudio.lwjgl.opengl.Container;
import net.aegistudio.lwjgl.opengl.Drawable;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GLContext;

public class FrameBufferObject implements Drawable
{
	private int bufferId = 0;
	private final Drawable drawable;
	
	public FrameBufferObject(Drawable drawable)
	{
		if(drawable == null)
			throw new IllegalArgumentException("The inner drawable of frame buffer object should not be empty!");
		this.drawable = drawable;
	}
	
	public int create()
	{
		if(this.bufferId == 0)
		{
			if(!GLContext.getCapabilities().GL_ARB_framebuffer_object) throw new FeatureNotSupportedException("frame buffer object");
			this.bufferId = ARBFramebufferObject.glGenFramebuffers();
		}
		return this.bufferId;
	}
	
	public void destroy()
	{
		if(this.bufferId != 0)
		{
			ARBFramebufferObject.glDeleteFramebuffers(bufferId);
			this.bufferId = 0;
		}
	}

	@Override
	public void onInit(Container container)
	{
		if(this.create() == 0) throw new RuntimeException("Could not create the corresponding FBO object!");
		this.drawable.onInit(container);
	}

	@Override
	public void onDraw(Container container)
	{
		ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, this.bufferId);
		int fbo_status = ARBFramebufferObject.glCheckFramebufferStatus(ARBFramebufferObject.GL_FRAMEBUFFER);
		if(fbo_status == ARBFramebufferObject.GL_FRAMEBUFFER_COMPLETE) this.drawable.onDraw(container);
		ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
	}

	@Override
	public void onDestroy(Container container)
	{
		this.destroy();
		this.drawable.onDestroy(container);
	}
}
