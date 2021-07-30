package dal;

import entity.Account;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public class AccountDAL extends GenericDAL<Account> {

    public AccountDAL() {
        super( Account.class );
    }

    @Override
    public List<Account> findAll() {
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults( "Account.findAll", null );
    }

    @Override
    public Account findById( int id ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "id", id );
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id" and value for it is put in map
        return findResult( "Account.findById", map );
    }

    public Account findByDisplayname( String nickname ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "nickname", nickname );
        return findResult( "Account.findByNickname", map );
    }

    public Account findByName( String name ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "name", name );
        return findResult( "Account.findByName", map );
    }

    public Account findByUsername( String username ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "username", username );
        return findResult( "Account.findByUsername", map );
    }

    public List<Account> findByPassword( String password ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "password", password );
        return findResults( "Account.findByPassword", map );
    }

    public List<Account> findContaining( String search ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "search", search );
        return findResults( "Account.findContaining", map );
    }

    public Account validateUser( String username, String pass ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "username", username );
        map.put( "password", pass );
        return findResult( "Account.validateUser", map );
    }
}
