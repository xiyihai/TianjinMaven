package daos.Impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import daos.Interface.TimestampDao;
import domains.Timestamp;

public class TimestampDaoImpl extends BaseDaoImpl<Timestamp> implements TimestampDao {
	private static Logger logger = LogManager.getLogger(TimestampDaoImpl.class);  
	
	@Override
	public List<Timestamp> findByTimestamp(String time) {
		// TODO Auto-generated method stub
		//这里还是通过sql语句来查找，因为在java中 java.sql.timestamp不能和字符串比较大小，而mysql数据库中可以直接比较
		//所以domain映射只不过为了方便取出数据库中的值，而根据前端传入的string，还是得通过sql语句执行
		logger.warn("时间戳查找历史数据:select * from tianjin_timestamp where date_format(timestamp,'%Y-%m-%d %H:%i') = '"+time+"'");
		return findBySql("select * from tianjin_timestamp where date_format(timestamp,'%Y-%m-%d %H:%i') = '"+time+"'", Timestamp.class);
	}
}
