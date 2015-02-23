package net.aegistudio.transparent.wavefront.material;

import net.aegistudio.transparent.wavefront.ModelBuilder;

public class MaterialLibBuilder implements ModelBuilder<String>
{
	protected String materialLib = null;
	
	@Override
	public void build(String[] splittedArguments)
	{
		this.materialLib = splittedArguments[1];
	}

	@Override
	public String getResult()
	{
		return this.materialLib;
	}
	
}
