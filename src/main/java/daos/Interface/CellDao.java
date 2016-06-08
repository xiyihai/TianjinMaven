package daos.Interface;

import java.util.List;

import domains.Cell;

public interface CellDao extends BaseDao<Cell> {

	//根据cellid号找到对应的表记录
	List<Cell> findByCellid(String cellid);
	
	//根据name找到对应的表记录,这里只能用模糊查询法,且为了避免很多
	List<Cell> findByCellName(String cellName,int offset,int length);
		
	
	//根据enBidrnc_id找到对应的基站cell集合
	List<Cell> findByEnBidrnc_id(String enBidrnc_id);
	
}
