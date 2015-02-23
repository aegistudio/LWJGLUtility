package net.aegistudio.transparent.wavefront.object;

import java.util.ArrayList;
import net.aegistudio.transparent.wavefront.ModelBuilder;

public class VertexBuilder implements ModelBuilder<ArrayList<float[]>>
{
	protected ArrayList<float[]> verticesPool = new ArrayList<float[]>();
	
	@Override
	public void build(String[] splittedArguments)
	{
		verticesPool.add(new float[]
		{
			splittedArguments.length > 1? Float.parseFloat(splittedArguments[1]) : 0.F,
			splittedArguments.length > 2? Float.parseFloat(splittedArguments[2]) : 0.F,
			splittedArguments.length > 3? Float.parseFloat(splittedArguments[3]) : 0.F,
		});
	}

	@Override
	public ArrayList<float[]> getResult()
	{
		return this.verticesPool;
	}
}
