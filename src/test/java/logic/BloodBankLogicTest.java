package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import entity.BloodBank;
import entity.BloodBank;
import entity.BloodGroup;
import entity.Person;
import entity.RhesusFactor;

/**
 * This class is has the example of how to add dependency when working with
 * junit. it is commented because some components need to be made first. You
 * will have to import everything you need.
 *
 * @author Milad Mobini
 * @author Shariar (Shawn) Emami
 */
class BloodBankLogicTest {

    private BloodBankLogic logic;
    private BloodBank expectedEntity;

    /**
     * runs before start of class and establish the database connection
     *
     * @throws Exception
     */
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat("/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test");
    }

    /**
     * runs after the class terminates and destroys the database connection
     *
     * @throws Exception
     */
    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    /**
     * runs before each test, in this method we create a new entity with all its
     * dependencies which we use as our expected entity to test the methods
     *
     * @throws Exception
     */
    @BeforeEach
    final void setUp() throws Exception {

        logic = LogicFactory.getFor("BloodBank");
        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction
        em.getTransaction().begin();
        //check if the depdendecy exists on DB already
        //em.find takes two arguments, the class type of return result and the primery key.
        Person p = em.find(Person.class, 1);
        //if result is null create the entity and persist it
        if (p == null) {
            //cearet object
            p = new Person();
            p.setAddress("K2C");
            p.setBirth(logic.convertStringToDate("1111-11-11 11:11:11"));
            p.setPhone("613");
            p.setFirstName("M");
            p.setLastName("C");
            //persist the dependency first
            em.persist(p);
        }

        //create the desired entity
        BloodBank entity = new BloodBank();
        entity = new BloodBank();
        entity.setName("JUNIT");
        entity.setPrivatelyOwned(true);
        entity.setEstablished(logic.convertStringToDate("1111-11-11 11:11:11"));
        entity.setEmplyeeCount(111);
        //add dependency to the desired entity
        entity.setOwner(p);

        //add desired entity to hibernate, entity is now managed.
        //we use merge instead of add so we can get the managed entity.
        expectedEntity = em.merge(entity);
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();
    }

    /**
     * runs after each method and removes the expected entity with all
     * dependencies
     *
     * @throws Exception
     */
    @AfterEach
    final void tearDown() throws Exception {
        if (expectedEntity != null) {
            logic.delete(expectedEntity);
            PersonLogic pl = LogicFactory.getFor("Person");
            pl.delete(expectedEntity.getOwner());
        }
    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<BloodBank> list = logic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();

        //make sure account was created successfully
        assertNotNull(expectedEntity);
        //delete the new account
        logic.delete(expectedEntity);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be one less
        assertEquals(originalSize - 1, list.size());
    }

    /**
     * helper method for testing all BloodBank fields
     *
     * @param expected
     * @param actual
     */
    private void assertBloodBankEquals(BloodBank expected, BloodBank actual) {
        //assert all field to guarantee they are the same
        if (expected.getOwner() != null) {
            assertEquals(expected.getOwner().getId(), actual.getOwner().getId());
        }
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertTrue(expected.getEstablished().compareTo(actual.getEstablished()) == 0);
        assertEquals(expected.getPrivatelyOwned(), actual.getPrivatelyOwned());
        assertEquals(expected.getEmplyeeCount(), actual.getEmplyeeCount());
    }

    @Test
    final void testGetWithId() {
        //using the id of test BloodBank get another account from logic
        BloodBank returnedBloodBank = logic.getWithId(expectedEntity.getId());
        //the two BloodBank (testBloodBank and returnedBloodBank) must be the same
        assertBloodBankEquals(expectedEntity, returnedBloodBank);
    }

    @Test
    final void testGetBloodBankWithName() {
        BloodBank returnedBloodBank = logic.getBloodBankWithName(expectedEntity.getName());
        assertEquals(expectedEntity.getName(), returnedBloodBank.getName());
    }

    @Test
    final void testGetBloodBankWithPrivatelyOwned() {
        List<BloodBank> returnedBloodBank = logic.getBloodBankWithPrivatelyOwned(expectedEntity.getPrivatelyOwned());
        for (BloodBank bloodBank : returnedBloodBank) {
            assertEquals(expectedEntity.getPrivatelyOwned(), bloodBank.getPrivatelyOwned());
        }
    }

    @Test
    final void testGetBloodBankWithEstablished() {
        List<BloodBank> returnedBloodBank = logic.getBloodBankWithEstablished(expectedEntity.getEstablished());
        for (BloodBank bloodBank : returnedBloodBank) {
            assertEquals(expectedEntity.getEstablished(), bloodBank.getEstablished());
        }
    }

    @Test
    final void testGetBloodBanksWithOwner() {
        BloodBank returnedBloodBank = logic.getBloodBanksWithOwner(expectedEntity.getOwner().getId());
        assertEquals(expectedEntity.getOwner().getId(), returnedBloodBank.getOwner().getId());
    }

    @Test
    final void testGetBloodBanksWithEmplyeeCount() {
        List<BloodBank> returnedBloodBank = logic.getBloodBanksWithEmplyeeCount(expectedEntity.getEmplyeeCount());
        for (BloodBank bloodBank : returnedBloodBank) {
            assertEquals(expectedEntity.getEmplyeeCount(), bloodBank.getEmplyeeCount());
        }
    }

    @Test
    final void testCreateEntity() {

        Map<String, String[]> sampleMap = new HashMap<>();

        sampleMap.put(BloodBankLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        sampleMap.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{Boolean.toString(expectedEntity.getPrivatelyOwned())});
        sampleMap.put(BloodBankLogic.ESTABLISHED, new String[]{logic.convertDateToString(expectedEntity.getEstablished())});
        sampleMap.put(BloodBankLogic.NAME, new String[]{expectedEntity.getName()});
        sampleMap.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{Integer.toString(expectedEntity.getEmplyeeCount())});

        BloodBank returnedBloodBank = logic.createEntity(sampleMap);
        returnedBloodBank.setOwner(expectedEntity.getOwner());

        assertBloodBankEquals(expectedEntity, returnedBloodBank);
    }

    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{Boolean.toString(expectedEntity.getPrivatelyOwned())});
        sampleMap.put(BloodBankLogic.ESTABLISHED, new String[]{logic.convertDateToString(expectedEntity.getEstablished())});
        sampleMap.put(BloodBankLogic.NAME, new String[]{expectedEntity.getName() + "ab"});
        sampleMap.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{Integer.toString(expectedEntity.getEmplyeeCount())});

        BloodBank returnedBloodBank = logic.createEntity(sampleMap);
        returnedBloodBank.setOwner(expectedEntity.getOwner());
        logic.add(returnedBloodBank);

        returnedBloodBank = logic.getWithId(returnedBloodBank.getId());

        assertEquals(sampleMap.get(BloodBankLogic.ESTABLISHED)[0], logic.convertDateToString(returnedBloodBank.getEstablished()));
        assertEquals(sampleMap.get(BloodBankLogic.EMPLOYEE_COUNT)[0], Integer.toString(returnedBloodBank.getEmplyeeCount()));
        assertEquals(sampleMap.get(BloodBankLogic.NAME)[0], returnedBloodBank.getName());
        assertEquals(sampleMap.get(BloodBankLogic.PRIVATELY_OWNED)[0], Boolean.toString(returnedBloodBank.getPrivatelyOwned()));

        logic.delete(returnedBloodBank);
    }

    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(BloodBankLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(BloodBankLogic.ESTABLISHED, new String[]{logic.convertDateToString(expectedEntity.getEstablished())});
            map.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{Boolean.toString(expectedEntity.getPrivatelyOwned())});
            map.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{Integer.toString(expectedEntity.getEmplyeeCount())});
            map.put(BloodBankLogic.NAME, new String[]{expectedEntity.getName()});
        };

        //idealy every test should be in its own method
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.ESTABLISHED, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.ESTABLISHED, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.PRIVATELY_OWNED, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.PRIVATELY_OWNED, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.EMPLOYEE_COUNT, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.EMPLOYEE_COUNT, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.NAME, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.NAME, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
    }

    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(BloodBankLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(BloodBankLogic.ESTABLISHED, new String[]{logic.convertDateToString(expectedEntity.getEstablished())});
            map.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{Boolean.toString(expectedEntity.getPrivatelyOwned())});
            map.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{Integer.toString(expectedEntity.getEmplyeeCount())});
            map.put(BloodBankLogic.NAME, new String[]{expectedEntity.getName()});
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
        sampleMap.replace(BloodBankLogic.ID, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.ID, new String[]{generateString.apply(46)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.NAME, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.NAME, new String[]{generateString.apply(111)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.EMPLOYEE_COUNT, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.EMPLOYEE_COUNT, new String[]{generateString.apply(111)});
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

        sampleMap.put(BloodBankLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        sampleMap.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{Boolean.toString(expectedEntity.getPrivatelyOwned())});
        sampleMap.put(BloodBankLogic.ESTABLISHED, new String[]{logic.convertDateToString(expectedEntity.getEstablished())});
        sampleMap.put(BloodBankLogic.NAME, new String[]{expectedEntity.getName()});
        sampleMap.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{Integer.toString(expectedEntity.getEmplyeeCount())});
        //idealy every test should be in its own method
        BloodBank returnedBloodBank = logic.createEntity(sampleMap);
        returnedBloodBank.setOwner(expectedEntity.getOwner());
        assertBloodBankEquals(expectedEntity, returnedBloodBank);

        assertEquals(sampleMap.get(BloodBankLogic.ID)[0], Integer.toString(returnedBloodBank.getId()));
        assertEquals(sampleMap.get(BloodBankLogic.ESTABLISHED)[0], logic.convertDateToString(returnedBloodBank.getEstablished()));
        assertEquals(sampleMap.get(BloodBankLogic.EMPLOYEE_COUNT)[0], Integer.toString(returnedBloodBank.getEmplyeeCount()));
        assertEquals(sampleMap.get(BloodBankLogic.NAME)[0], returnedBloodBank.getName());
        assertEquals(sampleMap.get(BloodBankLogic.PRIVATELY_OWNED)[0], Boolean.toString(returnedBloodBank.getPrivatelyOwned()));


        sampleMap = new HashMap<>();
        sampleMap.put(BloodBankLogic.ID, new String[]{Integer.toString(1)});
        sampleMap.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{"false"});
        sampleMap.put(BloodBankLogic.ESTABLISHED, new String[]{logic.convertDateToString(expectedEntity.getEstablished())});
        sampleMap.put(BloodBankLogic.NAME, new String[]{generateString.apply(100)});
        sampleMap.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{Integer.toString(Integer.MAX_VALUE)});

        //idealy every test should be in its own method
        returnedBloodBank = logic.createEntity(sampleMap);
        returnedBloodBank.setOwner(expectedEntity.getOwner());

        assertEquals(sampleMap.get(BloodBankLogic.ID)[0], Integer.toString(returnedBloodBank.getId()));
        assertEquals(sampleMap.get(BloodBankLogic.ESTABLISHED)[0], logic.convertDateToString(returnedBloodBank.getEstablished()));
        assertEquals(sampleMap.get(BloodBankLogic.EMPLOYEE_COUNT)[0], Integer.toString(returnedBloodBank.getEmplyeeCount()));
        assertEquals(sampleMap.get(BloodBankLogic.NAME)[0], returnedBloodBank.getName());
        assertEquals(sampleMap.get(BloodBankLogic.PRIVATELY_OWNED)[0], Boolean.toString(returnedBloodBank.getPrivatelyOwned()));

    }

    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("ID", "Owner ID", "Privately Owned", "established", "Name", "Employee Count"), list);
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(
                BloodBankLogic.ID,
                BloodBankLogic.OWNER_ID,
                BloodBankLogic.PRIVATELY_OWNED,
                BloodBankLogic.ESTABLISHED,
                BloodBankLogic.NAME,
                BloodBankLogic.EMPLOYEE_COUNT
        ), list);
    }

    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedEntity);

        assertEquals(expectedEntity.getId(), list.get(0));
        assertEquals(expectedEntity.getOwner(), list.get(1));
        assertEquals(expectedEntity.getPrivatelyOwned(), list.get(2));
        assertEquals(expectedEntity.getEstablished(), list.get(3));
        assertEquals(expectedEntity.getName(), list.get(4));
        assertEquals(expectedEntity.getEmplyeeCount(), list.get(5));
    }

}
