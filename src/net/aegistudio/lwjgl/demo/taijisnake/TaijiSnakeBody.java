package net.aegistudio.lwjgl.demo.taijisnake;

import org.lwjgl.opengl.GL11;

import net.aegistudio.lwjgl.graphic.Drawable;
import net.aegistudio.lwjgl.graphic.Canvas;

public class TaijiSnakeBody implements Drawable
{
	private final TaijiSnakeGame taijisnake;
	private final int xaxis, yaxis;
	private TaijiSnakeBody nextcomponent;
	private TaijiSnakeBody previouscomponent;
	
	public TaijiSnakeBody(TaijiSnakeGame taijisnake, int xaxis, int yaxis, TaijiSnakeBody nextcomponent)
	{
		this.taijisnake = taijisnake;
		this.xaxis = xaxis;
		this.yaxis = yaxis;
		this.nextcomponent = nextcomponent;
		if(this.nextcomponent != null) this.nextcomponent.previouscomponent = this;
	}
	
	public TaijiSnakeBody(TaijiSnakeGame taijisnake, int xaxis, int yaxis)
	{
		this(taijisnake, xaxis, yaxis, null);
	}
	
	public int getSnakeBodyXAxis()
	{
		return this.xaxis;
	}
	
	public int getSnakeBodyYAxis()
	{
		return this.yaxis;
	}
	
	public void popoutSnakeBody()
	{
		this.previouscomponent.nextcomponent = null;
	}
	
	@Override
	public void onInitialize(Canvas canvas)
	{
		
	}
	
	@Override
	public void onRefresh(Canvas canvas)
	{
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(this.taijisnake.getPositionXByAxis(xaxis, yaxis), this.taijisnake.getPositionYByAxis(xaxis, yaxis));
			GL11.glVertex2d(this.taijisnake.getPositionXByAxis(xaxis + 1, yaxis), this.taijisnake.getPositionYByAxis(xaxis + 1, yaxis));
			GL11.glVertex2d(this.taijisnake.getPositionXByAxis(xaxis + 1, yaxis + 1), this.taijisnake.getPositionYByAxis(xaxis + 1, yaxis + 1));
			GL11.glVertex2d(this.taijisnake.getPositionXByAxis(xaxis, yaxis + 1), this.taijisnake.getPositionYByAxis(xaxis, yaxis + 1));
		GL11.glEnd();
	}
	
	@Override
	public void onTerminate(Canvas canvas)
	{
		
	}
	
	public boolean hasSnakeHeadCollided(TaijiSnakeBody snakehead)
	{
		if((this.xaxis == snakehead.xaxis) && (this.yaxis == snakehead.yaxis)) return true;
		return false;
	}
	
	public boolean hasSnakeRunoutOfMap()
	{
		if(this.xaxis < 0) return true;
		if(this.yaxis < 0) return true;
		int plainsize = this.taijisnake.getChessPlainSize();
		if(this.xaxis >= plainsize) return true;
		if(this.yaxis >= plainsize) return true;
		return false;
	}
	
	public TaijiSnakeBody getSnakeNextComponent()
	{
		return this.nextcomponent;
	}
}