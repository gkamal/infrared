package infrared.test.repository;

import infrared.test.model.Person;

import java.sql.SQLException;

import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.setup.InfraREDLifeCycleListener;
import net.sf.infrared.aspects.api.ApiContext;
import net.sf.infrared.base.model.ExecutionTimer;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class PersonRepositoryImplTest extends AbstractTransactionalSpringContextTests {

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	public void testCrud() throws ClassNotFoundException, SQLException{
		
		InfraREDLifeCycleListener lifeCycleListener = new InfraREDLifeCycleListener();
		lifeCycleListener.initialized("infrared-test","test", "infrared-agent.properties");

        ApiContext ctx = new ApiContext(PersonRepositoryImplTest.class.getName(), "testCrud","test");
        ExecutionTimer timer = new ExecutionTimer(ctx);

		MonitorFactory.getFacade().recordExecutionBegin(timer);
		Person person = new Person("kamal","abc123"); 
		personRepository.create(person);
		flush();
		assertTrue(personRepository.authenticate("kamal", "abc123"));
		person.setPassword(";lkj123");
		personRepository.update(person);
		flush();
		
		MonitorFactory.getFacade().recordExecutionEnd(timer);
	}

	private void flush() {
		sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	protected String[] getConfigLocations() {
		return new String[] {"classpath:applicationContext.xml"};
	}

	
	
}
