package com.itextpdf.tool.xml.pipeline.ctx;

import com.itextpdf.tool.xml.exceptions.NoCustomContextException;
import com.itextpdf.tool.xml.pipeline.Pipeline;

/**
 * 责任链{@link Pipeline}传递的公用上下文
 * 
 * @author 玄葬
 *
 */
public interface WorkerContext {
	
	/**
	 * 获取当前pipeline的上下文
	 * @param pipeline	pipeline名称
	 * @return
	 * @throws NoCustomContextException
	 */
	CustomContext get(String pipeline) throws NoCustomContextException;
	
	/**
	 * 设置当前pipeline的上下文
	 * @param pipeline	pipeline名称
	 * @param pipelineContext	pipeline上下文
	 * @return
	 * @throws NoCustomContextException
	 */
	void put(String pipeline, CustomContext pipelineContext);
	
	

}
