package daos.Impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import daos.Interface.CellDao;
import domains.Cell;

public class CellDaoImpl extends BaseDaoImpl<Cell> implements CellDao {

	private static Logger logger = LogManager.getLogger(CellDaoImpl.class);  
	
	@Override
	public List<Cell> findByCellid(String cellid) {
		// TODO Auto-generated method stub
		logger.warn("cellid号查找辐射小区信息:from Cell as c where c.cellid = "+cellid);
		return find("from Cell as c where c.cellid = ?0",cellid);
	}

	@Override
	public List<Cell> findByCellName(String cellName,int offset, int length) {
		// TODO Auto-generated method stub
		logger.warn("name模糊查找辐射小区信息:from Cell as c where c.name like "+"%"+cellName+"% "+"offset:"+offset+" length:"+length);
		return findByPage("from Cell as c where c.name like ?0",offset,length,"%"+cellName+"%");
	}

	@Override
	public List<Cell> findByEnBidrnc_id(String enBidrnc_id) {
		// TODO Auto-generated method stub
		logger.warn("enBrnc_id号查找辐射小区信息:from Cell as c where c.enBidrnc_id = "+enBidrnc_id);
		return find("from Cell as c where c.enBidrnc_id = ?0",enBidrnc_id);
	}
	
	

}
