package net.aegistudio.lwjgl.opengl.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public enum EnumArrayPointer
{
	VERTEX(GL11.GL_VERTEX_ARRAY)
	{
		public void arrayPointer(int size, int type, int stride, long offset)
		{
			GL11.glVertexPointer(size, type, stride, offset);
		}
	},
	TEXTURE(GL11.GL_TEXTURE_COORD_ARRAY)
	{
		public void arrayPointer(int size, int type, int stride, long offset)
		{
			GL11.glTexCoordPointer(size, type, stride, offset);
		}
	},
	COLOR(GL11.GL_COLOR_ARRAY)
	{
		public void arrayPointer(int size, int type, int stride, long offset)
		{
			GL11.glColorPointer(size, type, stride, offset);
		}
	},
	SECONDARY_COLOR(GL14.GL_SECONDARY_COLOR_ARRAY)
	{
		public void arrayPointer(int size, int type, int stride, long offset)
		{
			GL14.glSecondaryColorPointer(size, type, stride, offset);
		}
	},
	FOG(GL14.GL_FOG_COORDINATE_ARRAY)
	{
		public void arrayPointer(int size, int type, int stride, long offset)
		{
			GL14.glFogCoordPointer(type, stride, offset);
		}
	},
	NORMAL(GL11.GL_NORMAL_ARRAY)
	{
		public void arrayPointer(int size, int type, int stride, long offset)
		{
			GL11.glNormalPointer(type, stride, offset);
		}
	},
	EDGE_FLAG(GL11.GL_EDGE_FLAG_ARRAY)
	{
		public void arrayPointer(int size, int type, int stride, long offset)
		{
			GL11.glEdgeFlagPointer(stride, offset);
		}
	};
	
	public final int stateName;
	
	private EnumArrayPointer(int stateName)
	{
		this.stateName = stateName;
	}
	
	public abstract void arrayPointer(int size, int type, int stride, long offset);
}
