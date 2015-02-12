package net.aegistudio.lwjgl.camera;

import org.lwjgl.opengl.GL11;

import net.aegistudio.lwjgl.graphic.Canvas;
import net.aegistudio.lwjgl.graphic.Container;

/**
 * To reach the usage target of camera, it should be the top level container
 * of its rendering scene.
 * @author aegistudio
 */

public abstract class Camera extends Canvas
{
	protected double x, y, z;
	protected double yawx, yawy, yawz;
	protected double zoomx, zoomy, zoomz;
	{
		this.reset();
	}
	
	
	public void translate(double x, double y, double z)
	{
		this.x += x; this.y += y; this.z += z;
	}
	
	public void rotate(double x, double y, double z)
	{
		this.yawx += x; this.yawy += y; this.yawz += z;
	}
	
	public void scale(double x, double y, double z)
	{
		this.zoomx *= x; this.zoomy *= y; this.zoomz *= z;
	}
	
	protected abstract void settingCamera();
	
	public void reset()
	{
		this.x = this.y = this.z = 0.D;
		this.yawx = this.yawy = this.yawz = 0.D;
		this.zoomx = this.zoomy = this.zoomz = 1.D;
	}
	
	@Override
	public void onInit(Container containter)
	{
		
	}
	
	public void onDraw(Container container)
	{
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glScaled(zoomx, zoomy, zoomz);
		
		GL11.glRotated(- yawz, 0.0D, 0.0D, 1.0D);
		GL11.glRotated(- yawy, 0.0D, 1.0D, 0.0D);
		GL11.glRotated(- yawx, 1.0D, 0.0D, 0.0D);
		
		GL11.glTranslated(-x, -y, -z);
		
		this.settingCamera();
		super.onDraw(container);
	}
}
