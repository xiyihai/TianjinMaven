package daos.Interface;

import java.util.List;

import domains.Voronoi;

public interface VoronoiDao extends BaseDao<Voronoi> {

	//通过设置范围来寻找屏幕内基站点
	List<Voronoi> findByRange(double minlng,double maxlng,double minlat,double maxlat);
	
	//通过enBidrnc_id找到对应的泰森多边形
	List<Voronoi> findByenBidrnc_id(String enBidrnc_id);
	
}
