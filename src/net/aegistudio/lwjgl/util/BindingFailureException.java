package net.aegistudio.lwjgl.util;

@SuppressWarnings("serial")
public class BindingFailureException extends RuntimeException
{
	public BindingFailureException(String info)
	{
		super(info);
	}
}
