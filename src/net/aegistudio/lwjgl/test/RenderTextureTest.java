package net.aegistudio.lwjgl.test;

import net.aegistudio.lwjgl.opengl.Container;
import net.aegistudio.lwjgl.opengl.Drawable;
import net.aegistudio.lwjgl.opengl.Scene;
import net.aegistudio.lwjgl.opengl.camera.Camera;
import net.aegistudio.lwjgl.opengl.camera.Frustum;
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
			int rot = 0;
			
			@Override
			public void onInit(Container container)
			{
				
			}

			@Override
			public void onDraw(Container container)
			{
				
				rot ++;
				GL11.glClearColor(1, 0, 1, 1);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				
				
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				GL11.glRotated(rot, 0, 0, 1);
				
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex2d(0.5, 0.5);
					GL11.glVertex2d(-0.5, 0.5);
					GL11.glVertex2d(-0.5, -0.5);
					GL11.glVertex2d(0.5, -0.5);
				GL11.glEnd();
			}

			@Override
			public void onDestroy(Container container)
			{
				
			}
		});
		final RenderTexture fboTex = new RenderTexture(theFBO, 200, 200, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
		theFBO.setViewport(200, 200);
		
		Camera theCamera = new Frustum(600, 480, 0.1, 2);
		theCamera.translate(0, 0, -0.12);
		theCamera.orient(0, 0.0002, 1);
		Scene theScene = new Scene();
		theCamera.registerDrawable(theScene);
		theScene.registerDrawable(new Drawable()
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
				GL11.glBegin(GL11.GL_QUADS);
					fboTex.addVertexWithST(100, 100, 1, 1);//GL11.glVertex2d(100, 100);
					fboTex.addVertexWithST(100, -100, 1, 0);//GL11.glVertex2d(100, -100);
					fboTex.addVertexWithST(-100, -100, 0, 0);//GL11.glVertex2d(-100, -100);
					fboTex.addVertexWithST(-100, 100, 0, 1);//GL11.glVertex2d(-100, 100);
				GL11.glEnd();
				fboTex.unbind();
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
			theFBO.onDraw(null);
			
			GL11.glClearColor(0, 0, 0, 1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			theCamera.onDraw(null);
			
			Display.update();
			Display.sync(60);
		}
		theCamera.onDestroy(null);
		Display.destroy();
	}
}
