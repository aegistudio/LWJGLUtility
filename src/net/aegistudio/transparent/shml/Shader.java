package net.aegistudio.transparent.shml;

import java.util.ArrayList;
import java.util.List;

import net.aegistudio.transparent.opengl.glsl.EnumShaderType;

import org.w3c.dom.Node;

public class Shader extends ShaderMarkupNode
{
	protected EnumShaderType shaderType;
	protected List<ShaderMarkupNode> variables;
	protected List<ShaderMarkupNode> functions;
	
	public Shader()
	{
		super.markups.put("function", Function.class);
		super.markups.put("variable", Variable.class);
	}
	
	public void parse(Node node) throws ShaderMarkupException
	{
		super.parse(node);
		String shaderType = super.attributes.get("type");
		this.shaderType = EnumShaderType.valueOf(shaderType.toUpperCase());
		if(this.shaderType == null) throw new ShaderMarkupException("shader.type");
		
		variables = this.childrens.get("variable");
		functions = this.childrens.get("function");
	}
	
	public EnumShaderType getShaderType()
	{
		return this.shaderType;
	}
	
	public boolean hasMainFunction()
	{
		if(functions != null) for(ShaderMarkupNode function : functions)
		{
			if(((Function)function).isMainFunction()) return true;
		}
		return false;
	}
	
	public String getShader(ShaderMarkupNode[] globals, String identifier)
	{		
		ArrayList<String> replacing = new ArrayList<String>();
		ArrayList<String> replacement = new ArrayList<String>();
		
		StringBuilder head = new StringBuilder();
		StringBuilder body = new StringBuilder();
		if(globals != null) for(ShaderMarkupNode globalNode : globals)
		{
			Variable global = (Variable)globalNode;
			replacing.add(global.getVariableName());
			replacement.add(global.getIdentifiedName(identifier));
			head.append(global.getIdentifiedString(identifier));
			head.append(";\n");
		}
		if(variables != null) for(ShaderMarkupNode currentNode : variables)
		{
			Variable current = (Variable)currentNode;
			replacing.add(current.getVariableName());
			replacement.add(current.getIdentifiedName(identifier));
			head.append(current.getIdentifiedString(identifier));
			head.append(";\n");
		}
		if(functions != null) for(ShaderMarkupNode functionNode : functions)
		{
			Function function = (Function)functionNode;
			head.append(function.getFunctionHeader(identifier));
			head.append(";\n");
			FunctionBody funcBody = function.getFunctionBody();
			if(funcBody != null)
			{
				body.append(function.getFunctionHeader(identifier));
				body.append("{");
				body.append(funcBody.getBody(replacing.toArray(new String[0]), replacement.toArray(new String[0])));
				body.append("}");
			}
		}
		return head.toString().concat(body.toString());
	}
}
