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
	
	public static void main(String[] arguments) throws Exception
	{
		WavefrontModel model = new WavefrontBuilder().build(new java.io.FileInputStream(new java.io.File("C:\\Users\\admin\\Desktop\\untitled.obj")));
		for(String str : model.listObjectModelNames()) System.out.println(str);
	}
}
