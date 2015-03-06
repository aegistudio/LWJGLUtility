package net.aegistudio.transparent.wavefront.object;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.BufferUtils;

import net.aegistudio.transparent.opengl.model.ArrayPointerEntry;
import net.aegistudio.transparent.opengl.model.EnumArrayPointer;
import net.aegistudio.transparent.opengl.model.Model;
import net.aegistudio.transparent.opengl.util.EnumBufferTarget;
import net.aegistudio.transparent.opengl.util.EnumBufferUsage;
import net.aegistudio.transparent.opengl.util.EnumPrimitive;
import net.aegistudio.transparent.opengl.util.VertexBufferObject;
import net.aegistudio.transparent.wavefront.WavefrontBuilder;
import net.aegistudio.transparent.wavefront.ModelBuilder;

public class ObjectBuilder implements ModelBuilder<Map<String, Model>>
{
	protected String theName = null;
	public WavefrontBuilder theParent = null;
	protected Map<String, List<int[]>> objectsMap = new TreeMap<String, List<int[]>>();
	
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
			this.objectsMap.put(theName, ((FaceBuilder)theParent.builder.get("f")).getResult());
	}
	
	public Map<String, VertexBufferObject[]> vboResources = new TreeMap<String, VertexBufferObject[]>();
	
	@Override
	public Map<String, Model> getResult()
	{
		if(this.theParent == null) return null;
		this.flush();
		
		List<float[]> vertexPool = ((VertexBuilder)this.theParent.builder.get("v")).getResult();
		List<float[]> normalPool = ((NormalBuilder)this.theParent.builder.get("vn")).getResult();
		List<float[]> texCoordPool = ((TextureMappingBuilder)this.theParent.builder.get("vt")).getResult();
		
		Map<String, Model> resultMap = new TreeMap<String, Model>();
		
		for(String modelKey : objectsMap.keySet())
		{
			List<VertexBufferObject> currentVBOs = new ArrayList<VertexBufferObject>();
			currentVBOs.clear();
			
			List<int[]> objectTuples = this.objectsMap.get(modelKey);
			if(objectTuples.size() == 0) continue;
			int[] frontierElement = objectTuples.get(0);
			
			FloatBuffer vertices = null;
			if(frontierElement[0] >= 0) vertices = BufferUtils.createFloatBuffer(objectTuples.size() * 3);

			FloatBuffer texCoords = null;
			if(frontierElement[1] >= 0) texCoords = BufferUtils.createFloatBuffer(objectTuples.size() * 2);
			
			FloatBuffer normals = null;
			if(frontierElement[2] >= 0) normals = BufferUtils.createFloatBuffer(objectTuples.size() * 3);
			
			for(int i = 0; i < objectTuples.size(); i ++)
			{
				int[] tuple = objectTuples.get(i);
				if(vertices != null)
				{
					float[] vertex = vertexPool.get(tuple[0]);
					vertices.put(vertex);
				}
				if(texCoords != null)
				{
					float[] texCoord = texCoordPool.get(tuple[1]);
					texCoords.put(texCoord);
				}
				if(normals != null)
				{
					float[] normal = normalPool.get(tuple[2]);
					normals.put(normal);
				}
			}
			
			ArrayPointerEntry vertexPointer = null; 
			if(vertices != null)
			{
				vertices.flip();
				VertexBufferObject vertexVBO = new VertexBufferObject(EnumBufferTarget.ARRAY, EnumBufferUsage.STATIC_DRAW, vertices);
				currentVBOs.add(vertexVBO);
				vertexPointer = new ArrayPointerEntry(EnumArrayPointer.VERTEX, 3, vertexVBO);
			}
			
			ArrayPointerEntry texCoordPointer = null; 
			if(texCoords != null)
			{
				texCoords.flip();
				VertexBufferObject texCoordVBO = new VertexBufferObject(EnumBufferTarget.ARRAY, EnumBufferUsage.STATIC_DRAW, texCoords);
				currentVBOs.add(texCoordVBO);
				texCoordPointer = new ArrayPointerEntry(EnumArrayPointer.TEXTURE, 2, texCoordVBO);
			}
			
			ArrayPointerEntry normalPointer = null; 
			if(normals != null)
			{
				normals.flip();
				VertexBufferObject normalVBO = new VertexBufferObject(EnumBufferTarget.ARRAY, EnumBufferUsage.STATIC_DRAW, normals);
				currentVBOs.add(normalVBO);
				normalPointer = new ArrayPointerEntry(EnumArrayPointer.NORMAL, 3, normalVBO);
			}
			
			Model generatedModel = new Model(vertexPointer, texCoordPointer, normalPointer);
			generatedModel.setPrimitive(EnumPrimitive.TRIANGLES);
			resultMap.put(modelKey, generatedModel);
			vboResources.put(modelKey, currentVBOs.toArray(new VertexBufferObject[0]));
		}
		
		return resultMap;
	}
}
