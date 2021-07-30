package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table( name = "blood_bank", catalog = "simplebloodbank", schema = "", uniqueConstraints = {
    @UniqueConstraint( columnNames = { "bank_id" } ),
    @UniqueConstraint( columnNames = { "name" } ) } )
@NamedQueries( {
    @NamedQuery( name = "BloodBank.findAll", query = "SELECT b FROM BloodBank b" ),
    @NamedQuery( name = "BloodBank.findByBankId", query = "SELECT b FROM BloodBank b WHERE b.bankId = :bankId" ),
    @NamedQuery( name = "BloodBank.findByName", query = "SELECT b FROM BloodBank b WHERE b.name = :name" ),
    @NamedQuery( name = "BloodBank.findByPrivatelyOwned", query = "SELECT b FROM BloodBank b WHERE b.privatelyOwned = :privatelyOwned" ),
    @NamedQuery( name = "BloodBank.findByEstablished", query = "SELECT b FROM BloodBank b WHERE b.established = :established" ),
    @NamedQuery( name = "BloodBank.findByOwner", query = "SELECT b FROM BloodBank b WHERE b.owner.id = :ownerId" ),
    @NamedQuery( name = "BloodBank.findByEmplyeeCount", query = "SELECT b FROM BloodBank b WHERE b.emplyeeCount = :emplyeeCount" ) } )
public class BloodBank implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "bank_id", nullable = false )
    private Integer bankId;
    @Basic( optional = false )
    @NotNull
    @Size( min = 1, max = 100 )
    @Column( name = "name", nullable = false, length = 100 )
    private String name;
    @Basic( optional = false )
    @NotNull
    @Column( name = "privately_owned", nullable = false )
    private boolean privatelyOwned;
    @Basic( optional = false )
    @NotNull
    @Column( name = "established", nullable = false )
    @Temporal( TemporalType.TIMESTAMP )
    private Date established;
    @Basic( optional = false )
    @NotNull
    @Column( name = "emplyee_count", nullable = false )
    private int emplyeeCount;
    @OneToMany( mappedBy = "bloodBank", fetch = FetchType.LAZY )
    private Set<BloodDonation> bloodDonationSet;
    @JoinColumn( name = "owner", referencedColumnName = "id" )
    @OneToOne( fetch = FetchType.LAZY )
    private Person owner;

    public BloodBank() {
    }

    public BloodBank( Integer bankId ) {
        this.bankId = bankId;
    }

    public BloodBank( Integer bankId, String name, boolean privatelyOwned, Date established, int emplyeeCount ) {
        this.bankId = bankId;
        this.name = name;
        this.privatelyOwned = privatelyOwned;
        this.established = established;
        this.emplyeeCount = emplyeeCount;
    }

    public Integer getId() {
        return bankId;
    }

    public void setId( Integer bankId ) {
        this.bankId = bankId;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public boolean getPrivatelyOwned() {
        return privatelyOwned;
    }

    public void setPrivatelyOwned( boolean privatelyOwned ) {
        this.privatelyOwned = privatelyOwned;
    }

    public Date getEstablished() {
        return established;
    }

    public void setEstablished( Date established ) {
        this.established = established;
    }

    public int getEmplyeeCount() {
        return emplyeeCount;
    }

    public void setEmplyeeCount( int emplyeeCount ) {
        this.emplyeeCount = emplyeeCount;
    }

    public Set<BloodDonation> getBloodDonationSet() {
        return bloodDonationSet;
    }

    public void setBloodDonationSet( Set<BloodDonation> bloodDonationSet ) {
        this.bloodDonationSet = bloodDonationSet;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner( Person owner ) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += ( bankId != null ? bankId.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object ) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !( object instanceof BloodBank ) ){
            return false;
        }
        BloodBank other = (BloodBank)object;
        if( ( this.bankId == null && other.bankId != null ) || ( this.bankId != null && !this.bankId.equals( other.bankId ) ) ){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BloodBank[ bankId=" + bankId + " ]";
    }

}
