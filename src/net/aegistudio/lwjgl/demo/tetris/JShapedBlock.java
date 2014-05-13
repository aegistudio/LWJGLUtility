package net.aegistudio.lwjgl.demo.tetris;

public class JShapedBlock extends Block
{
	private int orientation;
	
	public JShapedBlock(Tetris tetris, int positionx, int positiony, int orientation)
	{
		super(tetris, positionx, positiony);
		this.orientation = orientation % 4;
	}
	
	public boolean caninitialize()
	{
		if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery)) return false;
		switch(this.orientation)
		{
			case 0:
				if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)) return false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery)) return false;
				break;
			case 3: if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)) return false; break;
			default:
				break;
		}
		return true;
	}
	
	public synchronized void settetrisblock(boolean tetrisblock)
	{
		super.tetris.setTetrisBlock(super.masscenterx, super.masscentery, tetrisblock);
		switch(this.orientation)
		{
			case 3:
				super.tetris.setTetrisBlock(super.masscenterx - 1, super.masscentery, tetrisblock);
				super.tetris.setTetrisBlock(super.masscenterx, super.masscentery + 1, tetrisblock);
				super.tetris.setTetrisBlock(super.masscenterx, super.masscentery + 2, tetrisblock);
				break;
			case 2:
				super.tetris.setTetrisBlock(super.masscenterx, super.masscentery + 1, tetrisblock);
				super.tetris.setTetrisBlock(super.masscenterx - 1, super.masscentery + 1, tetrisblock);
				super.tetris.setTetrisBlock(super.masscenterx - 2, super.masscentery + 1, tetrisblock);
				break;
			case 1:
				super.tetris.setTetrisBlock(super.masscenterx, super.masscentery + 1, tetrisblock);
				super.tetris.setTetrisBlock(super.masscenterx, super.masscentery + 2, tetrisblock);
				super.tetris.setTetrisBlock(super.masscenterx + 1, super.masscentery + 2, tetrisblock);
				break;
			default:
				super.tetris.setTetrisBlock(super.masscenterx - 1, super.masscentery, tetrisblock);
				super.tetris.setTetrisBlock(super.masscenterx - 2, super.masscentery, tetrisblock);
				super.tetris.setTetrisBlock(super.masscenterx - 2, super.masscentery + 1, tetrisblock);
				break;
		}
	}
	
	public boolean movedownward()
	{
		if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery - 1)) return false;
		switch(this.orientation)
		{
			case 3:
				if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery - 1)) return false;
				break;
			case 2:
				if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)) return false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery)) return false;
				break;
			case 1:
				if(super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery + 1)) return false;
				break;
			default:
				if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery - 1)) return false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery - 1)) return false;
				break;
		}
		super.masscentery --;
		return true;
	}
	
	public void moveleft()
	{
		boolean shouldmoveleft = true;
		switch(this.orientation)
		{
			case 3:
				if(super.tetris.isTetrisBlock(super.masscenterx - 2 , super.masscentery)) shouldmoveleft = false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 1 , super.masscentery + 1)) shouldmoveleft = false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 1 , super.masscentery + 2)) shouldmoveleft = false;
				break;
			case 2:
				if(super.tetris.isTetrisBlock(super.masscenterx - 1 , super.masscentery)) shouldmoveleft = false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 3 , super.masscentery + 1)) shouldmoveleft = false;
				break;
			case 1:
				if(super.tetris.isTetrisBlock(super.masscenterx - 1 , super.masscentery)) shouldmoveleft = false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 1 , super.masscentery + 1)) shouldmoveleft = false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 1 , super.masscentery + 2)) shouldmoveleft = false;
				break;
			default:
				if(super.tetris.isTetrisBlock(super.masscenterx - 3, super.masscentery)) shouldmoveleft = false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 3, super.masscentery + 1)) shouldmoveleft = false;
				break;
		}
		if(shouldmoveleft) super.masscenterx --;
	}
	
	public void moveright()
	{
		boolean shouldmoveright = true;
		switch(this.orientation)
		{
			case 3:
				if(super.tetris.isTetrisBlock(super.masscenterx + 1 , super.masscentery)) shouldmoveright = false;
				if(super.tetris.isTetrisBlock(super.masscenterx + 1 , super.masscentery + 1)) shouldmoveright = false;
				if(super.tetris.isTetrisBlock(super.masscenterx + 1 , super.masscentery + 2)) shouldmoveright = false;
				break;
			case 2:
				if(super.tetris.isTetrisBlock(super.masscenterx + 1 , super.masscentery)) shouldmoveright = false;
				if(super.tetris.isTetrisBlock(super.masscenterx + 1 , super.masscentery + 1)) shouldmoveright = false;
				break;
			case 1:
				if(super.tetris.isTetrisBlock(super.masscenterx + 1 , super.masscentery)) shouldmoveright = false;
				if(super.tetris.isTetrisBlock(super.masscenterx + 1 , super.masscentery + 1)) shouldmoveright = false;
				if(super.tetris.isTetrisBlock(super.masscenterx + 2 , super.masscentery + 2)) shouldmoveright = false;
				break;
			default:
				if(super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery)) shouldmoveright = false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery + 1)) shouldmoveright = false;
				break;
		}
		if(shouldmoveright) super.masscenterx ++;
	}
	
	public void rotate()
	{
		boolean shouldrotate = true;
		switch(this.orientation)
		{
			case 1:
				if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery + 1)) shouldrotate = false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery + 1)) shouldrotate = false;
				break;
			case 2:
				if(super.tetris.isTetrisBlock(super.masscenterx - 1, super.masscentery)) shouldrotate = false;
				if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery + 2)) shouldrotate = false;
				break;
			case 3:
				if(super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery)) shouldrotate = false;
				if(super.tetris.isTetrisBlock(super.masscenterx - 2, super.masscentery + 1)) shouldrotate = false;
				break;
			default:
				if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery + 1)) shouldrotate = false;
				if(super.tetris.isTetrisBlock(super.masscenterx, super.masscentery + 2)) shouldrotate = false;
				if(super.tetris.isTetrisBlock(super.masscenterx + 1, super.masscentery + 2)) shouldrotate = false;
				break;
		}
		if(shouldrotate) this.orientation = (this.orientation + 1) % 4;
	}
	
}