package net.aegistudio.transparent.toolkit;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SyntaxHighlighter implements DocumentListener
{
	Style normalColor;
	SyntaxHighlightAlgorithm[] algorithms;
	
	public SyntaxHighlighter(Document theDocument, Color normalColor, SyntaxHighlightAlgorithm[] algorithms)
	{
		this.normalColor = ((StyledDocument)theDocument).addStyle("Keyword_Style", null);
		StyleConstants.setForeground(this.normalColor, normalColor);
		this.algorithms = algorithms;
	}
	
	@Override
	public void changedUpdate(DocumentEvent arg0)
	{
		
	}
	
	public Style getNormalStyle()
	{
		return this.normalColor;
	}
	
	public void createHighlight(Document document, int beginPos, int endPos, Style style)
	{
		SwingUtilities.invokeLater(new ColoringTask(document, beginPos, endPos - beginPos, style));
	}
	
	class ColoringTask implements Runnable
	{
		Document doc;
		int offset;
		int length;
		Style style;
		
		public ColoringTask(Document doc, int offset, int length, Style style)
		{
			this.doc = doc; this.offset = offset; this.length = length; this.style = style;
		}
		
		@Override
		public void run()
		{
			((StyledDocument)doc).setCharacterAttributes(offset, length, style, true);
		}
	}
	
	public void syntaxHighlight(Document document, int offset)
	{
		for(SyntaxHighlightAlgorithm algorithm : algorithms) algorithm.highlightActivate(this, document, offset);
	}
	
	public void fullSyntaxHighlight(Document document)
	{
		for(SyntaxHighlightAlgorithm algorithm : algorithms) algorithm.highlightThroughout(this, document);
	}
	
	int partialUpdateCounter = 0;
	
	@Override
	public void insertUpdate(DocumentEvent arg0)
	{
		if(partialUpdateCounter < 10)
		{
			this.syntaxHighlight(arg0.getDocument(), arg0.getOffset());
			partialUpdateCounter ++;
		}
		else
		{
			this.fullSyntaxHighlight(arg0.getDocument());
			partialUpdateCounter = 0;
		}
	}
	
	@Override
	public void removeUpdate(DocumentEvent arg0)
	{
		if(partialUpdateCounter < 10)
		{
			this.syntaxHighlight(arg0.getDocument(), arg0.getOffset() - 1);
			partialUpdateCounter ++;
		}
		else
		{
			this.fullSyntaxHighlight(arg0.getDocument());
			partialUpdateCounter = 0;
		}
	}
}
