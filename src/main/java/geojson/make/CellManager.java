package geojson.make;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import domains.Cell;

public class CellManager {

	//把生成的cell数据写入数据库
	public static void insertSB() throws IOException{
		Configuration conf=new Configuration().configure();
		StandardServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf=conf.buildSessionFactory(serviceRegistry);
	
		Session session=sf.openSession();
		Transaction tx=session.beginTransaction();
		
		ArrayList<String[]> csvList = new ArrayList<String[]>(); //用来保存数据  
        String csvFilePath = "F:\\play\\xyh\\同步文件\\毕业设计\\天津\\xyh_mysql_table\\cellname_final.csv";  
        CsvReader reader = new CsvReader(csvFilePath,',',Charset.forName("utf-8"));    //一般用这编码读就可以了      
          
        reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。  
          
        while(reader.readRecord()){ //逐行读入除表头的数据      
            csvList.add(reader.getValues());  
        }              
        reader.close();  
          
        for(int row=0;row<csvList.size();row++){
        	Cell cell = new Cell(csvList.get(row)[0], csvList.get(row)[1], csvList.get(row)[2], csvList.get(row)[3],
        			csvList.get(row)[4], csvList.get(row)[7], Double.valueOf(csvList.get(row)[5]), Double.valueOf(csvList.get(row)[6]));
        	
        	try {
            	session.save(cell);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(csvList.get(row)[2]);
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

	//在原始表中提取出有用数据，放入新的表，方便之后写入数据库
	public static void createTable() throws IOException {
		ArrayList<String[]> readList = new ArrayList<>(); //用来保存读取的数据  
		ArrayList<String[]> writeList = new ArrayList<>(); //用来保存生成的数据
		
		String csvReadFile = "F:\\play\\xyh\\同步文件\\毕业设计\\天津\\xyh_mysql_table\\cellname_init.csv";  
	    CsvReader reader = new CsvReader(csvReadFile,',',Charset.forName("utf-8"));    //一般用这编码读就可以了      
	          
	    String csvWriteFile = "F:\\play\\xyh\\同步文件\\毕业设计\\天津\\xyh_mysql_table\\cellname_final.csv";
        CsvWriter writer = new CsvWriter(csvWriteFile, ',', Charset.forName("utf-8"));  
        
	    
	    reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。  
	          
	    while(reader.readRecord()){ //逐行读入除表头的数据      
	        readList.add(reader.getValues());  
	    }              
	    reader.close();  
	          
        for(int row=0;row<readList.size();row++){
        	String nameid = readList.get(row)[0];
        	//需要把这个nameid分割出来，然后这个id的后三位去掉之后变成enbrnc_id,最后形成一个String[] cellid enbrnc_id name
        	//天津塘沽鑫达中心信源东盐路F[1036292133]
        	//天津万事通食品信源滨海职业学校F-HLH[1036293][1036293131]

        	//思路：先用 [ 作为分割符，若是长度大于2，则中间那个需要去掉，这样剩余：name cellid] ,把]也去掉
        	//剩余name cellid ,把cellid除去后3位其余获取出来作为enbrnc_id
        	
        	String[] strings = nameid.split("\\[");
        	
        	String name = strings[0];
        	String cellid = strings[strings.length-1].substring(0, strings[strings.length-1].length()-1);
        	
        	String enbrnc_id = cellid.substring(0, cellid.length()-3);
        	
        	String[] column = new String[]{cellid,enbrnc_id,name};
        	writeList.add(column);
        }
        
        for(int row=0;row<writeList.size();row++){
        	writer.writeRecord(writeList.get(row));
//        	System.out.println(writeList.get(row)[0]+" "+writeList.get(row)[1]+" "+writeList.get(row)[2]);
        }
        
        writer.close();
	}
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
	}

}
