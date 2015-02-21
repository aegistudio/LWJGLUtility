package net.aegistudio.transparent.wavefront.object;

import java.util.ArrayList;

import org.lwjgl.opengl.ARBVertexBufferObject;

import net.aegistudio.transparent.opengl.util.VertexBufferObject;
import net.aegistudio.transparent.wavefront.ModelBuilder;

public class VertexBuilder implements ModelBuilder<VertexBufferObject>
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
	public VertexBufferObject getResult()
	{
		float[] vertices_packed = new float[verticesPool.size() * 3];
		for(int i = 0; i < verticesPool.size(); i ++)
		{
			vertices_packed[3 * i + 0] = verticesPool.get(i)[0];
			vertices_packed[3 * i + 1] = verticesPool.get(i)[1];
			vertices_packed[3 * i + 2] = verticesPool.get(i)[2];
		}
		return new VertexBufferObject(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, ARBVertexBufferObject.GL_STATIC_DRAW_ARB, vertices_packed);
	}
}
