package net.aegistudio.transparent.wavefront.object;


import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.opengl.ARBVertexBufferObject;

import net.aegistudio.transparent.opengl.model.ArrayPointerEntry;
import net.aegistudio.transparent.opengl.model.EnumArrayPointer;
import net.aegistudio.transparent.opengl.model.Model;
import net.aegistudio.transparent.opengl.util.VertexBufferObject;
import net.aegistudio.transparent.wavefront.WavefrontBuilder;
import net.aegistudio.transparent.wavefront.ModelBuilder;

public class ObjectBuilder implements ModelBuilder<Map<String, Model>>
{
	protected String theName = null;
	public WavefrontBuilder theParent = null;
	protected Map<String, List<int[]>> facesMap = new TreeMap<String, List<int[]>>();
	
	@Override
	public void build(String[] splittedArguments)
	{
		this.flush();
		if(splittedArguments.length > 1) theName = splittedArguments[1];
		else this.theName = "untitled";
	}
	
	public void flush()
	{
		if(this.theParent == null) return;
		if(this.theName != null)
			this.facesMap.put(theName, ((FaceBuilder)theParent.builder.get("f")).getResult());
	}
	
	@Override
	public Map<String, Model> getResult()
	{
		if(this.theParent == null) return null;
		this.flush();
		VertexBuilder vertexBuilder = (VertexBuilder)this.theParent.builder.get("v");
		ArrayPointerEntry vertexPointer = new ArrayPointerEntry(EnumArrayPointer.VERTEX, vertexBuilder.getResult());
		
		List<float[]> normalPool = ((NormalBuilder)this.theParent.builder.get("vn")).getResult();
		List<float[]> texCoordPool = ((TextureMappingBuilder)this.theParent.builder.get("vt")).getResult();
		
		Map<String, Model> resultMap = new TreeMap<String, Model>();
		for(String modelKey : facesMap.keySet())
		{
			List<int[]> faces = this.facesMap.get(modelKey);
			if(faces.size() == 0) continue;
			int[] frontierElement = faces.get(0);
			
			int[] indices = null;
			if(frontierElement[0] >= 0) indices = new int[faces.size()];

			float[] texCoords = null;
			if(frontierElement[1] >= 0) texCoords = new float[faces.size() * 2];
			
			float[] normals = null;
			if(frontierElement[2] >= 0) normals = new float[faces.size() * 3];
			
			for(int i = 0; i < faces.size(); i ++)
			{
				int[] tuple = faces.get(i);
				if(indices != null) indices[i] = tuple[0];
				if(texCoords != null)
				{
					float[] texCoord = texCoordPool.get(tuple[1]);
					texCoords[2 * i + 0] = texCoord[0];
					texCoords[2 * i + 1] = texCoord[1];
				}
				if(normals != null)
				{
					float[] normal = normalPool.get(tuple[2]);
					normals[3 * i + 0] = normal[0];
					normals[3 * i + 1] = normal[1];
					normals[3 * i + 2] = normal[2];
				}
			}
			
			ArrayPointerEntry indexPointer = null; 
			if(indices != null)
			{
				VertexBufferObject indicesVBO = new VertexBufferObject(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, ARBVertexBufferObject.GL_STATIC_DRAW_ARB, indices);
				indexPointer = new ArrayPointerEntry(EnumArrayPointer.INDEX, indices.length, indicesVBO);
			}
			
			ArrayPointerEntry texCoordPointer = null; 
			if(texCoords != null)
			{
				VertexBufferObject texCoordVBO = new VertexBufferObject(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, ARBVertexBufferObject.GL_STATIC_DRAW_ARB, texCoords);
				texCoordPointer = new ArrayPointerEntry(EnumArrayPointer.INDEX, 2, texCoordVBO);
			}
			
			ArrayPointerEntry normalPointer = null; 
			if(normals != null)
			{
				VertexBufferObject normalVBO = new VertexBufferObject(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, ARBVertexBufferObject.GL_STATIC_DRAW_ARB, normals);
				normalPointer = new ArrayPointerEntry(EnumArrayPointer.INDEX, 3, normalVBO);
			}
			
			resultMap.put(modelKey, new Model(vertexPointer, indexPointer, texCoordPointer, normalPointer));
		}
		
		return resultMap;
	}
}
