/*
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2016 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with this program; if not, see http://www.gnu.org/licenses or
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a
 * covered work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.tool.xml.css;

import java.util.Arrays;
import java.util.List;

import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.HTML;

/**
 * CSS样式继承规则
 * 
 * @author 玄葬
 *
 */
public class DefaultCssInheritanceRules implements CssInheritanceRules {

	/**  除非声明inherit，否则不会从父类元素自动继承   **/
	private static final List<String> GLOBAL = Arrays.asList(new String[] {
			CSS.Property.WIDTH, CSS.Property.HEIGHT,
			CSS.Property.MIN_WIDTH, CSS.Property.MAX_WIDTH,
			CSS.Property.MIN_HEIGHT, CSS.Property.MAX_HEIGHT,
			CSS.Property.MARGIN,
			CSS.Property.MARGIN_LEFT, CSS.Property.MARGIN_RIGHT,
			CSS.Property.MARGIN_TOP, CSS.Property.MARGIN_BOTTOM,
			CSS.Property.PADDING,
			CSS.Property.PADDING_LEFT, CSS.Property.PADDING_RIGHT,
			CSS.Property.PADDING_TOP, CSS.Property.PADDING_BOTTOM,
			CSS.Property.BORDER_TOP_WIDTH, CSS.Property.BORDER_TOP_STYLE, CSS.Property.BORDER_TOP_COLOR,
			CSS.Property.BORDER_BOTTOM_WIDTH,CSS.Property.BORDER_BOTTOM_STYLE, CSS.Property.BORDER_BOTTOM_COLOR,
			CSS.Property.BORDER_LEFT_WIDTH,	CSS.Property.BORDER_LEFT_STYLE,	CSS.Property.BORDER_LEFT_COLOR,
			CSS.Property.BORDER_RIGHT_WIDTH, CSS.Property.BORDER_RIGHT_STYLE,CSS.Property.BORDER_RIGHT_COLOR,
			CSS.Property.PAGE_BREAK_BEFORE, CSS.Property.PAGE_BREAK_AFTER,
			CSS.Property.LEFT, CSS.Property.TOP,CSS.Property.RIGHT,CSS.Property.BOTTOM,
			CSS.Property.POSITION });
	
	/**  除非声明inherit，否则不会从父类元素自动继承的文本属性   **/
	private static final List<String> TEXT_CSS = Arrays.asList(new String[] {
			CSS.Property.VERTICAL_ALIGN, CSS.Property.TEXT_DECORATION,
			CSS.Property.TEXT_SHADOW, CSS.Property.WHITE_SPACE,
			CSS.Property.UNICODE_BIDI });
	
	/**  除非声明inherit，否则不会从父类元素自动继承的表格属性   **/
	private static final List<String> TABLE_CSS = Arrays.asList(new String[] { 
			CSS.Property.LINE_HEIGHT, CSS.Property.FONT_SIZE,
			CSS.Property.FONT_STYLE, CSS.Property.FONT_WEIGHT,
			CSS.Property.TEXT_INDENT,
            CSS.Property.CELLPADDING, CSS.Property.CELLPADDING_LEFT, CSS.Property.CELLPADDING_TOP,
            CSS.Property.CELLPADDING_RIGHT, CSS.Property.CELLPADDING_BOTTOM, CSS.Property.DIRECTION});
	
	/**  除非声明inherit，否则不会从父类元素自动继承的表格TR属性   **/
	private static final List<String> ROW_CSS = Arrays.asList(new String[] { "background-color", CSS.Property.DIRECTION });
	
	/**  除非声明inherit，否则不会从父类元素自动继承的DIV属性   **/
    private static final List<String> DIV_CSS = Arrays.asList(new String[] { CSS.Property.BACKGROUND , CSS.Property.BACKGROUND_COLOR, CSS.Property.FLOAT});
	
    /**  除非声明inherit，否则不会从父类元素自动继承的表格TD属性   **/
	private static final List<String> TD_CSS = Arrays.asList(new String[] { "vertical-align" });
	
	
	@Override
	public boolean isAutoInheritCss(final Tag tag, final String key) {
		if (GLOBAL.contains(key)) {
			return false;
		}
		
		if (HTML.Tag.INPUT.equals(tag.getName()) || HTML.Tag.TEXTAREA.equals(tag.getName())) {
			return !TEXT_CSS.contains(key);
		}
		if (HTML.Tag.TABLE.equals(tag.getName())) {
			return !TABLE_CSS.contains(key);
		}
		if (HTML.Tag.TABLE.equals(tag.getParent().getName())) {
			return !ROW_CSS.contains(key);
		}
		if (HTML.Tag.TD.equalsIgnoreCase(tag.getParent().getName())) {
			return !TD_CSS.contains(key);
		}
        if (HTML.Tag.DIV.equalsIgnoreCase(tag.getParent().getName())) {
			return !DIV_CSS.contains(key);
		}

		return true;
	}

}
