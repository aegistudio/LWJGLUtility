package net.aegistudio.transparent.openal;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioSystem;

import net.aegistudio.transparent.util.BindingFailureException;
import net.aegistudio.transparent.util.Resource;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class Wave implements Resource
{
	private final WaveData waveData;
	
	public Wave(InputStream inputStream) throws Exception
	{
		waveData = WaveData.create(AudioSystem.getAudioInputStream(inputStream));
	}
	
	public Wave(URL url) throws Exception
	{
		waveData = WaveData.create(AudioSystem.getAudioInputStream(url));
	}
	
	public Wave(String path) throws Exception
	{
		waveData = WaveData.create(AudioSystem.getAudioInputStream(new File(path)));
	}
	
	private int bufferId = 0;
	
	public void create()
	{
		if(this.waveData == null) throw new BindingFailureException("The wave data to be buffered is invalid!");
		if(this.bufferId == 0)
		{
			this.bufferId = AL10.alGenBuffers();
			if(AL10.alGetError() != AL10.AL_NO_ERROR) throw new BindingFailureException("Error while generating buffer!");
			
			AL10.alBufferData(this.bufferId, this.waveData.format, this.waveData.data, this.waveData.samplerate);
			this.waveData.dispose();
		}
	}

	public int getBufferId()
	{
		return this.bufferId;
	}
	
	@Override
	public void destroy()
	{
		if(this.bufferId != 0)
		{
			AL10.alDeleteBuffers(bufferId);
			this.bufferId = 0;
		}
	}
	
	public void finalize() throws Throwable
	{
		this.destroy();
		super.finalize();
	}
}
