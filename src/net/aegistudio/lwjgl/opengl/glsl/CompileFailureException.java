package net.aegistudio.lwjgl.opengl.glsl;

@SuppressWarnings("serial")
public class CompileFailureException extends RuntimeException
{
	public CompileFailureException(String info)
	{
		super(info);
	}
}
