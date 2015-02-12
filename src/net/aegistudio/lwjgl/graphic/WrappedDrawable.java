package net.aegistudio.lwjgl.graphic;

import java.lang.reflect.Method;

public class WrappedDrawable implements Drawable
{
	Method onInitMethod;
	Method onDrawMethod;
	Method onDestroyMethod;
	private final Object drawableObject;
	
	public WrappedDrawable(Object sementicDrawable)
	{
		this.drawableObject = sementicDrawable;
		Method[] methods = sementicDrawable.getClass().getDeclaredMethods();
		for(Method method : methods)
		{
			Drawable.Init initmethod = method.getAnnotation(Drawable.Init.class);
			if(initmethod != null) onInitMethod = method;
			
			Drawable.Draw drawmethod = method.getAnnotation(Drawable.Draw.class);
			if(drawmethod != null) onDrawMethod = method;
			
			Drawable.Destroy destroymethod = method.getAnnotation(Drawable.Destroy.class);
			if(destroymethod != null) onDestroyMethod = method;
		}
		
	}
	
	public boolean equals(Object object)
	{
		return this.drawableObject == object;
	}
	
	@Override
	public void onInit(Container canvas)
	{
		if(this.onInitMethod != null)
		try
		{
			this.onInitMethod.setAccessible(true);
			this.onInitMethod.invoke(drawableObject);
			this.onInitMethod.setAccessible(false);
		}
		catch(Exception e)
		{
		
		}
	}

	@Override
	public void onDraw(Container canvas)
	{
		if(this.onDrawMethod != null)
		try
		{
			this.onDrawMethod.setAccessible(true);
			this.onDrawMethod.invoke(drawableObject);
			this.onDrawMethod.setAccessible(false);
		}
		catch(Exception e)
		{
		
		}
	}

	@Override
	public void onDestroy(Container canvas)
	{
		if(this.onDestroyMethod != null)
		try
		{
			this.onDestroyMethod.setAccessible(true);
			this.onDestroyMethod.invoke(drawableObject);
			this.onDestroyMethod.setAccessible(false);
		}
		catch(Exception e)
		{
		
		}
	}
	
}
