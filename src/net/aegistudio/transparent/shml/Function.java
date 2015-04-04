package net.aegistudio.transparent.shml;

import java.util.List;

import org.w3c.dom.Node;

public class Function extends ShaderMarkupNode
{
	protected FunctionBody body;

	protected String functionName;
	protected String parameterList;
	
	public boolean isMainFunction = true;
	
	public Function()
	{
		super.markups.put("parameter", Variable.class);
		super.markups.put("body", FunctionBody.class);
	}
	
	@Override
	public void parse(Node node) throws ShaderMarkupException
	{
		super.parse(node);
		StringBuilder builder = new StringBuilder();
		
		String type = this.attributes.get("type");
		if(type == null) type = "void";
		builder.append(type);
		builder.append(' ');
		if(isMainFunction) isMainFunction = type.equals("void");
		
		String name = this.attributes.get("name");
		if(name == null) throw new ShaderMarkupException("function.name.missing");
		builder.append(name);
		if(isMainFunction) isMainFunction = name.equals("main");
		
		this.functionName = new String(builder);
		
		builder = new StringBuilder();
		builder.append('(');
		List<ShaderMarkupNode> parameters = super.childrens.get("parameter");
		boolean isFirst = true;
		if(parameters != null) for(ShaderMarkupNode parameter : parameters)
		{
			if(isFirst) isFirst = false;
			else builder.append(',');
			String parameterString = ((Variable)parameter).getIdentifiedString(null);
			builder.append(parameterString);
		}
		builder.append(')');
		
		if(isMainFunction) isMainFunction = (parameters == null) || (parameters.size() == 0);
		
		this.parameterList = new String(builder);
		
		List<ShaderMarkupNode> bodies = super.childrens.get("body");
		if(bodies != null)
		{
			if(bodies.size() == 1) this.body = (FunctionBody) bodies.get(0);
			else throw new ShaderMarkupException("function.body");
		}
		
		if(isMainFunction) isMainFunction = body != null;
	}
	
	public String getFunctionHeader(String identifier)
	{
		return this.getIdentifiedName(identifier).concat(parameterList);
	}
	
	public String getIdentifiedName(String identifier)
	{
		return this.functionName.concat("_").concat(identifier);
	}
	
	public String getFunctionName()
	{
		return this.functionName;
	}
	
	public FunctionBody getFunctionBody()
	{
		return this.body;
	}
	
	public boolean isMainFunction()
	{
		return this.isMainFunction;
	}
}
