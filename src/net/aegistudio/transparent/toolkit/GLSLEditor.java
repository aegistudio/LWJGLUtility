package net.aegistudio.transparent.toolkit;

import net.aegistudio.transparent.opengl.Container;
import net.aegistudio.transparent.opengl.glsl.ShaderProgram;

public class GLSLEditor
{
	ModelViewer modelviewer;
	ShaderProgram shaderProgram = null;
	boolean shouldInit = false; boolean shouldDestroy = false;
	
	public GLSLEditor() throws Exception
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
	}
	
	public static void main(String[] arguments) throws Exception
	{
		GLSLEditor glsleditor = new GLSLEditor();
		glsleditor.modelviewer.getFrame().setVisible(true);
	}
}