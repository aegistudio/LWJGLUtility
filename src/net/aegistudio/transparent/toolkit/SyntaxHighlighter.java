package net.aegistudio.transparent.toolkit;

import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SyntaxHighlighter implements DocumentListener
{
	TreeMap<String, Style> keywordSchemes = new TreeMap<String, Style>();
	Style normalColor;
	
	public SyntaxHighlighter(Document theDocument, Color normalColor, KeywordScheme[] schemes)
	{
		this.normalColor = ((StyledDocument)theDocument).addStyle("Keyword_Style", null);
		StyleConstants.setForeground(this.normalColor, normalColor);
		for(KeywordScheme scheme : schemes)
		{
			Style theStyle = ((StyledDocument)theDocument).addStyle("Keyword_Style", null);
			StyleConstants.setForeground(theStyle, scheme.keywordColor);
			for(String keyword : scheme.keywords)
				keywordSchemes.put(keyword, theStyle);
		}
	}
	
	@Override
	public void changedUpdate(DocumentEvent arg0)
	{
		
	}

	public void keywordHighlight(Document document, int offset)
	{
		try
		{
			StyledDocument doc = (StyledDocument) document;
			StringBuilder builder = new StringBuilder();
			
			int beginPos = offset;
			for(; beginPos >=0; beginPos --)
			{
				char element = doc.getText(beginPos, 1).charAt(0);
				if(isAvailableChar(element)) builder.append(element);
				else break;
			}
			builder.reverse();
			
			int endPos = offset + 1;
			for(; endPos < doc.getLength(); endPos ++)
			{
				char element = doc.getText(endPos, 1).charAt(0);
				if(isAvailableChar(element)) builder.append(element);
				else break;
			}
			
			Style theColor = this.keywordSchemes.get(new String(builder));
			if(theColor == null) theColor = this.normalColor;
			SwingUtilities.invokeLater(new ColoringTask(document, beginPos, endPos - beginPos, theColor));
		}
		catch(Exception e)
		{
			
		}
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
	
	public void fullSyntaxHighlight(Document document)
	{
		try
		{
			String content = document.getText(0, document.getLength());
			char[] contentArray = content.toCharArray();
			boolean shouldMark = true;
			ArrayList<Integer> tokens = new ArrayList<Integer>();
			
			for(int i = 0; i < contentArray.length; i ++)
			{
				if(shouldMark)
				{
					if(this.isAvailableChar(contentArray[i]))
					{
						shouldMark = false;
						tokens.add(i);
					}
					else continue;
				}
				else
				{
					if(!this.isAvailableChar(contentArray[i]))
						shouldMark = true;
					else continue;
				}
			}
			for(Integer token : tokens)
				keywordHighlight(document, token);
		}
		catch(Exception exception)
		{
			
		}
	}
	
	public boolean isAvailableChar(char current)
	{
		return 	  ('a' <= current && current <= 'z')
				||('A' <= current && current <= 'Z')
				||('0' <= current && current <= '9')
				|| '_' == current;
	}
	
	int partialUpdateCounter = 0;
	
	@Override
	public void insertUpdate(DocumentEvent arg0)
	{
		if(partialUpdateCounter < 10)
		{
			this.keywordHighlight(arg0.getDocument(), arg0.getOffset());
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
			this.keywordHighlight(arg0.getDocument(), arg0.getOffset() - 1);
			partialUpdateCounter ++;
		}
		else
		{
			this.fullSyntaxHighlight(arg0.getDocument());
			partialUpdateCounter = 0;
		}
	}
}
