package net.aegistudio.transparent.opengl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Canvas implements Container
{
	protected final Queue<Drawable> pendingDrawables;
	protected final Queue<Drawable> removingDrawables;
	protected final ArrayList<Drawable> drawable;
	
	public Canvas()
	{
		this.drawable = new ArrayList<Drawable>();
		this.pendingDrawables = new LinkedList<Drawable>();
		this.removingDrawables = new LinkedList<Drawable>();
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
			this.removingDrawables.add(drawable);
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
		while(!this.removingDrawables.isEmpty())
		{
			Drawable removing = this.removingDrawables.remove();
			removing.onDestroy(this);
			this.drawable.remove(removing);
		}
	}
	
	public synchronized void onDestroy(Container canvas)
	{
		for(Drawable drawable : drawable)
		{
			this.pendingDrawables.add(drawable);
			drawable.onDestroy(this);
		}
		drawable.clear();
	}
	
	public void finalize() throws Throwable
	{
		this.onDestroy(null);
		super.finalize();
	}
}