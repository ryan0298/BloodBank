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
package logic;

/**
 *
 * @author William
 */
public class BloodBankTest {
    private BloodBankLogic logic;
    private BloodBank bankEntity;
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
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
        BloodBank BloodBankEntity = new BloodBank();
        //if result is null create the entity and persist it
            //create object
            BloodBankEntity.setName("JUNIT");
            BloodBankEntity.setPrivatelyOwned(true);
            BloodBankEntity.setEstablished(logic.convertStringToDate("1111-11-11 11:11:11"));
            BloodBankEntity.setEmplyeeCount(111);
            //persist the dependency first
            em.persist(BloodBankEntity);


            bankEntity = em.merge(entity);

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
        if( expectedEntity != null ){
            logic.delete( expectedEntity );
        }
    }
        @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<BloodBankl> list = logic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();

        //make sure account was created successfully
        assertNotNull( bankEntity );
        //delete the new account
        logic.delete( bankEntity );

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be one less
        assertEquals( originalSize - 1, list.size() );
    }
    /**
     * helper method for testing all account fields
     *
     * @param expected
     * @param actual
     */
    private void assertBloodBankEquals( BloodBank expected, BloodBank actual ) {
        //assert all field to guarantee they are the same
        assertEquals( expected.getId(), actual.getId() );
        assertEquals( expected.getName(), actual.getName() );
        assertEquals( expected.getPrivatelyOwned(), actual.getPrivatelyOwned() );
        assertEquals( expected.getEstablished(), actual.getEstablished() );
        assertEquals( expected.getOwner(), actual.getOwner() );
        assertEquals( expected.getEmplyeeCount(), actual.getEmplyeeCount() );
    }

        @Test
    final void testGetId() {
        //using the id of test account get another account from logic
        BloodBank returnID = logic.getWithId( bankEntity.getId() );

        //the two accounts (bankEntity and returnID) must be the same
        assertBloodBankEquals( bankEntity, returnID );
    }
    @Test
    final void testGetBloodBankWithName() {
        int foundFull = 0;
        List<BloodBank> returnedPeople = logic.getPersonWithFirstName(expectedEntity.getName());
        for (BloodBank person : returnedPeople) {
            assertEquals(expectedEntity.getFirstName(), person.getFirstName());
            //exactly one record must be the same
            if (person.getId().equals(expectedEntity.getId())) {
                assertPersonEquals(expectedEntity, person);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

}
