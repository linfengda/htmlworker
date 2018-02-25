package com.itextpdf.tool.xml.css;

import java.util.Map;

import com.itextpdf.tool.xml.Tag;

/**
 * 表示当前HTML文档引入的CSS文件列表
 * 
 * @author 玄葬
 */
public interface CssFiles {
	
	/**
	 * 查找所有会渲染当前Tag的样式
	 * @param t the tag to check for.
	 * @return a map with property as key and the value as value of the property
	 */
	Map<String, String> getCSS(Tag t);
	/**
	 * 添加一个CSS文件
	 * @param css the CssFile to add.
	 */
	void add(CssFile css);
	/**
	 * 清空CSS文件列表
	 */
	void clear();
	/**
	 * CSS文件列表是否为空
	 * @return true if there are files with rules in this CssFiles collector
	 */
	boolean hasFiles();

}
