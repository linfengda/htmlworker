package xmlworker.pipeline.demo1;

public class XiangmuJL extends AbstractHandler {

	@Override
	public String handleRequest(String user, double fee) throws Exception {
		
		String result = "";
		if (fee < 500) {
			result = "报销成功！项目经理审批同意【"+user+"】的报销金额【"+fee+"元】";
		}else{
			if (null != getNext()) {
				result = getNext().handleRequest(user, fee);
			}
		}
		return result;
	}

}
