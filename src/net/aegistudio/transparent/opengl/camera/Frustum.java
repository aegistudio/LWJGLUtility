package net.aegistudio.transparent.opengl.camera;

import net.aegistudio.transparent.opengl.Container;

import org.lwjgl.opengl.GL11;

/**
 * A typical OpenGL projection whose viewing body is frustum. Be caution that the initial
 * orientation of this camera is Z-Neg.
 * @author aegistudio
 */

public class Frustum extends Camera
{
	protected double widthHalved, heightHalved, zNear, zFar;
	
	public Frustum(double width, double height, double focusDistance, double depth)
	{
		super();
		this.setupParameters(width, height, focusDistance, depth);
	}
	
	public Frustum(Container container, double width, double height, double focusDistance, double depth)
	{
		super(container);
		this.setupParameters(width, height, focusDistance, depth);
	}
	
	public void setupParameters(double width, double height, double focusDistance, double depth)
	{
		this.widthHalved = width / 2.0D;
		this.heightHalved = height / 2.0D;
		this.zNear = focusDistance;
		this.zFar = focusDistance + depth;
	}
	
	@Override
	public void settingCamera()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glFrustum(-widthHalved, widthHalved, -heightHalved, heightHalved, zNear, zFar);
	}
}
