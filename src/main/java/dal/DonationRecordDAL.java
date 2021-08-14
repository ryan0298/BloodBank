package dal;

import entity.DonationRecord;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Milad Mobini
 */
public class DonationRecordDAL extends GenericDAL<DonationRecord> {

    /**
     * constructor of DonationRecordDAL
     */
    public DonationRecordDAL() {
        super(DonationRecord.class);
    }

    /**
     * finds and returns all the instance of donation record from database
     *
     * @return List<DonationRecord> all the instance of donation record from
     * database
     */
    @Override
    public List<DonationRecord> findAll() {
        return findResults("DonationRecord.findAll", null);
    }

    /**
     * finds and returns the donation record instance with the same record ID
     * from database
     *
     * @param personId the record id
     * @return DonationRecord donation record instance with the same record ID
     */
    @Override
    public DonationRecord findById(int recordId) {
        Map<String, Object> map = new HashMap<>();
        map.put("recordId", recordId);
        return findResult("DonationRecord.findByRecordId", map);
    }

    /**
     * finds and returns the donation record instances with the same value for
     * tested from database
     *
     * @param personId the tested
     * @return List<DonationRecord> donation record instances with the same
     * value for tested
     */
    public List<DonationRecord> findByTested(boolean tested) {
        Map<String, Object> map = new HashMap<>();
        map.put("tested", tested);
        return findResults("DonationRecord.findByTested", map);
    }

    /**
     * finds and returns the donation record instances with the same value for
     * administrator from database
     *
     * @param personId the administrator
     * @return List<DonationRecord> donation record instances with the same
     * value for administrator
     */
    public List<DonationRecord> findByAdministrator(String administrator) {
        Map<String, Object> map = new HashMap<>();
        map.put("administrator", administrator);
        return findResults("DonationRecord.findByAdministrator", map);
    }

    /**
     * finds and returns the donation record instances with the same value for
     * hospital from database
     *
     * @param personId the Hospital name
     * @return List<DonationRecord> donation record instances with the same
     * value for hospital
     */
    public List<DonationRecord> findByHospital(String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("hospital", username);
        return findResults("DonationRecord.findByHospital", map);
    }

    /**
     * finds and returns the donation record instances with the same date value
     * for created from database
     *
     * @param personId the date created
     * @return List<DonationRecord> donation record instances with the same date
     * value for created
     */
    public List<DonationRecord> findByCreated(Date created) {
        Map<String, Object> map = new HashMap<>();
        map.put("created", created);
        return findResults("DonationRecord.findByCreated", map);
    }

    /**
     * finds and returns the donation record instances with the same value for
     * person Id from database
     *
     * @param personId the person id
     * @return List<DonationRecord> donation record instances with the same
     * value for person Id
     */
    public List<DonationRecord> findByPerson(int personId) {
        Map<String, Object> map = new HashMap<>();
        map.put("personId", personId);
        return findResults("DonationRecord.findByPerson", map);
    }

    /**
     * finds and returns the donation record instances with the same value for
     * donation Id from database
     *
     * @param donationId the donation id
     * @return List<DonationRecord> donation record instances with the same
     * value for donation Id
     */
    public List<DonationRecord> findByDonation(int donationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("donationId", donationId);
        return findResults("DonationRecord.findByDonation", map);
    }
}
