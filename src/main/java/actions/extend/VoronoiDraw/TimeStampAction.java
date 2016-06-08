package actions.extend.VoronoiDraw;

import actions.base.VBaseAction;

public class TimeStampAction extends VBaseAction {

	//接受前端过来的时间
	private String time;
	
	//返回前端需要的json数据
	private String BSinfo;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBSinfo() {
		return BSinfo;
	}

	public void setBSinfo(String bSinfo) {
		BSinfo = bSinfo;
	}

	public String execute() throws Exception{
	
		BSinfo = vds.selectJSONfromTimestamp(time);
		
		return SUCCESS;
	}
}
