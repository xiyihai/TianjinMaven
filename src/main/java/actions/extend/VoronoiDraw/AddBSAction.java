package actions.extend.VoronoiDraw;

import actions.base.VBaseAction;

public class AddBSAction extends VBaseAction{
	
	//接受前端过来的字符串，需要解析成一个json对象
	private String bs;
	
	
	public String getBs() {
		return bs;
	}


	public void setBs(String bs) {
		this.bs = bs;
	}


	public String execute() {
		if (vds.InsertBS(bs)) {
			//成功增加基站之后还需要更新Voronoi表，这里还是解析bs字符串，获取需要的数据
			vds.updateVoronoiForIn(bs);
			return SUCCESS;
		}else {
			return ERROR;
		}
	}
}
