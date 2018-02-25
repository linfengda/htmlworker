package com.itextpdf.tool.xml.parser.state;

import com.itextpdf.tool.xml.parser.HTMLParser;
import com.itextpdf.tool.xml.parser.State;

public class TagEncounteredState implements State {
	
	private final HTMLParser parser;
	
	/**
	 * @param parser the HTMLParser
	 */
	public TagEncounteredState(final HTMLParser parser) {
		this.parser = parser;
	}

	@Override
	public void process(final char character) {
		String tag = this.parser.memory().textBuffString();
		if (Character.isWhitespace(character) || character == '>' || character == '/' || character == ':' || character == '?' || tag.equals("!--")) {
			if (tag.length() > 0) {
				if (tag.equals("!DOCTYPE")) {						//<!DOCTYPE html>
					this.parser.memory().resetTextBuffer();
					this.parser.memory().append(character);
					this.parser.stateController().doctype();
				}
				else if (tag.equals("!--")) {						//<!-- this is a comment -->
					this.parser.memory().resetTextBuffer();
					this.parser.memory().resetCommentBuff();
					this.parser.stateController().comment();
					/**
					 * 避免'<!---->'这样的注释出错
					 * 详见CommentState和CloseCommentState
					 */
					if (character == '-') {
						this.parser.memory().commentBuff().append(character);
					} else {
						this.parser.memory().append(character);
					}
				}
				else if (Character.isWhitespace(character)) {		//<p style="font-size: 14px;">
					this.parser.memory().setCurrentTag(tag);
					this.parser.memory().resetTextBuffer();
					this.parser.stateController().tagAttributes();
				}
				else if (character == '>') {
					this.parser.memory().setCurrentTag(tag);
					this.parser.memory().resetTextBuffer();
					this.parser.startElement();
					this.parser.stateController().inTag();
				}
				else if (character == '/') {
					this.parser.memory().setCurrentTag(tag);
					this.parser.memory().resetTextBuffer();
					this.parser.stateController().selfClosing();
				}
				else if (character == ':') {
					this.parser.memory().setCurrentNameSpace(tag);
					this.parser.memory().resetTextBuffer();
				}
				
			} else {
				if (character == '/') {								//</div
					this.parser.stateController().closingTag();
				}
				else if (character == '?') {						//<? xml
					this.parser.memory().append(character);
                    this.parser.stateController().processingInstructions();
				}
			}
		} else {
			this.parser.memory().append(character);					//<div
		}
	}

}
