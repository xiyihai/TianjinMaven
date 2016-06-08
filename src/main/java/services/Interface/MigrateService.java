package services.Interface;

public interface MigrateService {
	
	//从数据库中获取城市信息并封装成json字符串
	String makeGEOJSONfromCounty();
}
