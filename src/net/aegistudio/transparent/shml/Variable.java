package net.aegistudio.transparent.shml;

import org.w3c.dom.Node;

/**
 * A variable node in shml, either a variable or an attribute.
 * @author aegistudio
 */

public class Variable extends ShaderMarkupNode
{
	String name;
	String type;
	String modifier;
	
	String trunk;
	
	@Override
	public void parse(Node node) throws ShaderMarkupException
	{
		super.parse(node);
		StringBuilder stringBuilder = new StringBuilder();
		
		modifier = this.attributes.get("modifier");
		if(modifier != null)
		{
			stringBuilder.append(modifier);
			stringBuilder.append(' ');
		}
		
		type = this.attributes.get("type");
		if(type == null) throw new ShaderMarkupException("variable.type.missing");
		stringBuilder.append(type);
		stringBuilder.append(' ');
		
		name = this.attributes.get("name");
		if(name == null) throw new ShaderMarkupException("variable.name.missing");
		stringBuilder.append(name);
		
		this.trunk = new String(stringBuilder);
	}
	
	public String getVariableName()
	{
		return this.name;
	}
	
	public String getVariableType()
	{
		return this.type;
	}
	
	public String getVariableModifier()
	{
		return this.modifier;
	}
	
	public String getIdentifiedName(String identifier)
	{
		if(identifier == null) return this.name;
		return this.name.concat("_").concat(identifier);
	}
	
	public String getIdentifiedString(String identifier)
	{
		if(identifier == null) return this.trunk;
		else return this.trunk.concat("_").concat(identifier);
	}
}
