package logic;

import dal.BloodDonationDAL;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;

import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author ryanh
 */
public class BloodDonationLogic extends GenericLogic<BloodDonation, BloodDonationDAL>{

    public BloodDonationLogic() {
        super(new BloodDonationDAL());
    }
    
    @Override
    public List<BloodDonation> getAll() {
        return null;
    }
    
    @Override
    public BloodDonation getWithId(int id) {
        return null;
    }
    
    public List<BloodDonation> getBloodDonationWithMilliliters(int milliliters) {
        return null;
    }
    
    public List<BloodDonation> getBloodDonationWithBloodGroup(BloodGroup bloodGroup) {
        return null;
    }
    
    public List<BloodDonation> getBloodDonationWithCreated(Date created) {
        return null;
    }
    
    public List<BloodDonation> getBloodDonationsWithRhd(RhesusFactor rhd) {
        return null;
    }
    
    public List<BloodDonation> getBloodDonationsWithBloodBank(int bankId) {
        return null;
    }
    
    @Override
    public BloodDonation createEntity(Map<String, String[]> paremeterMap) {
        return null;
    }
    
    @Override
    public List<String> getColumnNames() {
        return null;
    }
    
    @Override
    public List<String> getColumnCodes() {
        return null;
    }
    
    @Override
    public List<?> extractDataAsList(BloodDonation e) {
        return null;
    }
    
}
