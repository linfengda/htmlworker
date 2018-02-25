package com.itextpdf.tool.xml.parser;

import java.util.Map;

/**
 * <p>观察者设计模式<p>
 * <p>监听{@link HTMLLifeCycle}的生命周期事件<p>
 * 
 * @author 玄葬
 *
 */
public interface HTMLLifeCycleListener {
	
	/**
	 * 标签开始----->pipeline open方法----->生成PDF
	 */
	void startElement(String tag, Map<String, String> attributes, String ns);

	/**
	 * 标签结束----->pipeline close方法----->生成PDF
	 */
	void endElement(String tag, String ns);
	
	/**
	 * 标签内容----->pipeline content方法----->生成PDF
	 */
	void text(String text);

	/**
	 * 标签外内容
	 */
	void unknownText(String text);

	/**
	 * 注释
	 */
	void comment(String comment);

	/**
	 * 解析开始
	 */
	void init();

	/**
	 * 解析结束
	 */
	void close();

	
	
	
	

}
