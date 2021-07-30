package logic;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Shariar (Shawn) Emami
 * @param <E>
 */
public interface Logic<E> {

    void add( E entity );

    void delete( E entity );

    void detach( E entity );

    E update( E entity );

    /**
     * this method is used to send a list of all names to be used form table column headers. by having all names in one
     * location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnCodes and extractDataAsList
     *
     * @return list of all column display names.
     */
    List<String> getColumnNames();

    /**
     * this method returns a list of column names that match the official column names in the db. by having all names in
     * one location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnNames and extractDataAsList
     *
     * @return list of all column names in DB.
     */
    List<String> getColumnCodes();

    /**
     * return the list of values of all columns (variables) in given entity.
     *
     * this list must be in the same order as getColumnNames and getColumnCodes
     *
     * @param e - given Entity to extract data from.
     *
     * @return list of extracted values
     */
    List<?> extractDataAsList( E e );

    E createEntity( Map<String, String[]> parameterMap );

    /**
     * this method is only needed for bonus. this method needs to be overridden if the entity has dependencies. within
     * the method other logic's can be created to manipulate the dependencies. by default this method does the exact
     * same thing as createEntity method.
     *
     * @param parameterMap - new data with which to update an entity
     *
     * @return an updated entity with given requestData
     */
    E updateEntity( Map<String, String[]> parameterMap );

    List<E> getAll();

    E getWithId( int id );

    /**
     * this method is only needed for bonus. the search process is dependent on the @NamedQuery used.
     *
     * @param search - string word to search the db.
     *
     * @return a list of entities which contain the string argument
     */
    List<E> search( String search );

//    E getValue( String name, Object param );
//
//    List<E> getValues( String name, Object param );
}
