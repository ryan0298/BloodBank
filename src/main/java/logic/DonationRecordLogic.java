package logic;

import common.ValidationException;
import dal.AccountDAL;
import dal.BloodDonationDAL;
import dal.DonationRecordDAL;
import entity.Account;
import entity.BloodDonation;
import entity.DonationRecord;
import entity.Person;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    DonationRecordLogic() {
        super(new DonationRecordDAL());
    }

    @Override
    public List<DonationRecord> getAll() {
        return get(() -> dal().findAll());
    }

    @Override
    public DonationRecord getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    public List<DonationRecord> getDonationRecordWithTested(boolean tested) {
        return get(() -> dal().findByTested(tested));
    }

    public List<DonationRecord> getDonationRecordWithAdministrator(String administrator) {
        return get(() -> dal().findByAdministrator(administrator));
    }

    public List<DonationRecord> getDonationRecordWithHospital(String username) {
        return get(() -> dal().findByHospital(username));
    }

    public List<DonationRecord> getDonationRecordsWithCreated(Date created) {
        return get(() -> dal().findByCreated(created));
    }

    public List<DonationRecord> getDonationRecordsWithPerson(int personId) {
        return get(() -> dal().findByPerson(personId));
    }

    public List<DonationRecord> getDonationRecordsWithDonation(int donationId) {
        return get(() -> dal().findByDonation(donationId));
    }

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

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Person ID", "Blood Donation ID", "Tested", "Administrator", "Hospital", "Created");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, PERSON_ID, DONATION_ID, TESTED, ADMINISTRATOR, HOSPITAL, CREATED);
    }

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
