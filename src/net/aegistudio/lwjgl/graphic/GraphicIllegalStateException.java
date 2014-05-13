package net.aegistudio.lwjgl.graphic;

@SuppressWarnings("serial")
public class GraphicIllegalStateException extends Exception
{
	
	public GraphicIllegalStateException(String exceptiondescription)
	{
		super(exceptiondescription);
	}
	
	public GraphicIllegalStateException(String exceptiondescription, Exception exceptioncause)
	{
		super(exceptiondescription, exceptioncause);
	}
	
}