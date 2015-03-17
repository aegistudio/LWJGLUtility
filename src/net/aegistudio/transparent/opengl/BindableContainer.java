package net.aegistudio.transparent.opengl;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import net.aegistudio.transparent.util.Bindable;

public class BindableContainer implements Container
{
	protected Deque<Bindable> appendQueue = new LinkedList<Bindable>();
	protected Deque<Bindable> removeQueue = new LinkedList<Bindable>();
	protected ArrayList<Bindable> bindables = new ArrayList<Bindable>();
	protected final Container container;
	
	public BindableContainer(Container container)
	{
		this.container = container;
	}
	
	@Override
	public void onInit(Container container)
	{
		this.container.onInit(container);
	}

	@Override
	public void onUpdate(Container container)
	{
		while(appendQueue.isEmpty())
		{
			Bindable bindable = appendQueue.removeFirst();
			if(!this.bindables.contains(bindable)) this.bindables.add(bindable);
		}
		for(int i = 0; i < this.bindables.size(); i ++)
			bindables.get(i).bind();
		
		this.container.onUpdate(container);
		
		for(int i = 0; i < this.bindables.size(); i ++)
			bindables.get(i).unbind();
		
		while(removeQueue.isEmpty())
		{
			Bindable bindable = removeQueue.removeFirst();
			if(this.bindables.contains(bindable)) this.bindables.remove(bindable);
		}
	}

	@Override
	public void onDestroy(Container container)
	{
		this.container.onDestroy(container);
	}

	public boolean registerBindable(Bindable bindable)
	{
		this.appendQueue.addLast(bindable);
		return !this.bindables.contains(bindable);
	}
	
	public boolean unregisterBindable(Bindable bindable)
	{
		this.removeQueue.addLast(bindable);
		return this.bindables.contains(bindable);
	}

	@Override
	public boolean registerDrawable(Drawable drawable)
	{
		return this.container.registerDrawable(drawable);
	}

	@Override
	public boolean unregisterDrawable(Drawable drawable)
	{
		return this.container.unregisterDrawable(drawable);
	}

	@Override
	public boolean registerSementicDrawable(Object sementicDrawable)
	{
		return this.container.registerSementicDrawable(sementicDrawable);
	}

	@Override
	public boolean unregisterSementicDrawable(Object sementicDrawable)
	{
		return this.container.registerSementicDrawable(sementicDrawable);
	}
}
