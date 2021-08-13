package logic;

import common.EMFactory;
import common.TomcatStartUp;
import entity.BloodBank;
import entity.DonationRecord;
import entity.Person;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
            //persist the dependency first
            em.persist(bb);
        }

        //create the desired entity
        Person entity = new Person();
        entity.setId(1);
        entity.setFirstName("Jane");
        entity.setFirstName("Doe");
        entity.setPhone("613");
        entity.setAddress("123 Street");
        entity.setBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-05"));
        
        //add first dependency to the desired entity
        entity.setBloodBank(bb);
        
        //create dependency with dependency on previously existing person
        DonationRecord dr = new DonationRecord();
        dr.setPerson(entity);
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
}
