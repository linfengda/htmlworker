package xmlworker.pipeline.demo1;

public class ZongjianJL extends AbstractHandler {

	@Override
	public String handleRequest(String user, double fee) throws Exception {

		String result = "";
		if (fee < 10000) {
			result = "报销成功！总监审批同意【"+user+"】的报销金额【"+fee+"元】";
		}else{
			if (null != getNext()) {
				result = getNext().handleRequest(user, fee);
			}
		}
		return result;
	}

}
