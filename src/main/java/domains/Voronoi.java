package domains;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="tianjin_voronoi")
//表示开启二级缓存，并使用read-only策略,这里不能使用read-only,我需要更改数据
//使用NONSTRICT_READ_WRITE策略，更新不频繁几个小时或更长 
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
public class Voronoi {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@NotNull
	@Length(min=6,max=7)
	@Column(name="enBidrnc_id")
	private String enBidrnc_id;
	
	@NotNull
	@Column(name="longitude")
	private Double longitude;
	
	@NotNull
	@Column(name="latitude")
	private Double latitude;
	
	@Min(0)
	@Column(name="value")
	private Integer value;
	
	@Column(name="polygon_p")
	private String polygon_p;

	public Voronoi() {
		// TODO Auto-generated constructor stub
	}

	public Voronoi(String enBidrnc_id, Double longitude, Double latitude, Integer value) {
		super();
		this.enBidrnc_id = enBidrnc_id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.value = value;
	}

	
	public String getEnBidrnc_id() {
		return enBidrnc_id;
	}

	public void setEnBidrnc_id(String enBrnc_id) {
		this.enBidrnc_id = enBrnc_id;
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

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getPolygon_p() {
		return polygon_p;
	}

	public void setPolygon_p(String polygon_p) {
		this.polygon_p = polygon_p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Voronoi other = (Voronoi) obj;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Voronoi [enBidrnc_id=" + enBidrnc_id + ", longitude=" + longitude + ", latitude=" + latitude
				+ ", value=" + value + ", polygon_p=" + polygon_p + "]";
	}
	
	
}
