package com.itextpdf.tool.xml.parser;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 缓存解析到的HTML符号
 * 
 * @author 玄葬
 *
 */
public class HTMLParserMemory {
	
	private String currentTag;
	private String currentAttr;
	private final Map<String, String> attr = new LinkedHashMap<String, String>();
	private final StringBuilder textBuff = new StringBuilder();
	private final StringBuilder commentBuff = new StringBuilder();
	private final StringBuilder entityBuff = new StringBuilder();
	private String wsTag = "";
	private String currentNameSpace = "";
	private char lastChar;
	
	private String temp1 = "";
	
	
	
	public String getCurrentTag() {
		return currentTag;
	}
	public void setCurrentTag(String currentTag) {
		this.currentTag = currentTag;
		this.wsTag = currentTag;
		this.attr.clear();
	}
	public String getCurrentAttr() {
		return currentAttr;
	}
	public void setCurrentAttr(String currentAttr) {
		this.currentAttr = currentAttr;
	}
	public String getWsTag() {
		return wsTag;
	}
	public void setWsTag(String wsTag) {
		this.wsTag = wsTag;
	}
	public String getCurrentNameSpace() {
		return currentNameSpace;
	}
	public void setCurrentNameSpace(String currentNameSpace) {
		this.currentNameSpace = currentNameSpace;
	}
	public char getLastChar() {
		return lastChar;
	}
	public void setLastChar(char lastChar) {
		this.lastChar = lastChar;
	}
	public String getTemp1() {
		return temp1;
	}
	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}
	
	
	
	
	
	
	
	public Map<String, String> attr() {
		return new LinkedHashMap<String, String>(this.attr);
	}
	
	public StringBuilder textBuff() {
		return this.textBuff;
	}
	
	public StringBuilder commentBuff() {
		return this.commentBuff;
	}
	
	public StringBuilder entityBuff() {
		return this.entityBuff;
	}
    
    public String textBuffString() {
		return this.textBuff.toString();
	}
    
    public String commentBuffString() {
		return this.commentBuff.toString();
	}
    
    public String entityBuffString() {
		return this.entityBuff.toString();
	}
    
    public HTMLParserMemory append(final char character) {
        this.textBuff.append(character);
        return this;
    }
    
    public HTMLParserMemory append(final char[] bytes) {
        this.textBuff.append(bytes);
        return this;
    }
    
    public HTMLParserMemory append(final String string) {
        this.textBuff.append(string);
        return this;
    }
    
	public void resetTextBuffer() {
		this.textBuff.setLength(0);
	}
	
	public void resetCommentBuff() {
		this.commentBuff.setLength(0);
	}
	
	public void resetEntityBuff() {
		this.entityBuff.setLength(0);
	}
	
    public int textBufferSize() {
        return (null != this.textBuff) ? this.textBuff.length() : 0;
    }
    
    public int commentBuffSize() {
        return (null != this.commentBuff) ? this.commentBuff.length() : 0;
    }
    
    public int entityBuffSize() {
        return (null != this.entityBuff) ? this.entityBuff.length() : 0;
    }
    
	public boolean hasCurrentAttribute() {
		return null != this.currentAttr;
	}
	
	public void putCurrentAttrValue(final String content) {
		if (null != this.currentAttr) {
			attr.put(this.currentAttr.toLowerCase(), content);
			this.currentAttr = null;
		}
	}
	

}
