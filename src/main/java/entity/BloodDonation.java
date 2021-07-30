package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Shariar
 */
@Entity
@Table( name = "blood_donation", catalog = "simplebloodbank", schema = "", uniqueConstraints = {
    @UniqueConstraint( columnNames = { "donation_id" } ) } )
@NamedQueries( {
    @NamedQuery( name = "BloodDonation.findAll", query = "SELECT b FROM BloodDonation b" ),
    @NamedQuery( name = "BloodDonation.findByDonationId", query = "SELECT b FROM BloodDonation b WHERE b.donationId = :donationId" ),
    @NamedQuery( name = "BloodDonation.findByMilliliters", query = "SELECT b FROM BloodDonation b WHERE b.milliliters = :milliliters" ),
    @NamedQuery( name = "BloodDonation.findByBloodGroup", query = "SELECT b FROM BloodDonation b WHERE b.bloodGroup = :bloodGroup" ),
    @NamedQuery( name = "BloodDonation.findByRhd", query = "SELECT b FROM BloodDonation b WHERE b.rhd = :rhd" ),
    @NamedQuery( name = "BloodDonation.findByBloodBank", query = "SELECT b FROM BloodDonation b WHERE b.bloodBank.bankId = :bloodBankId" ),
    @NamedQuery( name = "BloodDonation.findByCreated", query = "SELECT b FROM BloodDonation b WHERE b.created = :created" ) } )
public class BloodDonation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "donation_id", nullable = false )
    private Integer donationId;
    @Basic( optional = false )
    @NotNull
    @Column( name = "milliliters", nullable = false )
    private int milliliters;
    @Basic( optional = false )
    @NotNull
    @Column( name = "blood_group", nullable = false, length = 2 )
    @Enumerated( EnumType.STRING )
    private BloodGroup bloodGroup;
    @Basic( optional = false )
    @NotNull
    @Column( name = "rhd", nullable = false )
    @Convert( converter = RhesusFactorConvertor.class, disableConversion = false )
    private RhesusFactor rhd;
    @Basic( optional = false )
    @NotNull
    @Column( name = "created", nullable = false )
    @Temporal( TemporalType.TIMESTAMP )
    private Date created;
    @JoinColumn( name = "bank_id", referencedColumnName = "bank_id" )
    @ManyToOne( fetch = FetchType.LAZY )
    private BloodBank bloodBank;
    @OneToMany( mappedBy = "bloodDonation", fetch = FetchType.LAZY )
    private Set<DonationRecord> donationRecordSet;

    public BloodDonation() {
    }

    public BloodDonation( Integer donationId ) {
        this.donationId = donationId;
    }

    public BloodDonation( Integer donationId, int milliliters, BloodGroup bloodGroup, RhesusFactor rhd, Date created ) {
        this.donationId = donationId;
        this.milliliters = milliliters;
        this.bloodGroup = bloodGroup;
        this.rhd = rhd;
        this.created = created;
    }

    public Integer getId() {
        return donationId;
    }

    public void setId( Integer donationId ) {
        this.donationId = donationId;
    }

    public int getMilliliters() {
        return milliliters;
    }

    public void setMilliliters( int milliliters ) {
        this.milliliters = milliliters;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup( BloodGroup bloodGroup ) {
        this.bloodGroup = bloodGroup;
    }

    public RhesusFactor getRhd() {
        return rhd;
    }

    public void setRhd( RhesusFactor rhd ) {
        this.rhd = rhd;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated( Date created ) {
        this.created = created;
    }

    public BloodBank getBloodBank() {
        return bloodBank;
    }

    public void setBloodBank( BloodBank bloodBank ) {
        this.bloodBank = bloodBank;
    }

    public Set<DonationRecord> getDonationRecordSet() {
        return donationRecordSet;
    }

    public void setDonationRecordSet( Set<DonationRecord> donationRecordSet ) {
        this.donationRecordSet = donationRecordSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += ( donationId != null ? donationId.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object ) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !( object instanceof BloodDonation ) ){
            return false;
        }
        BloodDonation other = (BloodDonation)object;
        if( ( this.donationId == null && other.donationId != null ) || ( this.donationId != null && !this.donationId.equals( other.donationId ) ) ){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BloodDonation[ donationId=" + donationId + " ]";
    }

}
