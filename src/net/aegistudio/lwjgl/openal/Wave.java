package net.aegistudio.lwjgl.openal;

import java.io.InputStream;
import java.net.URL;

import net.aegistudio.lwjgl.util.Scoped;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class Wave implements Scoped
{
	private final WaveData waveData;
	
	public Wave(InputStream inputStream)
	{
		waveData = WaveData.create(inputStream);
	}
	
	public Wave(URL url)
	{
		waveData = WaveData.create(url);
	}
	
	public Wave(String path)
	{
		waveData = WaveData.create(path);
	}
	
	private int bufferId = 0;
	
	public int create()
	{
		if(this.waveData == null) throw new RuntimeException("The wave data to be buffered is invalid!");
		if(this.bufferId == 0)
		{
			this.bufferId = AL10.alGenBuffers();
			if(AL10.alGetError() != AL10.AL_NO_ERROR) throw new RuntimeException("Error while generating buffer!");
			
			AL10.alBufferData(this.bufferId, this.waveData.format, this.waveData.data, this.waveData.samplerate);
			this.waveData.dispose();
		}
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
}
