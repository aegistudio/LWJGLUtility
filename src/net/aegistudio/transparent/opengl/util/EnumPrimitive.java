package net.aegistudio.transparent.opengl.util;

import org.lwjgl.opengl.GL11;

public enum EnumPrimitive
{
	POINTS(GL11.GL_POINTS),
	
	LINES(GL11.GL_LINES),
	LINE_STRIP(GL11.GL_LINE_STRIP),
	LINE_LOOP(GL11.GL_LINE_LOOP),
	
	TRIANGLES(GL11.GL_TRIANGLES),
	TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
	TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN),
	
	QUADS(GL11.GL_QUADS),
	QUAD_STRIP(GL11.GL_QUAD_STRIP),
	
	POLYGON(GL11.GL_POLYGON);
	
	public final int stateId;
	
	private EnumPrimitive(int stateId)
	{
		this.stateId = stateId;
	}
}
