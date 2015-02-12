package net.aegistudio.lwjgl.camera;

import org.lwjgl.opengl.GL11;

import net.aegistudio.lwjgl.graphic.Container;
import net.aegistudio.lwjgl.graphic.LocatedContainer;

/**
 * To reach the usage target of camera, it should be the top level container
 * of its rendering scene.
 * @author aegistudio
 */

public abstract class Camera extends LocatedContainer
{
	protected Camera()
	{
		super();
	}
	
	protected Camera(Container container)
	{
		super(container);
	}
	
	protected abstract void settingCamera();
	
	public void onDraw(Container container)
	{
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glScaled(scalex, scaley, scalez);
		
		GL11.glRotated(- rotz, 0.0D, 0.0D, 1.0D);
		GL11.glRotated(- roty, 0.0D, 1.0D, 0.0D);
		GL11.glRotated(- rotx, 1.0D, 0.0D, 0.0D);
		
		GL11.glTranslated(-x, -y, -z);
		
		this.settingCamera();
		theContainer.onDraw(container);
	}
}
