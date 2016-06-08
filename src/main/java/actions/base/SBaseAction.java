package actions.base;

import com.opensymphony.xwork2.ActionSupport;

import services.Interface.SearchService;

public class SBaseAction extends ActionSupport {
	//依赖的业务逻辑组件
	protected  SearchService ss;
	//依赖注入业务逻辑组件所必须的setter方法
	public void setSearchService(SearchService ss)
	{
		this.ss = ss;
	}
}
