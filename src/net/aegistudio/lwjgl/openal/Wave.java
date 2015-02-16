package net.aegistudio.lwjgl.openal;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import net.aegistudio.lwjgl.util.Scoped;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class Wave implements Scoped
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
