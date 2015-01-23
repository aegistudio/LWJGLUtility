package net.aegistudio.lwjgl.util;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;

/**
 * This class encapsulates the OpenGL vertex buffer object (VBO), which is used to store
 * rendering data in VRAM for higher efficiency.
 * @author aegistudio
 */

public class VertexBufferObject
{
	private int bufferId;
	
	private final int bufferTarget;
	private final int bufferUsage;
	
	private EnumDataType bufferType;
	private ByteBuffer buffer;
	
	/**
	 * Creates a VBO java object and pre-install some data, not buffer the data in VRAM.
	 * @param bufferTarget The target buffer of this VBO, like GL_VERTEX_ARRAY, GL_PIXEL_UNPACK_BUFFER, etc.
	 * @param bufferUsage The usage of this VBO, like GL_STATIC_DRAW, GL_DYNAMIC_COPY, etc.
	 * @param buffer The target array to buffer, the buffer array type could only be byte, int, float, double, short and their wrapper class.
	 */
	public VertexBufferObject(int bufferTarget, int bufferUsage, Object buffer)
	{
		if(buffer == null) throw new IllegalArgumentException("Buffer should not be null!");
		this.bufferTarget = bufferTarget;
		this.bufferUsage = bufferUsage;
		
		Class<?> clz = buffer.getClass().getComponentType();
		if(clz == null) throw new IllegalArgumentException("Unable to create buffer for not an array!");
		this.bufferType = EnumDataType.getDataType(clz);
		if(this.bufferType == null) throw new IllegalArgumentException("Unable to create buffer for given type!");
		
		int arrayLength = Array.getLength(buffer);
		this.buffer = BufferUtils.createByteBuffer(arrayLength * this.bufferType.getDataTypeSize());
		
		BufferProcessor processor = bufferTypeMap.get(this.bufferType);
		for(int i = 0; i < arrayLength; i ++)
			processor.putBuffer(this.buffer, buffer, i);
		this.buffer.flip();
	}
	
	
	private static final Map<EnumDataType, BufferProcessor> bufferTypeMap = new HashMap<EnumDataType, BufferProcessor>();
	static
	{
		bufferTypeMap.put(EnumDataType.BYTE, new BufferProcessor() {
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.put(Array.getByte(bufferArray, index));
			}

			@Override
			public void putSubBuffer(ByteBuffer subbuffer, Object obj)
			{
				subbuffer.put((Byte)obj);
			}
		});
		
		bufferTypeMap.put(EnumDataType.BYTE_WRAPPED, new BufferProcessor() {
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.put((Byte) Array.get(bufferArray, index));
			}
			
			@Override
			public void putSubBuffer(ByteBuffer subbuffer, Object obj)
			{
				subbuffer.put((Byte) obj);
			}
		});
		
		bufferTypeMap.put(EnumDataType.INT, new BufferProcessor() {
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putInt(Array.getInt(bufferArray, index));
			}
			
			@Override
			public void putSubBuffer(ByteBuffer subbuffer, Object obj)
			{
				subbuffer.putInt((Integer) obj);
			}
		});
		
		bufferTypeMap.put(EnumDataType.INT_WRAPPED, new BufferProcessor() {
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putInt((Integer) Array.get(bufferArray, index));
			}
			
			@Override
			public void putSubBuffer(ByteBuffer subbuffer, Object obj)
			{
				subbuffer.putInt((Integer) obj);
			}
		});
		
		bufferTypeMap.put(EnumDataType.DOUBLE, new BufferProcessor() {
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putDouble(Array.getDouble(bufferArray, index));
			}
			
			@Override
			public void putSubBuffer(ByteBuffer subbuffer, Object obj)
			{
				subbuffer.putDouble((Double) obj);
			}
		});
		
		bufferTypeMap.put(EnumDataType.DOUBLE_WRAPPED, new BufferProcessor() {
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putDouble((Double) Array.get(bufferArray, index));
			}
			
			@Override
			public void putSubBuffer(ByteBuffer subbuffer, Object obj)
			{
				subbuffer.putDouble((Double) obj);
			}
		});
		
		bufferTypeMap.put(EnumDataType.FLOAT, new BufferProcessor() {
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putFloat(Array.getFloat(bufferArray, index));
			}
			
			@Override
			public void putSubBuffer(ByteBuffer subbuffer, Object obj)
			{
				subbuffer.putFloat((Float) obj);
			}
		});
		
		bufferTypeMap.put(EnumDataType.FLOAT_WRAPPED, new BufferProcessor() {
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putFloat((Float) Array.get(bufferArray, index));
			}
			
			@Override
			public void putSubBuffer(ByteBuffer subbuffer, Object obj)
			{
				subbuffer.putFloat((Float) obj);
			}
		});
		
		bufferTypeMap.put(EnumDataType.SHORT, new BufferProcessor() {
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putShort(Array.getShort(bufferArray, index));
			}
			
			@Override
			public void putSubBuffer(ByteBuffer subbuffer, Object obj)
			{
				subbuffer.putShort((Short) obj);
			}
		});
		
		bufferTypeMap.put(EnumDataType.SHORT_WRAPPED, new BufferProcessor() {
			@Override
			public void putBuffer(ByteBuffer buffer, Object bufferArray, int index)
			{
				buffer.putShort((Short) Array.get(bufferArray, index));
			}
			
			@Override
			public void putSubBuffer(ByteBuffer subbuffer, Object obj)
			{
				subbuffer.putShort((Short) obj);
			}
		});
		
	}
	
	private interface BufferProcessor
	{
		public void putBuffer(ByteBuffer buffer, Object bufferArray, int index);
		public void putSubBuffer(ByteBuffer subbuffer, Object obj);
	}
	
	/**
	 * Creates a vertex buffer object in the VRAM and returns the created id.
	 * @return the created VBO indentity.
	 */
	public int create()
	{
		if(this.bufferId == 0)
		{
			this.bufferId = ARBVertexBufferObject.glGenBuffersARB();
			if(this.bufferId == 0) throw new RuntimeException("Fail to create space for the vertex buffer object!");
			
			ARBVertexBufferObject.glBindBufferARB(bufferTarget, bufferId);
			ARBVertexBufferObject.glBufferDataARB(bufferTarget, buffer, bufferUsage);
			ARBVertexBufferObject.glBindBufferARB(bufferTarget, 0);
		}
		return this.bufferId;
	}
	
	/**
	 * Set the current vertex buffer object in use.
	 */
	public void bind()
	{
		if(bufferId == 0) throw new RuntimeException("You must create the buffer before binding it!");
		ARBVertexBufferObject.glBindBufferARB(bufferTarget, bufferId);
	}
	
	/**
	 * Set the current vertex buffer object not in use.
	 */
	public void unbind()
	{
		if(bufferId == 0) throw new RuntimeException("You must create the buffer before unbinding it!");
		ARBVertexBufferObject.glBindBufferARB(bufferTarget, 0);
	}
	
	/**
	 * Substitute certain data in the vertex buffer object that's in the VRAM.
	 */
	public void subst(int position, Object obj)
	{
		if(position < 0 || position > (this.buffer.capacity() / this.bufferType.getDataTypeSize()))
				throw new IllegalArgumentException("The buffer space to change is out of the permitted position!");
		
		if(this.bufferId != 0)
		{
			ByteBuffer subbuffer = BufferUtils.createByteBuffer(this.bufferType.getDataTypeSize());
			
			if((EnumDataType.getDataType(obj.getClass()) == null) || (EnumDataType.getDataType(obj.getClass()).inferGLType() != this.bufferType.inferGLType()))
				throw new IllegalArgumentException("Mismatch between buffer component type and the type of element to change!");
			
			bufferTypeMap.get(this.bufferType).putSubBuffer(subbuffer, obj);
			subbuffer.flip();
			ARBVertexBufferObject.glBindBufferARB(bufferTarget, bufferId);
			ARBVertexBufferObject.glBufferSubDataARB(bufferTarget, position * this.bufferType.getDataTypeSize(), subbuffer);
			ARBVertexBufferObject.glBindBufferARB(bufferTarget, 0);
		}
	}
	
	/**
	 * Get the array component type of this buffer.
	 * @return the array component type.
	 */
	public EnumDataType getBufferType()
	{
		return this.bufferType;
	}
	
	/**
	 * Remove / release the vertex buffer object on the VRAM.
	 */
	public void delete()
	{
		ARBVertexBufferObject.glDeleteBuffersARB(bufferId);
		this.bufferId = 0;
	}
}
