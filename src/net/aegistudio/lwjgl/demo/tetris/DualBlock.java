package net.aegistudio.lwjgl.demo.tetris;

public class DualBlock extends Block
{
	
	private boolean vertical;
	
	public DualBlock(Tetris tetris, int masscenterx, int masscentery, boolean vertical)
	{
		super(tetris,masscenterx,masscentery);
		this.vertical = vertical;
	}
	
	public boolean caninitialize()
	{
		if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery)) return false;
		if(!vertical) if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)) return false;
		return true;
	}
	
	public synchronized void settetrisblock(boolean tetrisblock)
	{
		super.tetris.setTetrisBlock(super.masscenterx, super.masscentery, tetrisblock);
		if(vertical) super.tetris.setTetrisBlock(super.masscenterx, super.masscentery + 1, tetrisblock);
		else super.tetris.setTetrisBlock(super.masscenterx - 1, super.masscentery, tetrisblock);
	}
	
	public boolean movedownward()
	{
		if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery - 1)) return false;
		if(!vertical) if(super.tetris.isTetrisBlock(super.masscenterx -1, super.masscentery -1)) return false;
		super.masscentery --;
		return true;
	}
	
	public void moveleft()
	{
		if(vertical)
		{
			if(!(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery) | super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery + 1)))
			{
				super.masscenterx --;
			}
		}
		else
		{
			if(!super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery)) super.masscenterx --;
		}
	}
	
	public void moveright()
	{
		if(vertical)
		{
			if(!(super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery) | super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery + 1)))
			{
				super.masscenterx ++;
			}
		}
		else 
		{
			if(!super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery)) super.masscenterx ++;
		}
	}
	
	public void rotate()
	{
		if(vertical)
		{
			if(!super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)) this.vertical = false;
		}
		else this.vertical = true;
	}
	
}