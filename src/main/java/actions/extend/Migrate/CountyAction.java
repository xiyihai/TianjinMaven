package actions.extend.Migrate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.base.MBaseAction;

//这个action用来获取市县信息
public class CountyAction extends MBaseAction {
	
	private static Logger logger = LogManager.getLogger(CountyAction.class);
	//这个用来返回城市的信息
	private String Countyinfo;

	public String getCountyinfo() {
		return Countyinfo;
	}

	public void setCountyinfo(String countyinfo) {
		Countyinfo = countyinfo;
	}
	
	public String execute(){
		logger.warn("用户请求获取市县信息================================");
		Countyinfo = ms.makeGEOJSONfromCounty();
		return SUCCESS;
	}
}
