package net.aegistudio.transparent.wavefront;

import java.util.Map;

import net.aegistudio.transparent.opengl.model.Model;

public class WavefrontModel
{
	protected final Map<String, Model> objModels;
	
	public WavefrontModel(Map<String, Model> objModels)
	{
		this.objModels = objModels;
	}
	
	public Model getObjectModel(String objName)
	{
		return objModels.get(objName);
	}
	
	public String[] listObjectModelNames()
	{
		return objModels.keySet().toArray(new String[0]);
	}
}
