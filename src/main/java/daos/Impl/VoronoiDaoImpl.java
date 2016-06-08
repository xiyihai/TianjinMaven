package daos.Impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import daos.Interface.VoronoiDao;
import domains.Voronoi;

public class VoronoiDaoImpl extends BaseDaoImpl<Voronoi> implements VoronoiDao {
	private static Logger logger = LogManager.getLogger(VoronoiDaoImpl.class);  
	
	@Override
	public List<Voronoi> findByRange(double minlng, double maxlng, double minlat, double maxlat) {
		// TODO Auto-generated method stub
		logger.warn("局部范围内搜索基站:from Voronoi as v where v.longitude between "+minlng+" and "+maxlng+" and v.latitude between "+minlat+" and "+maxlat);
		return find("from Voronoi as v where v.longitude between ?0 and ?1 and v.latitude between ?2 and ?3",minlng,maxlng,minlat,maxlat);
	}

	@Override
	public List<Voronoi> findByenBidrnc_id(String enBidrnc_id) {
		// TODO Auto-generated method stub
		logger.warn("enBrnc_id号查找基站信息:from Voronoi as v where v.enBidrnc_id = "+enBidrnc_id);
		return find("from Voronoi as v where v.enBidrnc_id = ?0",enBidrnc_id);
	}
	
}
