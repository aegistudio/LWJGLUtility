package net.aegistudio.transparent.shml;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.aegistudio.transparent.opengl.glsl.EnumShaderType;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ShaderMarkupLanguage extends ShaderMarkupNode
{
	protected Map<String, ShaderMarkupNode> nameToShaderProgram = new TreeMap<String, ShaderMarkupNode>();
	
	public ShaderMarkupLanguage()
	{
		super.markups.put("shaders", ShaderProgram.class);
	}
	
	public static ShaderMarkupLanguage build(String filename) throws Exception
	{
		DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = documentBuilder.newDocumentBuilder();
		Document document = builder.parse(new File(filename));
		ShaderMarkupLanguage instance = new ShaderMarkupLanguage();
		instance.parse(document);
		return instance;
	}
	
	public void parse(Node node) throws ShaderMarkupException
	{
		super.parse(node);
		List<ShaderMarkupNode> shaderPrograms = super.childrens.get("shaders");
		for(ShaderMarkupNode shaderProgram : shaderPrograms)
			nameToShaderProgram.put(((ShaderProgram)shaderProgram).shaderName, shaderProgram);
	}
	
	public Map<EnumShaderType, List<String>> getShaderSource(String shaderName, String identifier) throws ShaderMarkupException
	{
		ShaderProgram shaderProgram = (ShaderProgram) nameToShaderProgram.get(shaderName);
		return shaderProgram.getProcessedString(identifier);
	}
}
