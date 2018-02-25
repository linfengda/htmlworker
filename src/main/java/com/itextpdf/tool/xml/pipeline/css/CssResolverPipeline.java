package com.itextpdf.tool.xml.pipeline.css;

import com.itextpdf.tool.xml.ProcessObject;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSSResolver;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.exceptions.PipelineException;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.Pipeline;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContext;

/**
 * CssResolverPipeline接收Tags标签并渲染CSS样式
 *
 * @author 玄葬
 *
 */
public class CssResolverPipeline extends AbstractPipeline<CSSResolver> {
	
	private CSSResolver cssResolver;

	public CssResolverPipeline(final CSSResolver cssResolver, final Pipeline<?> next) {
		super(next);
		this.cssResolver = cssResolver;
	}

	@Override
	public String getContextKey() {
		return CssResolverPipeline.class.getName();
	}

	@Override
	public Pipeline<?> init(WorkerContext context) throws PipelineException {
		try {
			CSSResolver ctx = cssResolver.clear();	// 使用CSSResolver上下文之前先清空非持久化CSS文件
			context.put(getContextKey(), ctx);
			return getNext();
		} catch (CssResolverException e) {
			throw new PipelineException(e);
		}
	}

	@Override
	public Pipeline<?> open(WorkerContext context, Tag t, ProcessObject po) throws PipelineException {
		CSSResolver cssResolver = getLocalContext(context);
		cssResolver.resolve(t);
		return getNext();
	}

	
	
	

}
