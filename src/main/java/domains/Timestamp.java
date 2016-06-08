package domains;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name="tianjin_timestamp")
//表示开启二级缓存，并使用read-only策略
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@Cacheable
public class Timestamp {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="timestamp")
	private java.sql.Timestamp timestamp;
	
	@Column(name="bsdata")
	private String bsdata;

	public java.sql.Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(java.sql.Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getBsdata() {
		return bsdata;
	}

	public void setBsdata(String bsdata) {
		this.bsdata = bsdata;
	}

	public Timestamp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Timestamp(String bsdata) {
		super();
		this.bsdata = bsdata;
	}
	
	
}
