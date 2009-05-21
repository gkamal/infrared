package infrared.test.service;

import infrared.test.model.Person;

public interface PersonService {

	public abstract void create(Person person);

	public abstract void update(Person person);

}