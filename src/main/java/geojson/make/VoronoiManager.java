package geojson.make;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.csvreader.CsvReader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder;

import domains.Voronoi;

public class VoronoiManager {
	
	//把最基本的数据先插入数据库
	public static void insertDB() throws IOException{
		Configuration conf=new Configuration().configure();
		StandardServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf=conf.buildSessionFactory(serviceRegistry);
	
		Session session=sf.openSession();
		Transaction tx=session.beginTransaction();
		
		ArrayList<String[]> csvList = new ArrayList<String[]>(); //用来保存数据  
        String csvFilePath = "F:\\play\\xyh\\同步文件\\毕业设计\\天津\\xyh_mysql_table\\voronoi_final.csv";  
        CsvReader reader = new CsvReader(csvFilePath,',',Charset.forName("utf-8"));    //一般用这编码读就可以了      
          
        reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。  
          
        while(reader.readRecord()){ //逐行读入除表头的数据      
            csvList.add(reader.getValues());  
        }              
        reader.close();  
          
        Random random = new Random();
        
        for(int row=0;row<csvList.size();row++){
        	Voronoi voronoi = new Voronoi(csvList.get(row)[0], Double.valueOf(csvList.get(row)[1]), 
        			Double.valueOf(csvList.get(row)[2]), random.nextInt(1000));
        	try {
            	session.save(voronoi);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(csvList.get(row)[0]);
			} 
        	//批量更新
        	if(row % 50 == 0) {
        	        session.flush();
        	        session.clear();
        	}
        }
		tx.commit();
		session.close();
		sf.close();
	}
	
	//把计算出的泰森多边形数据写入数据库
	public static void calpolygon_p(){
		Configuration conf=new Configuration().configure();
		StandardServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf=conf.buildSessionFactory(serviceRegistry);
	
		Session session=sf.openSession();
		Transaction tx=session.beginTransaction();
//		======================================================================
		
		String hql="from Voronoi";
		List<Voronoi> voronois=(List<Voronoi>)session.createQuery(hql).list();
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
			String hql2 = "from Voronoi as v where v.longitude = "+coordinate.x+" and v.latitude = "+coordinate.y;
			
			List<Voronoi> voronois2 = session.createQuery(hql2).list();
			for(int j = 0;j<voronois2.size();j++){
				Voronoi voronoi =voronois2.get(j);
				voronoi.setPolygon_p(polygon_p.toString());
				session.update(voronoi);
				
				//批量更新
	        	if(i % 50 == 0) {
	        	    session.flush();
	        	    session.clear();
	        	}
			}
			
		}
		
//	    ======================================================================
		tx.commit();
		session.close();
		sf.close();
	}
	
	
	public static void main(String[] args) throws Exception {
		//insertDB();
		//calpolygon_p();
		
	}
}
