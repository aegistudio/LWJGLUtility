package net.aegistudio.lwjgl.graphic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public abstract class Canvas implements Drawable
{
	
	protected final Queue<Drawable> pendingDrawables;
	protected final ArrayList<Drawable> drawable;
	
	public Canvas()
	{
		this.drawable = new ArrayList<Drawable>();
		this.pendingDrawables = new ArrayDeque<Drawable>();
	}
	
	public synchronized boolean registerDrawable(Drawable drawable)
	{
		if(drawable == null) return false;
		if(this.drawable.contains(drawable) || this.pendingDrawables.contains(drawable)) return false;
		this.pendingDrawables.add(drawable);
		return true;
	}
	
	public synchronized boolean unregisterDrawable(Drawable drawable)
	{
		if(drawable == null) return false;
		if(this.drawable.contains(drawable))
		{
			this.drawable.remove(drawable);
			drawable.onDestroy(this);
			return true;
		}
		else if(this.pendingDrawables.contains(drawable))
		{
			this.pendingDrawables.remove();
			return true;
		}
		else return false;
	}
	
	public synchronized void onDraw(Canvas canvas)
	{
		while(!this.pendingDrawables.isEmpty())
		{
			Drawable pending = this.pendingDrawables.remove();
			pending.onInit(this);
			drawable.add(pending);
		}
		for(Drawable drawable : drawable) drawable.onDraw(this);
	}
	
	public synchronized void onDestroy(Canvas canvas)
	{
		for(Drawable drawable : drawable) drawable.onDestroy(this);
	}
}