package net.aegistudio.transparent.util;

import net.aegistudio.transparent.opengl.Container;
import net.aegistudio.transparent.opengl.Drawable;

public class ScopedGraphic implements Drawable
{
	protected final Scoped scopedGraphic;
	
	public ScopedGraphic(Scoped scopedGraphic)
	{
		this.scopedGraphic = scopedGraphic;
	}
	
	@Override
	public void onInit(Container container)
	{
		this.scopedGraphic.create();
	}

	@Override
	public void onDraw(Container container)
	{
		
	}

	@Override
	public void onDestroy(Container container)
	{
		this.scopedGraphic.destroy();
	}
	
}
