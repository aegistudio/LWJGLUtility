package net.aegistudio.transparent.opengl.util;

import org.lwjgl.opengl.GL11;

public class Scanvager
{
	protected int scanvage = EnumBuffer.COLOR.bufferBit | EnumBuffer.DEPTH.bufferBit;
	
	public void addScanvage(EnumBuffer buffer)
	{
		scanvage = scanvage | buffer.bufferBit;
	}
	
	public void removeScanvage(EnumBuffer buffer)
	{
		scanvage = scanvage & (0xFFFFFFFF ^ buffer.bufferBit);
	}
	
	public void removeAllScanvage()
	{
		scanvage = 0x00000000;
	}
	
	public void scanvage()
	{
		GL11.glClear(scanvage);
	}
}
