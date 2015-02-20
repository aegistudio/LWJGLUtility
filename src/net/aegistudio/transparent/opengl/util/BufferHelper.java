package net.aegistudio.transparent.opengl.util;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public final class BufferHelper
{
	private BufferHelper(){}
	
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
	
	public static BufferProcessor getBufferProcessor(EnumDataType dataType)
	{
		return bufferTypeMap.get(dataType);
	}
	
	public interface BufferProcessor
	{
		public void putBuffer(ByteBuffer buffer, Object bufferArray, int index);
		public void putSubBuffer(ByteBuffer subbuffer, Object obj);
	}
}
