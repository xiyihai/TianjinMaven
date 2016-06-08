package vos;

import java.util.Arrays;
import java.util.List;

public class RegionVos {
	
	private Integer id;
	private Double longitude;
	private Double latitude;
	private String name;
	private String image;
	private Double area;
	private String[] towerids;
	private List<Double[]> coordinates;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Double getArea() {
		return area;
	}
	public void setArea(Double area) {
		this.area = area;
	}
	public String[] getTowerids() {
		return towerids;
	}
	public void setTowerids(String[] towerids) {
		this.towerids = towerids;
	}
	public List<Double[]> getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(List<Double[]> coordinates) {
		this.coordinates = coordinates;
	}
	public RegionVos(Integer id, Double longitude, Double latitude, String name, String image, Double area,
			String[] towerids, List<Double[]> coordinates) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.name = name;
		this.image = image;
		this.area = area;
		this.towerids = towerids;
		this.coordinates = coordinates;
	}
	@Override
	public String toString() {
		return "RegionVos [id=" + id + ", longitude=" + longitude + ", latitude=" + latitude + ", name=" + name
				+ ", image=" + image + ", area=" + area + ", towerids=" + Arrays.toString(towerids) + ", coordinates="
				+ coordinates + "]";
	}
	
	
	
}
