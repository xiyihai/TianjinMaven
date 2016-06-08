package actions.extend.VoronoiDraw;


import actions.base.VBaseAction;

//这个action接受前端勾画好的自定义景区信息，添加到数据库中
public class DivRegionAction extends VBaseAction {
	private String name;
	private Double longitude;
	private Double latitude;
	//这里传过来的是字符串，还需要转成Double
	private String area;
	private String points;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	
	public String execute(){
		if (vds.InsertRegion(name,Double.valueOf(area),longitude,latitude,points)) {
			return SUCCESS;
		}else {
			return ERROR;
		}
		
	}
	
	
}
