package actions.extend.SearchBS;

import actions.base.SBaseAction;

public class DeleteBSAction extends SBaseAction{
	
	//接受前端过来的cellidArray
	private String[] cellidArray;

	public String[] getCellidArray() {
		return cellidArray;
	}

	public void setCellidArray(String[] cellidArray) {
		this.cellidArray = cellidArray;
	}
	
	public String execute(){
		
		//删除之后就需要判断是否需要修改Voronoi了
		ss.updateVoronoiForOut(ss.deleteBS(cellidArray));
		return SUCCESS;
	}
}
