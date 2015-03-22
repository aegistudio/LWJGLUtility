package net.aegistudio.transparent.opengl.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

public enum EnumTextureTarget
{
	LINEAR(GL11.GL_TEXTURE_1D),
	PLAIN(GL11.GL_TEXTURE_2D),
	VOLUME(GL12.GL_TEXTURE_3D),
	CUBE(GL13.GL_TEXTURE_CUBE_MAP);
	
	public final int texTarget;
	
	private EnumTextureTarget(int texTarget)
	{
		this.texTarget = texTarget;
	}
}
