package actions.base;

import com.opensymphony.xwork2.ActionSupport;

import services.Interface.VoronoiDrawService;
//这个基站针对主页大部分功能
public class VBaseAction extends ActionSupport{
	
	//依赖的业务逻辑组件
	protected  VoronoiDrawService vds;
	//依赖注入业务逻辑组件所必须的setter方法
	public void setVoronoiDrawService(VoronoiDrawService vds)
	{
		this.vds = vds;
	}
}
