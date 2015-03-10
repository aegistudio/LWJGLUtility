package net.aegistudio.transparent.toolkit;

import java.awt.Color;

import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MultilineCommentHighlight implements SyntaxHighlightAlgorithm
{
	Style theStyle;
	public MultilineCommentHighlight(Document theDocument, Color commentColor)
	{
		theStyle = ((StyledDocument)theDocument).addStyle("Keyword_Style", null);
		StyleConstants.setForeground(theStyle, commentColor);
	}
	
	@Override
	public void highlightActivate(SyntaxHighlighter highlight, Document document, int offset)
	{
		try
		{
			StyledDocument doc = (StyledDocument) document;
			
			int beginPos = offset;
			for(; beginPos >=0; beginPos --)
			{
				if(doc.getText(beginPos, 2).equals("/*")) break;
			}
			if(beginPos < 0) return;
			
			int endPos = offset + 1;
			for(; endPos < doc.getLength(); endPos ++)
			{
				if(doc.getText(endPos, 2).equals("*/")) break;
			}
			
			highlight.createHighlight(document, beginPos, endPos + 1, this.theStyle);
		}
		catch(Exception e)
		{
			
		}
	}

	@Override
	public void highlightThroughout(SyntaxHighlighter highlight, Document document)
	{
		try
		{
			boolean isInsideToken = false;
			char[] string = document.getText(0, document.getLength()).toCharArray();
			for(int i = 0; i < string.length - 1; i ++)
			{
				if(isInsideToken)
				{
					if(string[i] == '*' && string[i + 1] == '/')
						isInsideToken = false;
				}
				else
				{
					if(string[i] == '/' && string[i + 1] == '*')
					{
						this.highlightActivate(highlight, document, i + 1);
						isInsideToken = true;
					}
				}
			}
		}
		catch(Exception e)
		{
			
		}
	}
}
