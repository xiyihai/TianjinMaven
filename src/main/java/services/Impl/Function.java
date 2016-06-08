package services.Impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import domains.Voronoi;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import vos.VoronoiBS;
import vos.VoronoiDraw;


//这个类中全是静态方法，方便各个service复用函数
public class Function {
		//复用：这里写一个把字符1,2;3,4这样区分的，直接制作成二维数据[[1,2],[3,4]]
		public static List<Double[]> changefromStr(String str){
			List<Double[]> polygonArray = new ArrayList<>();
			
			String[] points = str.split(";");
			
			for(int i=0;i<points.length;i++){
				String[] point = points[i].split(",");
				Double[] array = new Double[2];
				array[0]=Double.valueOf(point[0]);
				array[1]=Double.valueOf(point[1]);
				polygonArray.add(array);
			}
			return polygonArray;
		}
		
		//复用：这里是为了制作前端搜索需要的json数据格式
		public static String makeBSinfoJSON(Map<Voronoi, String> voronois,JSONArray cellidArray){
			JSONArray BSinfos = new JSONArray();
			Iterator<Entry<Voronoi, String>> iter = voronois.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Voronoi,String> entry = (Map.Entry<Voronoi,String>) iter.next();
				Voronoi key = (Voronoi) entry.getKey();
				String val = (String) entry.getValue();
		
				//这里需要注意，新加入的基站还没有polygon字符串，所以生成BS时需要判断一下
				if (key.getPolygon_p()!=null) {
					VoronoiBS bsinfo =new VoronoiBS(val, key.getValue(), key.getLongitude(),
							key.getLatitude(), changefromStr(key.getPolygon_p()));
				
						JSONObject temp = JSONObject.fromObject(bsinfo);
						BSinfos.add(temp);	
				}
		
			}			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("BSinfos", BSinfos);

			jsonObject.put("cellidArray", cellidArray);
			return jsonObject.toString();
		}

		//复用：这里制作泰森多边形需要的json数据格式
		public static String makeVoronoiJSON(List<Voronoi> voronois){
			Iterator<Voronoi> iterator = voronois.iterator();
			List<VoronoiDraw> voronoiDraws = new ArrayList<>();
			
			while (iterator.hasNext()) {
				Voronoi voronoi = iterator.next();
				//这里需要把上述基站中信息取出，封装在VoronoiDraw对象中，这样前端更容易解析
				String enBidrnc_id = voronoi.getEnBidrnc_id();
				Integer value = voronoi.getValue();
				Double[] coordinates = new Double[]{voronoi.getLongitude(),voronoi.getLatitude()};
				//对于polygon_p解析麻烦一些，需要拆分字符串，然后再组装
				List<Double[]> polygon_p = changefromStr(voronoi.getPolygon_p());
				
				VoronoiDraw voronoiDraw = new VoronoiDraw(enBidrnc_id, value, coordinates, polygon_p);
				voronoiDraws.add(voronoiDraw);
			}
			return JSONArray.fromObject(voronoiDraws).toString();
		}
}
