package net.aegistudio.transparent.shml.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class ShaderMarkupNode
{
	protected final TreeMap<String, Class<? extends ShaderMarkupNode>> markups
		= new TreeMap<String, Class<? extends ShaderMarkupNode>>();
	
	protected final TreeMap<String, List<ShaderMarkupNode>> childrens
		= new TreeMap<String, List<ShaderMarkupNode>>();
	
	protected final TreeMap<String, String> attributes = new TreeMap<String, String>();;
	
	public void parse(Node node) throws ShaderMarkupException
	{
		NodeList nodelist = node.getChildNodes();
		for(int i = 0; i < nodelist.getLength(); i ++)
		{
			Node child = nodelist.item(i);
			int nodeType = node.getNodeType();
			if(		nodeType == Node.TEXT_NODE
					|| nodeType == Node.CDATA_SECTION_NODE
					|| nodeType == Node.COMMENT_NODE) continue;
			else try
			{
				Class<? extends ShaderMarkupNode> parserClass = markups.get(child.getNodeName().toLowerCase());
				if(parserClass == null) continue;
				ShaderMarkupNode parser = parserClass.newInstance();
				parser.parse(child);
				List<ShaderMarkupNode> nodes = childrens.get(child.getNodeName());
				if(nodes == null) childrens.put(child.getNodeName().toLowerCase(), (nodes = new ArrayList<ShaderMarkupNode>()));
				nodes.add(parser);
			}
			catch(Exception e)
			{
				if(e instanceof ShaderMarkupException) throw (ShaderMarkupException)e;
				else throw new ShaderMarkupException("node.internalerror");
			}
		}
		
		NamedNodeMap attributeList = node.getAttributes();
		if(attributeList != null) for(int i = 0; i < attributeList.getLength(); i ++) 
		{
			Node child = attributeList.item(i);
			attributes.put(child.getNodeName().toLowerCase(), child.getNodeValue());
		}
	}
}
