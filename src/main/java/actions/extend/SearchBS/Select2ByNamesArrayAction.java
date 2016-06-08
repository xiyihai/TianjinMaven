package actions.extend.SearchBS;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.base.SBaseAction;
//这个action根据前端智能搜索确定好的name数组，返回这些name对应基站的信息
public class Select2ByNamesArrayAction extends SBaseAction {

	private static Logger logger = LogManager.getLogger(Select2ByNamesArrayAction.class);
	//接受前端过来的cellName数组
	private String[] cellNames;
	
	//用来返回信息
	private String BSinfo;
	
	public String[] getCellNames() {
		return cellNames;
	}

	public void setCellNames(String[] cellNames) {
		this.cellNames = cellNames;
	}

	public String getBSinfo() {
		return BSinfo;
	}

	public void setBSinfo(String bSinfo) {
		BSinfo = bSinfo;
	}
	
	public String execute(){
		if(cellNames.length==1&&cellNames[0].equals("")){
			
		}else {
			logger.warn("根据智能搜索框中选中值，向后台请求对应数据======================");
			BSinfo = ss.findBSByS2NameArray(cellNames);
		}
		
		return SUCCESS;
	}
	
	
}
