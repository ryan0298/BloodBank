package logic;

import common.ValidationException;
import dal.PersonDAL;
import entity.Person;
import java.util.Arrays;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 * Logic holder for the Person entity.
 *
 * @author Jack Avery
 * @author Ryanh
 * @author Milad Mobini
 */
public class PersonLogic extends GenericLogic<Person, PersonDAL> {

    /**
     * Stores the first_name column name space.
     */
    public static final String FIRST_NAME = "first_name";
    /**
     * Stores the last_name column name space.
     */
    public static final String LAST_NAME = "last_name";
    /**
     * Stores the phone column name space.
     */
    public static final String PHONE = "phone";
    /**
     * Stores the address column name space.
     */
    public static final String ADDRESS = "address";
    /**
     * Stores the birth column name space.
     */
    public static final String BIRTH = "birth";
    /**
     * Stores the id column name space.
     */
    public static final String ID = "person_id";
    /**
     * Stores the name space for the Blood Bank ID.
     */
    public static final String BLOODBANK_ID = "bloodbank_id";

    PersonLogic() {
        super(new PersonDAL());
    }

    /**
     * Returns a list containing every Person.
     *
     * @return List<Person> - All Person entities in the database.
     */
    @Override
    public List<Person> getAll() {
        return get(() -> dal().findAll());
    }

    /**
     * Returns the person with the given ID.
     *
     * @param id int - The ID of the Person
     * @return Person - The Person with the given ID
     */
    @Override
    public Person getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    /**
     * Returns a list of all Persons with the given First Name.
     *
     * @param firstName String - The First Name to filter by.
     * @return List<Person> - All Persons with the given First Name.
     */
    public List<Person> getPersonWithFirstName(String firstName) {
        return get(() -> dal().findByFirstName(firstName));
    }

    /**
     * Returns a list of all Persons with the given Last Name.
     *
     * @param lastName String - The Last Name to filter by.
     * @return List<Person> - All Persons with the given Last Name.
     */
    public List<Person> getPersonWithLastName(String lastName) {
        return get(() -> dal().findByLastName(lastName));
    }

    /**
     * Returns a list of all Persons with the given Phone number.
     *
     * @param phone String - The Phone number to filter by.
     * @return List<Person> - All Persons with the given Phone number.
     */
    public List<Person> getPersonWithPhone(String phone) {
        return get(() -> dal().findByPhone(phone));
    }

    /**
     * Returns a list of all Persons with the given Address.
     *
     * @param address String - The Address to filter by.
     * @return List<Person> - All Persons with the given Address.
     */
    public List<Person> getPersonWithAddress(String address) {
        return get(() -> dal().findByAddress(address));
    }

    /**
     * Returns a list of all Persons with the given Birth Date.
     *
     * @param birth Date - The Birth Date to filter by.
     * @return List<Person> - All Persons with the given Birth Date.
     */
    public List<Person> getPersonWithBirth(Date birth) {
        return get(() -> dal().findByBirth(birth));
    }

    /**
     * Given a map (key, value(s)) of parameters, creates a new Person.
     *
     * @param parameterMap Map<String, String[]> - The parameters for the new
     * Person.
     * @return Person - The new Person entity.
     */
    @Override
    public Person createEntity(Map<String, String[]> parameterMap) {
        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");

        Person entity = new Person();

        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }

        ObjIntConsumer< String> validator = (value, length) -> {
            if (value == null || value.trim().isEmpty() || value.length() > length) {
                String error = "";
                if (value == null || value.trim().isEmpty()) {
                    error = "value cannot be null or empty: " + value;
                }
                if (value.length() > length) {
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException(error);
            }
        };

        String firstName = parameterMap.get(FIRST_NAME)[0];
        String lastName = parameterMap.get(LAST_NAME)[0];
        String phone = parameterMap.get(PHONE)[0];
        String address = parameterMap.get(ADDRESS)[0];
        String birth = parameterMap.get(BIRTH)[0];
        birth = birth.replace("T", " "); //From Ryan's code

        validator.accept(firstName, 50);
        validator.accept(lastName, 50);
        validator.accept(phone, 15);
        validator.accept(address, 100);
        validator.accept(birth, 45);

        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setPhone(phone);
        entity.setAddress(address);

        try {
            entity.setBirth(convertStringToDate(birth));
        } catch (ValidationException ex) {
            entity.setBirth(new Date());
        }
        return entity;
    }

    /**
     * Returns a descriptive list of the Database columns for Person.
     *
     * @return List<String> - Formal names of the database columns.
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "First Name", "Last Name", "Phone Number", "Address", "Date of Birth");
    }

    /**
     * Returns a list containing the column codes for Person.
     *
     * @return List<String> - The column codes for Person.
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, BIRTH);
    }

    /**
     * Returns a list containing stored data for a Person.
     *
     * @param e Person - The Person to extract data from.
     * @return List<?> - The Persons' data, ordered in a list.
     */
    @Override
    public List<?> extractDataAsList(Person e) {
        return Arrays.asList(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getPhone(),
                e.getAddress(),
                e.getBirth(),
                e.getBloodBank(),
                e.getDonationRecordSet());
    }

}
