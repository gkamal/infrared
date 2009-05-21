package infrared.test.service;

import infrared.test.model.Person;
import infrared.test.repository.PersonRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=false)
public class PersonServiceImpl implements PersonService {
	
	private PersonRepository personRepository;

	/* (non-Javadoc)
	 * @see net.sf.infrared.test.service.PersonService#create(net.sf.infrared.test.model.Person)
	 */
	public void create(Person person) {
		personRepository.create(person);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.infrared.test.service.PersonService#update(net.sf.infrared.test.model.Person)
	 */
	public void update(Person person) {
		personRepository.update(person);
	}
	
}
