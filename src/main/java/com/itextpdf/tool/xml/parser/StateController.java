package com.itextpdf.tool.xml.parser;

import com.itextpdf.tool.xml.parser.state.AttributeValueState;
import com.itextpdf.tool.xml.parser.state.CloseCommentState;
import com.itextpdf.tool.xml.parser.state.CloseStarCommentState;
import com.itextpdf.tool.xml.parser.state.ClosingTagState;
import com.itextpdf.tool.xml.parser.state.CommentState;
import com.itextpdf.tool.xml.parser.state.DocTypeState;
import com.itextpdf.tool.xml.parser.state.DoubleQuotedAttrValueState;
import com.itextpdf.tool.xml.parser.state.InsideTagHTMLState;
import com.itextpdf.tool.xml.parser.state.ProcessingInstructionEncounteredState;
import com.itextpdf.tool.xml.parser.state.SelfClosingTagState;
import com.itextpdf.tool.xml.parser.state.SingleQuotedAttrValueState;
import com.itextpdf.tool.xml.parser.state.SpecialCharState;
import com.itextpdf.tool.xml.parser.state.StarCommentState;
import com.itextpdf.tool.xml.parser.state.TagAttributeState;
import com.itextpdf.tool.xml.parser.state.TagEncounteredState;
import com.itextpdf.tool.xml.parser.state.UnknownState;
import com.itextpdf.tool.xml.parser.state.UnquotedAttrState;

/**
 * <p>状态机设计模式<p>
 * <p>HTML解析过程中所有可能出现符号状态<p>
 * 
 * @author 玄葬
 *
 */
public class StateController {
	
	public final HTMLParser parser;
	public final State unknown;
	public final State tagEncountered;
	public final State tagAttributes;
	public final State inTag;
	public final State attrValue;
	public final State singleQuoted;
	public final State doubleQuoted;
	public final State selfClosing;
	public final State specialChar;
	public final State closingTag;
	public final State comment;
	public final State closeComment;
	public final State doctype;
	public final State unquoted;
    public final State processingInstruction;
	public final State starComment;
	public final State closeStarComment;
	
	
	/**
	 * 初始化所有状态
	 * @param parser the Parser
	 */
	public StateController(final HTMLParser parser) {
		this.parser = parser;
		unknown = new UnknownState(parser);
		tagEncountered = new TagEncounteredState(parser);
		tagAttributes = new TagAttributeState(parser);
		inTag = new InsideTagHTMLState(parser);
		attrValue = new AttributeValueState(parser);
		singleQuoted = new SingleQuotedAttrValueState(parser);
		doubleQuoted = new DoubleQuotedAttrValueState(parser);
		selfClosing = new SelfClosingTagState(parser);
		specialChar = new SpecialCharState(parser);
		closingTag = new ClosingTagState(parser);
		comment = new CommentState(parser);
		closeComment = new CloseCommentState(parser);
		doctype = new DocTypeState(parser);
		unquoted = new UnquotedAttrState(parser);
        processingInstruction = new ProcessingInstructionEncounteredState(parser);
		starComment = new StarCommentState(parser);
		closeStarComment = new CloseStarCommentState(parser);
	}
	
	
	/**
	 * set Parser state to {@link UnknownState}.
	 * @return Parser
	 */
	public HTMLParser unknown() {
		parser.setState(unknown);
		return parser;
	}

	/**
	 * set Parser state to {@link TagEncounteredState}.
	 * @return Parser
	 */
	public HTMLParser tagEncountered() {
		parser.setState(tagEncountered);
		return parser;
	}

	/**
	 * set Parser state to {@link TagAttributeState}.
	 * @return Parser
	 */
	public HTMLParser tagAttributes() {
		parser.setState(tagAttributes);
		return parser;
	}

	/**
	 * set Parser state to {@link InsideTagHTMLState}.
	 * @return Parser
	 */
	public HTMLParser inTag() {
		parser.setState(inTag);
		return parser;
	}

	/**
	 * set Parser state to {@link AttributeValueState}.
	 * @return Parser
	 */
	public HTMLParser attributeValue() {
		parser.setState(attrValue);
		return parser;
	}

	/**
	 * set Parser state to {@link SingleQuotedAttrValueState}.
	 * @return Parser
	 */
	public HTMLParser singleQuotedAttr() {
		parser.setState(singleQuoted);
		return parser;
	}

	/**
	 * set Parser state to {@link DoubleQuotedAttrValueState}.
	 * @return Parser
	 */
	public HTMLParser doubleQuotedAttr() {
		parser.setState(doubleQuoted);
		return parser;
	}

    /**
	 * set Parser state to {@link ProcessingInstructionEncounteredState}.
	 * @return Parser
	 */
    public HTMLParser processingInstructions() {
        parser.setState(processingInstruction);
		return parser;
    }

	/**
	 * set Parser state to {@link SelfClosingTagState}.
	 * @return Parser
	 */
	public HTMLParser selfClosing() {
		parser.setState(selfClosing);
		return parser;
	}

	/**
	 *set Parser state to {@link SpecialCharState}.
	 * @return Parser
	 */
	public HTMLParser specialChar() {
		parser.setState(this.specialChar);
		return parser;
	}

	/**
	 * set Parser state to {@link ClosingTagState}.
	 * @return Parser
	 */
	public HTMLParser closingTag() {
		parser.setState(this.closingTag);
		return parser;
	}

	/**
	 * set Parser state to {@link CommentState}.
	 * @return Parser
	 */
	public HTMLParser comment() {
		parser.setState(this.comment);
		return parser;
	}

	/**
	 * set Parser state to {@link CloseCommentState}.
	 * @return Parser
	 */
	public HTMLParser closeComment() {
		parser.setState(closeComment);
		return parser;
	}

	/**
	 * set Parser state to {@link DocTypeState}.
	 * @return Parser
	 */
	public HTMLParser doctype() {
		parser.setState(doctype);
		return parser;
	}
	/**
	 * set Parser state to {@link UnquotedAttrState}.
	 * @return Parser
	 *
	 */
	public HTMLParser unquotedAttr() {
		parser.setState(unquoted);
		return parser;

	}

	/**
	 * set Parser state to {@link StarCommentState}.
	 * @return Parser
	 */
	public HTMLParser starComment() {
		parser.setState(this.starComment);
		return parser;
	}

	/**
	 * set Parser state to {@link CloseStarCommentState}.
	 * @return Parser
	 */
	public HTMLParser closeStarComment() {
		parser.setState(this.closeStarComment);
		return parser;
	}

}
