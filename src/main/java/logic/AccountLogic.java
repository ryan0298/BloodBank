package logic;

import common.ValidationException;
import dal.AccountDAL;
import entity.Account;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public class AccountLogic extends GenericLogic<Account, AccountDAL> {

    /**
     * create static final variables with proper name of each column. this way you will never manually type it again,
     * instead always refer to these variables.
     *
     * by using the same name as column id and HTML element names we can make our code simpler. this is not recommended
     * for proper production project.
     */
    public static final String NICKNAME = "nickname";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String NAME = "name";
    public static final String ID = "id";

    AccountLogic() {
        super( new AccountDAL() );
    }

    @Override
    public List<Account> getAll() {
        return get( () -> dal().findAll() );
    }

    @Override
    public Account getWithId( int id ) {
        return get( () -> dal().findById( id ) );
    }

    public Account getAccountWithDisplayname( String displayname ) {
        return get( () -> dal().findByDisplayname( displayname ) );
    }

    public Account getAccountWithUsername( String username ) {
        return get( () -> dal().findByUsername( username ) );
    }

    public Account getAccountWithName( String name ) {
        return get( () -> dal().findByName( name ) );
    }

    public List<Account> getAccountsWithPassword( String password ) {
        return get( () -> dal().findByPassword( password ) );
    }

    public Account isCredentialValid( String username, String password ) {
        return get( () -> dal().validateUser( username, password ) );
    }

    @Override
    public List<Account> search( String search ) {
        return get( () -> dal().findContaining( search ) );
    }

    @Override
    public Account createEntity( Map<String, String[]> parameterMap ) {
        //do not create any logic classes in this method.

//        return new AccountBuilder().SetData( parameterMap ).build();
        Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );
        //same as if condition below
//        if (parameterMap == null) {
//            throw new NullPointerException("parameterMap cannot be null");
//        }

        //create a new Entity object
        Account entity = new Account();

        //ID is generated, so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        //the only time that we will have id is for update behaviour.
        if( parameterMap.containsKey( ID ) ){
            try {
                entity.setId( Integer.parseInt( parameterMap.get( ID )[ 0 ] ) );
            } catch( java.lang.NumberFormatException ex ) {
                throw new ValidationException( ex );
            }
        }

        //before using the values in the map, make sure to do error checking.
        //simple lambda to validate a string, this can also be place in another
        //method to be shared amoung all logic classes.
        ObjIntConsumer< String> validator = ( value, length ) -> {
            if( value == null || value.trim().isEmpty() || value.length() > length ){
                String error = "";
                if( value == null || value.trim().isEmpty() ){
                    error = "value cannot be null or empty: " + value;
                }
                if( value.length() > length ){
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException( error );
            }
        };

        //extract the date from map first.
        //everything in the parameterMap is string so it must first be
        //converted to appropriate type. have in mind that values are
        //stored in an array of String; almost always the value is at
        //index zero unless you have used duplicated key/name somewhere.
        String displayname = null;
        if( parameterMap.containsKey( NAME ) ){
            displayname = parameterMap.get( NICKNAME )[ 0 ];
            validator.accept( displayname, 45 );
        }
        String username = parameterMap.get( USERNAME )[ 0 ];
        String password = parameterMap.get( PASSWORD )[ 0 ];
        String name = parameterMap.get( NAME )[ 0 ];

        //validate the data
        validator.accept( username, 45 );
        validator.accept( password, 45 );
        validator.accept( name, 45 );

        //set values on entity
        entity.setDisplayname( displayname );
        entity.setUsername( username );
        entity.setPassword( password );
        entity.setName( name );

        return entity;
    }

    /**
     * this method is used to send a list of all names to be used form table column headers. by having all names in one
     * location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnCodes and extractDataAsList
     *
     * @return list of all column names to be displayed.
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList( "ID", "Name", "Nickname", "Username", "Password" );
    }

    /**
     * this method returns a list of column names that match the official column names in the db. by having all names in
     * one location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnNames and extractDataAsList
     *
     * @return list of all column names in DB.
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList( ID, NAME, NICKNAME, USERNAME, PASSWORD );
    }

    /**
     * return the list of values of all columns (variables) in given entity.
     *
     * this list must be in the same order as getColumnNames and getColumnCodes
     *
     * @param e - given Entity to extract data from.
     *
     * @return list of extracted values
     */
    @Override
    public List<?> extractDataAsList( Account e ) {
        return Arrays.asList( e.getId(), e.getName(), e.getNickname(), e.getUsername(), e.getPassword() );
    }
}
