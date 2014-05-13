package net.aegistudio.lwjgl.demo.tetris;

public abstract class Block
{
	protected final Tetris tetris;
	protected int masscenterx, masscentery;
	
	public Block(Tetris tetris, int masscenterx, int masscentery)
	{
		this.tetris = tetris;
		this.masscenterx = masscenterx;
		this.masscentery = masscentery;
	}
	
	//Don't include block replacing algorithms in these methods.
	public abstract boolean caninitialize();
	public abstract boolean movedownward();
	public abstract void moveleft();
	public abstract void moveright();
	public abstract void rotate();
	
	//Instead include them here!
	public abstract void settetrisblock(boolean tetrisblock);
	
}