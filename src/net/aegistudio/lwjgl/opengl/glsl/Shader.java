package net.aegistudio.lwjgl.opengl.glsl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import net.aegistudio.lwjgl.opengl.util.FeatureNotSupportedException;
import net.aegistudio.lwjgl.util.BindingFailureException;
import net.aegistudio.lwjgl.util.Scoped;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class Shader implements Scoped
{
	private String glslSource;
	private int glslShaderType;
	private int glslShaderId;
	
	public Shader(String glslSource, int shaderType)
	{
		this.glslSource = glslSource;
		this.glslShaderType = shaderType;
		this.glslShaderId = 0;
	}
	
	public int create()
	{
		if(this.glslShaderId == 0)
		{
			if(!GLContext.getCapabilities().GL_ARB_shader_objects)
				throw new FeatureNotSupportedException("shading languague object");
			
			this.glslShaderId = ARBShaderObjects.glCreateShaderObjectARB(glslShaderType);
			if(this.glslShaderId == 0) throw new BindingFailureException("Unable to allocate space for shader object!");
			
			ARBShaderObjects.glShaderSourceARB(glslShaderId, glslSource);
			ARBShaderObjects.glCompileShaderARB(glslShaderId);
			
			if(ARBShaderObjects.glGetObjectParameteriARB(glslShaderId, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
			{
				String failureInfo = checkStatus(glslShaderId);
				CompileFailureException compileFailureException = new CompileFailureException("Unable to compile shader objects, cause by: " + failureInfo);
				ARBShaderObjects.glDeleteObjectARB(this.glslShaderId);
				this.glslShaderId = 0;
				throw compileFailureException;
			}
		}
		return this.glslShaderId;
	}
	
	public void destroy()
	{
		if(this.glslShaderId != 0)
		{
			ARBShaderObjects.glDeleteObjectARB(this.glslShaderId);
			this.glslShaderId = 0;
		}
	}
	
	public int getShaderObjectId()
	{
		return this.glslShaderId;
	}
	
	static String checkStatus(int glslObjectId)
	{
		return ARBShaderObjects.glGetInfoLogARB(glslObjectId, 
				ARBShaderObjects.glGetObjectParameteriARB(glslObjectId, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
	public static Shader createShaderFromSourceFile(File sourceFile, int shaderType) throws Exception
	{
		if(!sourceFile.exists()) throw new FileNotFoundException();
		StringBuilder sb = new StringBuilder();
		BufferedReader buffReader = new BufferedReader(new FileReader(sourceFile));
		String currentLine = null;
		while((currentLine = buffReader.readLine()) != null) sb.append(currentLine).append('\n');
		buffReader.close();
		return new Shader(new String(sb), shaderType);
	}
	
	public void finalize() throws Throwable
	{
		this.destroy();
		super.finalize();
	}
}