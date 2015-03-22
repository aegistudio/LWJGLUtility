package net.aegistudio.transparent.opengl.util;

import org.lwjgl.opengl.ARBFramebufferObject;

public enum EnumAttachment
{
	COLOR(ARBFramebufferObject.GL_COLOR_ATTACHMENT0),
	DEPTH(ARBFramebufferObject.GL_DEPTH_ATTACHMENT),
	DEPTH_STENCIL(ARBFramebufferObject.GL_DEPTH_STENCIL_ATTACHMENT),
	STENCIL(ARBFramebufferObject.GL_STENCIL_ATTACHMENT);
	
	public final int attachment;
	
	private EnumAttachment(int attachment)
	{
		this.attachment = attachment;
	}
}
