package infrared.test.repository;

import infrared.test.model.Person;

import java.util.List;


import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepositoryImpl extends HibernateDaoSupport implements PersonRepository {

	/* (non-Javadoc)
	 * @see net.sf.infrared.test.repository.PersonRepository#create(net.sf.infrared.test.model.Person)
	 */
	public void create(Person person) {
		getHibernateTemplate().save(person);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.infrared.test.repository.PersonRepository#update(net.sf.infrared.test.model.Person)
	 */
	public void update(Person person) {
		getHibernateTemplate().merge(person);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.infrared.test.repository.PersonRepository#findAll()
	 */
	@SuppressWarnings("unchecked")
	public List<Person> findAll() {
		return getHibernateTemplate().find("from Person");
	}
	
	/* (non-Javadoc)
	 * @see net.sf.infrared.test.repository.PersonRepository#findByUserName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Person> findByUserName(String userName) {
		return getHibernateTemplate().find("from Person p where p.userName = ?",userName);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.infrared.test.repository.PersonRepository#authenticate(java.lang.String, java.lang.String)
	 */
	public boolean authenticate(String userName,String password) {
		return getSession().createQuery("from Person p where p.userName = :userName and p.password = :password")
							.setParameter("userName",userName)
							.setParameter("password", password)
							.uniqueResult() != null;
	}
	
}
