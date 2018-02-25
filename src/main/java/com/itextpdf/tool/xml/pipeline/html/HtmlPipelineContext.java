package com.itextpdf.tool.xml.pipeline.html;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.tool.xml.css.apply.MarginMemory;
import com.itextpdf.tool.xml.css.apply.PageSizeContainable;
import com.itextpdf.tool.xml.exceptions.NoDataException;
import com.itextpdf.tool.xml.html.CssAppliersAware;
import com.itextpdf.tool.xml.html.CssApplyService;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.pipeline.ctx.CustomContext;

/**
 * HtmlPipeline的上下文配置，可以配置：
 * 1. 是否容忍未知标签
 * 2. 是否允许书签
 * 3. 文档的pageSize
 * 4. 标签处理工厂tagFactory对象
 * 5. 图片来源
 * 6. 资源链接
 * 7. 资源根目录
 * 8. 字符编码
 * 
 * @author 玄葬
 *
 */
public class HtmlPipelineContext implements CustomContext, MarginMemory, PageSizeContainable {
	
	
	/** Key for the memory, used to store bookmark nodes **/
	public static final String BOOKMARK_TREE = "header.autobookmark.RootNode";
	/** Key for the memory, used in Html TagProcessing **/
	public static final String LAST_MARGIN_BOTTOM = "lastMarginBottom";
	
	private final LinkedList<StackKeeper> queue = new LinkedList<StackKeeper>();
	private final Map<String, Object> memory = new HashMap<String, Object>();
	private final List<Element> elements = new ArrayList<Element>();
	
	private boolean acceptUnknown = true;
	private boolean autoBookmark = true;
	private Rectangle pageSize = PageSize.A4;
	private List<String> roottags = Arrays.asList(new String[] { "body", "div" });
	
	private TagProcessorFactory tagFactory;
	private ImageProvider imageProvider;
	private LinkProvider linkprovider;
	private String resourcesRootPath;
	private Charset charset;
	private CssApplyService cssApplyService;
	
	
	
	public HtmlPipelineContext() {
		
	}
	
	public HtmlPipelineContext(CssApplyService cssApplyService) {
		this();
		this.cssApplyService = cssApplyService;
	}
	
	/**
	 * 找到标签的处理器TagProcessor
	 * @param tag
	 * @param nameSpace
	 * @return
	 */
	protected TagProcessor getProcessor(final String tag, final String nameSpace) {
		TagProcessor tp = tagFactory.getProcessor(tag, nameSpace);
		if (tp instanceof CssAppliersAware) {
			((CssAppliersAware) tp).setCssApplyService(this.cssApplyService);
		}
		return tp;
	}

	/**
	 * Add a {@link StackKeeper} to the top of the stack list.
	 * @param stackKeeper the {@link StackKeeper}
	 */
	protected void addFirst(final StackKeeper stackKeeper) {
		this.queue.addFirst(stackKeeper);

	}

	/**
	 * Retrieves, but does not remove, the head (first element) of this list.
	 * @return a StackKeeper or null if there are no elements on the stack
	 */
	protected StackKeeper peek() {
		if (!this.queue.isEmpty())
			return this.queue.getFirst();
		return null;
	}
	
	/**
	 * Retrieves and removes the top of the stack.
	 * @return a StackKeeper
	 * @throws NoStackException if there are no elements on the stack
	 */
	protected StackKeeper poll() throws NoStackException {
		try {
			return this.queue.removeFirst();
		} catch (NoSuchElementException e) {
			throw new NoStackException();
		}
	}
	
	
	
	
	
	
	
	public LinkedList<StackKeeper> getQueue() {
		return queue;
	}

	public Map<String, Object> getMemory() {
		return memory;
	}

	public List<Element> getElements() {
		return elements;
	}

	public boolean acceptUnknown() {
		return acceptUnknown;
	}

	public HtmlPipelineContext acceptUnknown(boolean acceptUnknown) {
		this.acceptUnknown = acceptUnknown;
		return this;
	}

	public boolean autoBookmark() {
		return autoBookmark;
	}

	public HtmlPipelineContext autoBookmark(boolean autoBookmark) {
		this.autoBookmark = autoBookmark;
		return this;
	}

	public List<String> getRoottags() {
		return roottags;
	}

	public HtmlPipelineContext setRoottags(List<String> roottags) {
		this.roottags = roottags;
		return this;
	}

	public HtmlPipelineContext setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public TagProcessorFactory getTagFactory() {
		return tagFactory;
	}

	public HtmlPipelineContext setTagFactory(TagProcessorFactory tagFactory) {
		this.tagFactory = tagFactory;
		return this;
	}

	public ImageProvider getImageProvider() {
		return imageProvider;
	}

	public HtmlPipelineContext setImageProvider(ImageProvider imageProvider) {
		this.imageProvider = imageProvider;
		return this;
	}

	public LinkProvider getLinkprovider() {
		return linkprovider;
	}

	public HtmlPipelineContext setLinkprovider(LinkProvider linkprovider) {
		this.linkprovider = linkprovider;
		return this;
	}

	public String getResourcesRootPath() {
		return resourcesRootPath;
	}

	public HtmlPipelineContext setResourcesRootPath(String resourcesRootPath) {
		this.resourcesRootPath = resourcesRootPath;
		return this;
	}

	public Charset getCharset() {
		return charset;
	}

	public HtmlPipelineContext setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}


	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Rectangle getPageSize() {
		return this.pageSize;
	}

	@Override
	public Float getLastMarginBottom() throws NoDataException {
		Map<String, Object> memory = getMemory();
		Object o =  memory.get(HtmlPipelineContext.LAST_MARGIN_BOTTOM);
		if (null == o) {
			throw new NoDataException();
		} else {
			return (Float) o;
		}
	}

	@Override
	public List<String> getRootTags() {
		return roottags;
	}

	@Override
	public void setLastMarginBottom(Float lmb) {
		getMemory().put(HtmlPipelineContext.LAST_MARGIN_BOTTOM, lmb);
	}
	
	
	


}
