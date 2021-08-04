package dal;

import entity.DonationRecord;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Milad Mobini
 */
public class DonationRecordDAL extends GenericDAL<DonationRecord> {

    public DonationRecordDAL() {
        super(DonationRecord.class);
    }

    @Override
    public List<DonationRecord> findAll() {
        return findResults("DonationRecord.findAll", null);
    }

    @Override
    public DonationRecord findById(int recordId) {
        Map<String, Object> map = new HashMap<>();
        map.put("recordId", recordId);
        return findResult("DonationRecord.findByRecordId", map);
    }

    public List<DonationRecord> findByTested(boolean tested) {
        Map<String, Object> map = new HashMap<>();
        map.put("tested", tested);
        return findResults("DonationRecord.findByTested", map);
    }

    public List<DonationRecord> findByAdministrator(String administrator) {
        Map<String, Object> map = new HashMap<>();
        map.put("administrator", administrator);
        return findResults("DonationRecord.findByAdministrator", map);
    }

    public List<DonationRecord> findByHospital(String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("hospital", username);
        return findResults("DonationRecord.findByHospital", map);
    }

    public List<DonationRecord> findByCreated(Date created) {
        Map<String, Object> map = new HashMap<>();
        map.put("created", created);
        return findResults("DonationRecord.findByCreated", map);
    }

    public List<DonationRecord> findByPerson(int personId) {
        Map<String, Object> map = new HashMap<>();
        map.put("personId", personId);
        return findResults("DonationRecord.findByPerson", map);
    }

    public List<DonationRecord> findByDonation(int donationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("donationId", donationId);
        return findResults("DonationRecord.findByDonation", map);
    }
}
