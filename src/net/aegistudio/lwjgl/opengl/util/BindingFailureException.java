package net.aegistudio.lwjgl.opengl.util;

@SuppressWarnings("serial")
public class BindingFailureException extends RuntimeException
{
	public BindingFailureException(String info)
	{
		super(info);
	}
}
