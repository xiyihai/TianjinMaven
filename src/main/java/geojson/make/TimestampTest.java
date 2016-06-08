package geojson.make;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import domains.Timestamp;


public class TimestampTest {
	
	public static void test() throws IOException{
		Configuration conf=new Configuration().configure();
		StandardServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf=conf.buildSessionFactory(serviceRegistry);
	
		Session session=sf.openSession();
		Transaction tx=session.beginTransaction();
		

		java.sql.Timestamp ts = java.sql.Timestamp.valueOf("2016-04-23 21:28:08");
		
		String hql = "from Timestamp as t where t.timestamp > '"+ts+"'";
		List<Timestamp> timestamps = (List<Timestamp>)session.createQuery(hql).list();
		java.sql.Timestamp time = timestamps.get(0).getTimestamp();
		System.out.println(time);
		
	
		tx.commit();
		session.close();
		sf.close();
	}
	
	public static void stringTotime(){
		String datestr = "2016-04-23 21:28:10.0";
		String datestr2 = "2016-04-23 21:20:10.0";
		
		java.sql.Timestamp ts = java.sql.Timestamp.valueOf(datestr);

		java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf(datestr2);
	 
		System.out.println(ts.getTime());
		 
	}
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		test();
		//stringTotime();
	}

}
