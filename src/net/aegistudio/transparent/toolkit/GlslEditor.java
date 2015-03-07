package net.aegistudio.transparent.toolkit;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import net.aegistudio.transparent.opengl.Container;
import net.aegistudio.transparent.opengl.glsl.EnumShaderType;
import net.aegistudio.transparent.opengl.glsl.ShaderProgram;

public class GlslEditor
{
	ModelViewer modelviewer;
	ShaderProgram shaderProgram = null;
	boolean shouldInit = false; boolean shouldDestroy = false;
	
	Frame editorFrame;
	Font[] systemFonts;
	JComboBox<String> shaderTypeCombo;
	JComboBox<String> fontCombo;
	JComboBox<Integer> fontSize;
	JTextPane editingArea;
	
	public GlslEditor() throws Exception
	{
		modelviewer = new ModelViewer()
		{
			public void onDraw(Container container)
			{
				if(shaderProgram != null)
				{
					if(shouldInit) shaderProgram.create();
					shaderProgram.bind();
				}
				super.onDraw(container);
				if(shaderProgram != null)
				{
					shaderProgram.unbind();
					if(shouldDestroy)
					{
						shaderProgram.destroy();
						shaderProgram = null;
					}
				}
			}
		};
		
		this.editorFrame = new Frame();
		this.editorFrame.setTitle("GL Shader Language Editor");
		this.editorFrame.setLocationRelativeTo(modelviewer.getFrame());
		this.editorFrame.setSize(400, modelviewer.getFrame().getHeight());
		this.editorFrame.setLocation(this.modelviewer.getFrame().getWidth(), 0);
		this.editorFrame.addWindowListener(this.modelviewer.getFrame().getListeners(WindowListener.class)[0]);
		this.editorFrame.setResizable(false);
		
		this.editorFrame.setLayout(null);
		
		int locator = 0;
		JLabel shaderTypeLabel = new JLabel();
		shaderTypeLabel.setHorizontalAlignment(JLabel.CENTER);
		shaderTypeLabel.setText("Shader");
		shaderTypeLabel.setLocation(locator, 25);
		shaderTypeLabel.setSize(55, 25); locator += 55;
		
		this.editorFrame.add(shaderTypeLabel);
		
		shaderTypeCombo = new JComboBox<String>();
		shaderTypeCombo.setLocation(locator, 25);
		shaderTypeCombo.setSize(120, 25); locator += 120;
		shaderTypeCombo.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent arg0)
			{
				shaderTypeCombo.setToolTipText("<html><b>Shader: </b>" + arg0.getItem().toString() + "</html>");
			}
		});
		this.editorFrame.add(shaderTypeCombo);
		
		JLabel fontLabel = new JLabel();
		fontLabel.setHorizontalAlignment(JLabel.CENTER);
		fontLabel.setText("Font");
		fontLabel.setLocation(locator, 25);
		fontLabel.setSize(35, 25); locator += 35;
		this.editorFrame.add(fontLabel);
		
		fontCombo = new JComboBox<String>();
		fontCombo.setLocation(locator, 25);
		fontCombo.setSize(125, 25); locator += 125;
		fontCombo.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent arg0)
			{
				fontCombo.setToolTipText("<html><b>Font Family: </b>" + arg0.getItem().toString() + "</html>");
				GlslEditor.this.changeEditorFont();
			}
		});
		this.editorFrame.add(fontCombo);
		
		fontSize = new JComboBox<Integer>();
		fontSize.setLocation(locator, 25);
		fontSize.setSize(63, 25); locator += 63;
		fontSize.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent arg0)
			{
				fontSize.setToolTipText("<html><b>Font Size: </b>" + arg0.getItem().toString() + " px</html>");
				GlslEditor.this.changeEditorFont();
			}
		});
		this.editorFrame.add(fontSize);
		
		this.editingArea = new JTextPane();
		
		KeywordScheme type = new KeywordScheme(new String[]{
		"void", "int", "float", "double", "struct",		//C specification
		"vec2", "vec3", "vec4", "mat2", "mat3", "mat4",
		"mat2x2", "mat2x3", "mat2x4",
		"mat3x2", "mat3x3", "mat3x4",
		"mat4x2", "mat4x3", "mat4x4",
		"sampler1D", "sampler2D", "sampler3D",
		"uniform", "attribute", "varying"			//OpenGL shader scope
		}, Color.BLUE);
		
		KeywordScheme glConstants = new KeywordScheme(new String[]
		{ "gl_Vertex", "gl_Position", "gl_Color"}, Color.CYAN.darker());
				
		
		this.editingArea.getDocument().addDocumentListener(
				new SyntaxHighlighter(this.editingArea.getDocument(), Color.BLACK, new KeywordScheme[]{type, glConstants})
		);
		JScrollPane editingAreaPane = new JScrollPane(this.editingArea);
		editingAreaPane.setSize(390, 300);
		editingAreaPane.setLocation(5, 50);
		editingAreaPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editingAreaPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.editorFrame.add(editingAreaPane);
	}
	
	protected Thread getSystemFontThread = new Thread()
	{
		public void run()
		{
			systemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
			int courierNewIndex = 0;
			for(int i = 0; i < systemFonts.length; i ++)
			{
				String theFontName = systemFonts[i].getFontName();
				if(theFontName.equals("Courier New")) courierNewIndex = i;
				fontCombo.addItem(theFontName);
			}
			fontCombo.revalidate();
			fontCombo.setSelectedIndex(courierNewIndex);
		}
	};
	
	public void changeEditorFont()
	{
		if(systemFonts != null)
		{
			int item = (Integer)fontSize.getSelectedItem();
			Font usingFont = systemFonts[fontCombo.getSelectedIndex()].deriveFont((float)item);
			this.editingArea.setFont(usingFont);
			this.editingArea.revalidate();
		}
	}
	
	protected Thread getShaderTypeThread = new Thread()
	{
		public void run()
		{
			EnumShaderType[] shaders = EnumShaderType.values();
			for(int i = 0; i < shaders.length; i ++)
				shaderTypeCombo.addItem(shaders[i].toString().toLowerCase().replace('_', ' '));
			shaderTypeCombo.revalidate();
			shaderTypeCombo.setSelectedIndex(0);
			
			for(int i = 7; i < 80; i ++) fontSize.addItem(i);
			fontSize.revalidate();
			fontSize.setSelectedItem(17);
		}
	};
	
	public static void main(String[] arguments) throws Exception
	{
		GlslEditor glsleditor = new GlslEditor();
		glsleditor.modelviewer.getFrame().setVisible(true);
		glsleditor.editorFrame.setVisible(true);
		glsleditor.getSystemFontThread.start();
		glsleditor.getShaderTypeThread.start();
	}
}