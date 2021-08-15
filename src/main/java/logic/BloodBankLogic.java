package logic;

import common.ValidationException;
import java.util.List;
import entity.BloodBank;
import java.util.Date;
import java.util.Map;
import dal.BloodBankDAL;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author ryanh
 * @author Milad Mobini
 */
public class BloodBankLogic extends GenericLogic<BloodBank, BloodBankDAL> {

    public static String OWNER_ID = "owner_id";
    public static String PRIVATELY_OWNED = "privately_owned";
    public static String ESTABLISHED = "established";
    public static String NAME = "name";
    public static String EMPLOYEE_COUNT = "employee_count";
    public static String ID = "id";

    /**
     * Constructor for BloodBankLogic
     */
    BloodBankLogic() {
        super(new BloodBankDAL());
    }

    /**
     * returns all the instance of blood bank from database using DAL
     *
     * @return List<BloodBank> all the instance of blood bank from database
     */
    @Override
    public List<BloodBank> getAll() {
        return get(() -> dal().findAll());
    }

    /**
     * returns the blood bank instance with the same record ID from database
     * using DAL
     *
     * @param id the id
     * @return BloodBank blood bank instance with the same bank ID
     */
    @Override
    public BloodBank getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    /**
     * returns the blood bank instances with the same value for name from
     * database using DAL
     *
     * @param name the name
     * @return List<BloodBank> blood bank instances with the same value for name
     */
    public BloodBank getBloodBankWithName(String name) {
        return get(() -> dal().findByName(name));
    }

    /**
     * returns the blood bank instances with the same value for privatelyOwned
     * from database using DAL
     *
     * @param privatelyOwned the privately Owned status
     * @return List<BloodBank> blood bank instances with the same value for
     * privatelyOwned
     */
    public List<BloodBank> getBloodBankWithPrivatelyOwned(boolean privatelyOwned) {
        return get(() -> dal().findByPreviouslyOwned(privatelyOwned));
    }

    /**
     * returns the blood bank instances with the same value for established from
     * database using DAL
     *
     * @param established the established
     * @return List<BloodBank> blood bank instances with the same value for
     * established
     */
    public List<BloodBank> getBloodBankWithEstablished(Date established) {
        return get(() -> dal().findByEstablished(established));
    }

    /**
     * returns the blood bank instances with the same value for ownerId from
     * database using DAL
     *
     * @param ownerId the owner Id
     * @return List<BloodBank> blood bank instances with the same value for
     * ownerId
     */
    public BloodBank getBloodBanksWithOwner(int ownerId) {
        return get(() -> dal().findByOwner(ownerId));
    }

    /**
     * returns the blood bank instances with the same value for count from
     * database using DAL
     *
     * @param count the count
     * @return List<BloodBank> blood bank instances with the same value for
     * count
     */
    public List<BloodBank> getBloodBanksWithEmplyeeCount(int count) {
        return get(() -> dal().findByEmployeeCount(count));
    }

    /**
     * create a blood bank entity from a map of string of all values
     *
     * @param parameterMap a map of all values
     * @return the expected blood bank instance
     */
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

        String privatelyOwned = paremeterMap.get(PRIVATELY_OWNED)[0];
        String established = paremeterMap.get(ESTABLISHED)[0];//initial date with T placeholder
        String newEstablishedRemovedT = established.replace("T", " ");//Updated created without the T placeholder
        String name = paremeterMap.get(NAME)[0];
        String employeeCount = paremeterMap.get(EMPLOYEE_COUNT)[0];

        validator.accept(name, 100);
        validator.accept(employeeCount, 45);

        entity.setPrivatelyOwned(Boolean.valueOf(privatelyOwned));

        try {
            entity.setEstablished(convertStringToDate(newEstablishedRemovedT));
        } catch (ValidationException e) {
            entity.setEstablished(new Date());
        }

        entity.setName(name);
        entity.setEmplyeeCount(Integer.valueOf(employeeCount));

        return entity;
    }

    /**
     * get all the column names for blood bank for table view
     *
     * @return List<String> list of names of columns
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Owner ID", "Privately Owned", "established", "Name", "Employee Count");
    }

    /**
     * get all the column names
     *
     * @return List<String> list of all column names
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, OWNER_ID, PRIVATELY_OWNED, ESTABLISHED, NAME, EMPLOYEE_COUNT);
    }

    /**
     * extract the date from a record and returns it as a list
     *
     * @param e the record
     * @return List<?> list of extracted values
     */
    @Override
    public List<?> extractDataAsList(BloodBank e) {
        return Arrays.asList(e.getId(), e.getOwner(), e.getPrivatelyOwned(), e.getEstablished(), e.getName(), e.getEmplyeeCount());
    }

}
