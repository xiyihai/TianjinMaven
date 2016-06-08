package services.Impl;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;

import daos.Interface.CellDao;
import daos.Interface.RegionDao;
import daos.Interface.TimestampDao;
import daos.Interface.VoronoiDao;
import domains.Cell;
import domains.Region;
import domains.Timestamp;
import domains.Voronoi;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.VoronoiDrawService;
import vos.RegionVos;

public class VoronoiDrawServiceImpl implements VoronoiDrawService {

	private static Logger logger = LogManager.getLogger(VoronoiDrawServiceImpl.class);  
	
	private VoronoiDao voronoiDao;
	private CellDao cellDao;
	private RegionDao regionDao;
	private TimestampDao timestampDao;
	
	public TimestampDao getTimestampDao() {
		return timestampDao;
	}

	public void setTimestampDao(TimestampDao timestampDao) {
		this.timestampDao = timestampDao;
	}

	public RegionDao getRegionDao() {
		return regionDao;
	}

	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}

	public CellDao getCellDao() {
		return cellDao;
	}

	public void setCellDao(CellDao cellDao) {
		this.cellDao = cellDao;
	}


	public VoronoiDao getVoronoiDao() {
		return voronoiDao;
	}

	public void setVoronoiDao(VoronoiDao voronoiDao) {
		this.voronoiDao = voronoiDao;
	}

	
	@Cacheable(value="Voronoi")
	@Override
	public String makeGEOJSONfromVoronoi() {
		// TODO Auto-generated method stub
		List<Voronoi> voronois = voronoiDao.findAll(Voronoi.class);
		return Function.makeVoronoiJSON(voronois);
	}


	@Override
	public String makeJSONfromPartVoronoi(double minlng,double maxlng,double minlat,double maxlat) {
		List<Voronoi> voronois = voronoiDao.findByRange(minlng, maxlng, minlat, maxlat);
		//这边封装数据应该和上面全局封装是一样的，所以需要提取出一个方法进行复用
		
		return Function.makeVoronoiJSON(voronois);
	}
    
	@Override
	public String makeGEOJSONfromCell(String enBidrnc_id) {
		// TODO Auto-generated method stub
		//开始封装json数据
		
		JSONObject celljson = new JSONObject();
		
		List<Cell> cells = cellDao.findByEnBidrnc_id(enBidrnc_id);
			
		//这里直接将List转换为json对象
		JSONArray cellArray = JSONArray.fromObject(cells);

		celljson.put("cells", cellArray);
		return celljson.toString();
	}

	@Override
	public String makeGEOJSONfromRegion() {
		// TODO Auto-generated method stub
		List<Region> regions = regionDao.findAll(Region.class);
		List<RegionVos> regionsVos = new ArrayList<>();
		
		//开始封装geojson数据
		Iterator<Region> iterator = regions.iterator();
		while (iterator.hasNext()) {
			Region region = iterator.next();
			Integer id = region.getId();
			Double longitude = region.getLongitude();
			Double latitude = region.getLatitude();
			String name = region.getName();
			String image = region.getImage();
			Double area = region.getArea();
			String[] towerids = new String[]{};
			//把towerids分割成数组,若是没有则不分割
			if (region.getTowerids()!=null) {
				towerids = region.getTowerids().split(",");
			}
			//封装coordinate形成polygon_p可能要麻烦一些
			List<Double[]> coordinates = Function.changefromStr(region.getPoints());
			
			RegionVos regionVos = new RegionVos(id, longitude, latitude, name, image, area, towerids, coordinates);
			
			regionsVos.add(regionVos);
		}
		
		return JSONArray.fromObject(regionsVos).toString();

	}

	@Override
	public boolean InsertRegion(String name, Double area, Double longitude, Double latitude, String points) {
		// TODO Auto-generated method stub
		//			解析数据
		StringBuffer stringBuffer = new StringBuffer();
		JSONArray array = JSONArray.fromObject(points);
		for(int i=0;i<array.size();i++){
			JSONArray point = array.getJSONArray(i);
			stringBuffer.append(point.get(0)).append(",").append(point.get(1));
			if ((i+1)!=array.size()) {
				stringBuffer.append(";");
			}
		}
//		=======================================================
//		写入数据库
		Region divRegion = new Region(name,area,stringBuffer.toString(),longitude,latitude);
		//记录写入信息日志
		logger.warn("插入特定关注区信息："+divRegion.toString());
		return regionDao.save(divRegion);

	}

	@Override
	public boolean InsertBS(String bs) {
		// TODO Auto-generated method stub
		JSONObject bsObject =  JSONObject.fromObject(bs);
		
		Cell cell = new Cell(bsObject.getString("_cellid"), bsObject.getString("_enBidrnc_id"),bsObject.getString("_name"),
				bsObject.getString("_covertype"), bsObject.getString("_coverregion"), bsObject.getString("_countyName"), 
				Double.valueOf(bsObject.getString("_lng")),Double.valueOf(bsObject.getString("_lat")));
		logger.warn("插入新辐射小区信息："+cell.toString());
		return cellDao.save(cell);
	}

	@Override
	public void updateVoronoiForIn(String bs) {
		// TODO Auto-generated method stub
		JSONObject bsObject = JSONObject.fromObject(bs);
		
		//判断enBid是否在Voronoi表中
		List<Voronoi> voronois = voronoiDao.findByenBidrnc_id(bsObject.getString("_enBidrnc_id"));
		if (voronois.size()==0) {
			//则需要在Voronoi表中插入一条新的数据
			
			Double lng = Double.valueOf(bsObject.getString("_lng"));
			Double lat = Double.valueOf(bsObject.getString("_lat"));
			
			//转换GPS经纬度成BD经纬度,这里经纬度信息需要交换位置
			double[] coordinate = GPStoBD.wgs2bd(lat, lng);
			DecimalFormat df = new DecimalFormat("#.00000000");
	
			Random random = new Random();
			Voronoi voronoi = new Voronoi(bsObject.getString("_enBidrnc_id"), Double.valueOf(df.format(coordinate[1])), Double.valueOf(df.format(coordinate[0])), random.nextInt(1000));
			
			logger.warn("新增辐射小区导致增加的基站信息："+voronoi.toString());
			//加入到表中去
			voronoiDao.save(voronoi);
		}
	}


	//这里针对历史缓存数据进行缓存
	@Cacheable(value="Timestamp")
	@Override
	public String selectJSONfromTimestamp(String time) {
		// TODO Auto-generated method stub
		List<Timestamp> timestamps = timestampDao.findByTimestamp(time);
		if (timestamps.size()>0) {
			Timestamp timestamp = timestamps.get(0);
			return timestamp.getBsdata();
		}else {
			return null;
		}
	}


}
