package actions.extend.VoronoiDraw;

import actions.base.VBaseAction;

//这个action，在前端开启图层之后响应鼠标事件，获取选中基站的具体信息
public class CellidInfoAction extends VBaseAction {
	
	//定义的返回json字符串信息
	private String Cellinfo;

	public String getCellinfo() {
		return Cellinfo;
	}

	public void setCellinfo(String cellinfo) {
		Cellinfo = cellinfo;
	}
	
	//用来获取post过来的json字符串
	private String enBidrnc_id;
	
	
	public String getEnBidrnc_id() {
		return enBidrnc_id;
	}

	public void setEnBidrnc_id(String enBidrnc_id) {
		this.enBidrnc_id = enBidrnc_id;
	}

	public String execute() throws Exception{
		Cellinfo = vds.makeGEOJSONfromCell(enBidrnc_id);
		return SUCCESS;
	}
}
