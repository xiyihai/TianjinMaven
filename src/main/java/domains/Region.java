package domains;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(name="tianjin_region")
//表示开启二级缓存，并使用read-only策略
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@Cacheable
public class Region {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@NotBlank
	@Column(name="name")
	private String name;
	
	@NotNull
	@Column(name="area")
	private Double area;
	
	@NotBlank
	@Column(name="points")
	private String points;
	
	@NotNull
	@Column(name="longitude")
	private Double longitude;
	
	@NotNull
	@Column(name="latitude")
	private Double latitude;
	
	@Column(name="image")
	private String image;
	
	@Column(name="towerids")
	private String towerids;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTowerids() {
		return towerids;
	}

	public void setTowerids(String towerids) {
		this.towerids = towerids;
	}

	public Region() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Region(String name, Double area, String points, Double longitude, Double latitude) {
		super();
		this.name = name;
		this.area = area;
		this.points = points;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	
}
