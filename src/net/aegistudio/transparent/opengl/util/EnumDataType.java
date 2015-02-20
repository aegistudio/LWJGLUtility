package net.aegistudio.transparent.opengl.util;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public enum EnumDataType
{
	BYTE(byte.class, 1, GL11.GL_UNSIGNED_BYTE),
	BYTE_WRAPPED(Byte.class, 1, GL11.GL_UNSIGNED_BYTE),
	INT(int.class, Integer.SIZE / Byte.SIZE, GL11.GL_UNSIGNED_INT),
	INT_WRAPPED(Integer.class, Integer.SIZE / Byte.SIZE, GL11.GL_UNSIGNED_INT),
	DOUBLE(double.class, Double.SIZE / Byte.SIZE, GL11.GL_DOUBLE),
	DOUBLE_WRAPPED(Double.class, Double.SIZE / Byte.SIZE, GL11.GL_DOUBLE),
	FLOAT(float.class, Float.SIZE / Byte.SIZE, GL11.GL_FLOAT),
	FLOAT_WRAPPED(Float.class, Float.SIZE / Byte.SIZE, GL11.GL_FLOAT),
	SHORT(short.class, Short.SIZE / Byte.SIZE, GL11.GL_SHORT),
	SHORT_WRAPPED(Short.class, Short.SIZE / Byte.SIZE, GL11.GL_SHORT);
	
	private final int datatypeSize;
	private final int inferGLType;
	private final Class<?> dataTypeClass;
	public static final Map<Class<?>, EnumDataType> mapper = new HashMap<Class<?>, EnumDataType>();
	
	private EnumDataType(Class<?> clazz, int componentSize, int inferGLType)
	{
		this.datatypeSize = componentSize;
		this.inferGLType = inferGLType;
		this.dataTypeClass = clazz;
	}
	
	public int getDataTypeSize()
	{
		return this.datatypeSize;
	}
	
	public int inferGLType()
	{
		return this.inferGLType;
	}
	
	static
	{
		EnumDataType[] datatypes = EnumDataType.values();
		for(EnumDataType datatype : datatypes) mapper.put(datatype.dataTypeClass, datatype);
	}
	
	public static EnumDataType getDataType(Class<?> clz)
	{
		return mapper.get(clz);
	}
}
