package xmlworker;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.HTMLFontProvider;
import com.itextpdf.tool.xml.HTMLHelper;
import com.itextpdf.tool.xml.css.CSSResolver;
import com.itextpdf.tool.xml.css.CssFiles;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssApplyServiceImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.HTMLParser;
import com.itextpdf.tool.xml.parser.HTMLParserListener;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class Test {
	
	public static void main(String[] args) {
		new Test().createPdf();
	}
	
	public void createPdf() {
		try {
			//Document doc = new Document();
			Document doc = new Document(new RectangleReadOnly(623, 792), 0, 0, 45, 8);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("D://test.pdf"));
			doc.open();
			
			// CSS
			CssFiles cssFiles = new CssFilesImpl();
			cssFiles.add(HTMLHelper.getCSS(this.getClass().getResourceAsStream("/css/default.css")));
			cssFiles.add(HTMLHelper.getCSS(this.getClass().getResourceAsStream("/css/table.css")));
			CSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
			
			// HTML
			HtmlPipelineContext hpc = new HtmlPipelineContext(new CssApplyServiceImpl(new HTMLFontProvider("msyh")));
			hpc.acceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
			
			// Pipelines
			PdfWriterPipeline pdfWriterPipeline = new PdfWriterPipeline(doc, writer);
			HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, pdfWriterPipeline);
			CssResolverPipeline cssResolverPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
			
			// HTMLParser
			HTMLParserListener listener = new HTMLParserListener(cssResolverPipeline);
			HTMLParser parser = new HTMLParser(listener);
//			parser.parse(new FileInputStream("D://test.html"));
//			parser.parse(new FileInputStream("D://demo_paragragh.html"));
//			parser.parse(new FileInputStream("D://demo_font.html"));
			parser.parse(new FileInputStream("D://demo_block.html"));
//			parser.parse(new FileInputStream("D://test3.html"));
			
			doc.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void test3() {
		try {
			ParserListenerWriter listener = new ParserListenerWriter();
			HTMLParser parser = new HTMLParser(listener);
			parser.parse(new FileInputStream("D://demo_font.html"));
			listener.printHtmlText();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	

}
