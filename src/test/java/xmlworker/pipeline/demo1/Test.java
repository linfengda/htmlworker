package xmlworker.pipeline.demo1;

public class Test {
	
	public static void main(String[] args) {
		
		//先要组装责任链  
		Handler xiangmuJL = new XiangmuJL();
		Handler bumenJL = new BumenJL();
		Handler zongjianJL = new ZongjianJL();
		xiangmuJL.setNext(bumenJL);
		bumenJL.setNext(zongjianJL);
		try {
			//System.out.println(xiangmuJL.handleRequest("张三", 200));
			System.out.println(xiangmuJL.handleRequest("李四", 600));
			//System.out.println(xiangmuJL.handleRequest("赵六", 6000));
			//System.out.println(xiangmuJL.handleRequest("aaa", 200000));
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	

}
