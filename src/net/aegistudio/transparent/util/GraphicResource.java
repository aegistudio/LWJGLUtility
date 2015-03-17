package net.aegistudio.transparent.util;

import net.aegistudio.transparent.opengl.Container;
import net.aegistudio.transparent.opengl.Drawable;

public class GraphicResource implements Drawable
{
	protected final Resource scopedGraphic;
	
	public GraphicResource(Resource scopedGraphic)
	{
		this.scopedGraphic = scopedGraphic;
	}
	
	@Override
	public void onInit(Container container)
	{
		this.scopedGraphic.create();
	}

	@Override
	public void onUpdate(Container container)
	{
		
	}

	@Override
	public void onDestroy(Container container)
	{
		this.scopedGraphic.destroy();
	}
	
	public boolean equals(Object object)
	{
		return this.scopedGraphic.equals(object);
	}
}
