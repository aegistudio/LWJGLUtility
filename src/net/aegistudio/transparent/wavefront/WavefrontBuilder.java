package net.aegistudio.transparent.wavefront;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.aegistudio.transparent.wavefront.material.MaterialLibBuilder;
import net.aegistudio.transparent.wavefront.object.FaceBuilder;
import net.aegistudio.transparent.wavefront.object.NormalBuilder;
import net.aegistudio.transparent.wavefront.object.ObjectBuilder;
import net.aegistudio.transparent.wavefront.object.TextureMappingBuilder;
import net.aegistudio.transparent.wavefront.object.VertexBuilder;

public class WavefrontBuilder
{
	public Map<String, ModelBuilder<?>> builder = new HashMap<String, ModelBuilder<?>>();
	{
		builder.put("#", new CommentBuilder());
		builder.put("o", new ObjectBuilder());
		((ObjectBuilder)builder.get("o")).theParent = this;
		builder.put("v", new VertexBuilder());
		builder.put("vn", new NormalBuilder());
		builder.put("vt", new TextureMappingBuilder());
		builder.put("f", new FaceBuilder());
		builder.put("mtllib", new MaterialLibBuilder());
	}
	
	public WavefrontModel build(InputStream modelInputStream) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(modelInputStream));
		String inputLine = null;
		while((inputLine = reader.readLine())!= null)
		{
			String[] splitted = inputLine.split(" ");
			ModelBuilder<?> callingBuilder = builder.get(splitted[0]);
			if(callingBuilder != null) callingBuilder.build(splitted);
		}
		
		ObjectBuilder objBuilder = (ObjectBuilder)builder.get("o");
		return new WavefrontModel(objBuilder.getResult(), objBuilder.vboResources);
	}
	
	public String[] getComments()
	{
		return ((CommentBuilder)builder.get("#")).getResult();
	}
	
	public String getMaterialLibrary()
	{
		return ((MaterialLibBuilder)builder.get("mtllib")).getResult();
	}
}
