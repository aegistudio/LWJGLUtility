package net.aegistudio.transparent.toolkit;

import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.List;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.filechooser.FileFilter;

import java.io.FileInputStream;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.lwjgl.opengl.GL11;

import net.aegistudio.transparent.opengl.Canvas;
import net.aegistudio.transparent.opengl.Container;
import net.aegistudio.transparent.opengl.Scene;
import net.aegistudio.transparent.opengl.WrappedAWTGLCanvas;
import net.aegistudio.transparent.opengl.camera.Camera;
import net.aegistudio.transparent.opengl.camera.Frustum;
import net.aegistudio.transparent.opengl.camera.Ortho;
import net.aegistudio.transparent.opengl.image.ImageUtils;
import net.aegistudio.transparent.opengl.lighting.Light;
import net.aegistudio.transparent.opengl.model.Model;
import net.aegistudio.transparent.opengl.texture.Texture;
import net.aegistudio.transparent.opengl.util.FlyweightDrawable;
import net.aegistudio.transparent.util.ScopedGraphic;
import net.aegistudio.transparent.wavefront.WavefrontBuilder;
import net.aegistudio.transparent.wavefront.WavefrontModel;

/**
 * This model viewer is used to visualize wavefront object models.
 * Please run it by calling it as the main class with JVM.
 * @author aegistudio
 */

public class ModelViewer extends Canvas
{
	Frame thisFrame;
	
	WavefrontModel wavefrontModel = null;	ScopedGraphic scoping_wavefrontModel = null;
	Model renderingModel = null;	FlyweightDrawable fw_renderingModel = null;
	Scene renderingScene = new Scene();
	WrappedAWTGLCanvas canvas;
	Camera camera = null;
	List modelList = new List();
	List loggerList = new List();
	
	Texture texture = null; ScopedGraphic scoping_texture = null;
	
	Light light = new Light();
	
	@Override
	public void onInit(Container container)
	{
		GL11.glEnable(GL11.GL_LIGHTING);
		light.diffuse(1.0f, 1.0f, 1.0f, 1.0f);
		light.create();
		light.bind();
	}
	
	@Override
	public void onDraw(Container container)
	{
		light.position(0, 0, -1, 0);
		if(ModelViewer.this.texture != null)
		{
			ModelViewer.this.texture.create();
			ModelViewer.this.texture.bind();
		}
		super.onDraw(container);
		if(ModelViewer.this.texture != null) ModelViewer.this.texture.unbind();
	}
	
	@Override
	public void onDestroy(Container container)
	{
		super.onDestroy(container);
		light.destroy();
	}
	
	public ModelViewer() throws Exception
	{
		thisFrame = new Frame();
		thisFrame.setTitle("Model Viewer");
		thisFrame.setSize(new Dimension(760, 480));
		thisFrame.setResizable(false);
		thisFrame.setLayout(null);
		
		canvas = new WrappedAWTGLCanvas(this);
		
		canvas.lockedRatio = true;
		canvas.setSize(600, 480);
		canvas.setLocation(0, 0);
		thisFrame.add(canvas);
		
		thisFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent event)
			{
				thisFrame.setVisible(false);
				thisFrame.dispose();
				System.exit(0);
			}
		});
		
		thisFrame.getToolkit().addAWTEventListener(new AWTEventListener()
		{

			@Override
			public void eventDispatched(AWTEvent arg0)
			{
				MouseWheelEvent mw = (MouseWheelEvent)arg0;
				int units = mw.getUnitsToScroll();
				if(units > 0) while(units > 0){ units -= 3; ModelViewer.this.zoomOut(); }
				else if(units < 0) while(units < 0){ units += 3; ModelViewer.this.zoomIn(); }
			}
		}, MouseEvent.MOUSE_WHEEL_EVENT_MASK);
		
		CanvasDragger dragger = new CanvasDragger();
		dragger.objdisp = this;
		canvas.addMouseMotionListener(dragger);
		canvas.addMouseListener(dragger);
		
		JTabbedPane tab = new JTabbedPane();
		tab.setLocation(600 + 3, 25);
		tab.setSize(140 + 12, 450);
		JPanel modelPanel = new JPanel();
		modelPanel.setSize(119, 110);
		tab.addTab("Model", modelPanel);
		thisFrame.add(tab);
		{
			Button openModelButton = new Button("Open Model");
			openModelButton.setPreferredSize(new Dimension(119, 25));
			openModelButton.addActionListener(new ActionListener()
			{
	
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					JFileChooser openFile = new JFileChooser();
					FileFilter filter = new FileFilter()
					{
						@Override
						public boolean accept(File arg0)
						{
							if(arg0.isDirectory()) return true;
							return arg0.getName().endsWith(".obj");
						}
	
						@Override
						public String getDescription()
						{
							return "Wavefront Obj Model";
						}					
					};
					openFile.setFileFilter(filter);
					openFile.setVisible(true);
					int state = openFile.showOpenDialog(thisFrame);
					if(state == JFileChooser.APPROVE_OPTION)
					{
						File file = openFile.getSelectedFile();
						try
						{
							ModelViewer.this.open(file);
						}
						catch(Exception e)
						{
							ModelViewer.this.output("Error while opening obj file: " + file);
						}
					}
				}
			});
			modelPanel.add(openModelButton);
			
			modelList.setEnabled(false);
			modelList.addItemListener(new ItemListener()
			{
				@Override
				public void itemStateChanged(ItemEvent arg0)
				{
					try
					{
						ModelViewer.this.loadModel(modelList.getSelectedItem());
					}
					catch(Exception e)
					{
						ModelViewer.this.output("Error while loading model: " + modelList.getSelectedItem());
					}
				}
			});
			JScrollPane modelSPane = new JScrollPane(modelList);
			modelSPane.setPreferredSize(new Dimension(130, 360));
			modelPanel.add(modelSPane);
		}
		
		JPanel texPanel = new JPanel();
		texPanel.setSize(119, 110);
		tab.addTab("Texture", texPanel);
		{
			Button bindTextureButton = new Button("Bind Texture");
			bindTextureButton.setPreferredSize(new Dimension(120 - 1, 25));
			bindTextureButton.addActionListener(new ActionListener()
			{
	
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					JFileChooser openFile = new JFileChooser();
					FileFilter filter = new FileFilter()
					{
						@Override
						public boolean accept(File arg0)
						{
							return true;
						}
	
						@Override
						public String getDescription()
						{
							return "Texture";
						}					
					};
					openFile.setFileFilter(filter);
					openFile.setVisible(true);
					int state = openFile.showOpenDialog(thisFrame);
					if(state == JFileChooser.APPROVE_OPTION)
					{
						File file = openFile.getSelectedFile();
						try
						{
							ModelViewer.this.bindTexture(file);
							ModelViewer.this.unbindTextureButton.setEnabled(true);
						}
						catch(Exception e)
						{
							ModelViewer.this.output("Error while opening texture: " + file);
						}
					}
				}
			});
			texPanel.add(bindTextureButton);
			
			unbindTextureButton = new Button("Unbind Texture");
			unbindTextureButton.setPreferredSize(new Dimension(120 - 1, 25));
			unbindTextureButton.setEnabled(false);
			unbindTextureButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					ModelViewer.this.unbindTexture();
					ModelViewer.this.unbindTextureButton.setEnabled(false);
				}
			});
			texPanel.add(unbindTextureButton);
		}
		
		JPanel camPanel = new JPanel();
		camPanel.setSize(119, 110);
		tab.addTab("Camera", camPanel);
		{
			ButtonGroup camera = new ButtonGroup();
			
			JRadioButton ortho = new JRadioButton("Ortho");
			camPanel.add(ortho);
			camera.add(ortho);
			ortho.addItemListener(new ItemListener()
			{
				Ortho theOrtho = new Ortho(6, 4.8, 6);
				@Override
				public void itemStateChanged(ItemEvent arg0)
				{
					if(ItemEvent.SELECTED == arg0.getStateChange())
						ModelViewer.this.setCamera(theOrtho);
				}
			});
			ortho.setSelected(true);
			
			JRadioButton frustum = new JRadioButton("Frustum");
			camPanel.add(frustum);
			camera.add(frustum);
			frustum.addItemListener(new ItemListener()
			{
				float depth = 7.f;
				float focusDistance = 2.f;
				Frustum frustum = new Frustum(6, 4.8, focusDistance, depth);
				{
					frustum.orient(0, 0, 1);
					frustum.translate(0, 0, - (-focusDistance / 10 + depth / 2));
					frustum.rotate(0, 0, 180);
					frustum.zoom(-1, 1, 1);
				}
				@Override
				public void itemStateChanged(ItemEvent arg0)
				{
					if(ItemEvent.SELECTED == arg0.getStateChange())
					{
						ModelViewer.this.setCamera(frustum);
					}
				}
			});
		}
		
		JPanel logPanel = new JPanel();
		logPanel.setSize(119, 210);
		tab.addTab("Logger", logPanel);
		{
			JScrollPane logSPane = new JScrollPane(this.loggerList);
			logSPane.setPreferredSize(new Dimension(129, 385));
			logPanel.add(logSPane);
		}
	}
	
	Button unbindTextureButton = null;
	
	public void setCamera(Camera theCamera)
	{
		if(theCamera == null) return;

		if(this.camera != null)
		{
			camera.unregisterDrawable(renderingScene);
			canvas.unregisterDrawable(camera);
		}
		
		this.camera = theCamera;
		camera.registerDrawable(renderingScene);
		canvas.registerDrawable(camera);
	}
	
	public void open(File model) throws Exception
	{
		if(model == null) return;
		if(wavefrontModel != null) canvas.unregisterDrawable(this.scoping_wavefrontModel);
		WavefrontBuilder builder = new WavefrontBuilder();
		this.wavefrontModel = builder.build(new FileInputStream(model));
		{
			//output comments
			String[] comments = builder.getComments();
			for(String comment : comments) output("# " + comment);
			
			//output models
			String[] models = wavefrontModel.listObjectModelNames();
			modelList.removeAll();
			modelList.setEnabled(false);
			for(String modeln : models)
			{
				if(!modelList.isEnabled()) modelList.setEnabled(true);
				output("+ " + modeln);
				modelList.add(modeln);
			}
		}
		this.scoping_wavefrontModel = new ScopedGraphic(wavefrontModel);
		canvas.registerDrawable(this.scoping_wavefrontModel);
	}
	
	public void bindTexture(File tex) throws Exception
	{
		if(tex == null) return;
		if(texture != null) canvas.unregisterDrawable(this.scoping_texture);
		texture = ImageUtils.createImageTexture(new FileInputStream(tex));
		this.scoping_wavefrontModel = new ScopedGraphic(texture);
		canvas.registerDrawable(scoping_texture);
	}
	
	public void unbindTexture()
	{
		canvas.unregisterDrawable(this.scoping_texture);
		this.scoping_texture = null;
		this.texture = null;
	}
	
	public void zoomIn()
	{
		this.renderingScene.zoom(1.1f, 1.1f, 1.1f);
	}
	
	public void zoomOut()
	{
		this.renderingScene.zoom(1/1.1f, 1/1.1f, 1/1.1f);
	}
	
	public void output(String registry)
	{
		this.loggerList.add(registry);
	}
	
	public void loadModel(String modelName) throws Exception
	{
		if(modelName == null) return;
		Model model = this.wavefrontModel.getObjectModel(modelName);
		if(model == this.renderingModel) return;
		if(this.renderingModel != null) this.renderingScene.unregisterDrawable(this.renderingModel);
		this.renderingModel = model;
		this.fw_renderingModel = new FlyweightDrawable(renderingModel);
		if(this.renderingModel != null) this.renderingScene.registerDrawable(this.renderingModel);
	}
	
	public Frame getFrame()
	{
		return this.thisFrame;
	}
	
	public static void main(String[] arguments) throws Exception
	{
		ModelViewer theFrame = new ModelViewer();
		theFrame.getFrame().setVisible(true);
	}
}

class CanvasDragger extends MouseAdapter implements MouseMotionListener, MouseListener
{
	ModelViewer objdisp;
	int X, Y;
	boolean state = false;
	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		if(state)
		{
			int deltaX = arg0.getX() - X;
			int deltaY = arg0.getY() - Y;
			X = arg0.getX();
			Y = arg0.getY();
			objdisp.renderingScene.rotate(- deltaY, - deltaX, 0);
		}
		else
		{
			X = arg0.getX();
			Y = arg0.getY();
			state = true;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		state = false;
	}
}