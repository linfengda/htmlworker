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
package com.itextpdf.tool.xml.pipeline.html;

import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.ProcessObject;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.NoTagProcessorException;
import com.itextpdf.tool.xml.exceptions.PipelineException;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.Pipeline;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContext;


/**
 * HtmlPipeline将标签和文本转换为PDF Elements
 * 
 * @author 玄葬
 *
 */
public class HtmlPipeline extends AbstractPipeline<HtmlPipelineContext> {

	private final HtmlPipelineContext hpc;

	public HtmlPipeline(final HtmlPipelineContext hpc, final Pipeline<?> next) {
		super(next);
		this.hpc = hpc;
	}

	@Override
	public String getContextKey() {
		return HtmlPipeline.class.getName();
	}

	@Override
	public Pipeline<?> init(final WorkerContext context) throws PipelineException {
		context.put(getContextKey(), hpc);
		return getNext();
	}

	@Override
	public Pipeline<?> open(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
		HtmlPipelineContext hcc = getLocalContext(context);
		try {
            t.setLastMarginBottom(hcc.getMemory().get(HtmlPipelineContext.LAST_MARGIN_BOTTOM));
            hcc.getMemory().remove(HtmlPipelineContext.LAST_MARGIN_BOTTOM);
			TagProcessor tp = hcc.getProcessor(t.getName(), t.getNameSpace());
			addStackKeeper(t, hcc, tp);
			List<Element> content = tp.startElement(context, t);
			if (content.size() > 0) {
				if (tp.isStackOwner()) {
					StackKeeper peek = hcc.peek();
					if (peek == null)
						throw new PipelineException(String.format(LocaleMessages.STACK_404, t.toString()));

					for (Element elem : content) {
						peek.add(elem);
					}
				} else {
					for (Element elem : content) {
						hcc.getElements().add(elem);
						if (elem.type() == Element.BODY ){
							WritableElement writableElement = new WritableElement();
							writableElement.add(elem);
							po.add(writableElement);
							hcc.getElements().remove(elem);
						}
					}
				}
			}
		} catch (NoTagProcessorException e) {
			if (!hcc.acceptUnknown()) {
				throw e;
			}
		}
		return getNext();
	}

	@Override
	public Pipeline<?> content(final WorkerContext context, final Tag t, final String text, final ProcessObject po)
			throws PipelineException {
		HtmlPipelineContext hcc = getLocalContext(context);
		TagProcessor tp;
		try {
			tp = hcc.getProcessor(t.getName(), t.getNameSpace());
//			String ctn = null;
//			if (null != hcc.charSet()) {
//				try {
//					ctn = new String(b, hcc.charSet().name());
//				} catch (UnsupportedEncodingException e) {
//					throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(
//							LocaleMessages.UNSUPPORTED_CHARSET), e);
//				}
//			} else {
//				ctn = new String(b);
//			}
			List<Element> elems = tp.content(context, t, text);
			if (elems.size() > 0) {
				StackKeeper peek = hcc.peek();
				if (peek != null) {
					for (Element e : elems) {
						peek.add(e);
					}
				} else {
					WritableElement writableElement = new WritableElement();
					for (Element elem : elems) {
						writableElement.add(elem);
					}
					po.add(writableElement);
				}
			}
		} catch (NoTagProcessorException e) {
			if (!hcc.acceptUnknown()) {
				throw e;
			}
		}
		return getNext();
	}

	@Override
	public Pipeline<?> close(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
		HtmlPipelineContext hcc = getLocalContext(context);
		TagProcessor tp;
		try {
            if (t.getLastMarginBottom() != null) {
                hcc.getMemory().put(HtmlPipelineContext.LAST_MARGIN_BOTTOM, t.getLastMarginBottom());
            } else {
                hcc.getMemory().remove(HtmlPipelineContext.LAST_MARGIN_BOTTOM);
            }
			tp = hcc.getProcessor(t.getName(), t.getNameSpace());
			List<Element> elems = null;
			if (tp.isStackOwner()) {
				// remove the element from the StackKeeper Queue if end tag is
				// found
				StackKeeper tagStack;
				try {
					tagStack = hcc.poll();
				} catch (NoStackException e) {
					throw new PipelineException(String.format(
							LocaleMessages.getInstance().getMessage(LocaleMessages.STACK_404), t.toString()), e);
				}
				elems = tp.endElement(context, t, tagStack.getElements());
			} else {
				elems = tp.endElement(context, t, hcc.getElements());
				hcc.getElements().clear();
			}
			if (elems.size() > 0) {
				StackKeeper stack = hcc.peek();

				if (stack != null) {
					for (Element elem : elems) {
						stack.add(elem);
					}
				} else {
					WritableElement writableElement = new WritableElement();
					po.add(writableElement);
					writableElement.addAll(elems);
				}
			}
		} catch (NoTagProcessorException e) {
			if (!hcc.acceptUnknown()) {
				throw e;
			}
		}
		return getNext();
	}

	protected void addStackKeeper(Tag t, HtmlPipelineContext hcc, TagProcessor tp) {
		if (tp.isStackOwner()) {
			hcc.addFirst(new StackKeeper(t));
		}
	}
}
