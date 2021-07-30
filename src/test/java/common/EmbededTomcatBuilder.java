package common;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public class EmbededTomcatBuilder {

    private boolean enableNaming;
    private boolean init;
    private int port;
    private String contextPath;
    private String folderPath;
    private String emPath;

    public EmbededTomcatBuilder enableNaming() {
        enableNaming = true;
        return this;
    }

    public EmbededTomcatBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public EmbededTomcatBuilder addWebApp(String contextPath, String folderPath) {
        this.contextPath = contextPath;
        this.folderPath = folderPath;
        return this;
    }

    public EmbededTomcatBuilder setEntityManagerFactoryListener(String emPath) {
        this.emPath = emPath;
        return this;
    }

    public EmbededTomcatBuilder init() {
        init = true;
        return this;
    }

    public Tomcat build() throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        if(enableNaming)
            tomcat.enableNaming();
        tomcat.setPort(port);
        Context context = tomcat.addWebapp(contextPath, new File(folderPath).getAbsolutePath());
        context.addApplicationListener(emPath);
        if(init)
            tomcat.init();
        return tomcat;
    }

    public static Tomcat defaultNetbeansBuild( String context, String contextListenerPath) throws LifecycleException {
        EmbededTomcatBuilder tomcat = new EmbededTomcatBuilder();
        return tomcat.enableNaming()
                .setPort(8080)
                .addWebApp( context, "src\\main\\webapp")
                .setEntityManagerFactoryListener(contextListenerPath)
                .init()
                .build();
    }
}
