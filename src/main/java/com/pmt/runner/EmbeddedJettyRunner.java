package com.pmt.runner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;


public class EmbeddedJettyRunner {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        System.out.println("[INFO] Creating Jetty Server on port " + port);
        Server server = new Server(port);

        System.out.println("[INFO] Creating WebAppContext");
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        webapp.setResourceBase("src/main/webapp");
        webapp.setParentLoaderPriority(true);
        // Disable default webapp descriptor to avoid loading jetty's webdefault.xml listeners
        webapp.setDefaultsDescriptor(null);

        // Configure Jetty to include JSP/Jasper related jars so the JSP engine can be found at runtime
        String jspContainerPattern = ".*/[^/]*(apache-jsp|tomcat-jasper|jasper|jsp|jetty-jsp).*\\.jar$";
        webapp.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", jspContainerPattern);

        // Register JSP servlet (use Tomcat Jasper servlet present in runtime dependencies)
        try {
            Class<?> jspClazz = Class.forName("org.apache.jasper.servlet.JspServlet");
            @SuppressWarnings("unchecked")
            Class<? extends javax.servlet.Servlet> jspServletClass = (Class<? extends javax.servlet.Servlet>) jspClazz;
            ServletHolder jspHolder = new ServletHolder("jsp", jspServletClass);
            jspHolder.setInitOrder(0);
            jspHolder.setInitParameter("logVerbosityLevel", "DEBUG");
            webapp.addServlet(jspHolder, "*.jsp");
        } catch (Throwable t) {
            System.out.println("[WARN] Could not register JSP servlet: " + t.getMessage());
        }

        // Use explicit configuration classes and omit AnnotationConfiguration to avoid scanning compiled classes
        webapp.setConfigurationClasses(new String[] {
            "org.eclipse.jetty.webapp.WebInfConfiguration",
            "org.eclipse.jetty.webapp.WebXmlConfiguration",
            "org.eclipse.jetty.webapp.MetaInfConfiguration",
            "org.eclipse.jetty.webapp.FragmentConfiguration",
            "org.eclipse.jetty.apache.jsp.JettyJasperInitializer",
            "org.eclipse.jetty.plus.webapp.EnvConfiguration",
            "org.eclipse.jetty.plus.webapp.PlusConfiguration",
            "org.eclipse.jetty.webapp.JettyWebXmlConfiguration"
        });

        server.setHandler(webapp);

        System.out.println("[INFO] Starting Jetty Server");
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to start Jetty: " + e.getMessage());
            System.err.println("[DEBUG] Stack trace:");
            for (StackTraceElement elem : e.getStackTrace()) {
                System.err.println("  at " + elem);
            }
            System.exit(1);
        }
        System.out.println("[SUCCESS] Embedded Jetty started at http://localhost:" + port);
        try {
            System.out.println("Servlet mappings and handlers:");
            if (webapp.getServletHandler() != null) {
                var mappings = webapp.getServletHandler().getServletMappings();
                if (mappings != null) {
                    for (var m : mappings) {
                        System.out.println("  mapping: " + m.getPathSpecs()[0] + " -> " + m.getServletName());
                    }
                }
            }
            System.out.println("Resource base: " + webapp.getResourceBase());
            java.io.File rb = new java.io.File(webapp.getResourceBase());
            if (rb.exists()) {
                String[] files = rb.list();
                System.out.println("Resource base contents: " + java.util.Arrays.toString(java.util.Objects.requireNonNull(files)));
            }
        } catch (Exception e) {
            System.out.println("Error while printing diagnostic info: " + e);
        }
        server.join();
    }
}
