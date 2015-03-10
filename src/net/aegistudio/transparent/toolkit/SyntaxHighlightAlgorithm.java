package net.aegistudio.transparent.toolkit;

import javax.swing.text.Document;

public interface SyntaxHighlightAlgorithm
{
	public void highlightActivate(SyntaxHighlighter highlight, Document document, int offset);
	
	public void highlightThroughout(SyntaxHighlighter highlight, Document document);
}
