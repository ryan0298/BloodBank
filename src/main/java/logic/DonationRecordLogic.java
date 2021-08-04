package logic;

import common.ValidationException;
import dal.BloodDonationDAL;
import dal.DonationRecordDAL;
import entity.DonationRecord;
import entity.Person;
import java.text.DateFormat;
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

    public String PERSON_ID = "person_id";
    public String DONATION_ID = "donation_id";
    public String TESTED = "tested";
    public String ADMINISTRATOR = "administrator";
    public String HOSPITAL = "hospital";
    public String CREATED = "created";
    public String ID = "id";

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

        String personId = parameterMap.get(PERSON_ID)[0];
        String donationId = parameterMap.get(DONATION_ID)[0];
        String tested = parameterMap.get(TESTED)[0];
        String admin = parameterMap.get(ADMINISTRATOR)[0];
        String hospital = parameterMap.get(HOSPITAL)[0];
        String created = parameterMap.get(CREATED)[0];

        //validate the data
        validator.accept(personId, 10);
        validator.accept(donationId, 10);
        validator.accept(tested, 6);
        validator.accept(admin, 45);
        validator.accept(hospital, 45);
        validator.accept(created, 45);
        
        //set values on entity
        entity.setAdministrator(admin);
        BloodDonationDAL donationRd = new BloodDonationDAL();
        entity.setBloodDonation(donationRd.findById(Integer.parseInt(donationId)));
        entity.setCreated(convertStringToDate(created));
        entity.setHospital(hospital);
        
//        PersonDAL person = new PersonDAL();
//        entity.setPerson(person.findById(Integer.parseInt(personId)));

        Person person = new Person(); //Temp
        entity.setPerson(person);     //Temp
        entity.setTested(Boolean.parseBoolean(tested));

        return entity;
    }

    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Person", "Blood Donation", "Tested", "Administrator", "Hospital", "Created");
    }

    public List<String> getColumnCodes() {
        return Arrays.asList(ID, PERSON_ID, DONATION_ID, TESTED, ADMINISTRATOR, HOSPITAL, CREATED);
    }

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
