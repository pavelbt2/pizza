package com.pizza.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.model.HUser;

@Repository("userDao")
@Transactional(propagation=Propagation.REQUIRED)
public class UserDaoImpl extends AbstractDao implements UserDao {
	
	@Override
	public void saveUser(HUser user) {
		getHibernateTemplate().save(user);
	}

//	@Override
//	public void updateUser(HUser user) {
//        getSession().update(user);		
//	}
//	
	@SuppressWarnings("unchecked")
	@Override
	public List<HUser> findAllUsers() {
		// TODO 
//		List<HUser> dummy = new ArrayList<HUser>();
//		HUser user = new HUser();
//		user.setFirstName("Dummy in Dao");
//		user.setLastName("B2");
//		user.setEmail("pavelb@kuku");
//		dummy.add(user);
		//return dummy;
		
		
	      Criteria criteria = getSession().createCriteria(HUser.class);
	      return (List<HUser>) criteria.list();
		
		
	}
//
//	@Override
//	public void deleteUserById(String userId) {
//		Query query = getSession().createSQLQuery("delete from HUser where id = :userId");
//		query.setString("id", userId);
//		query.executeUpdate();
//	}
//
//	@Override
//	public HUser findById(String userId) {
//        Criteria criteria = getSession().createCriteria(HUser.class);
//        criteria.add(Restrictions.eq("id",userId));
//        return (HUser) criteria.uniqueResult();
//	}

}
