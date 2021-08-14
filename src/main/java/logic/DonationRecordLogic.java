package logic;

import common.ValidationException;
import dal.DonationRecordDAL;
import entity.DonationRecord;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author Milad Mobini
 */
public class DonationRecordLogic extends GenericLogic<DonationRecord, DonationRecordDAL> {

    public static final String PERSON_ID = "person_id";
    public static final String DONATION_ID = "donation_id";
    public static final String TESTED = "tested";
    public static final String ADMINISTRATOR = "administrator";
    public static final String HOSPITAL = "hospital";
    public static final String CREATED = "created";
    public static final String ID = "record_id";

    /**
     * Constructor for DonationRecordLogic
     */
    DonationRecordLogic() {
        super(new DonationRecordDAL());
    }

    /**
     * returns all the instance of donation record from database using DAL
     *
     * @return List<DonationRecord> all the instance of donation record from
     * database
     */
    @Override
    public List<DonationRecord> getAll() {
        return get(() -> dal().findAll());
    }

    /**
     * returns the donation record instance with the same record ID from
     * database using DAL
     *
     * @param personId the record id
     * @return DonationRecord donation record instance with the same record ID
     */
    @Override
    public DonationRecord getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    /**
     * returns the donation record instances with the same value for tested from
     * database using DAL
     *
     * @param personId the tested
     * @return List<DonationRecord> donation record instances with the same
     * value for tested
     */
    public List<DonationRecord> getDonationRecordWithTested(boolean tested) {
        return get(() -> dal().findByTested(tested));
    }

    /**
     * returns the donation record instances with the same value for
     * administrator from database using DAL
     *
     * @param personId the administrator
     * @return List<DonationRecord> donation record instances with the same
     * value for administrator
     */
    public List<DonationRecord> getDonationRecordWithAdministrator(String administrator) {
        return get(() -> dal().findByAdministrator(administrator));
    }

    /**
     * returns the donation record instances with the same value for hospital
     * from database using DAL
     *
     * @param personId the Hospital name
     * @return List<DonationRecord> donation record instances with the same
     * value for hospital
     */
    public List<DonationRecord> getDonationRecordWithHospital(String username) {
        return get(() -> dal().findByHospital(username));
    }

    /**
     * returns the donation record instances with the same date value for
     * created from database using DAL
     *
     * @param personId the date created
     * @return List<DonationRecord> donation record instances with the same date
     * value for created
     */
    public List<DonationRecord> getDonationRecordsWithCreated(Date created) {
        return get(() -> dal().findByCreated(created));
    }

    /**
     * returns the donation record instances with the same value for person Id
     * from database using DAL
     *
     * @param personId the person id
     * @return List<DonationRecord> donation record instances with the same
     * value for person Id
     */
    public List<DonationRecord> getDonationRecordsWithPerson(int personId) {
        return get(() -> dal().findByPerson(personId));
    }

    /**
     * returns the donation record instances with the same value for donation Id
     * from database using DAL
     *
     * @param donationId the donation id
     * @return List<DonationRecord> donation record instances with the same
     * value for donation Id
     */
    public List<DonationRecord> getDonationRecordsWithDonation(int donationId) {
        return get(() -> dal().findByDonation(donationId));
    }

    /**
     * create a donation record entity from a map of string of all values
     *
     * @param parameterMap a map of all values
     * @return the expected donation record instance
     */
    @Override
    public DonationRecord createEntity(Map<String, String[]> parameterMap) {
        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");

        DonationRecord entity = new DonationRecord();

        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
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
        String tested = parameterMap.get(TESTED)[0];
        String admin = parameterMap.get(ADMINISTRATOR)[0];
        String hospital = parameterMap.get(HOSPITAL)[0];
        String created = parameterMap.get(CREATED)[0];
        created = created.replace("T", " "); //From Ryan's code
        //validate the data
        validator.accept(admin, 45);
        validator.accept(hospital, 65);
        //set values on entity
        entity.setAdministrator(admin);
        entity.setHospital(hospital);
        entity.setTested(Boolean.parseBoolean(tested));
        try {
            entity.setCreated(convertStringToDate(created));
        } catch (ValidationException ex) {
            entity.setCreated(new Date());
        }
        return entity;
    }

    /**
     * get all the column names for donation record for table view
     *
     * @return List<String> list of names of columns
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Person ID", "Blood Donation ID", "Tested", "Administrator", "Hospital", "Created");
    }

    /**
     * get all the column names
     *
     * @return List<String> list of all column names
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, PERSON_ID, DONATION_ID, TESTED, ADMINISTRATOR, HOSPITAL, CREATED);
    }

    /**
     * extract the date from a record and returns it as a list
     *
     * @param e the record
     * @return List<?> list of extracted values
     */
    @Override
    public List<?> extractDataAsList(DonationRecord e) {
        return Arrays.asList(
                e.getId(),
                e.getPerson(),
                e.getBloodDonation(),
                e.getTested(),
                e.getAdministrator(),
                e.getHospital(),
                e.getCreated());
    }
}
