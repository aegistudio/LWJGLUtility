package net.aegistudio.lwjgl.test;

import net.aegistudio.lwjgl.opengl.Container;
import net.aegistudio.lwjgl.opengl.Drawable;
import net.aegistudio.lwjgl.opengl.camera.Camera;
import net.aegistudio.lwjgl.opengl.camera.Ortho;
import net.aegistudio.lwjgl.opengl.texture.RenderTexture;
import net.aegistudio.lwjgl.opengl.util.FrameBufferObject;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public final class RenderTextureTest
{
	public static void main(String[] arguments) throws Exception
	{
		final FrameBufferObject theFBO = new FrameBufferObject(new Drawable()
		{
			@Override
			public void onInit(Container container)
			{
				
			}

			@Override
			public void onDraw(Container container)
			{
				GL11.glClearColor(1, 0, 1, 0);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				System.out.println("Drawing fbo!");
			}

			@Override
			public void onDestroy(Container container)
			{
				
			}
		});
		final RenderTexture fboTex = new RenderTexture(theFBO, 200, 200, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
		
		Camera theCamera = new Ortho(600, 480, 2);
		theCamera.registerDrawable(theFBO);
		theCamera.registerDrawable(new Drawable()
		{

			@Override
			public void onInit(Container container)
			{
				fboTex.create();
			}

			@Override
			public void onDraw(Container container)
			{
				fboTex.bind();
				GL11.glColor3d(1, 0, 0);
				GL11.glBegin(GL11.GL_QUADS);
					fboTex.addVertexWithST(100, 100, 1, 1);//GL11.glVertex2d(100, 100);
					fboTex.addVertexWithST(100, -100, 1, 0);//GL11.glVertex2d(100, -100);
					fboTex.addVertexWithST(-100, -100, 0, 0);//GL11.glVertex2d(-100, -100);
					fboTex.addVertexWithST(-100, 100, 0, 1);//GL11.glVertex2d(-100, 100);
				GL11.glEnd();
				fboTex.unbind();
				System.out.println("Drawing windows buffer!");
			}

			@Override
			public void onDestroy(Container container)
			{
				fboTex.destroy();
			}
		});
		Display.setDisplayMode(new DisplayMode(600, 480));
		Display.setTitle("Test - RenderTexture");
		Display.create();
		theCamera.onInit(null);
		while(!Display.isCloseRequested())
		{
			GL11.glClearColor(0, 0, 0, 0);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			theCamera.onDraw(null);
			theCamera.unregisterDrawable(theFBO);
			Display.update();
			Display.sync(60);
		}
		theCamera.onDestroy(null);
		Display.destroy();
	}
}
