package net.aegistudio.transparent.wavefront;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.aegistudio.transparent.opengl.Container;
import net.aegistudio.transparent.opengl.Drawable;
import net.aegistudio.transparent.opengl.model.Model;
import net.aegistudio.transparent.opengl.util.VertexBufferObject;
import net.aegistudio.transparent.util.Scoped;

/**
 * Please register this wavefront Model under the GL context so that 
 * it could auto manage its vertex buffer object resources.
 * @author aegistudio.
 */

public class WavefrontModel implements Drawable, Scoped
{
	protected final Map<String, Model> objModels;
	protected final Map<String, VertexBufferObject[]> vboResources;
	
	public WavefrontModel(Map<String, Model> objModels, Map<String, VertexBufferObject[]> vboResources)
	{
		this.objModels = objModels;
		this.vboResources = vboResources;
	}
	
	/**
	 * Get a object model, and initialize its resources by default.
	 * @param objName
	 * @return the Model whose depending resources are initialized.
	 */
	Set<String> getObjectModels = new TreeSet<String>();
	public Model getObjectModel(String objName)
	{
		Model model = objModels.get(objName);
		if(model != null) this.getObjectModels.add(objName);
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

	@Override
	public void onInit(Container container)
	{
		this.create();
	}

	@Override
	public void onDraw(Container container)
	{
		synchronized(this.getObjectModels)
		{
			Iterator<String> iter = getObjectModels.iterator();
			while(iter.hasNext())
			{
				String current = iter.next();
				VertexBufferObject[] resources = null;
				if((resources = vboResources.get(current)) != null) 
					for(VertexBufferObject resource : resources) resource.create();
				iter.remove();
			}
		}
	}

	@Override
	public void onDestroy(Container container)
	{
		this.destroy();
	}
}
