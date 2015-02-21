package net.aegistudio.transparent.wavefront.object;

import java.util.ArrayList;
import java.util.List;

import net.aegistudio.transparent.wavefront.ModelBuilder;

public class TextureMappingBuilder implements ModelBuilder<List<float[]>>
{
	protected ArrayList<float[]> texCoordPool = new ArrayList<float[]>();
	
	@Override
	public void build(String[] splittedArguments)
	{
		texCoordPool.add(new float[]
		{
			splittedArguments.length > 1? Float.parseFloat(splittedArguments[1]) : 0.F,
			splittedArguments.length > 2? Float.parseFloat(splittedArguments[2]) : 0.F,
		});
	}

	@Override
	public List<float[]> getResult()
	{
		return texCoordPool;
	}
}
