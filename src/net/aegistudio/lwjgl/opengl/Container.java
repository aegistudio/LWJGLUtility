package net.aegistudio.lwjgl.opengl;

public interface Container extends Drawable
{
	public boolean registerDrawable(Drawable drawable);
	
	public boolean unregisterDrawable(Drawable drawable);
	
	public boolean registerSementicDrawable(Object sementicDrawable);
	
	public boolean unregisterSementicDrawable(Object sementicDrawable);
}
