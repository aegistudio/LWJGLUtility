package net.aegistudio.transparent.wavefront.object;

import java.util.ArrayList;
import java.util.List;

import net.aegistudio.transparent.wavefront.ModelBuilder;

public class FaceBuilder implements ModelBuilder<List<int[]>>
{
	protected boolean isFlushed = true;
	protected ArrayList<int[]> vertices = new ArrayList<int[]>();
	
	@Override
	public void build(String[] splittedArguments)
	{
		if(splittedArguments.length <= 3) throw new IllegalArgumentException("It is impossible for two vertices to form a face, should be at least three!");
		for(int i = 0; i < splittedArguments.length - 3; i ++)
		{
			//Auto triangulate the surface.
			vertices.add(subsplit(splittedArguments[1]));
			vertices.add(subsplit(splittedArguments[i + 2]));
			vertices.add(subsplit(splittedArguments[i + 3]));
		}
	}

	protected int[] subsplit(String substring)
	{
		if(substring.matches("[0-9]*/[0-9]*/[0-9]*"))
		{
			//XXX Including iv/it/in and iv//in.
			String[] indices = substring.split("/", 3);
			int[] result = new int[indices.length];
			for(int i = 0; i < indices.length; i ++) if(indices[i].length() == 0) result[i] = -1;
			else result[i] = Integer.parseInt(indices[i]) - 1;
			
			return result;
		}
		else if(substring.matches("[0-9]*/[0-9]*"))
		{
			//XXX Including iv/it.
			String[] indices = substring.split("/", 2);
			int[] result = new int[3];
			for(int i = 0; i < indices.length; i ++) if(indices[i].length() == 0) result[i] = -1;
			else result[i] = Integer.parseInt(indices[i]) - 1;
			result[2] = -1;
			
			return result;
		}
		else return new int[] {Integer.parseInt(substring) - 1, -1, -1};	//XXX Including iv.
	}
	
	@Override
	public List<int[]> getResult()
	{
		this.isFlushed = true;
		List<int[]> returnFaces = this.vertices;
		this.vertices = new ArrayList<int[]>();
		return returnFaces;
	}
	
}
