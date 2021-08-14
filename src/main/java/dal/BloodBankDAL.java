package dal;

import entity.BloodBank;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ryanh
 * @author Milad Mobini
 */
public class BloodBankDAL extends GenericDAL<BloodBank> {

    /**
     * constructor of BloodBankDAL
     */
    public BloodBankDAL() {
        super(BloodBank.class);
    }

    /**
     * finds and returns all the instance of blood bank from database
     *
     * @return List<BloodBank> all the instance of blood bank from database
     */
    @Override
    public List<BloodBank> findAll() {
        return findResults("BloodBank.findAll", null);
    }

    /**
     * finds and returns the blood bank instance with the same record ID from
     * database
     *
     * @param bankId the bank id
     * @return BloodBank blood bank instance with the same bank ID
     */
    @Override
    public BloodBank findById(int bankId) {
        Map<String, Object> map = new HashMap<>();
        map.put("bankId", bankId);
        return findResult("BloodBank.findByBankId", map);
    }

    /**
     * finds and returns the blood bank instances with the same value for name
     * from database
     *
     * @param name the name
     * @return BloodBank blood bank instances with the same date value for
     * created
     */
    public BloodBank findByName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return findResult("BloodBank.findByName", map);
    }

    /**
     * finds and returns the blood bank instances with the same value for
     * privately Owned from database
     *
     * @param privatelyOwned the privately Owned status
     * @return List<BloodBank> blood bank instances with the same value for
     * created
     */
    public List<BloodBank> findByPreviouslyOwned(boolean privatelyOwned) {
        Map<String, Object> map = new HashMap<>();
        map.put("privatelyOwned", privatelyOwned);
        return findResults("BloodBank.findByPrivatelyOwned", map);
    }

    /**
     * finds and returns the blood bank instances with the same date value for
     * established from database
     *
     * @param established the date established
     * @return List<BloodBank> blood bank instances with the same date value for
     * created
     */
    public List<BloodBank> findByEstablished(Date established) {
        Map<String, Object> map = new HashMap<>();
        map.put("established", established);
        return findResults("BloodBank.findByEstablished", map);
    }

    /**
     * finds and returns the blood bank instances with the same value for
     * emplyeeCount from database
     *
     * @param emplyeeCount the date created
     * @return List<BloodBank> blood bank instances with the same value for
     * emplyeeCount
     */
    public List<BloodBank> findByEmployeeCount(int emplyeeCount) {
        Map<String, Object> map = new HashMap<>();
        map.put("emplyeeCount", emplyeeCount);
        return findResults("BloodBank.findByEmplyeeCount", map);
    }

    /**
     * finds and returns the blood bank instances with the same value for owner
     * Id from database
     *
     * @param ownerId the owner Id
     * @return List<BloodBank> blood bank instances with the same value for
     * owner Id
     */
    public BloodBank findByOwner(int ownerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("ownerId", ownerId);
        return findResult("BloodBank.findByOwner", map);
    }
}
