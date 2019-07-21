import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.MainServlet;

public class JettyRunner {

    public static void main(String[] args) throws Exception{

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(new ServletHolder(new MainServlet()), "");

        contextHandler.addServlet(new ServletHolder(new MainServlet()), "/addaccount");
        contextHandler.addServlet(new ServletHolder(new MainServlet()), "/delaccount");
        contextHandler.addServlet(new ServletHolder(new MainServlet()), "/addbudgetrow");
        contextHandler.addServlet(new ServletHolder(new MainServlet()), "/delbudgetrow");
        contextHandler.addServlet(new ServletHolder(new MainServlet()), "/addrecord");
        contextHandler.addServlet(new ServletHolder(new MainServlet()), "/delrecord");



        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("public_html");

        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[]{resourceHandler, contextHandler});


        Server server = new Server(8080);

        server.setHandler(handlerList);

        server.start();
        server.join();

    }


}
