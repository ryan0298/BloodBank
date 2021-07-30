package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Shariar
 */
@Entity
@Table( name = "donation_record", catalog = "simplebloodbank", schema = "", uniqueConstraints = {
    @UniqueConstraint( columnNames = { "record_id" } ) } )
@NamedQueries( {
    @NamedQuery( name = "DonationRecord.findAll", query = "SELECT d FROM DonationRecord d" ),
    @NamedQuery( name = "DonationRecord.findByRecordId", query = "SELECT d FROM DonationRecord d WHERE d.recordId = :recordId" ),
    @NamedQuery( name = "DonationRecord.findByTested", query = "SELECT d FROM DonationRecord d WHERE d.tested = :tested" ),
    @NamedQuery( name = "DonationRecord.findByAdministrator", query = "SELECT d FROM DonationRecord d WHERE d.administrator = :administrator" ),
    @NamedQuery( name = "DonationRecord.findByHospital", query = "SELECT d FROM DonationRecord d WHERE d.hospital = :hospital" ),
    @NamedQuery( name = "DonationRecord.findByPerson", query = "SELECT d FROM DonationRecord d WHERE d.person.id = :personId" ),
    @NamedQuery( name = "DonationRecord.findByDonation", query = "SELECT d FROM DonationRecord d WHERE d.bloodDonation.donationId = :donationId" ),
    @NamedQuery( name = "DonationRecord.findByCreated", query = "SELECT d FROM DonationRecord d WHERE d.created = :created" ) } )
public class DonationRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "record_id", nullable = false )
    private Integer recordId;
    @Basic( optional = false )
    @NotNull
    @Column( name = "tested", nullable = false )
    private boolean tested;
    @Basic( optional = false )
    @NotNull
    @Size( min = 1, max = 100 )
    @Column( name = "administrator", nullable = false, length = 100 )
    private String administrator;
    @Basic( optional = false )
    @NotNull
    @Size( min = 1, max = 100 )
    @Column( name = "hospital", nullable = false, length = 100 )
    private String hospital;
    @Basic( optional = false )
    @NotNull
    @Column( name = "created", nullable = false )
    @Temporal( TemporalType.TIMESTAMP )
    private Date created;
    @JoinColumn( name = "donation_id", referencedColumnName = "donation_id" )
    @ManyToOne( fetch = FetchType.LAZY )
    private BloodDonation bloodDonation;
    @JoinColumn( name = "person_id", referencedColumnName = "id" )
    @ManyToOne( fetch = FetchType.LAZY )
    private Person person;

    public DonationRecord() {
    }

    public DonationRecord( Integer recordId ) {
        this.recordId = recordId;
    }

    public DonationRecord( Integer recordId, boolean tested, String administrator, String hospital, Date created ) {
        this.recordId = recordId;
        this.tested = tested;
        this.administrator = administrator;
        this.hospital = hospital;
        this.created = created;
    }

    public Integer getId() {
        return recordId;
    }

    public void setId( Integer recordId ) {
        this.recordId = recordId;
    }

    public boolean getTested() {
        return tested;
    }

    public void setTested( boolean tested ) {
        this.tested = tested;
    }

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator( String administrator ) {
        this.administrator = administrator;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital( String hospital ) {
        this.hospital = hospital;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated( Date created ) {
        this.created = created;
    }

    public BloodDonation getBloodDonation() {
        return bloodDonation;
    }

    public void setBloodDonation( BloodDonation bloodDonation ) {
        this.bloodDonation = bloodDonation;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson( Person person ) {
        this.person = person;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += ( recordId != null ? recordId.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object ) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !( object instanceof DonationRecord ) ){
            return false;
        }
        DonationRecord other = (DonationRecord)object;
        if( ( this.recordId == null && other.recordId != null ) || ( this.recordId != null && !this.recordId.equals( other.recordId ) ) ){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DonationRecord[ recordId=" + recordId + " ]";
    }

}
