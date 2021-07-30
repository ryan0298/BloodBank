package dal;

import common.EMFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Shariar (Shawn) Emami
 * @param <T>
 */
abstract class GenericDAL<T> implements DataAccessLayer<T> {

    /**
     * entity class type of this DAO
     */
    private final Class<T> entityClass;

    /**
     * entity manager used in this DAO
     */
    private EntityManager em;

    /**
     * create a DAO and initialize the Entity class type
     *
     * @param entityClass
     */
    GenericDAL( Class<T> entityClass ) {
        this.entityClass = entityClass;
    }

    /**
     * start the transaction.
     */
    public void beginTransaction() {
        // get an entity manager from the factory
        em = EMFactory.getEMF().createEntityManager();
        //start the transaction
        em.getTransaction().begin();
    }

    /**
     * commit changes to theDB
     */
    public void commit() {
        em.getTransaction().commit();
    }

    /**
     * roll back the changed before the commit
     */
    public void rollback() {
        em.getTransaction().rollback();
    }

    /**
     * close the transaction and entity manager
     */
    public void closeTransaction() {
        em.close();
    }

    /**
     * commit changes and then close the transaction. same as calling commit and closeTransaction
     */
    public void commitAndCloseTransaction() {
        commit();
        closeTransaction();
    }

    /**
     * save this entity as persistence object which means the entity manager will track it.
     *
     * @param entity - entity object to be saved before commit
     */
    public void save( T entity ) {
        em.persist( entity );
    }

    /**
     * update this entity
     *
     * @param entity - entity to be updated
     * @return persistence instate of given entity
     */
    public T update( T entity ) {
        return em.<T>merge( entity );
    }

    /**
     * delete this entity
     *
     * @param entity - entity object to be removed
     */
    public void delete( T entity ) {
        //by merging this entity is tracked by entitymanger and if needed it can cascade remove
        T entityToBeRemoved = em.<T>merge( entity );
        //remove entity
        em.remove( entityToBeRemoved );
    }

    public void detach( T entity ) {
        em.detach( entity );
    }

    /**
     * get the entity with given key
     *
     * @param entityID = primary key
     * @return entity object or null of not found
     */
    public T find( Object entityID ) {
        return em.<T>find( entityClass, entityID );
    }

    /**
     * get all rows of this table using CriteriaQuery
     *
     * @return list of all rows
     */
    public List<T> findAllCQ() {
        //using the builder create a CriteriaQuery of type entityClass
        CriteriaQuery<T> cq = em.getCriteriaBuilder().<T>createQuery( entityClass );
        //select everything form entityClass
        cq.select( cq.<T>from( entityClass ) );
        //create the query based on CriteriaQuery, exeucte and return the results
        return em.<T>createQuery( cq ).getResultList();
    }

    /**
     * find one result using the named query and given parameters
     *
     * @param namedQuery - named query defined in entity class
     * @param parameters - parameters to be set in named query, a map of keys (name place holder in named query) and
     * value (value to replace the place holder in named query)
     * @return one result
     */
    protected T findResult( String namedQuery, Map<String, Object> parameters ) {
        T result = null;
        try {
            TypedQuery<T> query = checkCreateAndSetTypedQuery( namedQuery );
            query = setParameters( query, parameters );

            result = query.getSingleResult();

        } catch( NoResultException e ) {
            String message = "No result found for named query: " + namedQuery;
            Logger.getLogger( GenericDAL.class.getName() ).log( Level.WARNING, message, e );
        } catch( Exception e ) {
            Logger.getLogger( GenericDAL.class.getName() ).log( Level.SEVERE, null, e );
        }
        return result;
    }

    /**
     * find list of results using the named query and given parameters
     *
     * @param namedQuery - named query defined in entity class
     * @param parameters - parameters to be set in named query, a map of keys (name place holder in named query) and
     * value (value to replace the place holder in named query)
     * @return List result
     */
    protected List<T> findResults( String namedQuery, Map<String, Object> parameters ) {
        List<T> result = null;
        try {
            TypedQuery<T> query = checkCreateAndSetTypedQuery( namedQuery );
            query = setParameters( query, parameters );

            result = query.getResultList();
        } catch( NoResultException e ) {
            String message = "No result found for named query: " + namedQuery;
            Logger.getLogger( GenericDAL.class.getName() ).log( Level.WARNING, message, e );
        } catch( Exception e ) {
            Logger.getLogger( GenericDAL.class.getName() ).log( Level.SEVERE, null, e );
        }
        if( result == null ) {
            result = Collections.emptyList();
        }
        return result;
    }

    /**
     * set and create a named query
     *
     * @param nativeNamedQuery - query
     * @return created query
     */
    private TypedQuery<T> checkCreateAndSetTypedQuery( String namedQuery ) {
        Objects.requireNonNull( namedQuery, "named query cannot be null" );
        return em.<T>createNamedQuery( namedQuery, entityClass );
    }

    /**
     * if parameters is not null or empty add to query
     *
     * @param query - query to be modified
     * @param parameters - parameters to be set in named query, a map of keys (name place holder in named query) and
     * value (value to replace the place holder in named query)
     * @return modified query
     */
    private TypedQuery<T> setParameters( TypedQuery<T> query, Map<String, Object> parameters ) {
        if( parameters != null && !parameters.isEmpty() ){
            parameters.entrySet().forEach( ( entry ) -> {
                Logger.getLogger( getClass().getName() ).log( Level.INFO, "setParameters: {0}", new String[]{ entry.toString() } );
                query.setParameter( entry.getKey(), entry.getValue() );
            } );
        }
        return query;
    }
}
