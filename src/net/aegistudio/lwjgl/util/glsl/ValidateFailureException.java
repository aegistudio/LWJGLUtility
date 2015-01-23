package net.aegistudio.lwjgl.util.glsl;

@SuppressWarnings("serial")
public class ValidateFailureException extends RuntimeException
{
	public ValidateFailureException(String info)
	{
		super(info);
	}
}
