package services.Interface;

public interface UpdateVoronoiService {
	
	//作为定时任务，更新Voronoi表
	void updateVoronoi();
	
	//作为每隔10分钟，插入一次时间戳数据
	void insertTimeStamp();
}
