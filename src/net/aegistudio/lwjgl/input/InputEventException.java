package net.aegistudio.lwjgl.input;

@SuppressWarnings("serial")
public class InputEventException extends Exception
{
	
	public InputEventException(String exceptiondescription)
	{
		super(exceptiondescription);
	}
	
	public InputEventException(String exceptiondescription, Exception exceptioncause)
	{
		super(exceptiondescription, exceptioncause);
	}
	
}