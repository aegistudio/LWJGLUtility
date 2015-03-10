package net.aegistudio.transparent.toolkit;

import java.util.TreeMap;

import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class KeywordHighlight implements SyntaxHighlightAlgorithm
{
	TreeMap<String, Style> keywordSchemes = new TreeMap<String, Style>();
	
	public KeywordHighlight(Document theDocument, KeywordScheme[] schemes)
	{
		for(KeywordScheme scheme : schemes)
		{
			Style theStyle = ((StyledDocument)theDocument).addStyle("Keyword_Style", null);
			StyleConstants.setForeground(theStyle, scheme.keywordColor);
			for(String keyword : scheme.keywords)
				keywordSchemes.put(keyword, theStyle);
		}
	}
	
	
	@Override
	public void highlightActivate(SyntaxHighlighter highlight, Document document, int offset)
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
			if(theColor == null) theColor = highlight.getNormalStyle();
			highlight.createHighlight(document, beginPos, endPos, theColor);
		}
		catch(Exception e)
		{
			
		}
	}

	public boolean isAvailableChar(char current)
	{
		return 	  ('a' <= current && current <= 'z')
				||('A' <= current && current <= 'Z')
				||('0' <= current && current <= '9')
				|| '_' == current || '#' == current;
	}
	
	@Override
	public void highlightThroughout(SyntaxHighlighter highlight, Document document)
	{
		try
		{
			String content = document.getText(0, document.getLength());
			char[] contentArray = content.toCharArray();
			boolean shouldMark = true;
			
			for(int i = 0; i < contentArray.length; i ++)
			{
				if(shouldMark)
				{
					if(this.isAvailableChar(contentArray[i]))
					{
						shouldMark = false;
						this.highlightActivate(highlight, document, i);
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
		}
		catch(Exception exception)
		{
			
		}
	}
}
