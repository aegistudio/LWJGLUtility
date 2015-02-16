package net.aegistudio.lwjgl.openal;

import net.aegistudio.lwjgl.util.BindingFailureException;
import net.aegistudio.lwjgl.util.Scoped;

import org.lwjgl.openal.AL10;

public class Source implements Scoped
{
	private int sourceId;
	private final Wave wave;
	
	public Source(Wave wave)
	{
		this.wave = wave;
	}
	
	@Override
	public int create()
	{
		if(this.sourceId == 0)
		{
			this.sourceId = AL10.alGenSources();
			if(AL10.alGetError() != AL10.AL_NO_ERROR) throw new BindingFailureException("Error while generating source!");
			
			AL10.alSourcei(this.sourceId, AL10.AL_BUFFER, wave.create());
			AL10.alSource3f(this.sourceId, AL10.AL_POSITION, this.x, this.y, this.z);
			AL10.alSource3f(sourceId, AL10.AL_VELOCITY, this.velX, this.velY, this.velZ);
			
			AL10.alSourcef(this.sourceId, AL10.AL_PITCH, this.pitch);
			AL10.alSourcef(this.sourceId, AL10.AL_GAIN, this.gain);
			
			AL10.alSourcei(this.sourceId, AL10.AL_LOOPING, this.looping);
		}
		return this.sourceId;
	}

	private float x = 0, y = 0, z = 0;
	
	public void position(float x, float y, float z)
	{
		this.x = x; this.y = y; this.z = z;
		if(this.sourceId != 0) AL10.alSource3f(this.sourceId, AL10.AL_POSITION, this.x, this.y, this.z);
	}
	
	private float velX = 0, velY = 0, velZ = 0;
	
	public void velocity(float x, float y, float z)
	{
		this.velX = x; this.velY = y; this.velZ = z;
		if(this.sourceId != 0) AL10.alSource3f(sourceId, AL10.AL_VELOCITY, this.velX, this.velY, this.velZ);
	}
	
	private float pitch = 1.0f;
	private float gain = 1.0f;
	
	public void pitch(float pitch)
	{
		this.pitch = pitch;
		if(this.sourceId != 0) AL10.alSourcef(this.sourceId, AL10.AL_PITCH, this.pitch);
	}
	
	public void gain(float gain)
	{
		this.gain = gain;
		if(this.sourceId != 0) AL10.alSourcef(this.sourceId, AL10.AL_GAIN, this.gain);
	}
	
	private int looping = AL10.AL_FALSE;
	
	public void looping(boolean isLooping)
	{
		this.looping = (isLooping)? AL10.AL_TRUE : AL10.AL_FALSE;
		if(this.sourceId != 0) AL10.alSourcei(this.sourceId, AL10.AL_LOOPING, this.looping);
	}
	
	public void play()
	{
		if(this.sourceId != 0) AL10.alSourcePlay(sourceId);
	}
	
	public void pause()
	{
		if(this.sourceId != 0) AL10.alSourcePause(sourceId);
	}
	
	public void stop()
	{
		if(this.sourceId != 0) AL10.alSourceStop(sourceId);
	}
	
	@Override
	public void destroy()
	{
		if(this.sourceId != 0)
		{
			AL10.alDeleteSources(sourceId);
			this.sourceId = 0;
		}
	}
	
	public void finalize() throws Throwable
	{
		this.destroy();
		super.finalize();
	}
}
