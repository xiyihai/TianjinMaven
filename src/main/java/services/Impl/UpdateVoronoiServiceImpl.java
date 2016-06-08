package services.Impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder;

import daos.Interface.TimestampDao;
import daos.Interface.VoronoiDao;
import domains.Timestamp;
import domains.Voronoi;
import net.sf.json.JSONArray;
import services.Interface.UpdateVoronoiService;
import vos.VoronoiDraw;

public class UpdateVoronoiServiceImpl implements UpdateVoronoiService {

	private VoronoiDao voronoiDao;
	private TimestampDao timestampDao;
	
	public VoronoiDao getVoronoiDao() {
		return voronoiDao;
	}

	public void setVoronoiDao(VoronoiDao voronoiDao) {
		this.voronoiDao = voronoiDao;
	}

	public TimestampDao getTimestampDao() {
		return timestampDao;
	}

	public void setTimestampDao(TimestampDao timestampDao) {
		this.timestampDao = timestampDao;
	}

	@Override
	public void updateVoronoi() {
		// TODO Auto-generated method stub
		
		List<Voronoi> voronois = voronoiDao.findAll(Voronoi.class);
		int length = voronois.size();
		
		//这个长度和voronois一样
		List<Coordinate> coordinates = new ArrayList<>(length);
				
		for(int i=0; i<length; i++){
			coordinates.add(new Coordinate(voronois.get(i).getLongitude(), voronois.get(i).getLatitude()));
		}
				
		VoronoiDiagramBuilder builder = new VoronoiDiagramBuilder();
		builder.setSites(coordinates);
				
		//这里面包含很多个多边形数据
		GeometryCollection diagram = (GeometryCollection) builder.getDiagram(new GeometryFactory());
		
		//这个大小和length一样
		int totalDia = diagram.getNumGeometries();
		
		//这里每遍历一次，产生的polygon数组和voronoi不是像d3那样一一对应的！！！！！
		//只能通过得到的Polygon获得对应的点Coordinate
				
		//这里制作数据库中需要的字符串
		for(int i=0; i<totalDia; i++){
			//Returns an element Geometry from a GeometryCollection (or this, if the geometry is not a collection).
			Geometry g = diagram.getGeometryN(i);
			
			//返回形成的这个图形的地理坐标,是一个多边形数组，需要把这个数组组成字符串，写到polygon_p字段中
			Coordinate[] coords = g.getCoordinates();
			
			StringBuffer polygon_p = new StringBuffer();
			
			for(int j=0;j<coords.length;j++){
				
				//这里精确到6位就可以,这样格式化输出就是一个string
				DecimalFormat df = new DecimalFormat("#.000000");
				String coordstr = df.format(coords[j].x)+","+df.format(coords[j].y);
				polygon_p.append(coordstr);
				if (j != coords.length-1) {
					polygon_p.append(";");	
				}
			}
			
			//获取到多边形包含的那个离散点的经纬度坐标
			Coordinate coordinate = (Coordinate)g.getUserData();
			
			//根据经纬度找到数据库中那个基站点，这个会出现多个，和海南省不同,所以还需要for一遍
			//这里经纬度相同的点，形成的泰森多边形数据是一样的
			List<Voronoi> voronois2 = voronoiDao.find("from Voronoi as v where v.longitude = "+coordinate.x+" and v.latitude = "+coordinate.y);
			for(int j = 0;j<voronois2.size();j++){
				Voronoi voronoi =voronois2.get(j);
				voronoi.setPolygon_p(polygon_p.toString());
				//更新这一条数据
				voronoiDao.update(voronoi);
			}
		}
	}

	@Override
	public void insertTimeStamp() {
		// TODO Auto-generated method stub
		List<Voronoi> voronois = voronoiDao.findAll(Voronoi.class);
		Iterator<Voronoi> iterator = voronois.iterator();
		List<VoronoiDraw> voronoiDraws = new ArrayList<>();
		
		//产生随机数字作为人数
		Random random = new Random();
		
		while (iterator.hasNext()) {
			Voronoi voronoi = iterator.next();
			//这里需要把上述基站中信息取出，封装在VoronoiDraw对象中，这样前端更容易解析
			String enBidrnc_id = voronoi.getEnBidrnc_id();
			
			Integer value = random.nextInt(1000);
			
			Double[] coordinates = new Double[]{voronoi.getLongitude(),voronoi.getLatitude()};
			//对于polygon_p解析麻烦一些，需要拆分字符串，然后再组装
			List<Double[]> polygon_p = Function.changefromStr(voronoi.getPolygon_p());
			
			VoronoiDraw voronoiDraw = new VoronoiDraw(enBidrnc_id, value, coordinates, polygon_p);
			voronoiDraws.add(voronoiDraw);
		}
		//生成时间戳，并写入数据库
		Timestamp timestamp = new Timestamp(JSONArray.fromObject(voronoiDraws).toString());
		timestampDao.save(timestamp);
		
	}

}
