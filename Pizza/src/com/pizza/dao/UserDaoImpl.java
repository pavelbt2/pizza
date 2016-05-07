package com.pizza.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pizza.general.UserAlreadyExistsError;
import com.pizza.model.HUser;

@Repository("userDao")
@Transactional(propagation=Propagation.REQUIRED)
public class UserDaoImpl extends AbstractDao implements UserDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<HUser> findAllUsers() {
		Criteria criteria = getSession().createCriteria(HUser.class);
		return (List<HUser>) criteria.list();				
	}
	
	@Override
	public void saveUser(HUser user) throws UserAlreadyExistsError {
		try {
			getHibernateTemplate().save(user);
		} catch (ConstraintViolationException | DataIntegrityViolationException e) {
			// ConstraintViolationException - this is the hibernate native exception
			// DataIntegrityViolationException - this is thrown when using hibernate template (spring)
			throw new UserAlreadyExistsError();
		}
	}


//	@Override
//	public void updateUser(HUser user) {
//        getSession().update(user);		
//	}
//	

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
