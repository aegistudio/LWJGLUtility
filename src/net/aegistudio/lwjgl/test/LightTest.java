package net.aegistudio.lwjgl.test;

import net.aegistudio.lwjgl.opengl.Container;
import net.aegistudio.lwjgl.opengl.Drawable;
import net.aegistudio.lwjgl.opengl.camera.Camera;
import net.aegistudio.lwjgl.opengl.camera.Frustum;
import net.aegistudio.lwjgl.opengl.lighting.Light;
import net.aegistudio.lwjgl.opengl.util.DisplayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class LightTest
{
	public static void main(String[] arguments) throws Exception
	{
		Display.setTitle("Light");
		Display.setDisplayMode(new DisplayMode(600, 480));
		Display.create();
		
		final Light theLight = new Light();
		
		Camera theCamera = new Frustum(600, 480, 0.1, 200);
		theCamera.registerDrawable(new Drawable()
		{
			DisplayList displayList;
			
			@Override
			public void onInit(Container container)
			{
				displayList = new DisplayList()
				{
					@Override
					protected void display(int displayIndex)
					{
						GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, 1.0F);
						GL11.glBegin(GL11.GL_POLYGON);
						int count = 10;
						int radium = 100;
						for(int i = 0; i < count; i ++)
							GL11.glVertex3d(radium * Math.sin(2 * i * Math.PI/ count), radium * Math.cos(2 * i * Math.PI / count), 0.2);
						GL11.glEnd();
					}
				};
				displayList.create();
			}

			@Override
			public void onDraw(Container container)
			{
				theLight.bind();
				displayList.call(0);
				theLight.unbind();
			}

			@Override
			public void onDestroy(Container container)
			{
				
			}
			
		});
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glLightModeli(GL11.GL_LIGHT_MODEL_LOCAL_VIEWER, GL11.GL_TRUE);
		
		System.out.println("Using GL_LIGHT" + theLight.create());
		theCamera.orient(0, 0, 10000);
		theLight.specular(1, 0, 0, 1);
		theLight.position(0, 0, 1, 0);
		
		theCamera.onInit(null);
		while(!Display.isCloseRequested())
		{
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			theCamera.onDraw(null);
			
			Display.update();
			Display.sync(60);
		}
		theCamera.onDestroy(null);

		theLight.destroy();
		Display.destroy();
	}
}
