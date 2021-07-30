package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Shariar
 */
@Entity
@Table( name = "account", catalog = "simplebloodbank", schema = "" )
@NamedQueries( {
    @NamedQuery( name = "Account.findAll", query = "SELECT a FROM Account a" ),
    @NamedQuery( name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id" ),
    @NamedQuery( name = "Account.findByUsername", query = "SELECT a FROM Account a WHERE a.username = :username" ),
    @NamedQuery( name = "Account.findByPassword", query = "SELECT a FROM Account a WHERE a.password = :password" ),
    @NamedQuery( name = "Account.findByNickname", query = "SELECT a FROM Account a WHERE a.nickname = :nickname" ),
    @NamedQuery( name = "Account.findByName", query = "SELECT a FROM Account a WHERE a.name = :name" ),
    @NamedQuery( name = "Account.validateUser", query = "SELECT a FROM Account a WHERE a.password = :password and a.username = :username" ),
    @NamedQuery( name = "Account.findContaining", query = "SELECT a FROM Account a WHERE a.name like CONCAT('%', :search, '%') or a.nickname like CONCAT('%', :search, '%') or a.username like CONCAT('%', :search, '%')" )
} )
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id" )
    private Integer id;
    @Basic( optional = false )
    @NotNull
    @Size( min = 1, max = 45 )
    @Column( name = "name" )
    private String name;
    @Basic( optional = false )
    @NotNull
    @Size( min = 1, max = 45 )
    @Column( name = "username" )
    private String username;
    @Basic( optional = false )
    @NotNull
    @Size( min = 1, max = 45 )
    @Column( name = "password" )
    private String password;
    @Size( max = 45 )
    @Column( name = "nickname" )
    private String nickname;

    public Account() {
    }

    public Account( Integer id ) {
        this.id = id;
    }

    public Account( Integer id, String username, String password ) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setDisplayname( String displayname ) {
        this.nickname = displayname;
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
        if( !( object instanceof Account ) ){
            return false;
        }
        Account other = (Account)object;
        if( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) ){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Account[ id=" + id + " ]";
    }

}
