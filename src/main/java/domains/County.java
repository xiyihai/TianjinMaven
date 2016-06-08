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
@Table(name="tianjin_county")
//表示开启二级缓存，并使用read-only策略
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@Cacheable
public class County {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="countyid")
	private Integer countyid;
	
	@NotBlank
	@Column(name="name")
	private String name;

	@NotNull
	@Column(name="longitude")
	private Double longitude;
	
	@NotNull
	@Column(name="latitude")
	private Double latitude;

	public County() {
		super();
		// TODO Auto-generated constructor stub
	}

	public County(String name, Double longitude, Double latitude) {
		super();
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}

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
	
	
}
