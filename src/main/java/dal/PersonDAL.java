package dal;

import entity.Person;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jack Avery
 */
public class PersonDAL extends GenericDAL<Person> {
    
    public PersonDAL() {
        super(Person.class);
    }
    
    @Override
    public List<Person> findAll() {
        return findResults("Person.findAll", null);
    }
    
    @Override
    public Person findById(int personId) {
        Map<String, Object> map = new HashMap<>();
        map.put( "id", personId);
        return findResult("Person.findById", map);
    }
    
    public List<Person> findByFirstName(String firstName) {
        Map<String, Object> map = new HashMap<>();
        map.put( "firstName", firstName);
        return findResults("Person.findByFirstName", map);
    }
    
    public List<Person> findByLastName(String lastName) {
        Map<String, Object> map = new HashMap<>();
        map.put( "lastName", lastName);
        return findResults("Person.findByLastName", map);
    }
    
    public List<Person> findByPhone(String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put( "phone", phone);
        return findResults("Person.findByPhone", map);
    }
    
    public List<Person> findByAddress(String address) {
        Map<String, Object> map = new HashMap<>();
        map.put( "address", address);
        return findResults("Person.findByAddress", map);
    }
    
    public List<Person> findByBirth(Date birth) {
        Map<String, Object> map = new HashMap<>();
        map.put( "birth", birth);
        return findResults("Person.findByBirth", map);
    }
}
