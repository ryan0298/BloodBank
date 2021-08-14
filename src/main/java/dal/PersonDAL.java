package dal;

import entity.Person;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAL entity for the Person entity.
 *
 * @author Jack Avery
 */
public class PersonDAL extends GenericDAL<Person> {

    public PersonDAL() {
        super(Person.class);
    }

    /**
     * Returns a list containing every Person.
     *
     * @return List<Person> - All Person entities in the database.
     */
    @Override
    public List<Person> findAll() {
        return findResults("Person.findAll", null);
    }

    /**
     * Returns the person with the given ID.
     *
     * @param id int - The ID of the Person
     * @return Person - The Person with the given ID
     */
    @Override
    public Person findById(int personId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", personId);
        return findResult("Person.findById", map);
    }

    /**
     * Returns a list of all Persons with the given First Name.
     *
     * @param firstName String - The First Name to filter by.
     * @return List<Person> - All Persons with the given First Name.
     */
    public List<Person> findByFirstName(String firstName) {
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", firstName);
        return findResults("Person.findByFirstName", map);
    }

    /**
     * Returns a list of all Persons with the given Last Name.
     *
     * @param lastName String - The Last Name to filter by.
     * @return List<Person> - All Persons with the given Last Name.
     */
    public List<Person> findByLastName(String lastName) {
        Map<String, Object> map = new HashMap<>();
        map.put("lastName", lastName);
        return findResults("Person.findByLastName", map);
    }

    /**
     * Returns a list of all Persons with the given Phone number.
     *
     * @param phone String - The Phone number to filter by.
     * @return List<Person> - All Persons with the given Phone number.
     */
    public List<Person> findByPhone(String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        return findResults("Person.findByPhone", map);
    }

    /**
     * Returns a list of all Persons with the given Address.
     *
     * @param address String - The Address to filter by.
     * @return List<Person> - All Persons with the given Address.
     */
    public List<Person> findByAddress(String address) {
        Map<String, Object> map = new HashMap<>();
        map.put("address", address);
        return findResults("Person.findByAddress", map);
    }

    /**
     * Returns a list of all Persons with the given Birth Date.
     *
     * @param birth Date - The Birth Date to filter by.
     * @return List<Person> - All Persons with the given Birth Date.
     */
    public List<Person> findByBirth(Date birth) {
        Map<String, Object> map = new HashMap<>();
        map.put("birth", birth);
        return findResults("Person.findByBirth", map);
    }
}
