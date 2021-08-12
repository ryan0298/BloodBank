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


//temp
import entity.BloodBank;
import java.util.Set;
/**
 *
 * @author ryanh
 */
public class BloodDonationLogic extends GenericLogic<BloodDonation, BloodDonationDAL>{

    public static String BANK_ID = "bloodbank_id";
    public static String MILLILITERS = "milliliters";
    public static String BLOOD_GROUP = "blood_group";
    public static String RHESUS_FACTOR = "rhesus_factor";
    public static String CREATED = "created";
    public static String ID = "donation_id";
    
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
        String created = paremeterMap.get(CREATED)[0];//Initial date with the T placeholder
        String newCreatedRemovedT = created.replace("T", " ");//Updated created without the T placeholder
        String milliliters = paremeterMap.get(MILLILITERS)[0];
        String rhesusFactor = paremeterMap.get(RHESUS_FACTOR)[0];

        validator.accept(bankId, 45);
        validator.accept(bloodGroup, 45);
        validator.accept(created, 45);
        validator.accept(milliliters, 45);
        validator.accept(rhesusFactor, 45);

        //entity.setBloodDonation(donationRd.findById(Integer.parseInt(donationId)));
        //entity.setId(Integer.parseInt(bankId));
        //entity.setId(BloodBankDAL.findById(Integer.parseInt(bankId)));
        
        //Correct code
//        BloodBankDAL bloodBankDAL = new BloodBankDAL();
//        entity.setId(bloodBankDAL.findById(Integer.parseInt(bankId)));
        
        //Temp, to be removed
        //BloodBank bloodBank = new BloodBank();
        //bloodBank.setId(Integer.parseInt(bankId));
        //entity.setBloodBank(bloodBank);
        BloodBankLogic bloodBankLogic = LogicFactory.getFor( "BloodBank" );
        entity.setBloodBank(bloodBankLogic.getWithId(Integer.parseInt(bankId)));
        
        DonationRecordLogic bloodRecordLogic = LogicFactory.getFor( "DonationRecord" );
        entity.setDonationRecordSet(Set.copyOf(bloodRecordLogic.getDonationRecordsWithDonation(Integer.parseInt(paremeterMap.get(ID)[0]))));
        
        entity.setBloodGroup(BloodGroup.valueOf(bloodGroup));
        
        try {
            entity.setCreated(convertStringToDate(newCreatedRemovedT));
        }catch (ValidationException e) {
            entity.setCreated(new Date());
        }
        
        entity.setMilliliters(Integer.parseInt(milliliters));
        entity.setRhd(RhesusFactor.getRhesusFactor(rhesusFactor));

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
