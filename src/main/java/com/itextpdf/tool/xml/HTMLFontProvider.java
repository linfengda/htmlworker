/*
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2016 iText Group NV
 * Authors: Bruno Lowagie, et al.
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
 package com.itextpdf.tool.xml;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactoryImp;
import com.itextpdf.text.pdf.BaseFont;

/**
 * 修改HTML字体提供类，提供微软所有字体。
 * 
 * @author 玄葬
 */
public class HTMLFontProvider extends FontFactoryImp {
	private static final String FONTPATH = "/msfonts/";
    private String fontname;

    public HTMLFontProvider() {
        this(null);
    }

    /**
     * 构造一个字体提供类
     * @param fontname	默认字体
     */
	public HTMLFontProvider(String fontname) {
		super.registerDirectories();
		this.fontname = fontname;
	}

	public String getFontName() {
		return fontname;
	}

	public void setFontName(String fontName) {
		this.fontname = fontName;
	}

	@Override
    public Font getFont(final String fontname, final String encoding, final boolean embedded, final float size, final int style, final BaseColor color) {
		BaseFont basefont = null;
		try {
			if (fontname == null && this.fontname == null) {
				return new Font(Font.FontFamily.UNDEFINED, size, style, color); // 默认的英文字体
			}else{
				String name = (fontname != null ? fontname : this.fontname);
				basefont = getBaseFont(FONTPATH + name + ".ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, true);
				if (basefont == null) {
					return new Font(Font.FontFamily.UNDEFINED, size, style, color); // 默认的英文字体
				}
			}
		} catch (Exception e) {
			return new Font(Font.FontFamily.UNDEFINED, size, style, color); // 默认的英文字体
		}
		return new Font(basefont, size, style, color);
    }


}
