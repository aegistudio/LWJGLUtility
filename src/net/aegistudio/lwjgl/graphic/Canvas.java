package net.aegistudio.lwjgl.graphic;

import java.util.ArrayList;
import org.lwjgl.opengl.Display;

public abstract class Canvas
{
	
	protected final ArrayList<Drawable> drawable;
	
	public Canvas() throws GraphicIllegalStateException
	{
		this.drawable = new ArrayList<Drawable>();
	}
	
	public void onInitialize() throws GraphicIllegalStateException
	{
		if(!Display.isCreated()) throw new GraphicIllegalStateException("Could not initialize canvas, the lwjgl OpenGL context has not yet been created.");;
	}
	
	public boolean registerDrawable(Drawable drawable) throws GraphicIllegalStateException
	{
		if(!Display.isCreated()) throw new GraphicIllegalStateException("Could not register drawable, the lwjgl OpenGL context has not yet been created.");
		drawable.onInitialize(this);
		return this.drawable.add(drawable);
	}
	
	public boolean unregisterDrawable(Drawable drawable) throws GraphicIllegalStateException
	{
		if(!Display.isCreated()) throw new GraphicIllegalStateException("Could not unregister drawable, the lwjgl OpenGL context has not yet been created.");
		drawable.onTerminate(this);
		return this.drawable.remove(drawable);
	}
	
	public void onRefresh() throws GraphicIllegalStateException
	{
		if(!Display.isCreated()) throw new GraphicIllegalStateException("Could not refresh canvas, the lwjgl OpenGL context has not yet been created.");
		int drawablesize = this.drawable.size();
		for(int i = 0; i < drawablesize; i++) this.drawable.get(i).onRefresh(this);
	}
	
	public void onTerminate() throws GraphicIllegalStateException
	{
		if(!Display.isCreated()) throw new GraphicIllegalStateException("Could not terminate canvas, the lwjgl OpenGL context has not yet been created.");
		int drawablesize = this.drawable.size();
		for(int i = 0; i < drawablesize; i++) this.drawable.get(i).onTerminate(this);
	}
	
}