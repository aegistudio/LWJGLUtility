package net.aegistudio.lwjgl.demo.taijisnake;

import java.util.Random;

import net.aegistudio.lwjgl.graphic.Canvas;
import net.aegistudio.lwjgl.graphic.Container;

public class TaijiSnake extends Canvas
{
	private TaijiSnake.TaijiSnakeOrientation orientation;
	private TaijiSnakeGame taijisnake;
	private TaijiSnakeBody head;
	private TaijiSnakeBody food;
	private boolean turningprotection;
	private Random random;
	
	public TaijiSnake(TaijiSnakeGame taijisnake, TaijiSnakeOrientation orientation) throws Exception
	{
		super();
		this.taijisnake = taijisnake;
		this.orientation = orientation;
		this.turningprotection = false;
		random = new Random(System.currentTimeMillis());
	}
	
	public void onInitialize(Container canvas, int xaxis, int yaxis, int length) throws Exception
	{
	//	super.onInit(canvas);
		
		int deltax = this.orientation.getSnakeDeltaX();
		int deltay = this.orientation.getSnakeDeltaY();
		
		for(int i = length - 1; i >= 0; i-- )
		{
			this.head = new TaijiSnakeBody(this.taijisnake, xaxis - i * deltax, yaxis - i * deltay, this.head);
			super.registerDrawable(this.head);
		}
		
		this.generateSnakeFood();
	}
	
	public void generateSnakeFood() throws Exception
	{
		int plainsize = this.taijisnake.getChessPlainSize();
		if(this.food!=null) super.unregisterDrawable(this.food);
		boolean shouldfindnextfood = true;
		while(shouldfindnextfood)
		{
			shouldfindnextfood = false;
			this.food = new TaijiSnakeBody(this.taijisnake, random.nextInt(plainsize), random.nextInt(plainsize));
			for(TaijiSnakeBody snakebody = this.head; snakebody !=null; snakebody = snakebody.getSnakeNextComponent()) if(snakebody.hasSnakeHeadCollided(this.food))
			{
				shouldfindnextfood = true;
				break;
			}
		}
		super.registerDrawable(this.food);
	}
	
	public void changeOrientation(TaijiSnakeOrientation orientation)
	{
		if(!this.turningprotection)
		{
			this.orientation = this.orientation.changeOrientation(orientation);
			this.turningprotection = true;
		}
	}
	
	public boolean runSnake() throws Exception
	{
		this.head = new TaijiSnakeBody(this.taijisnake, this.head.getSnakeBodyXAxis() + this.orientation.getSnakeDeltaX(), this.head.getSnakeBodyYAxis() + this.orientation.getSnakeDeltaY(), this.head);
		super.registerDrawable(this.head);
		
		this.turningprotection = false;
		
		boolean shouldremovetail = true;
		if(this.head.hasSnakeHeadCollided(this.food))
		{
			shouldremovetail = false;
			this.generateSnakeFood();
			System.out.println("Ohhhh such yummy food! I feel I've grown longer.");
		}
		
		if(this.head.hasSnakeRunoutOfMap())
		{
			System.out.println("My head hit the wall, augggggh so painful.");
			return false;
		}
		
		TaijiSnakeBody snaketail = this.head;
		for(TaijiSnakeBody snakebody = this.head.getSnakeNextComponent(); snakebody !=null; snakebody = snakebody.getSnakeNextComponent())
		{
			snaketail = snakebody;
			if(snakebody.hasSnakeHeadCollided(this.head))
			{
				System.out.println("Ouch acheeee! My body ain't yummy food!");
				return false;
			}
		}
		
		if(shouldremovetail)
		{
			snaketail.popoutSnakeBody();
			super.unregisterDrawable(snaketail);
		}
		
		return true;
	}
	
	public enum TaijiSnakeOrientation
	{
		UP(0, 1),
		DOWN(0, -1),
		LEFT(-1,0),
		RIGHT(1,0);
		
		private final int deltax;
		private final int deltay;
		
		private TaijiSnakeOrientation(int deltax, int deltay)
		{
			this.deltax = deltax;
			this.deltay = deltay;
		}
		
		public int getSnakeDeltaX()
		{
			return this.deltax;
		}
		
		public int getSnakeDeltaY()
		{
			return this.deltay;
		}
		
		public TaijiSnakeOrientation changeOrientation(TaijiSnakeOrientation previousorientation)
		{
			if(this == UP || this == DOWN) if(previousorientation == LEFT || previousorientation == RIGHT) return previousorientation;
			if(this == LEFT || this == RIGHT) if(previousorientation == UP || previousorientation == DOWN) return previousorientation;
			return this;
		}
	}

	@Override
	public void onInit(Container canvas)
	{
		
	}
	
}