package daos.Interface;

import java.util.List;

import domains.Timestamp;

public interface TimestampDao extends BaseDao<Timestamp>{

	//通过时间戳来比较
	List<Timestamp> findByTimestamp(String time);
}
