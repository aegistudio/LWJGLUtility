package net.aegistudio.transparent.opengl.util;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;

public final class BufferHelper
{
	private BufferHelper(){}
	
	private static final Map<EnumDataType, BufferProcessor> bufferTypeMap = new HashMap<EnumDataType, BufferProcessor>();
	static
	{
		bufferTypeMap.put(EnumDataType.BYTE, new ArrayBufferProcessor() {
			{
				super.bufferType = EnumDataType.BYTE;
			}
			
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.put(Array.getByte(bufferArray, index));
			}
		});
		
		bufferTypeMap.put(EnumDataType.BYTE_WRAPPED, new ArrayBufferProcessor() {
			{
				super.bufferType = EnumDataType.BYTE_WRAPPED;
			}
			
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.put((Byte) Array.get(bufferArray, index));
			}
		});
		
		bufferTypeMap.put(EnumDataType.INT, new ArrayBufferProcessor(){
			{
				super.bufferType = EnumDataType.INT;
			}
			
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putInt(Array.getInt(bufferArray, index));
			}
		});
		
		bufferTypeMap.put(EnumDataType.INT_WRAPPED, new ArrayBufferProcessor() {
			{
				super.bufferType = EnumDataType.INT_WRAPPED;
			}
			
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putInt((Integer) Array.get(bufferArray, index));
			}
		});
		
		bufferTypeMap.put(EnumDataType.DOUBLE, new ArrayBufferProcessor() {
			{
				super.bufferType = EnumDataType.DOUBLE;
			}
			
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putDouble(Array.getDouble(bufferArray, index));
			}
		});
		
		bufferTypeMap.put(EnumDataType.DOUBLE_WRAPPED, new ArrayBufferProcessor() {
			{
				super.bufferType = EnumDataType.DOUBLE_WRAPPED;
			}
			
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putDouble((Double) Array.get(bufferArray, index));
			}
		});
		
		bufferTypeMap.put(EnumDataType.FLOAT, new ArrayBufferProcessor() {
			{
				super.bufferType = EnumDataType.FLOAT;
			}
			
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putFloat(Array.getFloat(bufferArray, index));
			}
		});
		
		bufferTypeMap.put(EnumDataType.FLOAT_WRAPPED, new ArrayBufferProcessor() {
			{
				super.bufferType = EnumDataType.FLOAT_WRAPPED;
			}
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putFloat((Float) Array.get(bufferArray, index));
			}
		});
		
		bufferTypeMap.put(EnumDataType.SHORT, new ArrayBufferProcessor() {
			{
				super.bufferType = EnumDataType.SHORT;
			}
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putShort(Array.getShort(bufferArray, index));
			}
		});
		
		bufferTypeMap.put(EnumDataType.SHORT_WRAPPED, new ArrayBufferProcessor() {
			{
				super.bufferType = EnumDataType.SHORT_WRAPPED;
			}
			
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putShort((Short) Array.get(bufferArray, index));
			}
		});
		
	}
	
	public static Object convertToArrayIfNecessary(Object obj)
	{
		if(obj instanceof Buffer) return obj;
		if(obj.getClass().isArray()) return obj;
		Object objArray = Array.newInstance(obj.getClass(), 1);
		Array.set(objArray, 0, obj);
		return objArray;
	}
	
	public static Class<?> getCertainClass(Object obj)
	{
		if(obj instanceof Buffer) return obj.getClass();
		else if(obj.getClass().isArray()) return obj.getClass().getComponentType();
		else return null; //Make sure convert to Array if necessary is called.
	}
	
	public static int getLength(Object obj)
	{
		if(obj instanceof Buffer) return ((Buffer)obj).capacity();
		else if(obj.getClass().isArray()) return Array.getLength(obj);
		else return 1;
	}
	
	public static BufferProcessor getBufferProcessor(EnumDataType dataType)
	{
		return bufferTypeMap.get(dataType);
	}
	
	public interface BufferProcessor
	{
		public Buffer makeBuffer(Object bufferArray);
		
		public void bufferData(int bufferTarget, Buffer buffer, int bufferUsage);
		
		public void substData(int bufferTarget, Buffer buffer, int position);
		
		public void texImage2D(int texTarget, int level, int internalformat, int width, int height, int border, int format, Buffer pixels);
	}
}

/**
 * All primitive or wrapped type arrays will use this processor, which commonly use byte buffer for convenience.
 * @author aegistudio
 */
abstract class ArrayBufferProcessor implements BufferHelper.BufferProcessor
{
	protected EnumDataType bufferType;
	
	@Override
	public ByteBuffer makeBuffer(Object buffer)
	{
		int arrayLength = Array.getLength(buffer);
		ByteBuffer returnedBuffer = BufferUtils.createByteBuffer(arrayLength * this.bufferType.getDataTypeSize());
		
		for(int i = 0; i < arrayLength; i ++)
			this.putBuffer(returnedBuffer, buffer, i);
		returnedBuffer.flip();
		return returnedBuffer;
	}
	
	protected abstract void putBuffer(ByteBuffer buffer, Object bufferArray, int index);
	
	public void bufferData(int bufferTarget, Buffer buffer, int bufferUsage)
	{
		ARBVertexBufferObject.glBufferDataARB(bufferTarget, (ByteBuffer)buffer, bufferUsage);
	}
	
	public void substData(int bufferTarget, Buffer buffer, int position)
	{
		ARBVertexBufferObject.glBufferSubDataARB(bufferTarget, position * this.bufferType.getDataTypeSize(), (ByteBuffer)buffer);
	}
	
	public void texImage2D(int texTarget, int level, int internalformat, int width, int height, int border, int format, Buffer pixels)
	{
		GL11.glTexImage2D(texTarget, level, internalformat, width, height, border, format, bufferType.inferGLType(), (ByteBuffer)pixels);
	}
}