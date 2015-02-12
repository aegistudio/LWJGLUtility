package net.aegistudio.lwjgl.camera;

import org.lwjgl.opengl.GL11;

public class Ortho extends Camera
{
	protected final double widthHalved, heightHalved, zNear, zFar;
	
	public Ortho(double width, double height, double depth)
	{
		this.widthHalved = width / 2.0D;
		this.heightHalved = height / 2.0D;
		this.zNear = depth / 2.0D;
		this.zFar = - depth / 2.0D;
	}
	
	@Override
	public void settingCamera()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-widthHalved, widthHalved, -heightHalved, heightHalved, zNear, zFar);
	}
}