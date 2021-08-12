package logic;

import common.ValidationException;
import java.util.List;
import entity.BloodBank;
import java.util.Date;
import java.util.Map;
import dal.BloodBankDAL;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import static logic.BloodDonationLogic.BLOOD_GROUP;
import static logic.BloodDonationLogic.CREATED;
import static logic.BloodDonationLogic.ID;
import static logic.BloodDonationLogic.MILLILITERS;
import static logic.BloodDonationLogic.RHESUS_FACTOR;
/**
 *
 * @author ryanh
 */
public class BloodBankLogic extends GenericLogic<BloodBank, BloodBankDAL>{
    
    String OWNER_ID = "owner_id";
    String PRIVATELY_OWNED = "privately_owned";
    String ESTABLISHED = "established";
    String NAME = "name";
    String EMPLOYEE_COUNT = "employee_count";
    String ID = "id";
    
    BloodBankLogic() {
        super( new BloodBankDAL() );
    }
    
    @Override
    public List<BloodBank> getAll() {
        return get( () -> dal().findAll() );
    }
    
    @Override
    public BloodBank getWithId(int id) {
        return get( () -> dal().findById(id));
    }
    
    public BloodBank getBloodBankWithName(String name) {
        return get( () -> dal().findByName(name));
    }
    
    public List<BloodBank> getBloodBankWithPrivatelyOwned(boolean privatelyOwned) {
        return get( () -> dal().findByPreviouslyOwned(privatelyOwned));
    }
    
    public List<BloodBank> getBloodBankWithEstablished(Date established) {
        return get( () -> dal().findByEstablished(established));
    }
    
    public BloodBank getBloodBanksWithOwner(int ownerId) {
        return get( () -> dal().findByOwner(ownerId));
    }
    
    public List<BloodBank> getBloodBanksWithEmplyeeCount(int count) {
        return get( () -> dal().findByEmployeeCount(count));
    }
    
    public BloodBank createEntity(Map<String, String[]> paremeterMap) {
        
        Objects.requireNonNull(paremeterMap, "parameterMap cannot be null");

        BloodBank entity = new BloodBank();

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

        //String ownerID = paremeterMap.get(OWNER_ID)[0];
        String privatelyOwned = paremeterMap.get(PRIVATELY_OWNED)[0];
        String established = paremeterMap.get(ESTABLISHED)[0];//initial date with T placeholder
        String newEstablishedRemovedT = established.replace("T", " ");//Updated created without the T placeholder
        String name = paremeterMap.get(NAME)[0];
        String employeeCount = paremeterMap.get(EMPLOYEE_COUNT)[0];
        //String id = paremeterMap.get(ID)[0];
        

        validator.accept(privatelyOwned, 45);
        validator.accept(established, 45);
        validator.accept(name, 45);
        validator.accept(employeeCount, 45);

        entity.setPrivatelyOwned(Boolean.valueOf(privatelyOwned));
        
        //entity.setEstablished(established);
        try {
            entity.setEstablished(convertStringToDate(newEstablishedRemovedT));
        }catch (ValidationException e) {
            entity.setEstablished(new Date());
        }
        
        entity.setName(name);
        entity.setEmplyeeCount(Integer.valueOf(employeeCount));

        return entity;
    }
    
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList( "owner_id", "privately_owned", "established", "name", "employee_count", "id" );
    }
    
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList( OWNER_ID, PRIVATELY_OWNED, ESTABLISHED, NAME, EMPLOYEE_COUNT, ID );
    }
    
    @Override
    public List<?> extractDataAsList(BloodBank e) {
        return Arrays.asList( e.getOwner(), e.getPrivatelyOwned(), e.getEstablished(), e.getName(), e.getEmplyeeCount(), e.getId() );
    }
        
}
