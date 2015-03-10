package net.aegistudio.transparent.toolkit;

import java.awt.Color;

import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class LineCommentHighlight implements SyntaxHighlightAlgorithm
{
	Style theStyle;
	public LineCommentHighlight(Document theDocument, Color commentColor)
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
				char element = doc.getText(beginPos, 1).charAt(0);
				if(element == '\n' || beginPos == 0) break;
			}
			
			int endPos = offset + 1;
			for(; endPos < doc.getLength(); endPos ++)
			{
				char element = doc.getText(endPos, 1).charAt(0);
				if(element == '\n') break;
			}
			
			for(int i = beginPos; i < endPos - 1; i ++)
			{
				if(doc.getText(i, 2).equals("//"))
				{
					highlight.createHighlight(document, i, endPos, this.theStyle);
					break;
				}
			}
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
			boolean isNewLine = true;
			char[] string = document.getText(0, document.getLength()).toCharArray();
			for(int i = 0; i < string.length; i ++)
			{
				if(isNewLine)
				{
					this.highlightActivate(highlight, document, i);
					isNewLine = false;
				}
				else
				{
					if(string[i] == '\n') isNewLine = true;
				}
			}
		}
		catch(Exception e)
		{
			
		}
	}
}
