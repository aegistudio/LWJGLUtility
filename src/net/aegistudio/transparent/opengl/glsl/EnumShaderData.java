package net.aegistudio.transparent.opengl.glsl;

import java.lang.reflect.Method;

import net.aegistudio.transparent.opengl.texture.Texture;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;

public enum EnumShaderData
{
	FLOAT(1, "f", float.class),
	VEC2(2, "f", float.class),
	VEC3(3, "f", float.class),
	VEC4(4, "f", float.class),
	
	INT(1, "s", short.class, "i", int.class),
	IVEC2(2, "s", short.class, "i", int.class),
	IVEC3(3, "s", short.class, "i", int.class),
	IVEC4(4, "s", short.class, "i", int.class),
	
	DOUBLE(1, "d", double.class, "f", float.class),
	DVEC2(2, "d", double.class, "f", float.class),
	DVEC3(3, "d", double.class, "f", float.class),
	DVEC4(4, "d", double.class, "f", float.class),
	
	TEXTURE(1, "s", short.class, "i", int.class)
	{
		public void vertexAttribute(int index, Object... objects)
		{
			super.vertexAttribute(index, ((Texture)objects[0]).getMultiTextureUnit());
		}
		
		public void uniform(int index, Object... objects)
		{
			super.uniform(index, ((Texture)objects[0]).getMultiTextureUnit());
		}
	};
	
	private EnumShaderData(int size, String attribSuffix, Class<?> vertClazz, String uniformSuffix, Class<?> uniformClazz) throws Error
	{
		try
		{
			this.size = size;
			Class<?>[] vertexClasses = new Class<?>[size + 1];
			Class<?>[] uniformClasses = new Class<?>[size + 1];
			
			for(int i = 0; i < size; i ++)
			{
				vertexClasses[i + 1] = vertClazz;
				uniformClasses[i + 1] = uniformClazz;
			}
			
			vertexClasses[0] = int.class;
			uniformClasses[0] = int.class;
			
			this.vertexAttributeMethod = ARBVertexShader.class.getMethod("glVertexAttrib" + size + attribSuffix + "ARB", vertexClasses);
			this.uniformMethod = ARBShaderObjects.class.getMethod("glUniform" + size + uniformSuffix + "ARB", uniformClasses);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Error("Unable to instantialize shader data enumeration object!");
		}
	}
	
	private EnumShaderData(int size, String suffix, Class<?> clazz) throws Error
	{
		this(size, suffix, clazz, suffix, clazz);
	}
	
	public final int size;
	public final Method vertexAttributeMethod;
	public final Method uniformMethod;
	
	
	public void vertexAttribute(int index, Object... objects)
	{
		try
		{
			this.vertexAttributeMethod.invoke(null, objects);
		}
		catch(Exception e)
		{
			
		}
	}
	public void uniform(int index, Object... objects)
	{
		try
		{
			this.vertexAttribute(index, objects);
		}
		catch(Exception e)
		{
			
		}
	}
}

