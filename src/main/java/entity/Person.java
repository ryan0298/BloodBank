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
@Table( name = "person", catalog = "simplebloodbank", schema = "", uniqueConstraints = {
    @UniqueConstraint( columnNames = { "id" } ) } )
@NamedQueries( {
    @NamedQuery( name = "Person.findAll", query = "SELECT p FROM Person p" ),
    @NamedQuery( name = "Person.findById", query = "SELECT p FROM Person p WHERE p.id = :id" ),
    @NamedQuery( name = "Person.findByFirstName", query = "SELECT p FROM Person p WHERE p.firstName = :firstName" ),
    @NamedQuery( name = "Person.findByLastName", query = "SELECT p FROM Person p WHERE p.lastName = :lastName" ),
    @NamedQuery( name = "Person.findByPhone", query = "SELECT p FROM Person p WHERE p.phone = :phone" ),
    @NamedQuery( name = "Person.findByAddress", query = "SELECT p FROM Person p WHERE p.address = :address" ),
    @NamedQuery( name = "Person.findByBirth", query = "SELECT p FROM Person p WHERE p.birth = :birth" ) } )
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id", nullable = false )
    private Integer id;
    @Basic( optional = false )
    @NotNull
    @Size( min = 1, max = 50 )
    @Column( name = "first_name", nullable = false, length = 50 )
    private String firstName;
    @Basic( optional = false )
    @NotNull
    @Size( min = 1, max = 50 )
    @Column( name = "last_name", nullable = false, length = 50 )
    private String lastName;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic( optional = false )
    @NotNull
    @Size( min = 1, max = 15 )
    @Column( name = "phone", nullable = false, length = 15 )
    private String phone;
    @Basic( optional = false )
    @NotNull
    @Size( min = 1, max = 100 )
    @Column( name = "address", nullable = false, length = 100 )
    private String address;
    @Basic( optional = false )
    @NotNull
    @Column( name = "birth", nullable = false )
    @Temporal( TemporalType.TIMESTAMP )
    private Date birth;
    @OneToOne( mappedBy = "owner", fetch = FetchType.LAZY )
    private BloodBank bloodBank;
    @OneToMany( mappedBy = "person", fetch = FetchType.LAZY )
    private Set<DonationRecord> donationRecordSet;

    public Person() {
    }

    public Person( Integer id ) {
        this.id = id;
    }

    public Person( Integer id, String firstName, String lastName, String phone, String address, Date birth ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.birth = birth;
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth( Date birth ) {
        this.birth = birth;
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
        hash += ( id != null ? id.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object ) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if( !( object instanceof Person ) ){
            return false;
        }
        Person other = (Person)object;
        if( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) ){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Person[ id=" + id + " ]";
    }

}
