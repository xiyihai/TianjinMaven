package actions.base;

import com.opensymphony.xwork2.ActionSupport;

import services.Interface.MigrateService;
//这个基类针对人口迁移图
public class MBaseAction extends ActionSupport{
	
	//依赖的业务逻辑组件
	protected  MigrateService ms;
	//依赖注入业务逻辑组件所必须的setter方法
	public void setMigrateService(MigrateService ms)
	{
		this.ms = ms;
	}

}
