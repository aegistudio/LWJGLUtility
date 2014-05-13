package net.aegistudio.lwjgl.graphic;

import net.aegistudio.lwjgl.graphic.Canvas;

public interface Drawable
{
	public void onInitialize(Canvas canvas) throws GraphicIllegalStateException;
	public void onRefresh(Canvas canvas) throws GraphicIllegalStateException;
	public void onTerminate(Canvas canvas) throws GraphicIllegalStateException;
}