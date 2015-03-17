package net.aegistudio.transparent.util;

import net.aegistudio.transparent.opengl.Canvas;
import net.aegistudio.transparent.opengl.Container;
import net.aegistudio.transparent.opengl.Drawable;

public class GraphicResourcePack implements Drawable
{
	private Container container = new Canvas()
	{
		@Override
		public void onInit(Container container)
		{
		}
	};
	
	@Override
	public void onInit(Container container)
	{
		this.container.onInit(container);
	}
	
	public boolean registerResource(Resource resource)
	{
		return this.container.registerDrawable(new GraphicResource(resource));
	}
	
	public boolean unregisterResource(Resource resource)
	{
		return this.container.unregisterDrawable(new GraphicResource(resource));
	}

	@Override
	public void onUpdate(Container container)
	{
		this.container.onUpdate(container);
		
	}

	@Override
	public void onDestroy(Container container)
	{
		this.container.onDestroy(container);
	}
}
