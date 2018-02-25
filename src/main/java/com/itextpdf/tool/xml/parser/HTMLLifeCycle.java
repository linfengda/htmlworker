package com.itextpdf.tool.xml.parser;

/**
 * 表示解析HTML文档的生命周期
 * 
 * @author 玄葬
 *
 */
public interface HTMLLifeCycle {
	
	public HTMLLifeCycle addHTMLLifeCycleListenter(HTMLLifeCycleListener l);
	
	public HTMLLifeCycle removeHTMLLifeCycleListenter(HTMLLifeCycleListener l);
	
	/**
	 * 当在开始标签前读取到任何内容时触发
	 */
	public void unknownText();
	
	/**
	 * 当读取到开始标签时触发
	 */
	public void startElement();
	
	/**
	 * 当读取到结束标签时触发
	 */
	public void endElement();
	
	/**
	 * 当读取到注释时触发
	 */
	public void comment();
	
	

}
