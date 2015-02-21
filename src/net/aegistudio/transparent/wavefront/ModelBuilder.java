package net.aegistudio.transparent.wavefront;

public interface ModelBuilder<ResultType>
{
	public void build(String[] splittedArguments);
	
	public ResultType getResult();
}