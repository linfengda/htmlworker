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
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public License along with this program; if not,
 * see http://www.gnu.org/licenses or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL: http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions of this program must display Appropriate
 * Legal Notices, as required under Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a covered work must retain the producer
 * line in every PDF that is created or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a commercial license. Buying such a license is
 * mandatory as soon as you develop commercial activities involving the iText software without disclosing the source
 * code of your own applications. These activities include: offering paid services to customers as an ASP, serving PDFs
 * on the fly in a web application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.html;

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDiv;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import com.itextpdf.tool.xml.css.apply.DivCssApplier;
import com.itextpdf.tool.xml.css.apply.HtmlCellCssApplier;
import com.itextpdf.tool.xml.css.apply.ImageCssApplier;
import com.itextpdf.tool.xml.css.apply.LineSeparatorCssApplier;
import com.itextpdf.tool.xml.css.apply.ListStyleTypeCssApplier;
import com.itextpdf.tool.xml.css.apply.MarginMemory;
import com.itextpdf.tool.xml.css.apply.NoNewLineParagraphCssApplier;
import com.itextpdf.tool.xml.css.apply.PageSizeContainable;
import com.itextpdf.tool.xml.css.apply.ParagraphCssApplier;
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;



public class CssApplyServiceImpl implements CssApplyService {

	private Map<Class<?>, CssApplier<? extends Element>> map;

	/**
	 * 在构造器中初始化一个map，用于存放标签类对应的CssApplier
	 */
	public CssApplyServiceImpl() {
		map = new HashMap<Class<?>, CssApplier<? extends Element>>();
		map.put(Chunk.class, new ChunkCssApplier(null));
		map.put(Paragraph.class, new ParagraphCssApplier(this));
		map.put(NoNewLineParagraph.class, new NoNewLineParagraphCssApplier());
		map.put(HtmlCell.class, new HtmlCellCssApplier());
		map.put(List.class, new ListStyleTypeCssApplier());
		map.put(LineSeparator.class, new LineSeparatorCssApplier());
		map.put(Image.class, new ImageCssApplier());
		map.put(PdfDiv.class, new DivCssApplier());
	}

	public CssApplyServiceImpl(FontProvider fontProvider) {
		this();
		map.put(Chunk.class, new ChunkCssApplier(fontProvider));
	}

	public void putCssApplier(Class<?> s, CssApplier c) {
		map.put(s, c);
	}

	public CssApplier getCssApplier(Class<?> s) {
		return map.get(s);
	}

	public Element apply(Element e, final Tag t, final MarginMemory mm, final PageSizeContainable psc, final HtmlPipelineContext ctx) {
		CssApplier c = null;
		for (Map.Entry<Class<?>, CssApplier<? extends Element>> entry : map.entrySet()) {
			if (entry.getKey().isInstance(e)) {
				c = entry.getValue();
				break;
			}
		}
		if (c == null) {
			throw new RuntimeException();
		}
		e = c.apply(e, t, mm, psc, ctx);
		return e;
	}

	public Element apply(final Element e, final Tag t, final HtmlPipelineContext ctx) {
		return this.apply(e, t, ctx, ctx, ctx);
	}

}
