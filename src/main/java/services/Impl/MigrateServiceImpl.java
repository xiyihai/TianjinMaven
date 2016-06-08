package services.Impl;

import java.util.List;


import daos.Interface.CountyDao;
import domains.County;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.MigrateService;

public class MigrateServiceImpl implements MigrateService {

	private CountyDao countyDao;
	
	public CountyDao getCountyDao() {
		return countyDao;
	}

	public void setCountyDao(CountyDao countyDao) {
		this.countyDao = countyDao;
	}
	
	@Override
	public String makeGEOJSONfromCounty() {
		// TODO Auto-generated method stub
		List<County> countys = countyDao.findAll(County.class);
		
		//开始制作geojson数据
		JSONObject countysInfo = new JSONObject();
		countysInfo.put("type", "CountyInfo");
		JSONArray features = new JSONArray();
		for(int i=0;i<countys.size();i++){
			//这里直接利用Bean转化方便
			features.add(JSONObject.fromObject(countys.get(i)));
		}
		countysInfo.put("features", features);
		
		return countysInfo.toString();
	}

}
