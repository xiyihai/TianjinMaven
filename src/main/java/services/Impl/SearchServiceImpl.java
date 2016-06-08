package services.Impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import daos.Interface.CellDao;
import daos.Interface.RegionDao;
import daos.Interface.VoronoiDao;
import domains.Cell;
import domains.Voronoi;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.SearchService;
import vos.VoronoiBS;

public class SearchServiceImpl implements SearchService {

	private static Logger logger = LogManager.getLogger(SearchServiceImpl.class);  
	
	private VoronoiDao voronoiDao;
	private CellDao cellDao;
	private RegionDao regionDao;
	
	public VoronoiDao getVoronoiDao() {
		return voronoiDao;
	}


	public void setVoronoiDao(VoronoiDao voronoiDao) {
		this.voronoiDao = voronoiDao;
	}


	public CellDao getCellDao() {
		return cellDao;
	}


	public void setCellDao(CellDao cellDao) {
		this.cellDao = cellDao;
	}


	public RegionDao getRegionDao() {
		return regionDao;
	}


	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}

	@Override
	public String findBSbyCellid(String cellid) {
		// TODO Auto-generated method stub
		
		//先通过id找到cell,只可能找到一个
		List<Cell> cells = cellDao.findByCellid(cellid);
		
		//通过enbid在voronoi表中查找到经纬度，只可能找到一个
		List<Voronoi> voronois = voronoiDao.findByenBidrnc_id(cells.get(0).getEnBrnc_id());
		
		//需要把这个字符串分割成一个二维数组的点传给前端[[1,2],[4,6]...]
		String polygonStr = voronois.get(0).getPolygon_p();
		
		//开始封装json数据
		VoronoiBS bsinfo =new VoronoiBS(cells.get(0).getName(), voronois.get(0).getValue(),
				voronois.get(0).getLongitude(), voronois.get(0).getLatitude(), Function.changefromStr(polygonStr));
		
		JSONArray BSinfos = new JSONArray();
		JSONObject temp = JSONObject.fromObject(bsinfo);
		
		BSinfos.add(temp);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("BSinfos", BSinfos);
		//为了能够删除搜索到的基站，必须把cellid作为数组存起来
		JSONArray cellidArray = new JSONArray();
		cellidArray.add(cellid);
		
		jsonObject.put("cellidArray", cellidArray);
		return jsonObject.toString();
	}

	@Override
	public String findBSbyCellName(String cellName,int offset,int length) {
		// TODO Auto-generated method stub
		
		//先通过name找到list<cell>
		List<Cell> cells = cellDao.findByCellName(cellName,offset,length);
		
		//再依次通过enBidrnc_id找到对应的Voronoi基站信息，这里经纬度相同的基站需要去重
		//去重工作利用HashMap集合<Voronoi,cellName>，当然先要重写voronoi类的equal和hashcode方法
		//这里的去重和海南去重完全不是一个概念，海南是同一个基站上面有多个cell，这里是不仅是这个原因，不同enBid可能对应同一个基站(经纬度相同)
		Map<Voronoi,String> voronois = new HashMap<>(); 
		

		//放置cellid数据，为了方便删除这个cell
		JSONArray cellidArray = new JSONArray();
		
		for(int i=0;i<cells.size();i++){
			List<Voronoi> vo = voronoiDao.findByenBidrnc_id(cells.get(i).getEnBrnc_id());			
			//因为只可能找到一个，所以get(0)x
			Voronoi v = vo.get(0);
			
			//这里需要利用key值相同来去重，key相等则字符串不用加入，不等则直接加进去
			//但这里有个问题，找到不同enBid，但基站同一个，那么后者没有加入的意义
			if (!voronois.containsKey(v)) {
				voronois.put(v , cells.get(i).getName());
			}
			
			//这里需要把cellid组装成数组
			cellidArray.add(cells.get(i).getCellid());
		}
		//======================================================================
		//开始制作json数据
		return Function.makeBSinfoJSON(voronois,cellidArray);
	}

	//这里返回去的字符串会一样，这里因为海南给的不同，而天津给的数据一样的缘故
	@Override
	public String matchCellName(String cellName,int offset,int length) {
		// TODO Auto-generated method stub
		List<Cell> cells = cellDao.findByCellName(cellName, offset, length);
		//这里又和海南数据不同，这里不同cellid name会一样，所以需要对name去重，否则前端显示会有重叠数据
		Set<String> names = new HashSet<>();
		for(int i=0;i<cells.size();i++){
			names.add(cells.get(i).getName());
		}
		
		JSONArray cellNameArray = new JSONArray();
		
		Iterator<String> iterator = names.iterator();
		while (iterator.hasNext()) {
			String name =  iterator.next();
			JSONObject temp = new JSONObject();
			temp.put("id", name);
			temp.put("text", name);
			cellNameArray.add(temp);
		}
		
		return cellNameArray.toString();
	}

	@Override
	public String findBSByS2NameArray(String[] cellNames) {
		// TODO Auto-generated method stub
		
		//这里也需要用Map来去重，因为传过来的字符串数组会映射在同一个基站上
		Map<Voronoi,String> voronois = new HashMap<>(); 
		
		//声明cellid数组
		JSONArray cellidArray = new JSONArray();
				
		for(int i=0;i<cellNames.length;i++){
			List<Cell> cells = cellDao.findByCellName(cellNames[i], 0, 20); //这里和海南又不相同，能找到多个，这里20只是一个上限

			for(int j=0;j<cells.size();j++){
				cellidArray.add(cells.get(j).getCellid());
			}
			
			Voronoi v = voronoiDao.findByenBidrnc_id(cells.get(0).getEnBrnc_id()).get(0);
			
			if (!voronois.containsKey(v)) {
				voronois.put(v , cellNames[i]);
			}
		}
		//开始制作json数据
		return Function.makeBSinfoJSON(voronois,cellidArray);
	}
	
	@Override
	public Set<String> deleteBS(String[] cellidArray) {
		// TODO Auto-generated method stub
		
		//用来暂时存储enBid，使用set是因为，删除的cellid虽然多个，但肯定对应在一个enBid上，这样可以去重
		Set<String> enBids = new HashSet<>();
		
		//思路是先根据cellid找到实体，在把实体删除
		for(int i=0;i<cellidArray.length;i++){
			Cell cell = cellDao.findByCellid(cellidArray[i]).get(0);
			enBids.add(cell.getEnBrnc_id());
			logger.warn("删除的辐射小区信息:"+cell.toString());
			cellDao.delete(cell);
		}
		return enBids;
	}

	@Override
	public void updateVoronoiForOut(Set<String> enBids) {
		// TODO Auto-generated method stub
		
		//根据enBid值，修改Voronoi表
		Iterator<String> iterator = enBids.iterator();
		while (iterator.hasNext()) {
			String enBid = iterator.next();
			List<Cell> cells = cellDao.findByEnBidrnc_id(enBid);
			if (cells.size() == 0) {
				//若是在cell表中没有了，这代表你需要把Voronoi表中的enBid删除
				Voronoi voronoi = voronoiDao.findByenBidrnc_id(enBid).get(0);
				logger.warn("删除辐射小区信息导致删除的基站信息:"+voronoi.toString());
				voronoiDao.delete(voronoi);
			}
		}
	
	}
	
}
