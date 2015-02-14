package net.aegistudio.lwjgl.demo.taijisnake;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import net.aegistudio.lwjgl.input.InputEventException;
import net.aegistudio.lwjgl.input.InputEventListener;
import net.aegistudio.lwjgl.input.InputEventMonitor;
import net.aegistudio.lwjgl.input.keyboard.KeyboardStatusEventMonitor;
import net.aegistudio.lwjgl.opengl.Container;
import net.aegistudio.lwjgl.opengl.Drawable;
import net.aegistudio.lwjgl.opengl.camera.Camera;
import net.aegistudio.lwjgl.opengl.camera.Ortho;
import net.aegistudio.lwjgl.demo.taijisnake.TaijiSnake.TaijiSnakeOrientation;

@SuppressWarnings("deprecation")
public class TaijiSnakeGame implements InputEventListener
{
	private final int chessplainsize, blocklength;
	private final int rotatequantization;
	private int rotationposition;
	
	private final double[] cosinetheta, sinetheta;
	private final double windowlength;
	
	private final Camera taijisnakecanvas;
	private final TaijiSnake taijisnake;
	private boolean hasgameover = false;
	
	private final TaijiSnakeInvoker taijisnakeinvoker;
	
	private final KeyboardStatusEventMonitor UP_ARROW,DOWN_ARROW,LEFT_ARROW,RIGHT_ARROW;
	
	public TaijiSnakeGame(int chessplainsize, int blocklength, int rotatecycle, int rotatequantization) throws Exception
	{
		
		this.chessplainsize = chessplainsize;
		this.blocklength = blocklength;
		this.rotatequantization = rotatequantization;
		this.windowlength = this.chessplainsize * this.blocklength * Math.sqrt(2.0);
		
		//Calculate the Taiji's angle that will be displayed in the game.
		
		this.cosinetheta = new double[this.rotatequantization];
		this.sinetheta = new double[this.rotatequantization];
		
		double quantizedtheta = 2 * Math.PI / this.rotatequantization;
		
		for(int i = 0; i < this.rotatequantization; i ++)
		{
			this.cosinetheta[i] = Math.cos(i * quantizedtheta);
			this.sinetheta[i] = Math.sin(i * quantizedtheta);
		}
		
		this.rotationposition = 0;
		
		this.taijisnakecanvas = new Ortho(this.windowlength, this.windowlength, 2);
		this.taijisnakecanvas.translate(this.windowlength / 2, this.windowlength / 2, 0);
		
		this.taijisnake = new TaijiSnake(this, TaijiSnake.TaijiSnakeOrientation.RIGHT);
		this.taijisnakeinvoker = new TaijiSnakeInvoker(this, rotatecycle);
		
		this.UP_ARROW = new KeyboardStatusEventMonitor(this, Keyboard.KEY_UP);
		this.DOWN_ARROW = new KeyboardStatusEventMonitor(this, Keyboard.KEY_DOWN);
		this.LEFT_ARROW = new KeyboardStatusEventMonitor(this, Keyboard.KEY_LEFT);
		this.RIGHT_ARROW = new KeyboardStatusEventMonitor(this, Keyboard.KEY_RIGHT);
	}
	
	public double getWindowLength()
	{
		return this.windowlength;
	}
	
	public int getChessPlainSize()
	{
		return this.chessplainsize;
	}
	
	@Override
	public void onInputEventException(InputEventException inputeventexception)
	{
		inputeventexception.printStackTrace();
	}
	
	@Override
	public void onInputEventResume(InputEventMonitor inputeventmonitor)
	{
		
	}
	
	@Override
	public void onInputEventResponse(InputEventMonitor inputeventmonitor)
	{
		if(inputeventmonitor == this.UP_ARROW) this.taijisnake.changeOrientation(TaijiSnakeOrientation.UP);
		if(inputeventmonitor == this.DOWN_ARROW) this.taijisnake.changeOrientation(TaijiSnakeOrientation.DOWN);
		if(inputeventmonitor == this.LEFT_ARROW) this.taijisnake.changeOrientation(TaijiSnakeOrientation.LEFT);
		if(inputeventmonitor == this.RIGHT_ARROW) this.taijisnake.changeOrientation(TaijiSnakeOrientation.RIGHT);
	}
	
	public double getPositionXByAxis(int xaxis, int yaxis)
	{
		double v1_xaxis = this.blocklength * this.cosinetheta[this.rotationposition];
		double v2_xaxis = - this.blocklength * this.sinetheta[this.rotationposition];
		double origin_xaxis = 0.5 * (this.getWindowLength() - (this.chessplainsize * v1_xaxis + this.chessplainsize * v2_xaxis));
		return origin_xaxis + xaxis * v1_xaxis + yaxis * v2_xaxis;
	}
	
	public double getPositionYByAxis(int xaxis, int yaxis)
	{
		double v1_yaxis = this.blocklength * this.sinetheta[this.rotationposition];
		double v2_yaxis = this.blocklength * this.cosinetheta[this.rotationposition];
		double origin_yaxis = 0.5 * (this.getWindowLength() - (this.chessplainsize * v1_yaxis + this.chessplainsize * v2_yaxis));
		return origin_yaxis + xaxis * v1_yaxis + yaxis * v2_yaxis;
	}
	
	public void onInitialize() throws Exception
	{
		this.taijisnakecanvas.onInit(null);
		this.taijisnakecanvas.registerDrawable(new TaijiSnakePlayground(this));
		this.taijisnakecanvas.registerDrawable(this.taijisnake);
		
		this.taijisnake.onInitialize(this.taijisnakecanvas, 3, 2, 3);
		
		this.taijisnakeinvoker.start();
		
		this.UP_ARROW.startInputEventMonitor();
		this.DOWN_ARROW.startInputEventMonitor();
		this.LEFT_ARROW.startInputEventMonitor();
		this.RIGHT_ARROW.startInputEventMonitor();
	}
	
	public void onRefresh() throws Exception
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		this.taijisnakecanvas.onDraw(null);
		Display.update();
		Display.sync(60);
	}
	
	public void onTerminate() throws Exception
	{
		this.taijisnakecanvas.onDestroy(null);
		this.taijisnakeinvoker.stop();
		
		this.UP_ARROW.stopInputEventMonitor();
		this.DOWN_ARROW.stopInputEventMonitor();
		this.LEFT_ARROW.stopInputEventMonitor();
		this.RIGHT_ARROW.stopInputEventMonitor();
	}
	
	public boolean hasSnakeGameOver()
	{
		return this.hasgameover;
	}
	
	public void onGame() throws Exception
	{
		this.rotationposition = (rotationposition + 1) % this.rotatequantization;
		if(!this.taijisnake.runSnake()) this.hasgameover = true;
	}
	
	public static void main(String[] arguments) throws Exception
	{
		
		TaijiSnakeGame taijisnake = new TaijiSnakeGame(20, 20, 100, 2000);
		int windowsize = (int)taijisnake.getWindowLength();
		
		Display.setDisplayMode(new DisplayMode(windowsize, windowsize));
		Display.setTitle("TaijiSnake");
		Display.create();
		Display.setVSyncEnabled(true);
		
		taijisnake.onInitialize();
		while(!Display.isCloseRequested() && !taijisnake.hasSnakeGameOver()) taijisnake.onRefresh();
		taijisnake.onTerminate();
		
		Display.destroy();
	}
	
}

class TaijiSnakePlayground implements Drawable
{
	private final TaijiSnakeGame taijisnake;
	
	public TaijiSnakePlayground(TaijiSnakeGame taijisnake)
	{
		this.taijisnake = taijisnake;
	}
	
	@Override
	public void onInit(Container canvas)
	{
		
	}
	
	@Override
	public void onDraw(Container canvas)
	{
		int plainsize = this.taijisnake.getChessPlainSize();
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2d(this.taijisnake.getPositionXByAxis(0, 0), this.taijisnake.getPositionYByAxis(0, 0));
			GL11.glVertex2d(this.taijisnake.getPositionXByAxis(0, plainsize), this.taijisnake.getPositionYByAxis(0, plainsize));
			GL11.glVertex2d(this.taijisnake.getPositionXByAxis(plainsize, plainsize), this.taijisnake.getPositionYByAxis(plainsize, plainsize));
			GL11.glVertex2d(this.taijisnake.getPositionXByAxis(plainsize, 0), this.taijisnake.getPositionYByAxis(plainsize, 0));
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_POINTS);
		for(int i = 1; i < plainsize; i++)
			for(int j = 1; j < plainsize; j++) GL11.glVertex2d(this.taijisnake.getPositionXByAxis(i, j), this.taijisnake.getPositionYByAxis(i, j));
		GL11.glEnd();
	}
	
	@Override
	public void onDestroy(Container canvas)
	{
		
	}
	
}

class TaijiSnakeInvoker extends Thread implements Runnable
{
	
	private final TaijiSnakeGame taijisnake;
	private final int refreshrate;
	
	public TaijiSnakeInvoker(TaijiSnakeGame taijisnake, int refreshrate)
	{
		this.taijisnake = taijisnake;
		this.refreshrate = refreshrate;
	}
	
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				if(!this.taijisnake.hasSnakeGameOver()) this.taijisnake.onGame();
				Thread.sleep(this.refreshrate);
			}
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
}