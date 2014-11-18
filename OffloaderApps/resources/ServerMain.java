import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;


public class ServerMain extends AbstractHandler
{

	private int n=0;
	
	@Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
		if (request.getParameter("n") != null) {
			n = Integer.parseInt(request.getParameter("n"));
		}
        response.setContentType("text/plain;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        
        Fibonnacci fi = new Fibonnacci();
        response.getWriter().println(fi.calcular(n));
        
       
    }
 
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8083);
        server.setHandler(new ServerMain());
 
        server.start();
        server.join();
    }

    
}
