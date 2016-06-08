package vos;

import java.util.List;
//这个类是为了辅助service层搜索功能中关于基站信息的存取而写的
public class VoronoiBS {

	private String name;
	private Integer value;
	private Double longitude;
	private Double latitude;
	private List<Double[]> polygon_p;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
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
	public List<Double[]>  getPolygon_p() {
		return polygon_p;
	}
	public void setPolygon_p(List<Double[]>  polygon_p) {
		this.polygon_p = polygon_p;
	}
	public VoronoiBS(String name, Integer value, Double longitude, Double latitude, List<Double[]>  polygon_p) {
		super();
		this.name = name;
		this.value = value;
		this.longitude = longitude;
		this.latitude = latitude;
		this.polygon_p = polygon_p;
	}
	
	
}
