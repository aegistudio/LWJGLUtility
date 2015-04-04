package net.aegistudio.transparent.shml.dom;

import org.w3c.dom.Node;

/**
 * The body of a shader function.
 * @author aegistudio
 */

public class FunctionBody extends ShaderMarkupNode
{
	protected String body;
	public void parse(Node node)
	{
		this.body = node.getTextContent();
	}
	
	public String getBody(String[] replacingVariables, String[] replacements)
	{
		if(this.body == null) return null;
		String newBody = this.body;
		for(int index = 0; index < replacingVariables.length; index ++)
		{
			StringBuilder builder = new StringBuilder();
			StringBuilder buffer = new StringBuilder();
			char[] bodyArray = newBody.toCharArray();
			boolean outsideVariable = true;
			for(int c = 0; c < bodyArray.length;)
			{
				if(outsideVariable)
				{
					if(!identifierChar(bodyArray[c]))
					{
						builder.append(bodyArray[c]);
						c ++;
					}
					else outsideVariable = false;
				}
				else
				{
					if(identifierChar(bodyArray[c]))
					{
						buffer.append(bodyArray[c]);
						c ++;
					}
					else
					{
						if(buffer.toString().equals(replacingVariables[index]))
							builder.append(replacements[index]);
						else builder.append(buffer.toString());
						buffer = new StringBuilder();
						outsideVariable = true;
					}
				}
			}
			
			if(buffer.length() > 0) if(buffer.toString().equals(replacingVariables[index]))
				builder.append(replacements[index]);
			else builder.append(buffer.toString());
			newBody = builder.toString();
		}
		
		return newBody;
	}
	
	public boolean identifierChar(char theChar)
	{
		return ('a' <= theChar && 'z' >= theChar)
			|| ('A' <= theChar && 'Z' >= theChar)
			|| ('0' <= theChar && '9' >= theChar)
			|| theChar == '_';
	}
}
