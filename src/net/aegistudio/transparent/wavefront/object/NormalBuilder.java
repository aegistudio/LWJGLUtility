package net.aegistudio.transparent.wavefront.object;

import java.util.ArrayList;
import java.util.List;

import net.aegistudio.transparent.wavefront.ModelBuilder;

public class NormalBuilder implements ModelBuilder<List<float[]>>
{
	protected boolean isTouched = false;
	protected ArrayList<float[]> normalsPool = new ArrayList<float[]>();
	
	@Override
	public void build(String[] splittedArguments)
	{
		normalsPool.add(new float[]
		{
			splittedArguments.length > 1? Float.parseFloat(splittedArguments[1]) : 0.F,
			splittedArguments.length > 2? Float.parseFloat(splittedArguments[2]) : 0.F,
			splittedArguments.length > 3? Float.parseFloat(splittedArguments[3]) : 0.F,
		});
	}

	@Override
	public List<float[]> getResult()
	{
		return normalsPool;
	}
}
