package vos;

import java.util.List;

//这个类是为了画出局部泰森多边形时，方便前台解析数据
public class VoronoiDraw {
	
	private String enBidrnc_id;
	private Integer value;
	private Double[] coordinates;
	private List<Double[]> polygon_p;
	
	
	
	public String getEnBidrnc_id() {
		return enBidrnc_id;
	}
	public void setEnBidrnc_id(String enBidrnc_id) {
		this.enBidrnc_id = enBidrnc_id;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Double[] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Double[] coordinates) {
		this.coordinates = coordinates;
	}
	public List<Double[]> getPolygon_p() {
		return polygon_p;
	}
	public void setPolygon_p(List<Double[]> polygon_p) {
		this.polygon_p = polygon_p;
	}
	public VoronoiDraw(String enBidrnc_id, Integer value, Double[] coordinates, List<Double[]> polygon_p) {
		super();
		this.enBidrnc_id = enBidrnc_id;
		this.value = value;
		this.coordinates = coordinates;
		this.polygon_p = polygon_p;
	}
	
	
}
