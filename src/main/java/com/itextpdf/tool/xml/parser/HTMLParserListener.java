package com.itextpdf.tool.xml.parser;

import java.util.Map;

import com.itextpdf.tool.xml.ProcessObject;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.PipelineException;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.pipeline.Pipeline;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContext;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;

/**
 * 接收标签事件，调用pipeline时生成PDF element</br>
 * 只有startElement/endElement/text方法会影响到最终生成的PDF
 * 
 * @author 玄葬
 *
 */
public class HTMLParserListener implements HTMLLifeCycleListener {

	/** 当前标签 **/
	private Tag tag;
	/** 管道开头 **/
	private Pipeline<?> rootpPipe;
	/** 管道上下文 **/
	private WorkerContext context;
	
	public HTMLParserListener(Pipeline<?> rootpPipe) {
		this.rootpPipe = rootpPipe;
		this.context = new WorkerContextImpl();
	}

	@Override
	public void init() {
		Pipeline<?> p = rootpPipe;
		try {
			while((p = p.init(this.context)) != null);
		} catch (PipelineException e) {
			throw new RuntimeWorkerException(e);
		}
	}

	@Override
	public void startElement(String tag, Map<String, String> attributes, String ns) {
		
		Tag t = new Tag(tag, attributes, ns);
		if (this.tag != null) {
			this.tag.addChild(t);
		}
		this.tag = t;
		ProcessObject po = new ProcessObject();
		Pipeline<?> p = rootpPipe;
		try {
			while((p = p.open(context, t, po)) != null);
		} catch (PipelineException e) {
			throw new RuntimeWorkerException(e);
		}
	}

	@Override
	public void endElement(String tag, String ns) {
		
		tag = tag.toLowerCase();
		if (this.tag != null && !this.tag.getName().equals(tag)) {	//判断标签是否闭合
			throw new RuntimeWorkerException(String.format(
					LocaleMessages.getInstance().getMessage(LocaleMessages.INVALID_NESTED_TAG), tag, this.tag.getName()));
		}
		Pipeline<?> p = rootpPipe;
		ProcessObject po = new ProcessObject();
		try {
			while((p = p.close(context, this.tag, po)) != null);
		} catch (PipelineException e) {
			throw new RuntimeWorkerException(e);
		} finally {
			if (null != this.tag)
				this.tag = this.tag.getParent();
		}
	}

	@Override
	public void text(String text) {
		
		if (text.startsWith("<![CDATA[") && text.endsWith("]]>")) {
			return;
        }
		if (null != this.tag) {
			Pipeline<?> p = rootpPipe;
			ProcessObject po = new ProcessObject();
			try {
				while((p = p.content(context, this.tag, text, po)) != null);
			} catch (PipelineException e) {
				throw new RuntimeWorkerException(e);
			}
		}
	}
	
	@Override
	public void unknownText(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void comment(String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	

	
	

}
