package actions.extend.SearchBS;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.base.SBaseAction;

//这个action针对前端非智能搜索请求
public class CellInfoSearchAction extends SBaseAction {
	
	private static Logger logger = LogManager.getLogger(CellInfoSearchAction.class);
	//用来接收前端数据
	private String cellId;
	private String cellName;
	
	//用来返回json数据
	private String BSinfo;

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public String getBSinfo() {
		return BSinfo;
	}

	public void setBSinfo(String bSinfo) {
		BSinfo = bSinfo;
	}
	
	public String execute(){
		
		if (!cellId.equals("")) {
			//通过id查找
			logger.warn("用户请求通过cellid获取辐射小区信息================================");
			BSinfo = ss.findBSbyCellid(cellId);
		}else {
			//通过name查找
			if (!cellName.equals("")) {
				logger.warn("用户请求通过name获取辐射小区信息================================");
				BSinfo = ss.findBSbyCellName(cellName,0,20); //这里设定为从0开始，查询条数
			}
		}
		
		return SUCCESS;
	}
	
}
