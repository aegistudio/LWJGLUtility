package net.aegistudio.lwjgl.openal;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

import net.aegistudio.lwjgl.util.Scoped;

public class Listener implements Scoped
{
	
	public Listener()
	{
		
	}
	
	@Override
	public int create()
	{
		if(!AL.isCreated()) return AL10.AL_FALSE;
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, velX, velY, velZ);
		AL10.alListener(AL10.AL_ORIENTATION, this.orientation);
		return AL10.AL_TRUE;
	}
	
	@Override
	public void destroy()
	{
		
	}
	
	private float x = 0, y = 0, z = 0;
	private float velX = 0, velY = 0, velZ = 0;
	
	public void position(float x, float y, float z)
	{
		this.x = x; this.y = y; this.z = z;
		if(AL.isCreated()) AL10.alListener3f(AL10.AL_POSITION, this.x, this.y, this.z);
	}
	
	public void velocity(float x, float y, float z)
	{
		this.velX = x; this.velY = y; this.velZ = z;
		if(AL.isCreated()) AL10.alListener3f(AL10.AL_VELOCITY, velX, velY, velZ);
	}
	
	private FloatBuffer orientation = BufferUtils.createFloatBuffer(6).put(new float[] {0, 0, 0, 0, 0, 0});
	{
		orientation.flip();
	}
	
	public void orientation(float atx, float aty, float atz, float upx, float upy, float upz)
	{
		this.orientation = BufferUtils.createFloatBuffer(6).put(new float[] {atx, aty, atz, upx, upy, upz});
		this.orientation.flip();
		if(AL.isCreated()) AL10.alListener(AL10.AL_ORIENTATION, this.orientation);
	}
	
	public void finalize() throws Throwable
	{
		this.destroy();
		super.finalize();
	}
}
