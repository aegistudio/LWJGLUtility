package net.aegistudio.lwjgl.util.glsl;

import java.util.HashMap;

import net.aegistudio.lwjgl.util.BindingFailureException;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

public class ShaderProgramObject
{
	private ShaderObject[] attachments;
	private int shaderProgramId = 0;
	
	private HashMap<String, Integer> uniformVariables = new HashMap<String, Integer>();
	private HashMap<String, Integer> attributeVariables = new HashMap<String, Integer>();
	
	public ShaderProgramObject(ShaderObject[] shaderObjects)
	{
		this.attachments = shaderObjects;
	}
	
	public int create()
	{
		if(this.shaderProgramId == 0)
		{
			shaderProgramId = ARBShaderObjects.glCreateProgramObjectARB();
			if(shaderProgramId == 0) throw new BindingFailureException("Unable to allocate space for shader program object!");
			
			try
			{
				for(ShaderObject shaderObject : attachments) if(shaderObject != null)
				{
					if(shaderObject.getShaderObjectId() == 0) shaderObject.create();
					ARBShaderObjects.glAttachObjectARB(shaderProgramId, shaderObject.getShaderObjectId());
				}
				
				ARBShaderObjects.glLinkProgramARB(shaderProgramId);
		        if(ARBShaderObjects.glGetObjectParameteriARB(shaderProgramId, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
		        {
		        	String failureInfo = ShaderObject.checkStatus(shaderProgramId);
		        	throw new LinkFailureException("Unable to link shader objects, cause by: " + failureInfo);
		        }
		        
		        ARBShaderObjects.glValidateProgramARB(shaderProgramId);
		        if (ARBShaderObjects.glGetObjectParameteriARB(shaderProgramId, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
		        {
		        	String failureInfo = ShaderObject.checkStatus(shaderProgramId);
		        	throw new ValidateFailureException("Unable to validate shader objects, cause by: " + failureInfo);
		        }
			}
			catch(RuntimeException re)
			{
				this.delete();
				throw re;
			}
		}
		return shaderProgramId;
	}
	
	public void bind()
	{
		if(this.shaderProgramId == 0) throw new RuntimeException("You must create the shader program object before binding it!");
		ARBShaderObjects.glUseProgramObjectARB(this.shaderProgramId);
	}
	
	public void unbind()
	{
		if(this.shaderProgramId == 0) throw new RuntimeException("You must create the shader program object before unbinding it!");
		ARBShaderObjects.glUseProgramObjectARB(0);
	}
	
	public void delete()
	{
		for(ShaderObject shaderObj : attachments) if(shaderObj != null)
			ARBShaderObjects.glDetachObjectARB(shaderProgramId, shaderObj.getShaderObjectId());
		ARBShaderObjects.glDeleteObjectARB(shaderProgramId);
		this.shaderProgramId = 0;
	}
	
	public int getUniformVariable(String string)
	{
		Integer uniformVariable = uniformVariables.get(string);
		if(uniformVariable == null)
		{
			uniformVariable = ARBShaderObjects.glGetUniformLocationARB(shaderProgramId, string);
			uniformVariables.put(string, uniformVariable);
		}
		return uniformVariable;
	}
	
	public int getAttributeVariable(String string)
	{
		Integer attributeVariable = attributeVariables.get(string);
		if(attributeVariable == null)
		{
			attributeVariable = ARBVertexShader.glGetAttribLocationARB(shaderProgramId, string);
			attributeVariables.put(string, attributeVariable);
		}
		return attributeVariable;
	}
}
