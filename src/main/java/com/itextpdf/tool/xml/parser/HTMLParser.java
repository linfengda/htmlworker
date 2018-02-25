package com.itextpdf.tool.xml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.itextpdf.tool.xml.parser.HTMLLifeCycleListener;
import com.itextpdf.tool.xml.parser.HTMLParserMemory;
import com.itextpdf.tool.xml.parser.State;
import com.itextpdf.tool.xml.parser.StateController;
import com.itextpdf.tool.xml.parser.TagState;

/**
 * 解析HTML文档的实现类，与 {@link HTMLLifeCycleListener} 绑定以接收解析事件
 * 
 * @author 玄葬
 *
 */
public class HTMLParser implements HTMLLifeCycle {
	
	private State previousState;					//上个符号状态
	private State state;							//当前符号状态，根据当前符号状态+读取到的符号=进入下一个状态
	private StateController stateController;		//HTML解析过程中所有可能出现符号状态的集合
	private HTMLParserMemory memory;				//缓存读取到的符号，直到遇到对应的结束符号，并返回缓存的字符串（tagName，attributeName，attributeValue等）
	private List<HTMLLifeCycleListener> listeners;	//HTML解析过程监听者
	private String text = null;						//缓存读取到的HTML文本内容
	private TagState tagState;						//当前标签状态
	
	
	public HTMLParser() {
		stateController = new StateController(this);
		stateController.unknown();
		memory = new HTMLParserMemory();
		listeners = new CopyOnWriteArrayList<HTMLLifeCycleListener>();
	}
	
	public HTMLParser(final HTMLLifeCycleListener listener) {
		this();
		listeners.add(listener);
	}
	
	public StateController stateController() {
		return stateController;
	}
	
	public HTMLParserMemory memory() {
		return memory;
	}
	
	public void setState(final State state) {
		this.previousState = this.state;
		this.state = state;
	}
	
	public void previousState() {
		setState(previousState);
	}
	
	
	@Override
	public HTMLLifeCycle addHTMLLifeCycleListenter(HTMLLifeCycleListener l) {
		 listeners.add(l);
	     return this;
	}

	@Override
	public HTMLLifeCycle removeHTMLLifeCycleListenter(HTMLLifeCycleListener l) {
		listeners.remove(l);
        return this;
	}
	
	@Override
    public void unknownText() {
        for (HTMLLifeCycleListener l : listeners) {
        	l.unknownText(this.memory.textBuffString());
        }
    }

	@Override
    public void startElement() {
    	this.tagState = TagState.OPEN;
        callText();
        for (HTMLLifeCycleListener l : listeners) {
        	l.startElement(this.memory.getCurrentTag(), this.memory.attr(), this.memory.getCurrentNameSpace());
        }
        this.memory.setCurrentNameSpace("");
    }

	@Override
    public void endElement() {
    	this.tagState = TagState.CLOSE;
        callText();
        for (HTMLLifeCycleListener l : listeners) {
            l.endElement(this.memory.getCurrentTag(), this.memory.getCurrentNameSpace());
        }
    }

	@Override
    public void comment() {
        callText();
        for (HTMLLifeCycleListener l : listeners) {
            l.comment(this.memory.textBuffString());
        }
    }
    
    /**
     * Call this method to submit the text to listeners.
     */
    private void callText() {
        if (null != text && text.length() > 0) {
            for (HTMLLifeCycleListener l : listeners) {
                l.text(text);
            }
            text = null;
        }
    }

    public void text(final String bs) {
        text = bs;
    }

    public TagState getTagState() {
        return this.tagState;
    }

	
	
	
	
	
	
	
	
	
	
	


	

	/**
	 * 使用系统默认编码读取HTML输入流并解析
     *
     * @param in the InputStream to parse
     * @throws IOException if IO went wrong
     */
    public void parse(final InputStream in) throws IOException {
        parse(new InputStreamReader(in));
    }

    /**
     * 使用指定编码读取HTML输入流并解析
     *
     * @param in      the stream to read
     * @param charSet to use for the constructed reader.
     * @throws IOException if reading fails
     */
    public void parse(final InputStream in, final Charset charSet) throws IOException {
        InputStreamReader reader = new InputStreamReader(in, charSet);
        parse(reader);

    }

    /**
     * HTML符号识别算法
     * 每次读取一个输入字符，并根据这些字符转移到下一个状态，当前的符号状态和当前Dom树状态共同影响结果。
     * 这意味着，读取同样的字符，可能因为当前状态的不同，得到不同的下一个状态。
     * 
     * @param r
     * @throws IOException
     */
    public void parse(final Reader r) throws IOException {
        for (HTMLLifeCycleListener l : listeners) {
        	l.init();
        }
        char read[] = new char[1];
        try {
        	while (-1 != (r.read(read))) {
        		state.process(read[0]);
        	}
        } finally {
        	for (HTMLLifeCycleListener l : listeners) {
        		l.close();
        	}
        	r.close();
        }
    }
    
    
    
    
    
    

}
