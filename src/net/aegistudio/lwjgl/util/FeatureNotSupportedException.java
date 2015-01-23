package net.aegistudio.lwjgl.util;

@SuppressWarnings("serial")
public class FeatureNotSupportedException extends RuntimeException
{
	public FeatureNotSupportedException(String info)
	{
		super(info);
	}
}
