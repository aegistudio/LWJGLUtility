package net.aegistudio.transparent.opengl;

/**
 * A adapter class that adapts container and positional function.
 * @author aegistudio
 */

public abstract class LocatedContainer implements Container
{
	
	protected final Container theContainer;
	
	protected double x, y, z;
	protected double rotx, roty, rotz;
	protected double scalex, scaley, scalez;
	{
		this.reset();
	}
	
	protected LocatedContainer(Container container)
	{
		if(container == null) throw new IllegalArgumentException("You should not register null as the container!");
		this.theContainer = container;
	}
	
	protected LocatedContainer()
	{
		this.theContainer = new Canvas()
		{
			@Override
			public void onInit(Container container)
			{
			}
		};
	}
	
	@Override
	public void onInit(Container container)
	{
		this.theContainer.onInit(container);
	}
	
	@Override
	public void onUpdate(Container container)
	{
		this.theContainer.onUpdate(container);
	}
	
	@Override
	public void onDestroy(Container container)
	{
		this.theContainer.onDestroy(container);
	}
	
	@Override
	public boolean registerDrawable(Drawable drawable)
	{
		return this.theContainer.registerDrawable(drawable);
	}
	
	@Override
	public boolean unregisterDrawable(Drawable drawable)
	{
		return this.theContainer.unregisterDrawable(drawable);
	}
	
	@Override
	public boolean registerSementicDrawable(Object sementicDrawable)
	{
		return this.theContainer.registerSementicDrawable(sementicDrawable);
	}
	
	@Override
	public boolean unregisterSementicDrawable(Object sementicDrawable)
	{
		return this.theContainer.unregisterSementicDrawable(sementicDrawable);
	}
	
	public void translate(double x, double y, double z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public void zoom(double x, double y, double z)
	{
		this.scalex *= x;
		this.scaley *= y;
		this.scalez *= z;
	}
	
	public void rotate(double x, double y, double z)
	{
		this.rotx += x;
		this.roty += y;
		this.rotz += z;
	}
	
	public void reset()
	{
		this.x = this.y = this.z = 0;
		this.scalex = this.scaley = this.scalez = 1.0D;
		this.rotx = this.roty = this.rotz = 0;
	}
}
