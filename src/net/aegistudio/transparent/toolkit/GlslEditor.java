package net.aegistudio.transparent.toolkit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.lwjgl.opengl.ARBShaderObjects;

import net.aegistudio.transparent.opengl.Container;
import net.aegistudio.transparent.opengl.glsl.EnumShaderType;
import net.aegistudio.transparent.opengl.glsl.Shader;
import net.aegistudio.transparent.opengl.glsl.ShaderProgram;

public class GlslEditor
{
	ModelViewer modelviewer;
	Shader[] shaders = null, legacyShaders = null;
	ShaderProgram shaderProgram = null, legacyShaderProgram = null;
	boolean flushCurrentShader = false;
	
	Frame editorFrame;
	Font[] systemFonts;
	JLabel shaderTypeLabel;
	JComboBox<String> shaderTypeCombo;
	JLabel fontLabel;
	JComboBox<String> fontCombo;
	JComboBox<Integer> fontSize;
	JTextField title;
	JTextPane editingArea;
	
	JButton first, previous, last, next;
	JTextField currentPage, totalPage; JLabel pageSlashLabel;
	
	JButton newPage, importPage, exportPage, deletePage, runShade;
	SyntaxHighlighter syntaxhighlighter;
	
	int currentPageIndex = 1;
	
	protected LinkedList<String> shaderPool;
	protected LinkedList<String> shaderTitle;
	protected LinkedList<Integer> shaderType;
	protected LinkedList<Object> shaderSelect;
	
	Frame assembler;
	JTable assembleList = new JTable();
	
	JButton selectDeselectAll, execute;
	
	@SuppressWarnings("serial")
	public GlslEditor() throws Exception
	{
		modelviewer = new ModelViewer()
		{
			public synchronized void onDraw(Container container)
			{
				if(shaderProgram != null || flushCurrentShader)
				{
					if(legacyShaderProgram != null)
					{
						legacyShaderProgram.unbind();
						legacyShaderProgram.destroy();
						
						for(Shader shader : legacyShaders) shader.destroy();
						
						legacyShaderProgram = null;
						legacyShaders = null;
					}
					
					if(shaderProgram != null) try
					{
						shaderProgram.create();
						legacyShaderProgram = shaderProgram;
						shaderProgram = null;
						legacyShaders = shaders;
						shaders = null;
					}
					catch(Exception e)
					{
						shaders = null;
						shaderProgram = null;
						legacyShaderProgram = null;
						legacyShaders = null;
						JOptionPane.showConfirmDialog(null, "Error while creating shader, caused by: \n" + e.getMessage(), "Shader Creation Failure!", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
					}
					flushCurrentShader = false;
				}
				
				if(legacyShaderProgram != null)
				{
					legacyShaderProgram.bind();
					if(this.texture != null)
					{
						int uniformShaderPos = legacyShaderProgram.getUniformVariable("sampler");
						if(uniformShaderPos >= 0) ARBShaderObjects.glUniform1iARB(uniformShaderPos, 0);
					}
				}
				
				super.onDraw(container);
				if(legacyShaderProgram != null)
					legacyShaderProgram.unbind();
			}
		};
		
		this.editorFrame = new Frame();
		this.editorFrame.setTitle("GL Shader Editor");
		this.editorFrame.setLocationRelativeTo(modelviewer.getFrame());
		this.editorFrame.setSize(400, modelviewer.getFrame().getHeight());
		this.editorFrame.setLocation(this.modelviewer.getFrame().getWidth(), 0);
		this.editorFrame.addWindowListener(this.modelviewer.getFrame().getListeners(WindowListener.class)[0]);
		this.editorFrame.setResizable(false);
		
		this.editorFrame.setLayout(null);
		
		int locator = 5;
		shaderTypeLabel = new JLabel();
		shaderTypeLabel.setHorizontalAlignment(JLabel.CENTER);
		shaderTypeLabel.setText("Shader");
		shaderTypeLabel.setLocation(locator, 50);
		shaderTypeLabel.setSize(50, 25); locator += 50;
		
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
		fontSize.setSize(63 - 4, 25); locator += 63 - 4;
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
		shaderTitle = new LinkedList<String>();
		shaderSelect = new LinkedList<Object>();
		String demo =
				"void main(){\n"
				+ "\tgl_Position = ftransform();\n"
				+ "\tgl_FrontColor = gl_Color;\n"
				+ "\tgl_FrontSecondaryColor = gl_SecondaryColor;\n"
				+ "\tgl_TexCoord[0] = gl_MultiTexCoord0;\n"
				+ "}";
		shaderPool.add(demo);	//demo page 1
		shaderType.add(EnumShaderType.VERTEX.ordinal());
		String demoTitle = "Untitled";
		shaderTitle.add(demoTitle);
		shaderSelect.add(new Object());
		this.editingArea.setText(demo);
		
		title = new JTextField();
		title.setText(demoTitle);
		title.setSize(390, 25);
		title.setLocation(5, 75);
		this.editorFrame.add(title);
		
		KeywordScheme type = new KeywordScheme(new String[]{
		"void", "int", "float", "double", "struct",	"const",
		"bool", "true", "false", //C specifications
		"vec2", "vec3", "vec4", "bvec2", "bvec3", "bvec4",
		"dvec2", "dvec3", "dvec4", "ivec2", "ivec3", "ivec4",
		"mat2", "mat3", "mat4",
		"mat2x2", "mat2x3", "mat2x4",
		"mat3x2", "mat3x3", "mat3x4",
		"mat4x2", "mat4x3", "mat4x4",
		"sampler1D", "sampler2D", "sampler3D",
		"samplerCube", "sampler1DShadow", "sampler2DShadow",				//OpenGL specifications
		"uniform", "attribute", "varying", "in", "out", "inout", "shared",	//OpenGL shader scope
		"gl_DepthRangeParameters", "gl_PointParameters", "gl_MaterialParameters", "gl_LightSourceParameters",
		"gl_LightModelParameters", "gl_LightModelProducts", "gl_LightProducts", "gl_FogParameters"
		}, Color.BLUE);
		
		KeywordScheme glConstants = new KeywordScheme(new String[]{
		"gl_Color", "gl_SecondaryColor", "gl_Normal", "gl_Vertex", "gl_MaxTextureCoords", "gl_MaxLights",
		"gl_MultiTexCoord0", "gl_MultiTexCoord1", "gl_MultiTexCoord2",
		"gl_MultiTexCoord3", "gl_MultiTexCoord4", "gl_MultiTexCoord5",
		"gl_MultiTexCoord6", "gl_MultiTexCoord7", "gl_FogCoord",
		"gl_Position", "gl_PointSize", "gl_ClipVertex", "gl_TexCoord", "gl_FogFragCoord",
		"gl_FrontColor", "gl_BackColor", "gl_FrontSecondaryColor", "gl_BackSecondaryColor",		//Vertex shader keywords.
		"gl_FragCoord", "gl_FrontFacing", "gl_FragColor", "gl_FragDepth",						//Fragment shader keywords.
		"gl_ModelViewMatrix", "gl_ProjectionMatrix", "gl_ModelViewProjectionMatrix", "gl_NormalMatrix", "gl_TextureMatrix",
		"gl_NormalScale", "gl_DepthRange", "gl_ClipPlane", "gl_Point", "gl_FrontMaterial", "gl_BackMaterial", "gl_LightSource",
		"gl_LightModel", "gl_FrontLightModelProduct", "gl_BackLightModelProduct",
		"gl_FrontLightProduct", "gl_BackLightProduct", "gl_Fog",
		"gl_TextureEnvColor", "gl_EyePlaneS", "gl_EyePlaneT", "gl_EyePlaneR", "gl_EyePlaneQ",
		"gl_ObjectPlaneS", "gl_ObjectPlaneT", "gl_ObjectPlaneR", "gl_ObjectPlaneQ"//Shared state.
		}, Color.CYAN.darker());
		
		KeywordScheme marcos = new KeywordScheme(new String[]
		{
				"#define", "#undef", "#ifdef", "#ifndef",
				"#if", "#elif", "#else", "#endif",
				"#line", "#pragma", "#version"
		}, Color.RED.darker());
		
		KeywordScheme control = new KeywordScheme(new String[]{
		"if", "else", "while", "for", "switch", "case", "default", "do", "continue", "return",		//Controls.
		"radians", "degrees", "sin", "cos", "tan", "asin", "acos", "atan",  	//Flow controls.
		"pow", "exp2", "log2", "sqrt", "inversesqrt", "abs", "sign",  
		"floor", "ceil", "fract", "mod", "min", "max", "clamp", "mix", "step", "smoothstep",
		"length", "distance", "dot", "cross", "normalize", "ftransform", "faceforward", "reflect",
		"matrixcompmult", "lessThan", "lessThanEqual", "greaterThan", "greaterThanEqual",
		"equal", "notEqual", "any", "all", "not",			//GL shader builtins.
		"texture1D", "texture1DProj", "texture1DLod", "texture1DProjLod",
		"shadow1D", "shadow1DProj", "shadow1DLod", "shadow1DProjLod", 
		"texture2D", "texture2DProj", "texture2DLod", "texture2DProjLod",
		"shadow2D", "shadow2DProj", "shadow2DLod", "shadow2DProjLod",
		"texture3D", "texture3DProj", "texture3DLod", "texture3DProjLod",
		"textureCube", "textureCubeLod", //GL texture access.
		"dFdx", "dFdy", "fwidth",	//GL fragment shader differentials.
		"noise1", "noise2", "noise3", "noise4"	//Perlin noises
		}, Color.magenta.darker());
		
		KeywordHighlight kw = new KeywordHighlight(this.editingArea.getDocument(), new KeywordScheme[]{type, glConstants, control, marcos});
		LineCommentHighlight lcm = new LineCommentHighlight(this.editingArea.getDocument(), Color.GREEN.darker());
		MultilineCommentHighlight mlcm = new MultilineCommentHighlight(this.editingArea.getDocument(), Color.GRAY);
		
		syntaxhighlighter = new SyntaxHighlighter(this.editingArea.getDocument(), Color.BLACK, new SyntaxHighlightAlgorithm[]{kw, lcm, mlcm});
		this.editingArea.getDocument().addDocumentListener(syntaxhighlighter);
		JScrollPane editingAreaPane = new JScrollPane(this.editingArea);
		editingAreaPane.setSize(390, 350);
		editingAreaPane.setLocation(5, 100);
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
		currentPage.addFocusListener(new FocusAdapter()
		{
			public void focusLost(FocusEvent e)
			{
				try
				{
					int targetPage = Integer.parseInt(currentPage.getText());
					if(targetPage < 1) targetPage = 1;
					if(targetPage > shaderPool.size()) targetPage = shaderPool.size();
					if(targetPage != currentPageIndex) switchToShader(targetPage);
					else currentPage.setText(Integer.toString(currentPageIndex));
				}
				catch(Exception exception)
				{
					currentPage.setText(Integer.toString(currentPageIndex));
				}
			}
		});
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
				shaderTitle.add("Untitled");
				shaderPool.add(currentPageIndex + 1 - 1, "");
				shaderType.add(currentPageIndex + 1 - 1, 0);
				shaderSelect.add(currentPageIndex + 1 - 1, new Object());
				switchToShader(currentPageIndex + 1);
			}
		});
		functionPanel.add(this.newPage);
		
		this.importPage = new JButton("Import");
		this.importPage.setSize(85, 25);
		this.importPage.setLocation(70, 0);
		this.importPage.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				editorFrame.setEnabled(false);
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
						return "Shader";
					}					
				};
				openFile.setFileFilter(filter);
				
				int state = openFile.showOpenDialog(editorFrame);
				if(state == JFileChooser.APPROVE_OPTION)
				{
					File file = openFile.getSelectedFile();
					try
					{
						BufferedReader br = new BufferedReader(new FileReader(file));
						StringBuilder input = new StringBuilder();
						String scanner = null;
						boolean isFirstLine = true;
						while((scanner = br.readLine()) != null)
						{
							if(!isFirstLine) input.append('\n');
							else isFirstLine = false;
							input.append(scanner);
						}
						br.close();
						
						String filename = file.getName();
						EnumShaderType shaderType = EnumShaderType.VERTEX;
						if(filename.endsWith("vert") || filename.endsWith("vsd")) shaderType = EnumShaderType.VERTEX;
						if(filename.endsWith("frag") || filename.endsWith("fsd")) shaderType = EnumShaderType.FRAGMENT;
						if(filename.endsWith("geom") || filename.endsWith("gsd")) shaderType = EnumShaderType.GEOMETRY;
						if(filename.endsWith("tcon") || filename.endsWith("tcsd")) shaderType = EnumShaderType.TESSELLATION_CONTROL;
						if(filename.endsWith("tevl") || filename.endsWith("tvsd")) shaderType = EnumShaderType.TESSELLATION_EVALUATION;
						
						GlslEditor.this.shaderTitle.add(currentPageIndex + 1 - 1, filename);
						GlslEditor.this.shaderType.add(currentPageIndex + 1 - 1, shaderType.ordinal());
						GlslEditor.this.shaderPool.add(currentPageIndex + 1 - 1, new String(input));
						GlslEditor.this.shaderSelect.add(currentPageIndex + 1 - 1, new Object());
						switchToShader(currentPageIndex + 1);
					}
					catch(Exception exception)
					{
						GlslEditor.this.shaderType.add(0);
						GlslEditor.this.shaderPool.add(currentPageIndex + 1 - 1, exception.getMessage());
						GlslEditor.this.shaderTitle.add(currentPageIndex + 1 - 1, "Invalid");
						GlslEditor.this.shaderSelect.add(currentPageIndex + 1 - 1, null);
						switchToShader(currentPageIndex + 1);
					}
				}
				editorFrame.setEnabled(true);
			}
		});
		functionPanel.add(this.importPage);
		
		this.exportPage = new JButton("Export");
		this.exportPage.setSize(85, 25);
		this.exportPage.setLocation(155, 0);
		this.exportPage.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				editorFrame.setEnabled(false);
				JFileChooser saveFile = new JFileChooser();
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
						return "Shader";
					}					
				};
				saveFile.setFileFilter(filter);
				
				while(true)
				{
					int state = saveFile.showSaveDialog(editorFrame);
					if(state == JFileChooser.APPROVE_OPTION)
					{
						File file = saveFile.getSelectedFile();
						if(file.exists())
						{
							int overwriteState = JOptionPane.showConfirmDialog(saveFile, "File \"" + file.getName() + "\" already exists, should overwrite?");
							if(overwriteState != JOptionPane.OK_OPTION) continue;
						}
						
						try
						{
							if(!file.exists()) file.createNewFile();
							PrintStream bw = new PrintStream(new FileOutputStream(file));
							bw.print(editingArea.getText());
							bw.close();
						}
						catch(Exception exception)
						{
							
						}
						break;
					}
					else break;
				}
				
				editorFrame.setEnabled(true);
			}
		});
		functionPanel.add(this.exportPage);
		
		this.deletePage = new JButton("Delete");
		this.deletePage.setSize(85, 25);
		this.deletePage.setLocation(240, 0);
		this.deletePage.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int pageToDelete = currentPageIndex;
				if(pageToDelete > 1)
				{
					switchToShader(currentPageIndex - 1);
					shaderTitle.remove(pageToDelete - 1);
					shaderPool.remove(pageToDelete - 1);
					shaderType.remove(pageToDelete - 1);
					shaderSelect.remove(pageToDelete - 1);
					synchronizeState();
				}
				else
				{
					switchToShader(2);
					shaderTitle.remove(0);
					shaderPool.remove(0);
					shaderType.remove(0);
					shaderSelect.remove(0);
					currentPageIndex = 1;
					synchronizeState();
				}
			}
		});
		functionPanel.add(this.deletePage);
		
		this.runShade = new JButton("Run");
		this.runShade.setSize(64, 25);
		this.runShade.setLocation(325, 0);
		this.runShade.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				switchToShader(currentPageIndex);
				
				DefaultTableModel model = new DefaultTableModel()
				{
					@Override
					public boolean isCellEditable(int row, int column)
					{
						return false;
					}
				};
				
				model.addColumn(" ");
				model.addColumn("Page");
				model.addColumn("Title");
				model.addColumn("Type");
				
				for(int i = 0; i < shaderPool.size(); i ++)
					model.addRow(new Object[]{shaderSelect.get(i), i + 1, shaderTitle.get(i), shaderTypeCombo.getItemAt(shaderType.get(i))});
				assembleList.setModel(model);
				
				assembleList.getColumnModel().getColumn(0).setPreferredWidth(25);
				assembleList.getColumnModel().getColumn(1).setPreferredWidth(65);
				assembleList.getColumnModel().getColumn(2).setPreferredWidth(205);
				assembleList.getColumnModel().getColumn(3).setPreferredWidth(65);
				
				assembler.setVisible(true);
			}
		});
		functionPanel.add(this.runShade);
		
		this.assembler = new Frame()
		{
			public void setVisible(boolean v)
			{
				super.setVisible(v);
				editorFrame.setEnabled(!v);
			}
		};
		this.assembler.setSize(400, 480);
		this.assembler.setTitle("Assembler");
		this.assembler.setLocation(this.editorFrame.getLocation().x, this.editorFrame.getLocation().y);
		this.assembler.setResizable(false);
		this.assembler.setLayout(null);
		this.assembler.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				assembler.setVisible(false);
			}
		});
		
		JScrollPane listPanel = new JScrollPane(this.assembleList);
		listPanel.setSize(390, 430);
		listPanel.setLocation(5, 25);
		this.assembler.add(listPanel);
		
		this.assembleList.setDefaultRenderer(Object.class, new TableCellRenderer()
		{
			TableCellRenderer parent = assembleList.getDefaultRenderer(Object.class);
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				if(column > 0) return parent.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				else
				{
					JCheckBox chk = new JCheckBox();
					chk.setSelected(value != null);
					return chk;
				}
			}
		});
		
		this.assembleList.getTableHeader().setReorderingAllowed(false);
		this.assembleList.getTableHeader().setResizingAllowed(false);
		this.assembleList.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent me)
			{
				if(assembleList.columnAtPoint(me.getPoint()) == 0)
				{
					int row = assembleList.rowAtPoint(me.getPoint());
					Object select = shaderSelect.get(row);
					shaderSelect.set(row, select == null? new Object() : null);
					assembleList.setValueAt(shaderSelect.get(row), row, 0);
				}
			}
		});
		
		this.selectDeselectAll = new JButton();
		this.selectDeselectAll.setText("All / None");
		this.selectDeselectAll.setSize(120, 25);
		this.selectDeselectAll.setLocation(5, listPanel.getLocation().y + listPanel.getHeight() - 1);
		this.selectDeselectAll.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				boolean allSelected = true;
				for(Object selected : shaderSelect) if(selected == null)
				{
					allSelected = false;
					break;
				}
				
				for(int i = 0; i < shaderSelect.size(); i ++)
				{
					if(allSelected) shaderSelect.set(i, null);	//De-select All
					else shaderSelect.set(i, new Object());	//Select All
					
					assembleList.setValueAt(shaderSelect.get(i), i, 0);
				}
			}
		});
		this.assembler.add(this.selectDeselectAll);
		this.assembleList.setDoubleBuffered(true);
		
		this.execute = new JButton();
		this.execute.setText("Execute");
		this.execute.setSize(120, 25);
		this.execute.setLocation(this.assembler.getWidth() - 6 - 120, listPanel.getLocation().y + listPanel.getHeight() - 1);
		this.execute.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				ArrayList<Shader> shaders = new ArrayList<Shader>();
				EnumShaderType[] shaderTypes = EnumShaderType.values();
				for(int i = 0; i < shaderPool.size(); i ++) if(shaderSelect.get(i) != null)
				{
					Shader shader = new Shader(shaderPool.get(i), shaderTypes[shaderType.get(i)]);
					shaders.add(shader);
				}
				if(shaders.size() <= 0) flushCurrentShader = true;
				else
				{
					GlslEditor.this.shaders = shaders.toArray(new Shader[0]);
					shaderProgram = new ShaderProgram(GlslEditor.this.shaders);
				}
			}
		});
		this.assembler.add(this.execute);
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
		this.title.setFont(font);
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
		
		this.selectDeselectAll.setFont(font);
		this.execute.setFont(font);
		
		this.assembleList.getTableHeader().setFont(font);
		this.assembleList.setFont(font);
		
		this.editorFrame.revalidate();
	}
	
	public void synchronizeState()
	{
		this.currentPage.setText(Integer.toString(currentPageIndex));
		this.totalPage.setText(Integer.toString(this.shaderPool.size()));
		this.deletePage.setEnabled(this.shaderPool.size() > 1);
		
		this.previous.setEnabled(this.currentPageIndex > 1);
		this.first.setEnabled(this.currentPageIndex > 1);
		this.next.setEnabled(this.currentPageIndex < this.shaderPool.size());
		this.last.setEnabled(this.currentPageIndex < this.shaderPool.size());
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
		shaderTitle.set(this.currentPageIndex - 1, this.title.getText());
		
		this.editingArea.setText(this.shaderPool.get(page - 1));
		this.title.setText(this.shaderTitle.get(page - 1));
		this.shaderTypeCombo.setSelectedIndex(this.shaderType.get(page - 1));
		this.currentPageIndex = page;

		this.syntaxhighlighter.fullSyntaxHighlight(this.editingArea.getDocument());
		this.synchronizeState();
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