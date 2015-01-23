package net.aegistudio.lwjgl.util.glsl;

@SuppressWarnings("serial")
public class CompileFailureException extends RuntimeException
{
	public CompileFailureException(String info)
	{
		super(info);
	}
}
