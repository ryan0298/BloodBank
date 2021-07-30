package common;

import org.apache.catalina.LifecycleState;
import org.apache.catalina.startup.Tomcat;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public class TomcatStartUp {

    private static Tomcat tomcat;

    public static void createTomcat( String context, String contextListenerPath, String puName ) throws Exception {
        EMFactory.setPUName( puName );
        if( tomcat == null ){
            tomcat = EmbededTomcatBuilder.defaultNetbeansBuild( context, contextListenerPath );
        }
        if( tomcat.getServer().getState() != LifecycleState.STARTED ){
            tomcat.start();
        }
    }

    public static void stopAndDestroyTomcat() throws Exception {
//        tomcat.stop();
//        tomcat.destroy();
//        while (tomcat.getServer().getState() != LifecycleState.DESTROYED) {
//            TimeUnit.NANOSECONDS.sleep(500);
//        }
//        tomcat = null;
    }
}
