package dal;

import java.util.List;

/**
 *
 * @author Shariar (Shawn) Emami
 * @param <E>
 */
public interface DataAccessLayer <E>{
    
    List<E> findAll();
    
    E findById( int id);
    
    void beginTransaction();

    /**
     * commit changes to theDB
     */
    void commit();

    /**
     * roll back the changed before the commit
     */
    void rollback();

    /**
     * close the transaction and entity manager
     */
    void closeTransaction();

    /**
     * commit changes and then close the transaction.
     * same as calling commit and closeTransaction
     */
    void commitAndCloseTransaction();

    /**
     * save this entity as persistence object which means the entity manager will track it.
     * @param entity - entity object to be saved before commit
     */
    void save(E entity);

    /**
     * delete this entity
     * @param entity - entity object to be removed
     */
    void delete(E entity);

    /**
     * update this entity
     * @param entity - entity to be updated
     * @return persistence instate of given entity
     */
    E update(E entity);
    
    void detach( E entity);

    /**
     * get the entity with given key
     * @param entityID = primary key
     * @return entity object or null of not found
     */
    E find(Object entityID);

    /**
     * get all rows of this table using CriteriaQuery
     * @return list of all rows
     */
    List<E> findAllCQ();
}
