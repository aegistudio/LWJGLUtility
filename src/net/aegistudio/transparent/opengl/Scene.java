package net.aegistudio.transparent.opengl;

import org.lwjgl.opengl.GL11;

public class Scene extends LocatedContainer
{
	public Scene()
	{
		super();
	}
	
	public Scene(Container container)
	{
		super(container);
	}
	
	public void onDraw(Container canvas)
	{
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glRotated(rotx, 1, 0, 0);
		GL11.glRotated(roty, 0, 1, 0);
		GL11.glRotated(rotz, 0, 0, 1);
		GL11.glScaled(scalex, scaley, scalez);
		theContainer.onDraw(canvas);
		GL11.glPopMatrix();
	}
}
