
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
 * @author William
 */
public class BloodBankLogic {
public static String OWNER_ID = "owner_id";
public static String PRIVATELY_OWNED = "privately_owned";
public static String ESTABLISHED = "established";
public static String NAME : String = "name";
public static String EMPLOYEE_COUNT = "employee_count";
public static String ID = "id";
    public BloodBankLogic() {
        super(new BloodBankDAL());

    }
@Override
    public List<BloodBank> getAll() {
        return get( () -> dal().findAll() );
    }
@Override
    @Override
    public BloodBank getWithId(int id) {
        return get( () -> dal().findById(id));
    }
@Override
    public BloodBank getBloodBankWithName(String name){
        return get( () -> dal().findByName(name));
    }
@Override
    public List<BloodBank> getBloodBankWithPrivatelyOwned(boolean privatelyOwned){
          return get( () -> dal().findByPrivatelyOwned(privatelyOwned));
    }
@Override
    public List<BloodBank> getBloodBankWithEstablished(Date established){
          return get( () -> dal().findByEstabilished(privatelyOwned));

    }
@Override 
    public BloodBank getBloodBankWithOwner(int ownerId){
          return get( () -> dal().findByOwner(ownerId));

    }
@Override
    public List<BloodBank> getBloodBankWithEmployeeCount(int count){
                  return get( () -> dal().findByEmployeeCount(count));

    }
@Override
    public BloodBank createEntity(Map<String, String[]> paremeterMap){
         Objects.requireNonNull(paremeterMap, "parameterMap cannot be null");

        BloodBank bankEntity = new BloodBank();
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
         String bankId = paremeterMap.get(BANK_ID);
         String owner = paremeterMap.get(OWNER);
         String name = paremeterMap.get(NAME);
         String privatelyOwned = paremeterMap.get(PRIVATELY_OWNED);
         String employee = paremeterMap.get(EMPLYEE_COUNT);
        

            bankEntity.setName(NAME.valueOf(name));
            bankEntity.setId(ID.valueOf(bankId));
            bankEntity.setPrivatelyOwned(PRIVATELY_OWNED.valueOf(privatelyOwned));
            bankEntity.setEmplyeeCount(EMPLOYEE_COUNT.valueOf(employee));
            bankEntity.setOwner(OWNER.valueOf(owner))

            return bankEntity;
    }
@Override
     public List<String> getColumnNames() {
        return Arrays.asList( "bank_id", "owner", "name", "privately_owned", "established", "id" );
    }
    
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList( BANK_ID, OWNER, NAME, PRIVATELY_OWNED, EMPLYEE_COUNT );
    }
    
    @Override
    public List<?> extractDataAsList(BloodDonation e) {
        return Arrays.asList( e.getBloodBankWithName(), e.getBloodBankWithPrivatelyOwned(), e.getBloodBankWithEstablished(), e.getBloodBanksWithOwner(), e.getBloodBanksWithEmplyeeCount(), e.getId() );
    }
    
}
