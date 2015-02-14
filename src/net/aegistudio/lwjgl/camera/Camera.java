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
	
	private static final double GL_ANGLE_RATIO = 180.D / Math.PI;
	
	public void orient(double x, double y, double z)
	{
		double modulus_sq = x * x + y * y + z * z;
		if(modulus_sq <= 0) throw new IllegalArgumentException("The modulus of the given vector should be larger than zero!");
		
		double projection_modulus = Math.sqrt(y * y + z * z);
		
		if(projection_modulus != 0.D)
		{
			//(0, y, z) is the projection vector on the YZ-Plane, and the (x, 0, 0) is the normal vector of the
			//Firstly, find the angle between (0, 0, -1) and (0, y, z)
			//|a||b|cos theta = a`b = -z thus cos theta = -z / sqrt(y2 + z2), theta = arccos(-z / sqrt(y2 + z2)).
			//when y > 0, theta = theta, when y < 0, theta = 2 * pi - theta.
			
			double theta_ratio = -z / projection_modulus;
			double theta = (y > 0)? Math.acos(theta_ratio) : 2.D * Math.PI - Math.acos(theta_ratio);
			this.rotx = theta * GL_ANGLE_RATIO;
			
			//The rotation on y axis depends on the angle formed by its projection vector and original vector.
			
			double phi_ratio = x / Math.sqrt(modulus_sq);
			double phi = Math.asin(phi_ratio);
			this.roty = - phi * GL_ANGLE_RATIO;
			
			this.rotz = 0.D;
		}
		else
		{
			this.rotx = 0.D;
			this.roty = (x > 0)? 270.D : 90.D;
			this.rotz = 0.D;
		}
	}
	
	public void orient(double theta, double phi)
	{
		this.orient(Math.cos(phi) * Math.cos(theta), Math.cos(phi) * Math.sin(theta), Math.sin(phi));
	}
}
