package net.aegistudio.lwjgl.demo.tetris;

public class SShapedBlock extends Block
{
	
	private boolean vertical;
	
	public SShapedBlock(Tetris tetris, int masscenterx, int masscentery, boolean vertical)
	{
		super(tetris,masscenterx,masscentery);
		this.vertical = vertical;
	}
	
	public boolean caninitialize()
	{
		if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery)) return false;
		if(vertical) if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery + 1)) return false;
		else
		{
			if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)) return false;
			if(super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery +1)) return false;
		}
		return true;
	}
	
	public synchronized void settetrisblock(boolean tetrisblock)
	{
		super.tetris.setTetrisBlock(super.masscenterx, super.masscentery, tetrisblock);
		if(vertical)
		{
			super.tetris.setTetrisBlock(super.masscenterx, super.masscentery + 1, tetrisblock);
			super.tetris.setTetrisBlock(super.masscenterx - 1, super.masscentery + 1, tetrisblock);
			super.tetris.setTetrisBlock(super.masscenterx - 1, super.masscentery + 2, tetrisblock);
		}
		else
		{
			super.tetris.setTetrisBlock(super.masscenterx - 1, super.masscentery, tetrisblock);
			super.tetris.setTetrisBlock(super.masscenterx, super.masscentery + 1, tetrisblock);
			super.tetris.setTetrisBlock(super.masscenterx + 1, super.masscentery + 1, tetrisblock);
		}
	}
	
	public boolean movedownward()
	{
		if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery - 1)) return false;
		if(vertical)
		{
			if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)) return false;
		}
		else
		{
			if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery - 1)) return false;
			if(super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery)) return false;
		}
		super.masscentery --;
		return true;
	}
	
	public void moveleft()
	{
		if(vertical)
		{
			if(!(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)
				|super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery + 1)
				|super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery + 2)))
			{
				super.masscenterx --;
			}
		}
		else
		{
			if(!(super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery)
				|super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery + 1)))
			{
				super.masscenterx --;
			}
		}
	}
	
	public void moveright()
	{
		if(vertical)
		{
			if(!(super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery)
				|super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery + 1)
				|super.tetris.isTetrisBlock(super.masscenterx, super.masscentery + 2)))
			{
				super.masscenterx ++;
			}
		}
		else 
		{
			if(!(super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery)
				|super.tetris.isTetrisBlock(super.masscenterx + 2, super.masscentery + 1))) super.masscenterx ++;
		}
	}
	
	public void rotate()
	{
		if(vertical)
		{
			if(!(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)
				|super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery + 1))) this.vertical = false;
		}
		else
		{
			if(!(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery + 1)
				|super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery + 2))) this.vertical = true;
		}
	}
	
}