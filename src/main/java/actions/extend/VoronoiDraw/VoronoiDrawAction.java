package actions.extend.VoronoiDraw;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.base.VBaseAction;
//这个action回应热力图、全局基站请求
public class VoronoiDrawAction extends VBaseAction{
	
	private static Logger logger = LogManager.getLogger(VoronoiDrawAction.class);
	//放置需要返回基站的geojson字符串
	private String BSinfo;

	public String getBSinfo() {
		return BSinfo;
	}

	public void setBSinfo(String bSinfo) {
		BSinfo = bSinfo;
	}
	
	public String execute() throws Exception{
		logger.warn("用户请求开启全局基站图层或者热力图====================================");
		BSinfo = vds.makeGEOJSONfromVoronoi();
		return SUCCESS;
	}
}
