package com.itextpdf.tool.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.css.CSSFileWrapper;
import com.itextpdf.tool.xml.css.CSSResolver;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssFileProcessor;
import com.itextpdf.tool.xml.css.CssFiles;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.CssApplyService;
import com.itextpdf.tool.xml.html.CssApplyServiceImpl;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.HTMLParser;
import com.itextpdf.tool.xml.parser.HTMLParserListener;
import com.itextpdf.tool.xml.pipeline.Pipeline;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * HTMLHelper类提供解析HTML的接口
 * 
 * @author 玄葬
 */
public class HTMLHelper {
	
	private HTMLHelper() {}
	private static HTMLHelper myself = new HTMLHelper();
	public synchronized static HTMLHelper getInstance() {
		return myself;
	}
	
	/**	读取默认资源文件 **/
	private CssFile defaultCssFile;
	private TagProcessorFactory tpf;
	
	public synchronized CssFile getDefaultCSS() {
		if (null == defaultCssFile) {
			defaultCssFile = getCSS(HTMLHelper.class.getResourceAsStream("/css/default.css"));
		}
		return defaultCssFile;
	}
	
	/**
	 * @return	a <code>DefaultTagProcessorFactory<code> that maps HTML tags to {@link TagProcessor}s
	 */
	public synchronized TagProcessorFactory getDefaultTagProcessorFactory() {
		if (null == tpf) {
			tpf = Tags.getHtmlTagProcessorFactory();
		}
		return tpf;
	}
	
	
	public static synchronized CssFile getCSS(InputStream in) {
        CssFile cssFile = null;
        if (null != in) {
            final CssFileProcessor cssFileProcessor = new CssFileProcessor();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            try {
                char[] buffer = new char[8192];
                int length;
                while ((length = br.read(buffer)) > 0) {
                    for(int i = 0 ; i < length; i++) {
                        cssFileProcessor.process(buffer[i]);
                    }
                }
                cssFile = new CSSFileWrapper(cssFileProcessor.getCss(), true);
            } catch (final IOException e) { throw new RuntimeWorkerException(e); }
            finally
            { try { in.close(); } catch (final IOException e) { throw new RuntimeWorkerException(e); } }
        }
        return cssFile;
    }
	
	
	
	
	
	/**
	 * 解析XHTML，并将生成的{@link Element}通过pipeline终点传给ElementHandler类处理
	 *
	 * @param d the handler
	 * @param in the reader
	 * @throws IOException thrown when something went wrong with the IO
	 */
	public void parseXHtml(final ElementHandler d, final Reader in) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		CSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		
		HtmlPipelineContext hpc = new HtmlPipelineContext(null);
		hpc.acceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
		
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(d, 
				null)));
		HTMLParserListener worker = new HTMLParserListener(pipeline);
		HTMLParser p = new HTMLParser();
		p.addHTMLLifeCycleListenter(worker);
		p.parse(in);
	}

	/**
	 * 解析XHTML，并将生成的{@link Element}通过{@link PdfWriterPipeline}传给{@link PdfWriter}类处理
	 *
	 * @param writer the PdfWriter
	 * @param doc the Document
	 * @param in the reader
	 * @throws IOException thrown when something went wrong with the IO
	 */
	public void parseXHtml(final PdfWriter writer, final Document doc, final Reader in) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		CSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		
		HtmlPipelineContext hpc = new HtmlPipelineContext(null);
		hpc.acceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
		
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new PdfWriterPipeline(doc,
				writer)));
		HTMLParserListener worker = new HTMLParserListener(pipeline);
		HTMLParser p = new HTMLParser();
		p.addHTMLLifeCycleListenter(worker);
		p.parse(in);
	}

	public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final Charset charset, final FontProvider fontProvider) throws IOException {
		parseXHtml(writer, doc, in, HTMLHelper.class.getResourceAsStream("/default.css"), charset, fontProvider);
	}
	
    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile) throws IOException {
        parseXHtml(writer, doc, in, inCssFile, null, new HTMLFontProvider());
	}

    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile, final FontProvider fontProvider) throws IOException {
        parseXHtml(writer, doc, in, inCssFile, null, fontProvider);
	}
    
	public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile, final Charset charset, final FontProvider fontProvider) throws IOException {
		parseXHtml(writer, doc, in, inCssFile, charset, fontProvider, null);
	}

	/**
	 * @param writer the writer to use
	 * @param doc the document to use
	 * @param in the {@link InputStream} of the XHTML source.
	 * @param in the {@link CssFiles} of the css files.
	 * @param charset the charset to use
	 * @param resourcesRootPath defines the root path to find resources in case they are defined in html with relative paths (e.g. images)
	 * @throws IOException if the {@link InputStream} could not be read.
	 */
	public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile, final Charset charset, final FontProvider fontProvider, final String resourcesRootPath) throws IOException {
        CssFilesImpl cssFiles = new CssFilesImpl();
        if (inCssFile != null)
            cssFiles.add(getCSS(inCssFile));
        else
            cssFiles.add(getDefaultCSS());
        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
        HtmlPipelineContext hpc = new HtmlPipelineContext(new CssApplyServiceImpl(fontProvider));
        hpc.acceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory()).setResourcesRootPath(resourcesRootPath);
        HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(doc, writer));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        HTMLParserListener worker = new HTMLParserListener(pipeline);
        HTMLParser p = new HTMLParser(worker);
		if (charset != null)
			p.parse(in, charset);
		else
			p.parse(in);
	}

	/**
	 * @param d the ElementHandler
	 * @param in the InputStream
	 * @param charset the charset to use
	 * @throws IOException if something went seriously wrong with IO.
	 */
	public void parseXHtml(final ElementHandler d, final InputStream in, final Charset charset) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext(null);
		hpc.acceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory());
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(d,
				null)));
		HTMLParserListener worker = new HTMLParserListener(pipeline);
		HTMLParser p = new HTMLParser(worker);
		if (charset != null)
			p.parse(in, charset);
		else
			p.parse(in);
	}


    /**
     * Parses an HTML string and a string containing CSS into a list of Element objects.
     * The FontProvider will be obtained from iText's FontFactory object.
     * 
     * @param   html    a String containing an XHTML snippet
     * @param   css     a String containing CSS
     * @return  an ElementList instance
     */
    public static ElementList parseToElementList(String html, String css) throws IOException {
        // CSS
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        if (css != null) {
            CssFile cssFile = HTMLHelper.getCSS(new ByteArrayInputStream(css.getBytes()));
            cssResolver.addCss(cssFile);
        }
        
        // HTML
        CssApplyService cssAppliers = new CssApplyServiceImpl(FontFactory.getFontImp());
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.autoBookmark(false);
        
        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        
        // XML Worker
        HTMLParserListener worker = new HTMLParserListener(cssPipeline);
        HTMLParser p = new HTMLParser(worker);
        p.parse(new ByteArrayInputStream(html.getBytes()));
        
        return elements;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
