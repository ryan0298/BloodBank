package dal;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.util.Date;
import java.util.List;
/**
 *
 * @author ryanh
 */
public class BloodDonationDAL extends GenericDAL{

    public BloodDonationDAL() {
        super(BloodDonationDAL.class);
    }
    
    @Override
    public List<BloodDonation> findAll() {
        return null;
    }
    
    @Override
    public BloodDonation findById(int donationId) {
        return null;
    }
    
    public List<BloodDonation> findByMilliliters(int milliliters) {
        return null;
    }
    
    public List<BloodDonation> findByBloodGroup(BloodGroup bloodGroup) {
        return null;
    }
    
    public List<BloodDonation> findByRhd(RhesusFactor rhd) {
        return null;
    }
    
    public List<BloodDonation> findByCreated(Date created) {
        return null;
    }
    
    public List<BloodDonation> findByBloodBank(int bloodBankId) {
        return null;
    }
    
    
}
