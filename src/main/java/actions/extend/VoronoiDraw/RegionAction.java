package actions.extend.VoronoiDraw;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.base.VBaseAction;

//这个action用来加载数据库中关于景区的信息
public class RegionAction extends VBaseAction {
	
	private static Logger logger = LogManager.getLogger(RegionAction.class);
	
	private String Regioninfo;

	public String getRegioninfo() {
		return Regioninfo;
	}

	public void setRegioninfo(String regioninfo) {
		Regioninfo = regioninfo;
	}
	
	public String execute() throws Exception{
		
		logger.warn("用户请求获取特定关注区信息===========================================");
		
		Regioninfo = vds.makeGEOJSONfromRegion();
		return SUCCESS;
	}
	
	
}
