package net.aegistudio.transparent.toolkit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	JLabel shaderTypeLabel;
	JComboBox<String> shaderTypeCombo;
	JLabel fontLabel;
	JComboBox<String> fontCombo;
	JComboBox<Integer> fontSize;
	JTextPane editingArea;
	
	JButton first, previous, last, next;
	
	@SuppressWarnings("serial")
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
		shaderTypeLabel = new JLabel();
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
		
		fontLabel = new JLabel();
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
		
		this.editingArea = new JTextPane()
		{
		    public boolean getScrollableTracksViewportWidth()
		    {
		        return false;
		    }
		    
		    public void setSize(Dimension dimension)
		    {
		        if (dimension.width < getParent().getSize().width)
		        		dimension.width = getParent().getSize().width;
		        super.setSize(dimension);
		    }
		};
		
		KeywordScheme type = new KeywordScheme(new String[]{
		"void", "int", "float", "double", "struct",	"const",
		"bool", "true", "false", //C specification
		"vec2", "vec3", "vec4", "mat2", "mat3", "mat4",
		"mat2x2", "mat2x3", "mat2x4",
		"mat3x2", "mat3x3", "mat3x4",
		"mat4x2", "mat4x3", "mat4x4",
		"sampler1D", "sampler2D", "sampler3D",
		"uniform", "attribute", "varying", "in", "out", "shared"	//OpenGL shader scope
		}, Color.BLUE);
		
		KeywordScheme glConstants = new KeywordScheme(new String[]{
		"gl_Color", "gl_SecondaryColor", "gl_Normal", "gl_Vertex",
		"gl_MultiTexCoord0", "gl_MultiTexCoord1", "gl_MultiTexCoord2",
		"gl_MultiTexCoord3", "gl_MultiTexCoord4", "gl_MultiTexCoord5",
		"gl_MultiTexCoord6", "gl_MultiTexCoord7", "gl_FogCoord"}, Color.CYAN.darker());
		
		KeywordScheme control = new KeywordScheme(new String[]
		{ "if", "else", "while", "for", "switch", "case", "default", "do", "continue", "return"}, Color.magenta.darker());
		
		this.editingArea.getDocument().addDocumentListener(
				new SyntaxHighlighter(this.editingArea.getDocument(), Color.BLACK, new KeywordScheme[]{type, glConstants, control})
		);
		JScrollPane editingAreaPane = new JScrollPane(this.editingArea);
		editingAreaPane.setSize(390, 300);
		editingAreaPane.setLocation(5, 50);
		editingAreaPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editingAreaPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.editorFrame.add(editingAreaPane);
		
		JPanel navigators = new JPanel();
		navigators.setLayout(null);
		navigators.setSize(this.editorFrame.getWidth() - 10, 25);
		navigators.setLocation(5, editingAreaPane.getLocation().y + editingAreaPane.getHeight());
		this.editorFrame.add(navigators);
		
		first = new JButton("<<");
		first.setLocation(0, 0);
		first.setSize(50, 25);
		navigators.add(first);
		
		previous = new JButton("<");
		previous.setLocation(50, 0);
		previous.setSize(50, 25);
		navigators.add(previous);
		
		last = new JButton(">>");
		last.setSize(50, 25);
		last.setLocation(navigators.getSize().width - last.getWidth(), 0);
		navigators.add(last);
		
		next = new JButton(">");
		next.setSize(50, 25);
		next.setLocation(navigators.getSize().width - last.getWidth() - next.getWidth(), 0);
		navigators.add(next);
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
			goodizeFont(systemFonts[courierNewIndex]);
		}
	};
	
	public void goodizeFont(Font font)
	{
		font = font.deriveFont(14.0f);
		this.fontLabel.setFont(font);
		this.fontCombo.setFont(font);
		this.fontSize.setFont(font);
		this.shaderTypeLabel.setFont(font);
		this.shaderTypeCombo.setFont(font);
		this.first.setFont(font);
		this.previous.setFont(font);
		this.last.setFont(font);
		this.next.setFont(font);
		this.editorFrame.revalidate();
	}
	
	public void changeEditorFont()
	{
		if(systemFonts != null)
		{
			int item = (Integer)fontSize.getSelectedItem();
			Font usingFont = systemFonts[fontCombo.getSelectedIndex()].deriveFont((float)item);
			this.editingArea.setFont(usingFont);
			this.editorFrame.revalidate();
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
	
	public void switchToShader(int index)
	{
		
	}
	
	public static void main(String[] arguments) throws Exception
	{
		GlslEditor glsleditor = new GlslEditor();
		glsleditor.modelviewer.getFrame().setVisible(true);
		glsleditor.editorFrame.setVisible(true);
		glsleditor.getSystemFontThread.start();
		glsleditor.getShaderTypeThread.start();
	}
}