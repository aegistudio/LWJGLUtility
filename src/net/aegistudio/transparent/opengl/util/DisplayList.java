package net.aegistudio.transparent.opengl.util;

import net.aegistudio.transparent.util.BindingFailureException;
import net.aegistudio.transparent.util.Scoped;

import org.lwjgl.opengl.GL11;

/**
 * This class encapsulates the OpenGL display list, which is used as a callable command
 * series and is higher efficient in rendering.
 * The method DisplayList.display(index) should be implemented, the content inside is 
 * the display list call backs.
 * @author aegistudio
 */

public abstract class DisplayList implements Scoped
{
	private int displayListId = 0;
	private final int displayListRange;
	
	public DisplayList(int displayListRange)
	{
		this.displayListRange = displayListRange;
	}
	
	public DisplayList()
	{
		this(1);
	}
	
	public int create()
	{
		return this.create(false, false);
	}
	
	public int create(boolean force_recompile, boolean compile_and_execute)
	{
		if((this.displayListId == 0) || force_recompile)
		{
			if(this.displayListId == 0)
			{
				this.displayListId = GL11.glGenLists(displayListRange);
				if(this.displayListId == 0) throw new BindingFailureException("Unable to allocate display list location!");
			}
			
			for(int i = 0; i < this.displayListRange; i ++)
			{
				GL11.glNewList(this.displayListId + i, compile_and_execute? GL11.GL_COMPILE_AND_EXECUTE : GL11.GL_COMPILE);
				this.display(i);
				GL11.glEndList();
			}
		}
		return this.displayListId;
	}
	
	protected abstract void display(int displayIndex);
	
	public void call(int displayListIndex)
	{
		if(displayListIndex > this.displayListRange || displayListIndex < 0)
			throw new IllegalArgumentException("The display list index is out of permitted display list range!");
		if(this.displayListId != 0) GL11.glCallList(this.displayListId + displayListIndex);
		else this.display(displayListIndex);
	}
	
	public void destroy()
	{
		if(this.displayListId != 0)
		{
			GL11.glDeleteLists(displayListId, displayListRange);
			displayListId = 0;
		}
	}
}
