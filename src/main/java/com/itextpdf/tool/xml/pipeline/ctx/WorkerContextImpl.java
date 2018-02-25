package com.itextpdf.tool.xml.pipeline.ctx;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.itextpdf.tool.xml.exceptions.NoCustomContextException;

public class WorkerContextImpl implements WorkerContext {
	private final Map<String, Object> map;

	public WorkerContextImpl() {
		this.map = new ConcurrentHashMap<String, Object>();
	}

	@Override
	public CustomContext get(String pipeline) throws NoCustomContextException {
		Object c = map.get(pipeline);
		if (c != null) {
			return (CustomContext) c;
		}
		throw new NoCustomContextException();
	}

	@Override
	public void put(String pipeline, CustomContext pipelineContext) {
		map.put(pipeline, pipelineContext);
	}


}
