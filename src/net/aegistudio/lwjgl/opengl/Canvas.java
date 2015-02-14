package net.aegistudio.lwjgl.opengl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public abstract class Canvas implements Container
{
	
	protected final Queue<Drawable> pendingDrawables;
	protected final ArrayList<Drawable> drawable;
	
	public Canvas()
	{
		this.drawable = new ArrayList<Drawable>();
		this.pendingDrawables = new ArrayDeque<Drawable>();
	}
	
	public synchronized boolean registerSementicDrawable(Object drawable)
	{
		if(drawable instanceof Drawable) return this.registerDrawable((Drawable)drawable);
		Drawable wrappedDrawable = new WrappedDrawable(drawable);
		return this.registerDrawable(wrappedDrawable);
	}
	
	public synchronized boolean unregisterSementicDrawable(Object drawable)
	{
		if(drawable instanceof Drawable) return this.unregisterDrawable((Drawable)drawable);
		Drawable wrappedDrawable = new WrappedDrawable(drawable);
		return this.unregisterDrawable(wrappedDrawable);
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
	
	public synchronized void onDraw(Container canvas)
	{
		while(!this.pendingDrawables.isEmpty())
		{
			Drawable pending = this.pendingDrawables.remove();
			pending.onInit(this);
			drawable.add(pending);
		}
		for(Drawable drawable : drawable) drawable.onDraw(this);
	}
	
	public synchronized void onDestroy(Container canvas)
	{
		for(Drawable drawable : drawable) drawable.onDestroy(this);
	}
}