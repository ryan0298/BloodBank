package logic;

import common.ValidationException;
import dal.BloodDonationDAL;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.util.Arrays;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author ryanh
 */
public class BloodDonationLogic extends GenericLogic<BloodDonation, BloodDonationDAL>{

    public String BANK_ID = "bank_id";
    public String MILLILITERS = "milliliters";
    public String BLOOD_GROUP = "blood_group";
    public String RHESUS_FACTOR = "rhesus_factor";
    public String CREATED = "created";
    public String ID = "id";
    
    public BloodDonationLogic() {
        super(new BloodDonationDAL());
    }
    
    @Override
    public List<BloodDonation> getAll() {
        return get( () -> dal().findAll() );
    }
    
    @Override
    public BloodDonation getWithId(int id) {
        return get( () -> dal().findById(id));
    }
    
    public List<BloodDonation> getBloodDonationWithMilliliters(int milliliters) {
        return get( () -> dal().findByMilliliters(milliliters));
    }
    
    public List<BloodDonation> getBloodDonationWithBloodGroup(BloodGroup bloodGroup) {
        return get( () -> dal().findByBloodGroup(bloodGroup));
    }
    
    public List<BloodDonation> getBloodDonationWithCreated(Date created) {
        return get( () -> dal().findByCreated(created));
    }
    
    public List<BloodDonation> getBloodDonationsWithRhd(RhesusFactor rhd) {
        return get( () -> dal().findByRhd(rhd));
    }
    
    public List<BloodDonation> getBloodDonationsWithBloodBank(int bankId) {
        return get( () -> dal().findByBloodBank(bankId));
    }
    
    @Override
    public BloodDonation createEntity(Map<String, String[]> paremeterMap) {
        Objects.requireNonNull(paremeterMap, "parameterMap cannot be null");

        BloodDonation entity = new BloodDonation();

        if (paremeterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(paremeterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }

        ObjIntConsumer< String> validator = (value, length) -> {
            if (value == null || value.trim().isEmpty() || value.length() > length) {
                String error = "";
                if (value == null || value.trim().isEmpty()) {
                    error = "value cannot be null or empty: " + value;
                }
                if (value.length() > length) {
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException(error);
            }
        };

        String bankId = paremeterMap.get(BANK_ID)[0];
        String bloodGroup = paremeterMap.get(BLOOD_GROUP)[0];
        String created = paremeterMap.get(CREATED)[0];
        String milliliters = paremeterMap.get(MILLILITERS)[0];
        String rhesusFactor = paremeterMap.get(RHESUS_FACTOR)[0];

        validator.accept(bankId, 45);
        validator.accept(bloodGroup, 45);
        validator.accept(created, 45);
        validator.accept(milliliters, 45);
        validator.accept(rhesusFactor, 45);

        entity.setId(Integer.parseInt(bankId));
        entity.setBloodGroup(BloodGroup.valueOf(bloodGroup));
        entity.setCreated(convertStringToDate(created));
        entity.setMilliliters(Integer.getInteger(milliliters));
        entity.setRhd(RhesusFactor.valueOf(rhesusFactor));

        return entity;

    }
    
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList( "bank_id", "milliliters", "blood_group", "rhesus_factor", "created", "id" );
    }
    
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList( BANK_ID, MILLILITERS, BLOOD_GROUP, RHESUS_FACTOR, CREATED, ID );
    }
    
    @Override
    public List<?> extractDataAsList(BloodDonation e) {
        return Arrays.asList( e.getBloodBank(), e.getMilliliters(), e.getBloodGroup(), e.getRhd(), e.getCreated(), e.getId() );
    }
    
}
