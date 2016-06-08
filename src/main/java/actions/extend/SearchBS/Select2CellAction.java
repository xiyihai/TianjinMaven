package actions.extend.SearchBS;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.base.SBaseAction;

//这个action接受前端智能搜索输入的值，返回提示的值
public class Select2CellAction extends SBaseAction {
	
	private static Logger logger = LogManager.getLogger(Select2CellAction.class);
	private String cellName;

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	
	//用来定义返回值
	private String cellNameArray;

	public String getCellNameArray() {
		return cellNameArray;
	}

	public void setCellNameArray(String cellNameArray) {
		this.cellNameArray = cellNameArray;
	}
	
	public String execute(){
		logger.warn("根据智能搜索框中输入的值，返回相应提示的值==========================================");
		cellNameArray  = ss.matchCellName(cellName, 0, 60);
		return SUCCESS;
	}
}
