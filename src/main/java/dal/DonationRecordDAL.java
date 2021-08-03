package dal;

import entity.DonationRecord;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Milad Mobini
 */
public class DonationRecordDAL extends GenericDAL<DonationRecord> {

    public DonationRecordDAL() {
        super( DonationRecord.class );
    }

    @Override
    public List<DonationRecord> findAll() {
        return findResults( "Account.findAll", null );
    }

    @Override
    public DonationRecord findById(int recordId) {
        return null;
    }

    public List<DonationRecord> findByTested(boolean tested) {
        return null;
    }

    public List<DonationRecord> findByAdministrator(String administrator) {
        return null;
    }

    public List<DonationRecord> findByHospital(String username) {
        return null;
    }

    public List<DonationRecord> findByCreated(Date created) {
        return null;
    }

    public List<DonationRecord> findByPerson(int personId) {
        return null;
    }

    public List<DonationRecord> findByDonation(int donationId) {
        return null;
    }
}
