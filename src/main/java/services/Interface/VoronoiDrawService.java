package services.Interface;

public interface VoronoiDrawService {
	
	//先获取数据库中voronoi字段信息，然后封装成前端需要的geojson数据
	String makeGEOJSONfromVoronoi();
	
	//这里获取屏幕内泰森多边形数据，封装成前端需要的json格式
	String makeJSONfromPartVoronoi(double minlng,double maxlng,double minlat,double maxlat);
	
	//根据enBidrnc_id字符串,从数据库中找到对应的cell数据信息
	String makeGEOJSONfromCell(String enBidrnc_id);
	
	//获取数据库中去景区信息，用来初始化按钮
	String makeGEOJSONfromRegion();
	
	//通过前端传来的自定义地区的数据，解析成数据库中需要的字符串
    boolean InsertRegion(String name,Double area,Double longitude,Double latitude,String points);
    
    //通过前端传过来的json对象转成的字符串，解析成数据库中需要的数据
    boolean InsertBS(String bs);
    
    //加入cell，之后需要更新voronoi表
    void  updateVoronoiForIn(String bs);
    
    //从数据库中取出那个时间戳的json数据
    String selectJSONfromTimestamp(String time);
}
