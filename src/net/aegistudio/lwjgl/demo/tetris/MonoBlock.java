package net.aegistudio.lwjgl.demo.tetris;

public class MonoBlock extends Block
{
	
	public MonoBlock(Tetris tetris, int masscenterx, int masscentery)
	{
		super(tetris,masscenterx,masscentery);
	}
	
	@Override
	public synchronized void settetrisblock(boolean tetrisblock)
	{
		super.tetris.setTetrisBlock(super.masscenterx, super.masscentery, tetrisblock);
	}
	
	@Override
	public boolean caninitialize()
	{
		return !super.tetris.isTetrisBlock(masscenterx, masscentery);
	}
	
	@Override
	public boolean movedownward()
	{
		if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery - 1)) return false; 
		super.masscentery--;
		return true;
	}
	
	@Override
	public void moveleft()
	{
		if(!super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)) super.masscenterx --;
	}
	
	@Override
	public void moveright()
	{
		if(!super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery)) super.masscenterx ++;
	}
	
	@Override
	public void rotate()
	{
		
	}
	
}