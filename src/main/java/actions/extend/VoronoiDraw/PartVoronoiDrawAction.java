package actions.extend.VoronoiDraw;

import actions.base.VBaseAction;

//这个类主要是为了响应前端 画出屏幕内泰森多边形
public class PartVoronoiDrawAction extends VBaseAction {
	//用来接受前端json
	private double minlng;
	private double minlat;
	private double maxlng;
	private double maxlat;
	
	//用来返回范围内基站信息
	private String BSinfo;
	
	public double getMinlng() {
		return minlng;
	}
	public void setMinlng(double minlng) {
		this.minlng = minlng;
	}
	public double getMinlat() {
		return minlat;
	}
	public void setMinlat(double minlat) {
		this.minlat = minlat;
	}
	public double getMaxlng() {
		return maxlng;
	}
	public void setMaxlng(double maxlng) {
		this.maxlng = maxlng;
	}
	public double getMaxlat() {
		return maxlat;
	}
	public void setMaxlat(double maxlat) {
		this.maxlat = maxlat;
	}
	
	public String getBSinfo() {
		return BSinfo;
	}
	public void setBSinfo(String bSinfo) {
		BSinfo = bSinfo;
	}
	
	
	public String execute(){
		
		BSinfo = vds.makeJSONfromPartVoronoi(minlng, maxlng, minlat, maxlat);
		
		return SUCCESS;
	}
}
