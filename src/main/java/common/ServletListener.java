package common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * do not modify this class
 *
 * lazy initialize EntityManagerFactory when the first servlet is created. do not create again.
 *
 * @see
 * <a href="https://www.deadcoderising.com/execute-code-on-webapp-startup-and-shutdown-using-servletcontextlistener/">
 * Startup and shutdown using ServletContextListener</a>
 * @see <a href="https://javaee.github.io/javaee-spec/javadocs/javax/servlet/ServletContextListener.html">Interface
 * ServletContextListener API</a>
 *
 * @author Shariar (Shawn) Emami
 */
@WebListener
public class ServletListener implements ServletContextListener {

    private static final Logger LOG = LogManager.getLogger();

    /**
     * this method is triggered when the web application is starting the initialization. This will be invoked before any
     * of the filters and servlets are initialized.
     *
     * @param sce
     */
    @Override
    public void contextInitialized( ServletContextEvent sce ) {
        LOG.debug( "Initializing EMF" );
        EMFactory.initializeEMF();
        LOG.debug( "EMF initialized" );
    }

    /**
     * this method is triggered when the ServletContext is about to be destroyed. This will be invoked after all the
     * servlets and filters have been destroyed.
     *
     * @param sce
     */
    @Override
    public void contextDestroyed( ServletContextEvent sce ) {
        LOG.debug( "Destroying EMF" );
        EMFactory.closeEMF();
        LOG.debug( "EMF Destroyed" );
    }
}
