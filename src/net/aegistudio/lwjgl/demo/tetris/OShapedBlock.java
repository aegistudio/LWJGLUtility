package net.aegistudio.lwjgl.demo.tetris;

public class OShapedBlock extends Block
{
	
	public OShapedBlock(Tetris tetris, int masscenterx, int masscentery)
	{
		super(tetris,masscenterx,masscentery);
	}
	
	@Override
	public synchronized void settetrisblock(boolean tetrisblock)
	{
		super.tetris.setTetrisBlock(super.masscenterx, super.masscentery, tetrisblock);
		super.tetris.setTetrisBlock(super.masscenterx - 1, super.masscentery, tetrisblock);
		super.tetris.setTetrisBlock(super.masscenterx , super.masscentery + 1, tetrisblock);
		super.tetris.setTetrisBlock(super.masscenterx - 1, super.masscentery + 1, tetrisblock);
	}
	
	@Override
	public boolean caninitialize()
	{
		return !(super.tetris.isTetrisBlock(masscenterx, masscentery)||super.tetris.isTetrisBlock(masscenterx - 1, masscentery));
	}
	
	@Override
	public boolean movedownward()
	{
		if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery - 1)) return false; 
		if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery - 1)) return false; 
		super.masscentery--;
		return true;
	}
	
	@Override
	public void moveleft()
	{
		if(!(super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery)|super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery + 1))) super.masscenterx --;
	}
	
	@Override
	public void moveright()
	{
		if(!(super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery)|super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery + 1))) super.masscenterx ++;
	}
	
	@Override
	public void rotate()
	{
		
	}
	
}