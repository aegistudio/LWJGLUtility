package net.aegistudio.transparent.wavefront;

import java.util.Map;

import net.aegistudio.transparent.opengl.model.Model;
import net.aegistudio.transparent.opengl.util.VertexBufferObject;
import net.aegistudio.transparent.util.Scoped;

/**
 * Please register this wavefront Model under the GL context so that 
 * it could auto manage its vertex buffer object resources.
 * @author aegistudio.
 */

public class WavefrontModel implements Scoped
{
	protected final Map<String, Model> objModels;
	protected final Map<String, VertexBufferObject[]> vboResources;
	
	public WavefrontModel(Map<String, Model> objModels, Map<String, VertexBufferObject[]> vboResources)
	{
		this.objModels = objModels;
		this.vboResources = vboResources;
	}
	
	/**
	 * Get a object model.
	 * @param objName
	 * @return the Model which is queried.
	 */
	public Model getObjectModel(String objName)
	{
		Model model = objModels.get(objName);
		return model;
	}
	
	public String[] listObjectModelNames()
	{
		return objModels.keySet().toArray(new String[0]);
	}

	@Override
	public int create()
	{
		return 0;
	}
	
	@Override
	public void destroy()
	{
		VertexBufferObject[] resources = null;
		for(String model : objModels.keySet()) if((resources = vboResources.get(model)) != null)
			for(VertexBufferObject resource : resources) resource.destroy();
	}
}
