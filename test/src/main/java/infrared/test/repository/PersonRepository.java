package infrared.test.repository;

import infrared.test.model.Person;

import java.util.List;


public interface PersonRepository {

	public abstract void create(Person person);

	public abstract void update(Person person);

	@SuppressWarnings("unchecked")
	public abstract List<Person> findAll();

	@SuppressWarnings("unchecked")
	public abstract List<Person> findByUserName(String userName);

	public abstract boolean authenticate(String userName, String password);

}