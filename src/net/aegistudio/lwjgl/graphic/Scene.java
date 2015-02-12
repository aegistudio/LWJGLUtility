package net.aegistudio.lwjgl.graphic;

import org.lwjgl.opengl.GL11;

public class Scene extends Canvas
{

	private double posx, posy, posz;
	private double scalex, scaley, scalez;
	private double rotx, roty, rotz;
	
	public Scene()
	{
		this.reset();
	}
	
	@Override
	public void onInit(Container canvas)
	{
	}
	
	public void onDraw(Container canvas)
	{
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glTranslated(posx, posy, posz);
		GL11.glRotated(rotx, 1, 0, 0);
		GL11.glRotated(roty, 0, 1, 0);
		GL11.glRotated(rotz, 0, 0, 1);
		GL11.glScaled(scalex, scaley, scalez);
		super.onDraw(canvas);
		GL11.glPopMatrix();
	}
	
	public void move(double x, double y, double z)
	{
		this.posx += x;
		this.posy += y;
		this.posz += z;
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
		this.posx = this.posy = this.posz = 0;
		this.scalex = this.scaley = this.scalez = 1.0D;
		this.rotx = this.roty = this.rotz = 0;
	}
}
