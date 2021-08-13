package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import entity.BloodBank;
import entity.DonationRecord;
import entity.Person;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Jack Avery
 */
public class PersonLogicTest {
    
    private PersonLogic logic;
    private Person expectedEntity;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat("/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test");
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }
    
    @BeforeEach
    final void setUp() throws Exception {

        logic = LogicFactory.getFor("Person");
        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction
        em.getTransaction().begin();
        //em.find takes two arguments, the class type of return result and the primery key.
        
        //create the desired entity
        Person entity = new Person();
        entity.setFirstName("Jane");
        entity.setLastName("Doe");
        entity.setPhone("613");
        entity.setAddress("123 Street");
        entity.setBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-13"));
        em.persist(entity);
        
        //create first dependency
        BloodBank bb = em.find(BloodBank.class, 1);
        //if result is null create the entity and persist it
        if (bb == null) {
            //create object
            bb = new BloodBank();
            bb.setName("JUNIT");
            bb.setPrivatelyOwned(true);
            bb.setEstablished(logic.convertStringToDate("1111-11-11 11:11:11"));
            bb.setEmplyeeCount(111);
            //set up 1:1 relation
            bb.setOwner(entity);
            //persist the dependency first
            em.persist(bb);
        }
        
        //add first dependency to the desired entity
        entity.setBloodBank(bb);
        
        //create second dependency
        DonationRecord dr = em.find(DonationRecord.class, 1);
        if (dr == null) {
            dr = new DonationRecord();
            dr.setAdministrator("Jack");
            dr.setCreated(new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-13"));
            dr.setHospital("college");
            dr.setTested(true);
            //complete 1:1 relation
            dr.setPerson(entity);
            //persist the dependency
            em.persist(dr);
        }
        Set<DonationRecord> drs = Set.of(dr);
        //add second dependency
        entity.setDonationRecordSet(drs);
        
        //add desired entity to hibernate, entity is now managed.
        //we use merge instead of add so we can get the managed entity.
        expectedEntity = em.merge(entity);
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if (expectedEntity != null) {
            logic.delete(expectedEntity);
        }
    }

    @Test
    final void testGetAll() {
        //get all the records from the DB
        List<Person> list = logic.getAll();
        //store the size of list, this way we know how many records exits in DB
        int originalSize = list.size();

        //make sure record was created successfully
        assertNotNull(expectedEntity);
        //delete the new record
        logic.delete(expectedEntity);

        //get all records again
        list = logic.getAll();
        //the new size of records must be one less
        assertEquals(originalSize - 1, list.size());
    }
    
    private void assertPersonEquals(Person expected, Person actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getPhone(), actual.getPhone());
        assertEquals(expected.getBirth(), actual.getBirth());
        assertEquals(expected.getBloodBank(), actual.getBloodBank());
        assertEquals(expected.getDonationRecordSet(), actual.getDonationRecordSet());
    }
    
    @Test
    final void testGetWithId() {
        //using the id of test record get another record from logic
        Person returnedPerson = logic.getWithId(expectedEntity.getId());

        //the two records (testAcounts and returnedDonationRecords) must be the same
        assertPersonEquals(expectedEntity, returnedPerson);
    }
    
    @Test
    final void testGetPersonWithFirstName() {
        int foundFull = 0;
        List<Person> returnedPeople = logic.getPersonWithFirstName(expectedEntity.getFirstName());
        for (Person person : returnedPeople) {
            assertEquals(expectedEntity.getFirstName(), person.getFirstName());
            //exactly one record must be the same
            if (person.getId().equals(expectedEntity.getId())) {
                assertPersonEquals(expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }
    
    @Test
    final void testGetPersonWithLastName() {
        int foundFull = 0;
        List<Person> returnedPeople = logic.getPersonWithLastName(expectedEntity.getLastName());
        for (Person person : returnedPeople) {
            assertEquals(expectedEntity.getLastName(), person.getLastName());
            //exactly one record must be the same
            if (person.getId().equals(expectedEntity.getId())) {
                assertPersonEquals(expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }
    
    @Test
    final void testGetPersonWithPhone() {
        int foundFull = 0;
        List<Person> returnedPeople = logic.getPersonWithPhone(expectedEntity.getPhone());
        for (Person person : returnedPeople) {
            assertEquals(expectedEntity.getPhone(), person.getPhone());
            //exactly one record must be the same
            if (person.getId().equals(expectedEntity.getId())) {
                assertPersonEquals(expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }
    
    @Test
    final void testGetPersonWithAddress() {
        int foundFull = 0;
        List<Person> returnedPeople = logic.getPersonWithAddress(expectedEntity.getAddress());
        for (Person person : returnedPeople) {
            assertEquals(expectedEntity.getAddress(), person.getAddress());
            //exactly one record must be the same
            if (person.getId().equals(expectedEntity.getId())) {
                assertPersonEquals(expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }
    
    @Test
    final void testGetPersonWithBirth() {
        int foundFull = 0;
        List<Person> returnedPeople = logic.getPersonWithBirth(expectedEntity.getBirth());
        for (Person person : returnedPeople) {
            assertEquals(expectedEntity.getBirth(), person.getBirth());
            //exactly one record must be the same
            if (person.getId().equals(expectedEntity.getId())) {
                assertPersonEquals(expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }
    
    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(PersonLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        sampleMap.put(PersonLogic.FIRST_NAME, new String[]{expectedEntity.getFirstName()});
        sampleMap.put(PersonLogic.LAST_NAME, new String[]{expectedEntity.getLastName()});
        sampleMap.put(PersonLogic.PHONE, new String[]{expectedEntity.getPhone()});
        sampleMap.put(PersonLogic.ADDRESS, new String[]{expectedEntity.getAddress()});
        sampleMap.put(PersonLogic.BIRTH, new String[]{logic.convertDateToString(expectedEntity.getBirth())});

        Person returnedPerson = logic.createEntity(sampleMap);
        returnedPerson.setBloodBank(expectedEntity.getBloodBank());
        returnedPerson.setDonationRecordSet(expectedEntity.getDonationRecordSet());

        assertPersonEquals(expectedEntity, returnedPerson);
    }
    
    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(PersonLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(PersonLogic.FIRST_NAME, new String[]{expectedEntity.getFirstName()});
            map.put(PersonLogic.LAST_NAME, new String[]{expectedEntity.getLastName()});
            map.put(PersonLogic.PHONE, new String[]{expectedEntity.getPhone()});
            map.put(PersonLogic.ADDRESS, new String[]{expectedEntity.getAddress()});
            map.put(PersonLogic.BIRTH, new String[]{logic.convertDateToString(expectedEntity.getBirth())});
        };

        //idealy every test should be in its own method
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(PersonLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.FIRST_NAME, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(PersonLogic.FIRST_NAME, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.LAST_NAME, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(PersonLogic.LAST_NAME, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.PHONE, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(PersonLogic.PHONE, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.ADDRESS, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(PersonLogic.ADDRESS, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.BIRTH, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(PersonLogic.BIRTH, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
    }
    
    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(PersonLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(PersonLogic.FIRST_NAME, new String[]{expectedEntity.getFirstName()});
            map.put(PersonLogic.LAST_NAME, new String[]{expectedEntity.getLastName()});
            map.put(PersonLogic.PHONE, new String[]{expectedEntity.getPhone()});
            map.put(PersonLogic.ADDRESS, new String[]{expectedEntity.getAddress()});
            map.put(PersonLogic.BIRTH, new String[]{logic.convertDateToString(expectedEntity.getBirth())});
        };

        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            //from 97 inclusive to 123 exclusive
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };

        //idealy every test should be in its own method
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.FIRST_NAME, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(PersonLogic.FIRST_NAME, new String[]{generateString.apply(51)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.LAST_NAME, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(PersonLogic.LAST_NAME, new String[]{generateString.apply(51)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.PHONE, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(PersonLogic.PHONE, new String[]{generateString.apply(16)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(PersonLogic.ADDRESS, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(PersonLogic.ADDRESS, new String[]{generateString.apply(101)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
    }
    
    
    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };

        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(PersonLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        sampleMap.put(PersonLogic.FIRST_NAME, new String[]{expectedEntity.getFirstName()});
        sampleMap.put(PersonLogic.LAST_NAME, new String[]{expectedEntity.getLastName()});
        sampleMap.put(PersonLogic.PHONE, new String[]{expectedEntity.getPhone()});
        sampleMap.put(PersonLogic.ADDRESS, new String[]{expectedEntity.getAddress()});
        sampleMap.put(PersonLogic.BIRTH, new String[]{logic.convertDateToString(expectedEntity.getBirth())});

        //idealy every test should be in its own method
        Person returnedPerson = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(PersonLogic.ID)[0]), returnedPerson.getId());
        assertEquals(sampleMap.get(PersonLogic.FIRST_NAME)[0], returnedPerson.getFirstName());
        assertEquals(sampleMap.get(PersonLogic.LAST_NAME)[0], returnedPerson.getLastName());
        assertEquals(sampleMap.get(PersonLogic.PHONE)[0], returnedPerson.getPhone());
        assertEquals(sampleMap.get(PersonLogic.ADDRESS)[0], returnedPerson.getAddress());
        assertEquals(sampleMap.get(PersonLogic.BIRTH)[0], logic.convertDateToString(returnedPerson.getBirth()));

        sampleMap = new HashMap<>();
        sampleMap.put(PersonLogic.ID, new String[]{Integer.toString(1)});
        sampleMap.put(PersonLogic.FIRST_NAME, new String[]{generateString.apply(50)});
        sampleMap.put(PersonLogic.LAST_NAME, new String[]{generateString.apply(50)});
        sampleMap.put(PersonLogic.PHONE, new String[]{generateString.apply(15)});
        sampleMap.put(PersonLogic.ADDRESS, new String[]{generateString.apply(100)});
        sampleMap.put(PersonLogic.BIRTH, new String[]{logic.convertDateToString(expectedEntity.getBirth())});

        //idealy every test should be in its own method
        returnedPerson = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(PersonLogic.ID)[0]), returnedPerson.getId());
        assertEquals(sampleMap.get(PersonLogic.FIRST_NAME)[0], returnedPerson.getFirstName());
        assertEquals(sampleMap.get(PersonLogic.LAST_NAME)[0], returnedPerson.getLastName());
        assertEquals(sampleMap.get(PersonLogic.PHONE)[0], returnedPerson.getPhone());
        assertEquals(sampleMap.get(PersonLogic.ADDRESS)[0], returnedPerson.getAddress());
        assertEquals(sampleMap.get(PersonLogic.BIRTH)[0], logic.convertDateToString(returnedPerson.getBirth()));
    }
    
    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("ID", "First Name", "Last Name", "Phone Number", "Address", "Date of Birth"), list);
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(PersonLogic.ID, PersonLogic.FIRST_NAME, PersonLogic.LAST_NAME, PersonLogic.PHONE, PersonLogic.ADDRESS, PersonLogic.BIRTH), list);
    }

    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedEntity);

        assertEquals(expectedEntity.getId(), list.get(0));
        assertEquals(expectedEntity.getFirstName(), list.get(1));
        assertEquals(expectedEntity.getLastName(), list.get(2));
        assertEquals(expectedEntity.getPhone(), list.get(3));
        assertEquals(expectedEntity.getAddress(), list.get(4));
        assertEquals(expectedEntity.getBirth(), list.get(5));
        assertEquals(expectedEntity.getBloodBank(), list.get(6));
        assertEquals(expectedEntity.getDonationRecordSet(), list.get(7));
    }
}
