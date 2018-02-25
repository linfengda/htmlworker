package xmlworker.pipeline.demo1;

public abstract class AbstractHandler implements Handler {
	
	/** 责任链的下一个请求处理对象 **/
	private Handler next;

	public Handler getNext() {
		return next;
	}

	public void setNext(Handler next) {
		this.next = next;
	}
	
	public abstract String handleRequest(String user, double fee) throws Exception;
	

}
