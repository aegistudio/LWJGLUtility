package net.aegistudio.transparent.opengl.glsl;

import java.util.HashMap;

import net.aegistudio.transparent.util.Bindable;
import net.aegistudio.transparent.util.BindingFailureException;
import net.aegistudio.transparent.util.Resource;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

public class ShaderProgram implements Resource, Bindable
{
	private Shader[] shaders;
	private int shaderProgramId = 0;
	
	private HashMap<String, Integer> uniformVariables = new HashMap<String, Integer>();
	private HashMap<String, Integer> attributeVariables = new HashMap<String, Integer>();
	
	public ShaderProgram(Shader[] shaders)
	{
		this.shaders = shaders;
	}
	
	public void create()
	{
		if(this.shaderProgramId == 0)
		{
			shaderProgramId = ARBShaderObjects.glCreateProgramObjectARB();
			if(shaderProgramId == 0) throw new BindingFailureException("Unable to allocate space for shader program object!");
			
			try
			{
				for(Shader shader : shaders) if(shader != null)
				{
					if(shader.getShaderObjectId() == 0) shader.create();
					ARBShaderObjects.glAttachObjectARB(shaderProgramId, shader.getShaderObjectId());
				}
				
				ARBShaderObjects.glLinkProgramARB(shaderProgramId);
		        if(ARBShaderObjects.glGetObjectParameteriARB(shaderProgramId, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
		        {
		        	String failureInfo = Shader.checkStatus(shaderProgramId);
		        	throw new LinkFailureException("Unable to link shader objects, cause by: " + failureInfo);
		        }
		        
		        ARBShaderObjects.glValidateProgramARB(shaderProgramId);
		        if (ARBShaderObjects.glGetObjectParameteriARB(shaderProgramId, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
		        {
		        	String failureInfo = Shader.checkStatus(shaderProgramId);
		        	throw new ValidateFailureException("Unable to validate shader objects, cause by: " + failureInfo);
		        }
			}
			catch(RuntimeException re)
			{
				this.destroy();
				throw re;
			}
		}
	}
	
	protected static ShaderProgram shaderProgramUsing = null;
	
	protected ShaderProgram previousProgram;
	
	public void bind()
	{
		if(this.shaderProgramId == 0) throw new RuntimeException("You must create the shader program object before binding it!");
		
		ShaderProgram previous = shaderProgramUsing;
		while(previous != null)
		{
			if(previous == this) return;
			previous = previous.previousProgram;
		}
		previousProgram = shaderProgramUsing;
		shaderProgramUsing = this;
		ARBShaderObjects.glUseProgramObjectARB(this.shaderProgramId);
	}
	
	public void unbind()
	{
		if(this.shaderProgramId == 0) throw new RuntimeException("You must create the shader program object before unbinding it!");
		if(shaderProgramUsing == this)
		{
			if(previousProgram != null)
				ARBShaderObjects.glUseProgramObjectARB(previousProgram.shaderProgramId);
			else ARBShaderObjects.glUseProgramObjectARB(0);
			shaderProgramUsing = previousProgram;
		}
	}
	
	public void destroy()
	{
		for(Shader shader : shaders) if(shader != null)
			ARBShaderObjects.glDetachObjectARB(shaderProgramId, shader.getShaderObjectId());
		ARBShaderObjects.glDeleteObjectARB(shaderProgramId);
		this.shaderProgramId = 0;
	}
	
	public Uniform getUniform(String string, EnumShaderData shaderData)
	{
		Integer uniformVariable = uniformVariables.get(string);
		if(uniformVariable == null)
		{
			uniformVariable = ARBShaderObjects.glGetUniformLocationARB(shaderProgramId, string);
			uniformVariables.put(string, uniformVariable);
		}
		if(uniformVariable < 0) return null;
		return new Uniform(uniformVariable, shaderData);
	}
	
	public VertexAttribute getVertexAttribute(String string, EnumShaderData shaderData)
	{
		Integer attributeVariable = attributeVariables.get(string);
		if(attributeVariable == null)
		{
			attributeVariable = ARBVertexShader.glGetAttribLocationARB(shaderProgramId, string);
			attributeVariables.put(string, attributeVariable);
		}
		if(attributeVariable < 0) return null;
		return new VertexAttribute(attributeVariable, shaderData);
	}
	
	public void finalize() throws Throwable
	{
		this.destroy();
		super.finalize();
	}
	
	public int getShaderProgramId()
	{
		return this.shaderProgramId;
	}
}
