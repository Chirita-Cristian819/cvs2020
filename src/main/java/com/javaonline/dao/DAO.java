package com.javaonline.dao;

import util.HibernateUtil;
import org.hibernate.Session;
import com.javaonline.RegBean;
public class DAO extends HibernateUtil {
 
public RegBean add(RegBean reg) {
	
	Session session = HibernateUtil.createSessionFactory().getCurrentSession();
	session.beginTransaction();
	session.saveOrUpdate(reg);
	session.getTransaction().commit();
	return reg;
}
 
}