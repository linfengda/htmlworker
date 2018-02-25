
package com.itextpdf.tool.xml.html;

import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.apply.MarginMemory;
import com.itextpdf.tool.xml.css.apply.PageSizeContainable;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * 渲染PDF元素接口
 *
 * @author 玄葬
 */
public interface CssApplyService {

	/**
	 * 传入元素和标签，返回渲染后的元素。
	 * 
	 * @param e the Element
	 * @param t the tag
	 * @param mm the MarginMemory
	 * @param psc the {@link PageSize} container
	 * @param ctx an HtmlPipelineContext
	 * @return the element with CSS applied onto, note: the element can be a new element.
	 */
	Element apply(Element e, final Tag t, final MarginMemory mm, final PageSizeContainable psc, final HtmlPipelineContext ctx);

	/**
	 * 传入元素和标签，返回渲染后的元素。
	 * 
	 * @param e the Element
	 * @param t the tag
	 * @param ctx an HtmlPipelineContext
	 * @return the element with CSS applied onto, note: the element can be a new element.
	 */
	Element apply(final Element e, final Tag t, final HtmlPipelineContext ctx);

	void putCssApplier(Class<?> s, CssApplier c);

	CssApplier getCssApplier(Class<?> s);
	
}
