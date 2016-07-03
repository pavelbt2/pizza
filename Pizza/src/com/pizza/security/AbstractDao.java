package com.pizza.security;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

public abstract class AbstractDao extends HibernateDaoSupport {
	  
    @Autowired
    public void anyMethodName(SessionFactory sessionFactory)
    {
        setSessionFactory(sessionFactory);
    }
    
    protected Session getSession() {
    	return getHibernateTemplate().getSessionFactory().getCurrentSession();
    }
}