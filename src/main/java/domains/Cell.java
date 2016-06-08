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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(name="tianjin_cell_info")
//表示开启二级缓存，并使用read-only策略
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@Cacheable
public class Cell {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Length(min=9,max=10)
	@Column(name="cellid")
	private String cellid;
	
	@NotNull
	@Length(min=6,max=7)
	@Column(name="enBidrnc_id")
	private String enBidrnc_id;
	
	@NotBlank
	@Column(name="name")
	private String name;
	
	@NotNull
	@Column(name="longitude")
	private Double longitude;
	
	@NotNull
	@Column(name="latitude")
	private Double latitude;
	
	@NotBlank
	@Column(name="covertype")
	private String covertype;
	
	@NotBlank
	@Column(name="coverregion")
	private String coverregion;
	
	@NotBlank
	@Column(name="countyname")
	private String countyname;
	
	public Cell() {
		// TODO Auto-generated constructor stub
	}

	public Cell(String cellid, String enBidrnc_id, String name, String covertype, String coverregion, String countyname, Double longitude, Double latitude) {
		super();
		this.cellid = cellid;
		this.enBidrnc_id = enBidrnc_id;
		this.name = name;
		this.covertype = covertype;
		this.coverregion = coverregion;
		this.countyname = countyname;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String getCellid() {
		return cellid;
	}

	public void setCellid(String cellid) {
		this.cellid = cellid;
	}

	public String getEnBrnc_id() {
		return enBidrnc_id;
	}

	public void setEnBrnc_id(String enBrnc_id) {
		this.enBidrnc_id = enBrnc_id;
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

	public String getCovertype() {
		return covertype;
	}

	public void setCovertype(String covertype) {
		this.covertype = covertype;
	}

	public String getCoverregion() {
		return coverregion;
	}

	public void setCoverregion(String coverregion) {
		this.coverregion = coverregion;
	}

	public String getCountyname() {
		return countyname;
	}

	public void setCountyname(String countyname) {
		this.countyname = countyname;
	}

	@Override
	public String toString() {
		return "Cell [cellid=" + cellid + ", enBidrnc_id=" + enBidrnc_id + ", name=" + name + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", covertype=" + covertype + ", coverregion=" + coverregion
				+ ", countyname=" + countyname + "]";
	}

	
}
