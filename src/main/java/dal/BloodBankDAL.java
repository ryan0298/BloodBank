
package dal;

/**
 *
 * @author William
 */
public class BloodBankDAL extents GenericDAL<BloodBank> {
       
 public BloodDonationDAL() {
        super(BloodDonation.class);
    }
    public List<BloodBank> findAll(){
         return findResults("BloodBank.findAll", null);
    }
    
    public BloodDonation findByID(int bankId){
        Map<String, Object> map = new HashMap<>();
        map.put( "bankId", bankId);
        return findResult("BloodBank.findByBankId", map);
    }

    public BloodBank findByName(String name){
        Map<String, Object> map = new HashMap<>();
        map.put( "name", name);
        return findResult("BloodBank.findByName", map);
    }

    public List<BloodBank> findByPrivatelyOwned(boolean privatelyOwned){
    Map<String, Object> map = new HashMap<>();
        map.put( "privatelyOwned", privatelyOwned);
        return findResult("BloodBank.findByPrivatelyOwned", map);
    }
    
    public List<BloodBank> findByEstabilished(Date established){
        Map<String, Object> map = new HashMap<>();
        map.put( "established", established);
        return findResult("BloodBank.findByEstabilished", map);
    }

    public List<BloodBank> findByEmployeeCount(int employeeCount){
        Map<String, Object> map = new HashMap<>();
        map.put( "emplyeeCount", employeeCount);
        return findResult("BloodBank.findByEmplyeeCount", map);
    }
    public BloodBank findByOwner(int OwnerId){
        Map<String, Object> map = new HashMap<>();
        map.put( "ownerId", OwnerId);
        return findResult("BloodBank.findByOwner", map);
    }
    public List<BloodBank> findContaining(String search){
        Map<String, Object> map = new HashMap<>();
        map.put( "search", search );
        return findResults( "BloodBank.findContaining", map );
    }
}
