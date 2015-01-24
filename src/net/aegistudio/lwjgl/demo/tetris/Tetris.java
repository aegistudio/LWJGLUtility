package net.aegistudio.lwjgl.demo.tetris;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import net.aegistudio.lwjgl.graphic.*;
import net.aegistudio.lwjgl.input.*;
import net.aegistudio.lwjgl.input.keyboard.KeyboardStatusEventMonitor;
import net.aegistudio.lwjgl.util.image.ImageRGBA;
import net.aegistudio.lwjgl.util.texture.ImageTexture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Tetris implements InputEventListener
{
	private final TetrisSheet tetris;
	private final int tetris_height, tetris_width, tetris_length;
	private boolean isgameover;
	private final Canvas tetris_canvas;
	
	private final KeyboardStatusEventMonitor keyboard_w, keyboard_s, keyboard_a, keyboard_d;
	private final KeyboardStatusEventMonitor keyboard_up, keyboard_down, keyboard_left, keyboard_right;
	
	private Block currentgameblock_left, currentgameblock_right;
	private Random random;
	
	public int score = 0;
	
	public Tetris(int width, int height, int length) throws Exception
	{
		this.tetris_width = width;
		this.tetris_height = height;
		this.tetris_length = length;
		this.isgameover = false;
		
		this.tetris = new TetrisSheet(this.tetris_height, this.tetris_width);
		
		this.tetris_canvas = new GraphicPlainCanvas(this.getWindowWidth(), this.getWindowHeight())
		{
			public void onInit(Canvas canvas)
			{
				super.onInit(canvas);
				
				ArrayList<File> blocks = new ArrayList<File>();
				ArrayList<File> backgrounds = new ArrayList<File>();
				File dir = new File("res");
				File[] files = dir.listFiles();
				for(File file : files) if(file.isFile()) if(file.getName().endsWith(".png")) blocks.add(file);
				files = new File(dir, "back").listFiles();
				for(File file : files) if(file.isFile()) if(file.getName().endsWith(".png")) backgrounds.add(file);
				
				try
				{
					int candidated = random.nextInt(blocks.size());
					wallTexture = new ImageTexture(new ImageRGBA(ImageIO.read(blocks.get(candidated))));
					wallTexture.create();
					blocks.remove(candidated);
					
					blockTexture = new ImageTexture(new ImageRGBA(ImageIO.read(blocks.get(random.nextInt(blocks.size())))));
					blockTexture.create();
					backgroundTexture = new ImageTexture(new ImageRGBA(ImageIO.read(backgrounds.get(random.nextInt(backgrounds.size())))));
					backgroundTexture.create();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
			}
		};
		
		this.keyboard_w = new KeyboardStatusEventMonitor(this, Keyboard.getKeyIndex("W"));
		this.keyboard_s = new KeyboardStatusEventMonitor(this, Keyboard.getKeyIndex("S"));
		this.keyboard_a = new KeyboardStatusEventMonitor(this, Keyboard.getKeyIndex("A"));
		this.keyboard_d = new KeyboardStatusEventMonitor(this, Keyboard.getKeyIndex("D"));
		
		this.keyboard_up = new KeyboardStatusEventMonitor(this, Keyboard.getKeyIndex("UP"));
		this.keyboard_down = new KeyboardStatusEventMonitor(this, Keyboard.getKeyIndex("DOWN"));
		this.keyboard_left = new KeyboardStatusEventMonitor(this, Keyboard.getKeyIndex("LEFT"));
		this.keyboard_right = new KeyboardStatusEventMonitor(this, Keyboard.getKeyIndex("RIGHT"));
		
		this.random = new Random(System.currentTimeMillis());
	}
	
	@Override
	public void onInputEventException(InputEventException inputeventexception)
	{
		inputeventexception.printStackTrace();
	}
	
	@Override
	public void onInputEventResponse(InputEventMonitor inputeventmonitor)
	{
		synchronized(this.tetris)
		{
			this.currentgameblock_left.settetrisblock(false);
			
			if(this.keyboard_a == inputeventmonitor) this.currentgameblock_left.moveleft();
			if(this.keyboard_d == inputeventmonitor) this.currentgameblock_left.moveright();
			if(this.keyboard_s == inputeventmonitor) while(this.currentgameblock_left.movedownward());
			if(this.keyboard_w == inputeventmonitor) this.currentgameblock_left.rotate();
			
			this.currentgameblock_left.settetrisblock(true);
			
			this.currentgameblock_right.settetrisblock(false);
			
			if(this.keyboard_left == inputeventmonitor) this.currentgameblock_right.moveleft();
			if(this.keyboard_right == inputeventmonitor) this.currentgameblock_right.moveright();
			if(this.keyboard_down == inputeventmonitor) while(this.currentgameblock_right.movedownward());
			if(this.keyboard_up == inputeventmonitor) this.currentgameblock_right.rotate();
			
			this.currentgameblock_right.settetrisblock(true);
		}
	}
	
	@Override
	public void onInputEventResume(InputEventMonitor inputeventmonitor)
	{
		
	}
	
	public int getWindowWidth()
	{
		return (this.tetris_width + 2) * this.tetris_length;
	}
	
	public int getWindowHeight()
	{
		return (this.tetris_height + 1) * this.tetris_length;
	}
	
	public Canvas getTetrisCanvas()
	{
		return this.tetris_canvas;
	}
	
	public void setTetrisBlock(int x, int y, boolean tetrisblock)
	{
		this.tetris.setTetrisBlock(y, x, tetrisblock);
	}
	
	public void onInitialize() throws Exception
	{
		this.tetris_canvas.onInit(null);
		this.tetris_canvas.registerDrawable(new TetrisColor(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		
		for(int i = -1; i < this.tetris_width + 1; i++) this.tetris_canvas.registerDrawable(new TetrisBlock(this, i, -1, Tetris.wallTexture));
		for(int i = 0; i < this.tetris_height; i++)
		{
			this.tetris_canvas.registerDrawable(new TetrisBlock(this, -1, i, Tetris.wallTexture));
			this.tetris_canvas.registerDrawable(new TetrisBlock(this, this.tetris_width, i, Tetris.wallTexture));
		}
		
		this.tetris_canvas.registerDrawable(new TetrisColor(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		for(int i = 0; i < this.tetris_width; i++) for(int j = 0; j < this.tetris_height; j++) this.tetris_canvas.registerDrawable(new TetrisGameBlock(this, i, j, Tetris.blockTexture));
		
		this.keyboard_w.startInputEventMonitor();
		this.keyboard_s.startInputEventMonitor();
		this.keyboard_a.startInputEventMonitor();
		this.keyboard_d.startInputEventMonitor();
		
		this.keyboard_up.startInputEventMonitor();
		this.keyboard_down.startInputEventMonitor();
		this.keyboard_left.startInputEventMonitor();
		this.keyboard_right.startInputEventMonitor();
		
		this.tetris_canvas.registerDrawable(drawBackground);
	}
	
	public boolean isTetrisBlock(int x, int y)
	{
		if(x < 0 | x >= this.tetris_width) return true;
		if(y < 0) return true;
		if(y >= this.tetris_height) return false;
		return this.tetris.getTetrisBlock(y, x);
	}
	
	public int getTetrisBlockLength()
	{
		return this.tetris_length;
	}
	
	public void onRefresh() throws Exception
	{
		this.tetris_canvas.onDraw(null);
		Display.setTitle(title + " - score: " + score);
		Display.update();
		Display.sync(60);
	}
	
	public Drawable drawBackground = new Drawable()
	{

		@Override
		public void onInit(Canvas canvas)
		{
		}

		@Override
		public void onDraw(Canvas canvas)
		{
			if(backgroundTexture != null)
			{
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				backgroundTexture.bind();
				GL11.glBegin(GL11.GL_QUADS);
					backgroundTexture.addVertexWithST(0, 0, -0.1, 0, 0);
					backgroundTexture.addVertexWithST(0, getWindowHeight(), -0.1, 0, tetris_height);
					backgroundTexture.addVertexWithST(getWindowWidth(), getWindowHeight(), -0.1, tetris_width, tetris_height);
					backgroundTexture.addVertexWithST(getWindowWidth(), 0, -0.1, tetris_width, 0);
				GL11.glEnd();
				backgroundTexture.unbind();
			}
		}

		@Override
		public void onDestroy(Canvas canvas)
		{
		}
		
	};
	
	public void onTerminate() throws Exception
	{
		this.tetris_canvas.onDestroy(null);
		this.keyboard_w.stopInputEventMonitor();
		this.keyboard_s.stopInputEventMonitor();
		this.keyboard_a.stopInputEventMonitor();
		this.keyboard_d.stopInputEventMonitor();
		this.keyboard_up.stopInputEventMonitor();
		this.keyboard_down.stopInputEventMonitor();
		this.keyboard_left.stopInputEventMonitor();
		this.keyboard_right.stopInputEventMonitor();
	}
	
	public boolean isGameOver()
	{
		if(this.isgameover) try
		{
			throw new StackOverflowError("It's not the program stack over flow, it's your brain!");
		}
		catch(Error e)
		{
			e.printStackTrace();
		}
		return this.isgameover;
	}
	
	public void generateblock_left()
	{
		this.currentgameblock_left = this.pickupBlockRandomly(this.tetris_width/2 - 3, this.tetris_height - 1);
		if(this.currentgameblock_left.caninitialize()) this.currentgameblock_left.settetrisblock(true);
		else this.isgameover = true;
	}
	
	public void generateblock_right()
	{
		this.currentgameblock_right = this.pickupBlockRandomly(this.tetris_width/2 + 3, this.tetris_height - 1);
		if(this.currentgameblock_right.caninitialize()) this.currentgameblock_right.settetrisblock(true);
		else this.isgameover = true;
	}
	
	
	public Block pickupBlockRandomly(int positionx, int positiony)
	{
		int randomizedindex = random.nextInt(7);
		int randomizedorientation = random.nextInt(4);
		boolean randomboolean = random.nextBoolean();
		switch(randomizedindex)
		{
			case 0: return new SShapedBlock(this, positionx, positiony, randomboolean);
			case 1: return new IShapedBlock(this, positionx, positiony, randomboolean);
			case 2: return new OShapedBlock(this, positionx, positiony);
			case 3: return new ZShapedBlock(this, positionx, positiony, randomboolean);
			case 4: return new LShapedBlock(this, positionx, positiony, randomizedorientation);
			case 5: return new JShapedBlock(this, positionx, positiony, randomizedorientation);
			default: return new TShapedBlock(this, positionx, positiony, randomizedorientation);
		}
	}
	
	public void onGame()
	{
		synchronized(this.tetris)
		{
			if(this.currentgameblock_left == null) this.generateblock_left();
			if(this.currentgameblock_right == null) this.generateblock_right();
			else
			{
				this.currentgameblock_left.settetrisblock(false);
				this.currentgameblock_right.settetrisblock(false);
				boolean movedownward_left = this.currentgameblock_left.movedownward();
				boolean movedownward_right = this.currentgameblock_right.movedownward();
				
				int erased = 0;
				for(int i = 0; i < this.tetris_height; i++) while(this.tetris.refreshTetrisRow(i))
				{
						this.currentgameblock_left.movedownward();
						this.currentgameblock_right.movedownward();	
						System.out.println("Wow, you've erased a row!");
						erased ++;
				}
				if(erased == 1) score += 1;
				else if(erased == 2) score += 3;
				else if(erased == 3) score += 6;
				else score += erased << 2;
				
				this.currentgameblock_left.settetrisblock(true);
				this.currentgameblock_right.settetrisblock(true);
				
				if(!movedownward_left) this.generateblock_left();
				if(!movedownward_right) this.generateblock_right();
			}
		}
	}
	
	public static ImageTexture blockTexture = null;
	public static ImageTexture wallTexture = null;
	public static ImageTexture backgroundTexture = null;
	public static final String title = "Tetricraft";
	
	@SuppressWarnings("deprecation")
	public static void main(String[] arguments) throws Exception
	{
		int rowcount = (arguments.length >= 1)? Integer.parseInt(arguments[0]):20;
		int columncount = (arguments.length >= 2)? Integer.parseInt(arguments[1]):30;
		int blockwidth = (arguments.length >= 3)? Integer.parseInt(arguments[2]):20;
		int refreshrate = (arguments.length >= 4)? Integer.parseInt(arguments[3]):100;
		
		Tetris tetris = new Tetris(rowcount, columncount, blockwidth);
		Display.setDisplayMode(new DisplayMode(tetris.getWindowWidth(), tetris.getWindowHeight()));
		Display.setTitle(title);
		
		Display.create();
		
		TetrisInvoker tetrisinvoker = new TetrisInvoker(tetris, refreshrate);
		tetrisinvoker.start();
		
		tetris.onInitialize();
		
		while(!Display.isCloseRequested() && !tetris.isGameOver()) tetris.onRefresh();
		
		tetrisinvoker.stop();
		
		tetris.onTerminate();
		Display.destroy();
	}
	
}

class TetrisInvoker extends Thread implements Runnable
{
	
	private final Tetris tetris;
	private final int gametick;
	
	public TetrisInvoker(Tetris tetris, int gametick)
	{
		this.tetris = tetris;
		this.gametick = gametick;
	}
	
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				this.tetris.onGame();
				super.sleep(this.gametick);
			}
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
}

class TetrisColor implements Drawable
{
	
	private final double red, green, blue;
	
	public TetrisColor(double red, double green, double blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	@Override
	public void onInit(Canvas canvas)
	{
		
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		GL11.glColor3d(this.red, this.green, this.blue);
	}
	
	@Override
	public void onDestroy(Canvas canvas)
	{
		
	}
	
}

class TetrisGameBlock extends TetrisBlock
{
	
	public TetrisGameBlock(Tetris tetris, int x, int y, ImageTexture texture)
	{
		super(tetris, x, y, texture);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		if(super.tetris.isTetrisBlock(super.x, super.y)) super.onDraw(canvas);
	}
	
}

class TetrisBlock implements Drawable
{
	
	protected final Tetris tetris;
	protected final int x, y;
	protected final ImageTexture texture;
	
	public TetrisBlock(Tetris tetris, int x, int y, ImageTexture texture)
	{
		this.tetris = tetris;
		this.x = x;
		this.y = y;
		this.texture = texture;
	}
	
	@Override
	public void onInit(Canvas canvas)
	{
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		int length = tetris.getTetrisBlockLength();
		int beginx = (this.x + 1) * length;
		int beginy = (this.y + 1) * length;
		
		if(texture != null) texture.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
		if(texture == null)
		{
			GL11.glVertex2i(beginx, beginy);
			GL11.glVertex2i(beginx, beginy+length);
			GL11.glVertex2i(beginx + length, beginy + length);
			GL11.glVertex2i(beginx + length, beginy);
		}
		else
		{
			texture.addVertexWithST(beginx, beginy, 0, 0);
			texture.addVertexWithST(beginx, beginy + length, 0, 1);
			texture.addVertexWithST(beginx + length, beginy + length, 1, 1);
			texture.addVertexWithST(beginx + length, beginy, 1, 0);
		}
		GL11.glEnd();
		
		if(texture != null) texture.bind();
	}
	
	@Override
	public void onDestroy(Canvas canvas)
	{
		
	}

}

class TetrisSheet
{
	private final ArrayList<boolean[]> tetrissheet;
	private final int column;
	
	public TetrisSheet(int row, int column)
	{
		this.column = column;
		this.tetrissheet = new ArrayList<boolean[]>();
		for(int i = 0; i < row; i++) this.tetrissheet.add(new boolean[this.column]);
	}
	
	public boolean getTetrisBlock(int row, int column)
	{
		return this.tetrissheet.get(row)[column];
	}
	
	public void setTetrisBlock(int row, int column, boolean tetrisblock)
	{
		if(0 <= column && column < this.column) if(0 <= row && row < this.tetrissheet.size()) this.tetrissheet.get(row)[column] = tetrisblock;
	}
	
	public boolean refreshTetrisRow(int row)
	{
		boolean[] tetrisrow = this.tetrissheet.get(row);
		for(int i = 0; i < this.column; i++) if(!tetrisrow[i]) return false;
		int tetrissheetsize = this.tetrissheet.size();
		for(int i = row; i < tetrissheetsize - 1; i++) this.tetrissheet.set(i, this.tetrissheet.get(i + 1));
		this.tetrissheet.set(tetrissheetsize - 1, new boolean[this.column]);
		return true;
	}
	
}