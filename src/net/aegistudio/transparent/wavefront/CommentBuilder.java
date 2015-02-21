package net.aegistudio.transparent.wavefront;

import java.util.ArrayList;

public class CommentBuilder implements ModelBuilder<String[]>
{
	protected ArrayList<String> comments = new ArrayList<String>();
	
	@Override
	public void build(String[] splittedArguments)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 1; i < splittedArguments.length; i ++)
		{
			if(i > 1) stringBuilder.append(' ');
			stringBuilder.append(splittedArguments[i]);
		}
		comments.add(new String(stringBuilder));
	}

	@Override
	public String[] getResult()
	{
		return comments.toArray(new String[0]);
	}
}
