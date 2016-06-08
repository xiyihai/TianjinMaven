package geojson.make;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;


import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class GPStoBaidu {
	
/*	pi: 圆周率。
	a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
	ee: 椭球的偏心率。
	x_pi: 圆周率转换量。
	WGS:GPS采用的坐标系  GCJ：谷歌中国采用的坐标系 BD：百度地图采用的坐标系
	
	transformLat(lat, lon): 转换方法，比较复杂，不必深究了。输入：横纵坐标，输出：转换后的横坐标。
	transformLon(lat, lon): 转换方法，同样复杂，自行脑补吧。输入：横纵坐标，输出：转换后的纵坐标。
*/
	
	static double pi = 3.14159265358979324;
	static double a = 6378245.0;
	static double ee = 0.00669342162296594323;
	public final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	public static double[] wgs2bd(double lat, double lon) {
	       double[] wgs2gcj = wgs2gcj(lat, lon);
	       double[] gcj2bd = gcj2bd(wgs2gcj[0], wgs2gcj[1]);
	       return gcj2bd;
	}

	public static double[] gcj2bd(double lat, double lon) {
	       double x = lon, y = lat;
	       double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
	       double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
	       double bd_lon = z * Math.cos(theta) + 0.0065;
	       double bd_lat = z * Math.sin(theta) + 0.006;
	       return new double[] { bd_lat, bd_lon };
	}

	public static double[] bd2gcj(double lat, double lon) {
	       double x = lon - 0.0065, y = lat - 0.006;
	       double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
	       double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
	       double gg_lon = z * Math.cos(theta);
	       double gg_lat = z * Math.sin(theta);
	       return new double[] { gg_lat, gg_lon };
	}

	public static double[] wgs2gcj(double lat, double lon) {
	       double dLat = transformLat(lon - 105.0, lat - 35.0);
	       double dLon = transformLon(lon - 105.0, lat - 35.0);
	       double radLat = lat / 180.0 * pi;
	       double magic = Math.sin(radLat);
	       magic = 1 - ee * magic * magic;
	       double sqrtMagic = Math.sqrt(magic);
	       dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
	       dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
	       double mgLat = lat + dLat;
	       double mgLon = lon + dLon;
	       double[] loc = { mgLat, mgLon };
	       return loc;
	}

	private static double transformLat(double lat, double lon) {
	       double ret = -100.0 + 2.0 * lat + 3.0 * lon + 0.2 * lon * lon + 0.1 * lat * lon + 0.2 * Math.sqrt(Math.abs(lat));
	       ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
	       ret += (20.0 * Math.sin(lon * pi) + 40.0 * Math.sin(lon / 3.0 * pi)) * 2.0 / 3.0;
	       ret += (160.0 * Math.sin(lon / 12.0 * pi) + 320 * Math.sin(lon * pi  / 30.0)) * 2.0 / 3.0;
	       return ret;
	}

	private static double transformLon(double lat, double lon) {
	       double ret = 300.0 + lat + 2.0 * lon + 0.1 * lat * lat + 0.1 * lat * lon + 0.1 * Math.sqrt(Math.abs(lat));
	       ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
	       ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
	       ret += (150.0 * Math.sin(lat / 12.0 * pi) + 300.0 * Math.sin(lat / 30.0 * pi)) * 2.0 / 3.0;
	       return ret;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//GPS:109.431129	18.2941
		//Baidu:109.4418611	18.29890962 都保留8位
		
		ArrayList<String[]> readList = new ArrayList<>(); //用来保存读取的数据  
		ArrayList<String[]> writeList = new ArrayList<>(); //用来保存生成的数据
		
		String csvReadFile = "F:\\play\\xyh\\同步文件\\毕业设计\\天津\\xyh_mysql_table\\voronoi_init.csv";  
	    CsvReader reader = new CsvReader(csvReadFile,',',Charset.forName("utf-8"));    //一般用这编码读就可以了      
	          
	    String csvWriteFile = "F:\\play\\xyh\\同步文件\\毕业设计\\天津\\xyh_mysql_table\\voronoi_final.csv";
        CsvWriter writer = new CsvWriter(csvWriteFile, ',', Charset.forName("utf-8"));  
        
	    
	    reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。  
	          
	    while(reader.readRecord()){ //逐行读入除表头的数据      
	        readList.add(reader.getValues());  
	    }              
	    reader.close();  
	    
	    System.out.println(readList.size());
	    for(int row=0;row<readList.size();row++){
	    	DecimalFormat df = new DecimalFormat("#.00000000");
	    	
	    	String lng = df.format(Double.valueOf(readList.get(row)[1]));
        	String lat = df.format(Double.valueOf(readList.get(row)[2]));
        	
        	double[] bd = wgs2bd(Double.valueOf(lat), Double.valueOf(lng));
        	
        	String[] bdc = new String[]{String.valueOf(bd[1]),String.valueOf(bd[0])};
        	
        	writeList.add(bdc);
	    }
	    
	    System.out.println(writeList.size());
	    for(int row=0;row<writeList.size();row++){
        	writer.writeRecord(writeList.get(row));
	    }
		writer.close();

	}

}
