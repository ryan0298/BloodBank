package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import entity.Account;
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
import entity.BloodDonation;
import entity.BloodBank;
import entity.BloodGroup;
import entity.RhesusFactor;
import entity.DonationRecord;
import java.util.Iterator;

/**
 * This class is has the example of how to add dependency when working with
 * junit. it is commented because some components need to be made first. You
 * will have to import everything you need.
 *
 * @author Shariar (Shawn) Emami
 */
class BloodDonationTest {

    private BloodDonationLogic logic;
    private BloodDonation expectedEntity;

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

        logic = LogicFactory.getFor("BloodDonation");
        /* **********************************
         * ***********IMPORTANT**************
         * **********************************/
        //we only do this for the test.
        //always create Entity using logic.
        //we manually make the account to not rely on any logic functionality , just for testing

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

        //create the desired entity
        BloodDonation entity = new BloodDonation();
        entity.setMilliliters(100);
        entity.setBloodGroup(BloodGroup.AB);
        entity.setRhd(RhesusFactor.Negative);
        entity.setCreated(logic.convertStringToDate("1111-11-11 11:11:11"));
        //add dependency to the desired entity
        entity.setBloodBank(bb);

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
        //get all the accounts from the DB
        List<BloodDonation> list = logic.getAll();
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
     * helper method for testing all BloodDonation fields
     *
     * @param expected
     * @param actual
     */
    private void assertBloodDonationEquals(BloodDonation expected, BloodDonation actual) {
        //assert all field to guarantee they are the same
        //TODO check if depdendency exixts
        if(expected.getBloodBank() != null) {
        assertEquals(expected.getBloodBank().getId(), actual.getBloodBank().getId());    
        }
        
        assertEquals(expected.getBloodGroup(), actual.getBloodGroup());

//        assertEquals( expected.getCreated(), actual.getCreated() );
        assertTrue(expected.getCreated().compareTo(actual.getCreated()) == 0);
//        assertEquals( expected.getDonationRecordSet(), actual.getDonationRecordSet() );

        //assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getMilliliters(), actual.getMilliliters());
        assertEquals(expected.getRhd(), actual.getRhd());
    }

    @Test
    final void testGetWithId() {
        //using the id of test BloodDonation get another account from logic
        BloodDonation returnedBloodDonation = logic.getWithId(expectedEntity.getId());

        //the two BloodDonation (testBloodDonation and returnedBloodDonation) must be the same
        assertBloodDonationEquals(expectedEntity, returnedBloodDonation);
    }

    @Test
    final void testGetBloodDonationWithMilliliters() {

        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithMilliliters(expectedEntity.getMilliliters());

        for (BloodDonation bloodDonation : returnedBloodDonation) {
            assertEquals(expectedEntity.getMilliliters(), bloodDonation.getMilliliters());
        }
    }

    @Test
    final void testGetBloodDonationWithBloodGroup() {

        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithBloodGroup(expectedEntity.getBloodGroup());

        for (BloodDonation bloodDonation : returnedBloodDonation) {
            assertEquals(expectedEntity.getBloodGroup(), bloodDonation.getBloodGroup());
        }
    }

    @Test
    final void testGetBloodDonationWithCreated() {

        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithCreated(expectedEntity.getCreated());

        for (BloodDonation bloodDonation : returnedBloodDonation) {
            assertEquals(expectedEntity.getCreated(), bloodDonation.getCreated());
        }
    }

    @Test
    final void testGetBloodDonationsWithRhd() {

        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationsWithRhd(expectedEntity.getRhd());

        for (BloodDonation bloodDonation : returnedBloodDonation) {
            assertEquals(expectedEntity.getRhd(), bloodDonation.getRhd());
        }
    }

    @Test
    final void testGetBloodDonationsWithBloodBank() {

        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationsWithBloodBank(expectedEntity.getBloodBank().getId());

        for (BloodDonation bloodDonation : returnedBloodDonation) {
            assertEquals(expectedEntity.getBloodBank().getId(), bloodDonation.getBloodBank().getId());
        }
    }

    @Test
    final void testCreateEntityAndAdd() {

        Map<String, String[]> sampleMap = new HashMap<>();

        //sampleMap.put(BloodDonationLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        sampleMap.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
        sampleMap.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});
        sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().name()});
        sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
        sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});

        BloodDonation returnedBloodDonation = logic.createEntity(sampleMap);
        returnedBloodDonation.setBloodBank(expectedEntity.getBloodBank());
        logic.add(returnedBloodDonation);

        returnedBloodDonation = logic.getWithId(returnedBloodDonation.getId());

//        System.out.println("Bank ID: "  + BloodDonationLogic.BANK_ID);
//        System.out.println("returnedBloodDonation: "  + returnedBloodDonation.getBloodBank().getId().toString());
        assertEquals(sampleMap.get(BloodDonationLogic.BANK_ID)[0], Integer.toString(returnedBloodDonation.getBloodBank().getId()));
        assertEquals(sampleMap.get(BloodDonationLogic.BLOOD_GROUP)[0], returnedBloodDonation.getBloodGroup().name());
        assertEquals(sampleMap.get(BloodDonationLogic.CREATED)[0], logic.convertDateToString(returnedBloodDonation.getCreated()));
        //assertEquals( sampleMap.get( BloodDonationLogic.ID )[ 0 ], Integer.toString(returnedBloodDonation.getId()) );
        assertEquals(sampleMap.get(BloodDonationLogic.MILLILITERS)[0], Integer.toString(returnedBloodDonation.getMilliliters()));
        assertEquals(sampleMap.get(BloodDonationLogic.RHESUS_FACTOR)[0], returnedBloodDonation.getRhd().name());

        assertBloodDonationEquals(expectedEntity, returnedBloodDonation);

        logic.delete(returnedBloodDonation);
    }

    @Test
    final void testCreateEntity() {

        Map<String, String[]> sampleMap = new HashMap<>();
        
        sampleMap.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
        sampleMap.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});
        sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().name()});
        sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
        sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});

        BloodDonation returnedBloodDonation = logic.createEntity(sampleMap);
        returnedBloodDonation.setBloodBank(expectedEntity.getBloodBank());
        
        assertBloodDonationEquals( expectedEntity, returnedBloodDonation );
    }
    
    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
        map.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});
        map.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().name()});
        map.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
        map.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});
        };

        //idealy every test should be in its own method
        fillMap.accept( sampleMap );
        sampleMap.replace( BloodDonationLogic.BANK_ID, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( BloodDonationLogic.BANK_ID, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( BloodDonationLogic.BLOOD_GROUP, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( BloodDonationLogic.BLOOD_GROUP, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( BloodDonationLogic.CREATED, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( BloodDonationLogic.CREATED, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( BloodDonationLogic.MILLILITERS, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( BloodDonationLogic.MILLILITERS, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( BloodDonationLogic.RHESUS_FACTOR, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( BloodDonationLogic.RHESUS_FACTOR, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
    }
    
    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
        map.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});
        map.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().name()});
        map.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
        map.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});
        };

        IntFunction<String> generateString = ( int length ) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            //from 97 inclusive to 123 exclusive
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };

        //idealy every test should be in its own method
        fillMap.accept( sampleMap );
        sampleMap.replace( BloodDonationLogic.BANK_ID, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( BloodDonationLogic.BANK_ID, new String[]{ "12b" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( BloodDonationLogic.BLOOD_GROUP, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( BloodDonationLogic.BLOOD_GROUP, new String[]{ generateString.apply( 46 ) } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( BloodDonationLogic.CREATED, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( BloodDonationLogic.CREATED, new String[]{ generateString.apply( 46 ) } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( BloodDonationLogic.MILLILITERS, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( BloodDonationLogic.MILLILITERS, new String[]{ generateString.apply( 46 ) } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( BloodDonationLogic.RHESUS_FACTOR, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace(  BloodDonationLogic.RHESUS_FACTOR, new String[]{ generateString.apply( 46 ) } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
    }
    
    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = ( int length ) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };

        Map<String, String[]> sampleMap = new HashMap<>();
//        sampleMap.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString( 1 )});
//        sampleMap.put(BloodDonationLogic.CREATED, new String[]{generateString.apply( 1 )});
//        sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{generateString.apply( 1 )});
//        sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{generateString.apply( 1 )});
//        sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{generateString.apply( 1 )});

        sampleMap.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
        sampleMap.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});
        sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().name()});
        sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
        sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});

        
        //idealy every test should be in its own method
        BloodDonation returnedBloodDonation = logic.createEntity( sampleMap );
        returnedBloodDonation.setBloodBank(expectedEntity.getBloodBank());
        
        assertEquals(sampleMap.get(BloodDonationLogic.BANK_ID)[0], Integer.toString(returnedBloodDonation.getBloodBank().getId()));
        assertEquals(sampleMap.get(BloodDonationLogic.BLOOD_GROUP)[0], returnedBloodDonation.getBloodGroup().name());
        assertEquals(sampleMap.get(BloodDonationLogic.CREATED)[0], logic.convertDateToString(returnedBloodDonation.getCreated()));
        assertEquals(sampleMap.get(BloodDonationLogic.MILLILITERS)[0], Integer.toString(returnedBloodDonation.getMilliliters()));
        assertEquals(sampleMap.get(BloodDonationLogic.RHESUS_FACTOR)[0], returnedBloodDonation.getRhd().name());

        sampleMap = new HashMap<>();

        sampleMap.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(1)});
        sampleMap.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});
        sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{"A"});
        sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(1)});
        sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});
        
        //idealy every test should be in its own method
        returnedBloodDonation = logic.createEntity( sampleMap );
        returnedBloodDonation.setBloodBank(expectedEntity.getBloodBank());
        
        assertEquals(Integer.parseInt(sampleMap.get(BloodDonationLogic.BANK_ID)[0]), returnedBloodDonation.getBloodBank().getId());
        assertEquals(sampleMap.get(BloodDonationLogic.BLOOD_GROUP)[0], returnedBloodDonation.getBloodGroup().name());
        assertEquals(sampleMap.get(BloodDonationLogic.CREATED)[0], logic.convertDateToString(returnedBloodDonation.getCreated()));
        assertEquals(sampleMap.get(BloodDonationLogic.MILLILITERS)[0], Integer.toString(returnedBloodDonation.getMilliliters()));
        assertEquals(sampleMap.get(BloodDonationLogic.RHESUS_FACTOR)[0], returnedBloodDonation.getRhd().name());
        
    }
        
    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals( Arrays.asList( "bank_id", "milliliters", "blood_group", "rhesus_factor", "created", "id" ), list );
    }
    
    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals( Arrays.asList( BloodDonationLogic.BANK_ID, 
                BloodDonationLogic.MILLILITERS,
                BloodDonationLogic.BLOOD_GROUP ,
                BloodDonationLogic.RHESUS_FACTOR,
                BloodDonationLogic.CREATED, 
                BloodDonationLogic.ID
                ), list );
    }
    
    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList( expectedEntity );
        
        assertEquals( expectedEntity.getBloodBank().getId(), list.get( 0 ) );
        assertEquals( expectedEntity.getMilliliters(), list.get( 1 ) );
        assertEquals( expectedEntity.getBloodGroup().name(), list.get( 2 ) );
        assertEquals( expectedEntity.getRhd().name(), list.get( 3 ) );
        assertEquals( logic.convertDateToString(expectedEntity.getCreated()), list.get( 4 ) );
        assertEquals( expectedEntity.getId(), list.get( 5 ) );
    }
    
//    sampleMap.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
//        sampleMap.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});
//        sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().name()});
//        sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
//        sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});
}
