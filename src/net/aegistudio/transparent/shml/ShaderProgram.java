package net.aegistudio.transparent.shml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.aegistudio.transparent.opengl.glsl.EnumShaderType;

import org.w3c.dom.Node;

public class ShaderProgram extends ShaderMarkupNode
{
	protected String shaderName;
	
	public ShaderProgram()
	{
		super.markups.put("variable", Variable.class);
		super.markups.put("shader", Shader.class);
	}
	
	protected boolean firstLine = false;
	
	public void firstLine(StringBuilder builder)
	{
		if(!firstLine) builder.append('\n');
		firstLine = false;
	}
	
	public void parse(Node node) throws ShaderMarkupException
	{
		super.parse(node);
		this.shaderName = this.attributes.get("name");
		if(this.shaderName == null) throw new ShaderMarkupException("shaders.name.missing");
	}
	
	public Map<EnumShaderType, List<String>> getProcessedString(String identifier) throws ShaderMarkupException
	{
		identifier = this.shaderName.concat("_").concat(identifier);
		Map<EnumShaderType, List<String>> returnValue = new TreeMap<EnumShaderType, List<String>>();
		int[] mainFunctionCount = new int[EnumShaderType.values().length];
		List<ShaderMarkupNode> shaders = super.childrens.get("shader");
		if(shaders == null) return returnValue;
		ShaderMarkupNode[] globals = super.childrens.get("variable").toArray(new ShaderMarkupNode[0]);
		for(ShaderMarkupNode shader : shaders)
		{
			EnumShaderType type = ((Shader)shader).getShaderType();
			if(((Shader)shader).hasMainFunction()) mainFunctionCount[type.ordinal()] += 1;
			if(mainFunctionCount[type.ordinal()] > 1) throw new ShaderMarkupException("shader.main.duplicate");
			List<String> shaderString = returnValue.get(type);
			if(shaderString == null) returnValue.put(type, (shaderString = new ArrayList<String>()));
			shaderString.add(((Shader)shader).getShader(globals, identifier));
		}
		for(EnumShaderType type : returnValue.keySet()) if(mainFunctionCount[type.ordinal()] == 0)
			throw new ShaderMarkupException("shader.main.missing");
		return returnValue;
	}
	
	public String getShaderName()
	{
		return this.shaderName;
	}
	
	public List<ShaderMarkupNode> getVariables()
	{
		List<ShaderMarkupNode> variables = this.childrens.get("variable");
		if(variables == null) variables = new ArrayList<ShaderMarkupNode>();
		return variables;
	}
}
