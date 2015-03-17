package net.aegistudio.transparent.util;

import net.aegistudio.transparent.opengl.Canvas;
import net.aegistudio.transparent.opengl.Container;

public class GraphicResourcePack extends Canvas
{
	@Override
	public void onInit(Container container)
	{
		
	}
	
	public void registerResource(Resource resource)
	{
		super.registerDrawable(new GraphicResource(resource));
	}
	
	public void unregisterResource(Resource resource)
	{
		super.unregisterDrawable(new GraphicResource(resource));
	}
}
