package net.aegistudio.transparent.toolkit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
	JTextField currentPage, totalPage; JLabel pageSlashLabel;
	
	JButton newPage, importPage, exportPage, deletePage, runShade;
	SyntaxHighlighter syntaxhighlighter;
	
	int currentPageIndex = 1;
	
	protected LinkedList<String> shaderPool;
	protected LinkedList<Integer> shaderType;
	
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
		shaderTypeLabel.setLocation(locator, 50);
		shaderTypeLabel.setSize(55, 25); locator += 55;
		
		this.editorFrame.add(shaderTypeLabel);
		
		shaderTypeCombo = new JComboBox<String>();
		shaderTypeCombo.setLocation(locator, 50);
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
		fontLabel.setLocation(locator, 50);
		fontLabel.setSize(35, 25); locator += 35;
		this.editorFrame.add(fontLabel);
		
		fontCombo = new JComboBox<String>();
		fontCombo.setLocation(locator, 50);
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
		fontSize.setLocation(locator, 50);
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
		
		shaderPool = new LinkedList<String>();
		shaderType = new LinkedList<Integer>();
		String demo = "void main(){\n\tgl_Vertex = ftransform();\n}";
		shaderPool.add(demo);	//demo page 1
		shaderType.add(EnumShaderType.VERTEX.ordinal());
		this.editingArea.setText(demo);
		
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
		
		syntaxhighlighter = new SyntaxHighlighter(this.editingArea.getDocument(), Color.BLACK, new KeywordScheme[]{type, glConstants, control});
		this.editingArea.getDocument().addDocumentListener(syntaxhighlighter);
		JScrollPane editingAreaPane = new JScrollPane(this.editingArea);
		editingAreaPane.setSize(490, 375);
		editingAreaPane.setLocation(5, 75);
		editingAreaPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editingAreaPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.editorFrame.add(editingAreaPane);
		
		//Adding navigators.
		
		JPanel navigators = new JPanel();
		navigators.setLayout(null);
		navigators.setSize(this.editorFrame.getWidth() - 10, 25);
		navigators.setLocation(5, editingAreaPane.getLocation().y + editingAreaPane.getHeight());
		this.editorFrame.add(navigators);
		
		int navigator_width = navigators.getSize().width;
		int navigator_height = navigators.getSize().height;
		
		first = new JButton("<<");
		first.setLocation(0, 0);
		first.setSize((int)(navigator_width * 0.15f), navigator_height);
		first.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				switchToShader(1);
			}
		});
		navigators.add(first);
		
		previous = new JButton("<");
		previous.setLocation((int)(navigator_width * 0.15f), 0);
		previous.setSize((int)(navigator_width * 0.15f), navigator_height);
		previous.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				switchToShader(currentPageIndex - 1);
			}
		});
		navigators.add(previous);
		
		last = new JButton(">>");
		last.setSize((int) (navigator_width * 0.15f), navigator_height);
		last.setLocation((int) (0.85f * navigator_width), 0);
		last.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				switchToShader(shaderPool.size());
			}
		});
		navigators.add(last);
		
		next = new JButton(">");
		next.setSize((int) (navigator_width * 0.15f), navigator_height);
		next.setLocation((int) (0.7f * navigator_width), 0);
		next.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				switchToShader(currentPageIndex + 1);
			}
		});
		navigators.add(next);
		
		currentPage = new JTextField();
		currentPage.setSize((int) (navigator_width * 0.15), navigator_height);
		currentPage.setLocation((int)(navigator_width * 0.3f), 0);
		currentPage.setHorizontalAlignment(JTextField.CENTER);
		navigators.add(currentPage);
		
		totalPage = new JTextField();
		totalPage.setSize((int) (navigator_width * 0.15), navigator_height);
		totalPage.setLocation((int)(navigator_width * 0.55f), 0);
		totalPage.setEditable(false);
		totalPage.setHorizontalAlignment(JTextField.CENTER);
		navigators.add(totalPage);
		
		pageSlashLabel = new JLabel("/");
		pageSlashLabel.setSize(80, 25);
		pageSlashLabel.setLocation((navigators.getSize().width - 80) / 2, 0);
		pageSlashLabel.setHorizontalAlignment(JLabel.CENTER);
		navigators.add(pageSlashLabel);
		
		//Adding function buttons.
		
		JPanel functionPanel = new JPanel();
		functionPanel.setSize(390, 25);
		functionPanel.setLocation(5, 25);
		functionPanel.setLayout(null);
		this.editorFrame.add(functionPanel);
		
		this.newPage = new JButton("New");
		this.newPage.setSize(70, 25);
		this.newPage.setLocation(0, 0);
		this.newPage.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				shaderPool.add(currentPageIndex + 1 - 1, "");
				shaderType.add(currentPageIndex + 1 - 1, 0);
				switchToShader(currentPageIndex + 1);
			}
		});
		functionPanel.add(this.newPage);
		
		this.importPage = new JButton("Import");
		this.importPage.setSize(85, 25);
		this.importPage.setLocation(70, 0);
		functionPanel.add(this.importPage);
		
		this.exportPage = new JButton("Export");
		this.exportPage.setSize(85, 25);
		this.exportPage.setLocation(155, 0);
		functionPanel.add(this.exportPage);
		
		this.deletePage = new JButton("Delete");
		this.deletePage.setSize(85, 25);
		this.deletePage.setLocation(240, 0);
		functionPanel.add(this.deletePage);
		
		this.runShade = new JButton("Run");
		this.runShade.setSize(65, 25);
		this.runShade.setLocation(325, 0);
		functionPanel.add(this.runShade);
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
		this.currentPage.setFont(font);
		this.totalPage.setFont(font);
		
		this.newPage.setFont(font);
		this.importPage.setFont(font);
		this.exportPage.setFont(font);
		this.deletePage.setFont(font);
		this.runShade.setFont(font);
		
		this.editorFrame.revalidate();
	}
	
	public void changeEditorFont()
	{
		if(systemFonts != null)
		{
			Integer itemObject = (Integer)fontSize.getSelectedItem();
			float item = (itemObject == null)? 17.0f: (float)((int)itemObject);
			Font usingFont = systemFonts[fontCombo.getSelectedIndex()].deriveFont(item);
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
	
	public void switchToShader(int page)
	{
		shaderPool.set(this.currentPageIndex - 1, this.editingArea.getText());
		shaderType.set(this.currentPageIndex - 1, this.shaderTypeCombo.getSelectedIndex());
		
		this.editingArea.setText(this.shaderPool.get(page - 1));
		this.shaderTypeCombo.setSelectedIndex(this.shaderType.get(page - 1));
		this.currentPageIndex = page;
		this.currentPage.setText(Integer.toString(currentPageIndex));
		this.totalPage.setText(Integer.toString(this.shaderPool.size()));
		this.deletePage.setEnabled(this.shaderPool.size() > 1);
		this.syntaxhighlighter.fullSyntaxHighlight(this.editingArea.getDocument());
		
		this.previous.setEnabled(this.currentPageIndex > 1);
		this.first.setEnabled(this.currentPageIndex > 1);
		this.next.setEnabled(this.currentPageIndex < this.shaderPool.size());
		this.last.setEnabled(this.currentPageIndex < this.shaderPool.size());
	}
	
	public void createDisplay()
	{
		editorFrame.setVisible(true);
		getSystemFontThread.start();
		getShaderTypeThread.start();
		modelviewer.getFrame().setVisible(true);
		modelviewer.fontGoodizer.start();
		this.switchToShader(1);
	}
	
	public static void main(String[] arguments) throws Exception
	{
		GlslEditor glsleditor = new GlslEditor();
		glsleditor.createDisplay();
	}
}