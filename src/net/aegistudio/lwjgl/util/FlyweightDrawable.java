package net.aegistudio.lwjgl.util;

import net.aegistudio.lwjgl.graphic.Container;
import net.aegistudio.lwjgl.graphic.Drawable;

/**
 * This class is a adapter for drawable which is used in flyweight manner.
 * @author aegistudio
 */

public class FlyweightDrawable implements Drawable
{
	protected final Drawable flyweight;
	protected int flyweight_counter;
	
	public FlyweightDrawable(Drawable flyweight)
	{
		this.flyweight = flyweight;
		this.flyweight_counter = 0;
	}
	
	@Override
	public void onInit(Container container)
	{
		if(this.flyweight_counter <= 0) this.flyweight.onInit(container);
		this.flyweight_counter ++;
	}

	@Override
	public void onDraw(Container container)
	{
		this.flyweight.onDraw(container);
	}

	@Override
	public void onDestroy(Container container)
	{
		this.flyweight_counter --;
		if(this.flyweight_counter <= 0) this.flyweight.onDestroy(container);
	}
}
