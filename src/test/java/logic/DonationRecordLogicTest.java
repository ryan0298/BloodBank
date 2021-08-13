package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import entity.BloodBank;
import entity.DonationRecord;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.DonationRecord;
import entity.Person;
import entity.RhesusFactor;
import java.text.SimpleDateFormat;
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
import org.opentest4j.AssertionFailedError;

/**
 * @author Milad Mobini
 * @author Shariar (Shawn) Emami
 */
class DonationRecordLogicTest {
    
    private DonationRecordLogic logic;
    private DonationRecord expectedEntity;

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

        logic = LogicFactory.getFor("DonationRecord");
        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction
        em.getTransaction().begin();
        //check if the depdendecy exists on DB already
        //em.find takes two arguments, the class type of return result and the primery key.

        BloodBank bb = em.find(BloodBank.class, 1);
        //if result is null create the entity and persist it
        if (bb == null) {
            //cearet object
            bb = new BloodBank();
            bb.setName("JUNIT");
            bb.setPrivatelyOwned(true);
            bb.setEstablished(logic.convertStringToDate("1111-11-11 11:11:11"));
            bb.setEmplyeeCount(111);
            //persist the dependency first
            em.persist(bb);
        }

        BloodDonation bd = em.find(BloodDonation.class, 1);
        //if result is null create the entity and persist it
        if (bd == null) {
            //cearet object
            bd = new BloodDonation();
            bd.setMilliliters(100);
            bd.setBloodGroup(BloodGroup.AB);
            bd.setRhd(RhesusFactor.Negative);
            bd.setCreated(logic.convertStringToDate("1111-11-11 11:11:11"));
            bd.setBloodBank(bb);
            //persist the dependency first
            em.persist(bd);
        }
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
        DonationRecord entity = new DonationRecord();
        entity.setAdministrator("Milad");
        entity.setCreated(new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-05"));
        entity.setHospital("college");
        entity.setTested(true);
        //add dependency to the desired entity
        entity.setBloodDonation(bd);
        entity.setPerson(p);
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
        List<DonationRecord> list = logic.getAll();
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

    private void assertDonationRecordEquals(DonationRecord expected, DonationRecord actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAdministrator(), actual.getAdministrator());
        assertEquals(expected.getBloodDonation().getId(), actual.getBloodDonation().getId());
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getHospital(), actual.getHospital());
        assertEquals(expected.getTested(), actual.getTested());
        assertEquals(expected.getPerson().getId(), actual.getPerson().getId());
    }

    @Test
    final void testGetWithId() {
        //using the id of test record get another record from logic
        DonationRecord returnedDonationRecord = logic.getWithId(expectedEntity.getId());

        //the two records (testAcounts and returnedDonationRecords) must be the same
        assertDonationRecordEquals(expectedEntity, returnedDonationRecord);
    }

    @Test
    final void testGetDonationRecordWithTested() {
        int foundFull = 0;
        List<DonationRecord> returnedDonationRecords = logic.getDonationRecordWithTested(expectedEntity.getTested());
        for (DonationRecord record : returnedDonationRecords) {
            assertEquals(expectedEntity.getTested(), record.getTested());
            //exactly one record must be the same
            if (record.getId().equals(expectedEntity.getId())) {
                assertDonationRecordEquals(expectedEntity, record);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void getDonationRecordWithAdministrator() {
        int foundFull = 0;
        List<DonationRecord> returnedDonationRecords = logic.getDonationRecordWithAdministrator(expectedEntity.getAdministrator());
        for (DonationRecord record : returnedDonationRecords) {
            assertEquals(expectedEntity.getAdministrator(), record.getAdministrator());
            //exactly one record must be the same
            if (record.getId().equals(expectedEntity.getId())) {
                assertDonationRecordEquals(expectedEntity, record);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void getDonationRecordWithHospital() {
        int foundFull = 0;
        List<DonationRecord> returnedDonationRecords = logic.getDonationRecordWithHospital(expectedEntity.getHospital());
        for (DonationRecord record : returnedDonationRecords) {
            assertEquals(expectedEntity.getHospital(), record.getHospital());
            //exactly one record must be the same
            if (record.getId().equals(expectedEntity.getId())) {
                assertDonationRecordEquals(expectedEntity, record);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void getDonationRecordsWithCreated() {
        int foundFull = 0;
        List<DonationRecord> returnedDonationRecords = logic.getDonationRecordsWithCreated(expectedEntity.getCreated());
        for (DonationRecord record : returnedDonationRecords) {
            assertEquals(expectedEntity.getCreated(), record.getCreated());
            //exactly one record must be the same
            if (record.getId().equals(expectedEntity.getId())) {
                assertDonationRecordEquals(expectedEntity, record);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void getDonationRecordsWithPerson() {
        int foundFull = 0;
        List<DonationRecord> returnedDonationRecords = logic.getDonationRecordsWithPerson(expectedEntity.getPerson().getId());
        for (DonationRecord record : returnedDonationRecords) {
            assertEquals(expectedEntity.getPerson().getId(), record.getPerson().getId());
            //exactly one record must be the same
            if (record.getId().equals(expectedEntity.getId())) {
                assertDonationRecordEquals(expectedEntity, record);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void getDonationRecordsWithDonation() {
        int foundFull = 0;
        List<DonationRecord> returnedDonationRecords = logic.getDonationRecordsWithDonation(expectedEntity.getBloodDonation().getId());
        for (DonationRecord record : returnedDonationRecords) {
            assertEquals(expectedEntity.getBloodDonation().getId(), record.getBloodDonation().getId());
            //exactly one record must be the same
            if (record.getId().equals(expectedEntity.getId())) {
                assertDonationRecordEquals(expectedEntity, record);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(DonationRecordLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        sampleMap.put(DonationRecordLogic.PERSON_ID, new String[]{expectedEntity.getPerson().getId().toString()});
        sampleMap.put(DonationRecordLogic.DONATION_ID, new String[]{expectedEntity.getBloodDonation().getId().toString()});
        sampleMap.put(DonationRecordLogic.TESTED, new String[]{Boolean.toString(expectedEntity.getTested())});
        sampleMap.put(DonationRecordLogic.ADMINISTRATOR, new String[]{expectedEntity.getAdministrator()});
        sampleMap.put(DonationRecordLogic.HOSPITAL, new String[]{expectedEntity.getHospital()});
        sampleMap.put(DonationRecordLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});

        DonationRecord returnedDonationRecord = logic.createEntity(sampleMap);
        returnedDonationRecord.setBloodDonation(expectedEntity.getBloodDonation());
        returnedDonationRecord.setPerson(expectedEntity.getPerson());

        assertDonationRecordEquals(expectedEntity, returnedDonationRecord);
    }

    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(DonationRecordLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(DonationRecordLogic.TESTED, new String[]{Boolean.toString(expectedEntity.getTested())});
            map.put(DonationRecordLogic.ADMINISTRATOR, new String[]{expectedEntity.getAdministrator()});
            map.put(DonationRecordLogic.HOSPITAL, new String[]{expectedEntity.getHospital()});
            map.put(DonationRecordLogic.CREATED, new String[]{expectedEntity.getCreated().toString()});
        };

        //idealy every test should be in its own method
        fillMap.accept(sampleMap);
        sampleMap.replace(DonationRecordLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(DonationRecordLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(DonationRecordLogic.TESTED, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(DonationRecordLogic.TESTED, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(DonationRecordLogic.ADMINISTRATOR, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(DonationRecordLogic.ADMINISTRATOR, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(DonationRecordLogic.HOSPITAL, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(DonationRecordLogic.HOSPITAL, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(DonationRecordLogic.CREATED, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(DonationRecordLogic.CREATED, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
    }

    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(DonationRecordLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(DonationRecordLogic.TESTED, new String[]{Boolean.toString(expectedEntity.getTested())});
            map.put(DonationRecordLogic.ADMINISTRATOR, new String[]{expectedEntity.getAdministrator()});
            map.put(DonationRecordLogic.HOSPITAL, new String[]{expectedEntity.getHospital()});
            map.put(DonationRecordLogic.CREATED, new String[]{expectedEntity.getCreated().toString()});
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
        sampleMap.replace(DonationRecordLogic.ID, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(DonationRecordLogic.ID, new String[]{generateString.apply(46)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(DonationRecordLogic.ADMINISTRATOR, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(DonationRecordLogic.ADMINISTRATOR, new String[]{generateString.apply(111)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(DonationRecordLogic.HOSPITAL, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(DonationRecordLogic.HOSPITAL, new String[]{generateString.apply(111)});
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
        sampleMap.put(DonationRecordLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        sampleMap.put(DonationRecordLogic.TESTED, new String[]{Boolean.toString(expectedEntity.getTested())});
        sampleMap.put(DonationRecordLogic.ADMINISTRATOR, new String[]{expectedEntity.getAdministrator()});
        sampleMap.put(DonationRecordLogic.HOSPITAL, new String[]{expectedEntity.getHospital()});
        sampleMap.put(DonationRecordLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});

        //idealy every test should be in its own method
        DonationRecord returnedDonationRecord = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(DonationRecordLogic.ID)[0]), returnedDonationRecord.getId());
        assertEquals(sampleMap.get(DonationRecordLogic.TESTED)[0], Boolean.toString(returnedDonationRecord.getTested()));
        assertEquals(sampleMap.get(DonationRecordLogic.ADMINISTRATOR)[0], returnedDonationRecord.getAdministrator());
        assertEquals(sampleMap.get(DonationRecordLogic.HOSPITAL)[0], returnedDonationRecord.getHospital());
        assertEquals(sampleMap.get(DonationRecordLogic.CREATED)[0], logic.convertDateToString(returnedDonationRecord.getCreated()));

        sampleMap = new HashMap<>();
        sampleMap.put(DonationRecordLogic.ID, new String[]{Integer.toString(1)});
        sampleMap.put(DonationRecordLogic.TESTED, new String[]{"false"});
        sampleMap.put(DonationRecordLogic.ADMINISTRATOR, new String[]{generateString.apply(45)});
        sampleMap.put(DonationRecordLogic.HOSPITAL, new String[]{generateString.apply(65)});
        sampleMap.put(DonationRecordLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});

        //idealy every test should be in its own method
        returnedDonationRecord = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(DonationRecordLogic.ID)[0]), returnedDonationRecord.getId());
        assertEquals(sampleMap.get(DonationRecordLogic.TESTED)[0], Boolean.toString(returnedDonationRecord.getTested()));
        assertEquals(sampleMap.get(DonationRecordLogic.ADMINISTRATOR)[0], returnedDonationRecord.getAdministrator());
        assertEquals(sampleMap.get(DonationRecordLogic.HOSPITAL)[0], returnedDonationRecord.getHospital());
    }

    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("ID", "Person ID", "Blood Donation ID", "Tested", "Administrator", "Hospital", "Created"), list);
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(DonationRecordLogic.ID, DonationRecordLogic.PERSON_ID, DonationRecordLogic.DONATION_ID, DonationRecordLogic.TESTED, DonationRecordLogic.ADMINISTRATOR, DonationRecordLogic.HOSPITAL, DonationRecordLogic.CREATED), list);
    }

    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedEntity);

        assertEquals(expectedEntity.getId(), list.get(0));
        assertEquals(expectedEntity.getPerson(), list.get(1));
        assertEquals(expectedEntity.getBloodDonation(), list.get(2));
        assertEquals(expectedEntity.getTested(), list.get(3));
        assertEquals(expectedEntity.getAdministrator(), list.get(4));
        assertEquals(expectedEntity.getHospital(), list.get(5));
        assertEquals(expectedEntity.getCreated(), list.get(6));
    }
}
