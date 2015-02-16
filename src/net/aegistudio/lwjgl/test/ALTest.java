package net.aegistudio.lwjgl.test;

import net.aegistudio.lwjgl.openal.Listener;
import net.aegistudio.lwjgl.openal.Source;
import net.aegistudio.lwjgl.openal.Wave;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class ALTest
{
	public static void main(String[] arguments) throws Exception
	{
		System.out.println("Creating OpenAL context...");
		AL.create();
		System.out.println("Created OpenAL context...");
		
		Listener listener = new Listener();
		listener.create();
		
		Wave testWave = new Wave("D:\\FancyPants.wav");
		testWave.create();
		
		Source source = new Source(testWave);
		source.create();
		
		Display.setTitle("AL");
		Display.setDisplayMode(new DisplayMode(600, 480));
		Display.create();
		Keyboard.create();
		
		while(!Display.isCloseRequested())
		{
			while(Keyboard.next())
			{
				if(Keyboard.getEventKey() == Keyboard.KEY_SPACE) source.play();
			}
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		
		source.destroy();
		testWave.destroy();	
		listener.destroy();
		
		Keyboard.destroy();
		AL.destroy();
	}
}
