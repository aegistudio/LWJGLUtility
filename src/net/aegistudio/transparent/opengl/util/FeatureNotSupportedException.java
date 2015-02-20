package net.aegistudio.transparent.opengl.util;

@SuppressWarnings("serial")
public class FeatureNotSupportedException extends RuntimeException
{
	public FeatureNotSupportedException(String featureName)
	{
		super("Your video card does not support feature $featureName!".replace("$featureName", featureName));
	}
}
