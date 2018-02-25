package xmlworker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;

import com.lowagie.text.pdf.BaseFont;

public class Test2 {
	
	public static void main(String[] args) {
		Test2 t = new Test2();
		t.test1();
	}
	
	public void test1() {
		try {
			ITextRenderer render = new ITextRenderer();  
            ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(render.getOutputDevice());  
            render.getSharedContext().setUserAgentCallback(callback);  
            callback.setSharedContext(render.getSharedContext());  
            
            InputStream is = new FileInputStream("D://test3.html");
            String content = new String(IOUtils.toByteArray(is));
            FileOutputStream os = new FileOutputStream("D://test3.pdf");
            render.getFontResolver().addFont("/msfonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);  
            render.setDocumentFromString(content);
            render.getSharedContext().setBaseURL("file:///D:/image/");
            render.layout();  
            render.createPDF(os);  
            render.finishPDF();
  
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class ResourceLoaderUserAgent extends ITextUserAgent {

		public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
			super(outputDevice);
		}
		
		@Override
		protected InputStream resolveAndOpenStream(String arg0) {
			InputStream is = super.resolveAndOpenStream(arg0);  
            System.out.println("加载资源文件： " + arg0);  
            return is;  
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
