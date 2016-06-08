package geojson.make;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_ADDPeer;

import domains.County;

public class CountyManager {

	public static void changeToBd() throws IOException{
		ArrayList<String[]> readList = new ArrayList<>(); //用来保存读取的数据  
		ArrayList<String[]> writeList = new ArrayList<>(); //用来保存生成的数据
		
		String csvReadFile = "F:\\play\\xyh\\同步文件\\毕业设计\\天津\\xyh_mysql_table\\county_init.csv";  
	    CsvReader reader = new CsvReader(csvReadFile,',',Charset.forName("utf-8"));    //一般用这编码读就可以了      
	          
	    String csvWriteFile = "F:\\play\\xyh\\同步文件\\毕业设计\\天津\\xyh_mysql_table\\county_final.csv";
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
        	
        	double[] bd = GPStoBaidu.wgs2bd(Double.valueOf(lat), Double.valueOf(lng));
        	
        	String[] bdc = new String[]{String.valueOf(bd[1]),String.valueOf(bd[0])};
        	
        	writeList.add(bdc);
	    }
	    
	    System.out.println(writeList.size());
	    for(int row=0;row<writeList.size();row++){
        	writer.writeRecord(writeList.get(row));
	    }
		writer.close();

	}
	
	public static void insertDB() throws IOException{
		Configuration conf=new Configuration().configure();
		StandardServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf=conf.buildSessionFactory(serviceRegistry);
	
		Session session=sf.openSession();
		Transaction tx=session.beginTransaction();
		
		
		ArrayList<String[] > csvList = new ArrayList<>();
		
		String csvpath = "F:\\play\\xyh\\同步文件\\毕业设计\\天津\\xyh_mysql_table\\county_final.csv";
		CsvReader reader = new CsvReader(csvpath, ',', Charset.forName("utf-8"));
		
		reader.readHeaders();
		while(reader.readRecord()){ //逐行读入除表头的数据      
            csvList.add(reader.getValues());  
        }              
        reader.close();  
          
        for(int row=0;row<csvList.size();row++){
        	County county = new County(csvList.get(row)[2], Double.valueOf(csvList.get(row)[0]),Double.valueOf(csvList.get(row)[1]));
        	session.save(county);
        }
		tx.commit();
		session.close();
		sf.close();
	}
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		//insertDB();
		//changeToBd();
	}

}
