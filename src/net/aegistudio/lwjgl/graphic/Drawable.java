package net.aegistudio.lwjgl.graphic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.aegistudio.lwjgl.graphic.Canvas;

public interface Drawable
{
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Init{}
	
	public void onInit(Canvas canvas);
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Draw{}
	
	public void onDraw(Canvas canvas);
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Destroy{}
	
	public void onDestroy(Canvas canvas);
}