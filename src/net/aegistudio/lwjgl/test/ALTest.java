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
		
		float gain = 1.0f;
		float pitch = 1.0f;
		while(!Display.isCloseRequested())
		{
			while(Keyboard.next()) if(Keyboard.getEventKeyState())
			{
				if(Keyboard.getEventKey() == Keyboard.KEY_SPACE) source.play();
				if(Keyboard.getEventKey() == Keyboard.KEY_UP)
				{
					gain += 0.1f;
					source.gain(gain);
					System.out.println("gain: " + gain);
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_DOWN)
				{
					gain -= 0.1f;
					source.gain(gain);
					System.out.println("gain: " + gain);
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT)
				{
					pitch += 0.1f;
					source.pitch(pitch);
					System.out.println("pitch: " + pitch);
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_LEFT)
				{
					pitch -= 0.1f;
					source.pitch(pitch);
					System.out.println("pitch: " + pitch);
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_P) source.pause();
				if(Keyboard.getEventKey() == Keyboard.KEY_S) source.stop();
				if(Keyboard.getEventKey() == Keyboard.KEY_L)
				{
					source.looping(true);
					System.out.println("looping: true");
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_K)
				{
					source.looping(false);
					System.out.println("looping: false");
				}
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
