package xmlworker.pipeline.demo1;

public interface Handler {
	
	Handler getNext();

	void setNext(Handler next);
	
	String handleRequest(String user, double fee) throws Exception;

}
