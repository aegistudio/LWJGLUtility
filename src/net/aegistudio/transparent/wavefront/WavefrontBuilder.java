package net.aegistudio.transparent.wavefront;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

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
	}
	
	public WavefrontModel build(InputStream blenderInputStream) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(blenderInputStream));
		String inputLine = null;
		while((inputLine = reader.readLine())!= null)
		{
			String[] splitted = inputLine.split(" ");
			ModelBuilder<?> callingBuilder = builder.get(splitted[0]);
			if(callingBuilder != null) callingBuilder.build(splitted);
		}
		
		return new WavefrontModel(((ObjectBuilder)builder.get("o")).getResult());
	}
}
