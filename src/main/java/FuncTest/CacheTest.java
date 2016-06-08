package FuncTest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import domains.County;



public class CacheTest {
	
	public static void cache(SessionFactory sf){
		
		Session session=sf.openSession();		
		
		County county = (County)session.get(County.class, 1);
		System.out.println(county.getName());
		session.close();
		
		Session session2 = sf.openSession();
		County county2=(County)session2.get(County.class,1);
		System.out.println(county2.getName());
		County county3=(County)session2.get(County.class,2);
		System.out.println(county3.getName());
		
		session2.close();
	}
	
	public static void queryCache(SessionFactory sf){
		Session session=sf.openSession();		
		String hql = "from County";
		session.createQuery(hql).setCacheable(true).list();
		session.close();
		Session session2 = sf.openSession();
		session2.createQuery("from County").setCacheable(true).list();
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Configuration conf=new Configuration().configure();
		StandardServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf=conf.buildSessionFactory(serviceRegistry);
	
		cache(sf);
		queryCache(sf);
		

		sf.close();
	}

}
