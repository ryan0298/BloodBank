package common;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * do not modify this class
 *
 * lazy initialize EntityManagerFactory.
 *
 * @author Shariar (Shawn) Emami
 */
public final class EMFactory {

    private static final Logger LOG = LogManager.getLogger();

    private static EntityManagerFactory emFactory;
    private static String puName = "simplebloodbank-PU";

    private EMFactory() {
    }

    public static void setPUName( String pu ) {
        puName = pu;
    }

    public static void initializeEMF() {
        LOG.debug( "initializeing EntityManagerFactory" );
        if( emFactory == null ){
            LOG.debug( "creating EntityManagerFactory" );
            emFactory = Persistence.createEntityManagerFactory( puName );
        }
    }

    public static void closeEMF() {
        LOG.debug( "closing EntityManagerFactory" );
        if( emFactory != null ){
            emFactory.close();
            LOG.debug( "closed EntityManagerFactory" );
        }
    }

    public static EntityManagerFactory getEMF() {
        LOG.debug( "getting EntityManagerFactory" );
        initializeEMF();
        return emFactory;
    }
}
