package view;

import entity.Account;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.AccountLogic;
import logic.Logic;
import logic.LogicFactory;

/**
 *
 * @author Shariar (Shawn) Emami
 */
@WebServlet( name = "AccountTable", urlPatterns = { "/AccountTable" } )
public class AccountTableView extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        response.setContentType( "text/html;charset=UTF-8" );
        try( PrintWriter out = response.getWriter() ) {
            out.println( "<!DOCTYPE html>" );
            out.println( "<html>" );
            out.println( "<head>" );
            out.println( "<title>AccountViewNormal</title>" );
            out.println( "</head>" );
            out.println( "<body>" );

            out.println( "<table style=\"margin-left: auto; margin-right: auto;\" border=\"1\">" );
            out.println( "<caption>Account</caption>" );
            //this is an example, for your other tables use getColumnNames from
            //logic to create headers in a loop.

//            AccountLogic logic = LogicFactory.getFor( "Account" );
            Logic<Account> logic = LogicFactory.getFor( "Account" );
            out.println( "<tr>" );
            logic.getColumnNames().forEach( c -> out.printf( "<th>%s</th>", c ) );
//            out.println( "<th>ID</th>" );
//            out.println( "<th>Displayname</th>" );
//            out.println( "<th>Username</th>" );
//            out.println( "<th>Password</th>" );
            out.println( "</tr>" );

//            List<Account> entities = logic.getAll();
//            for( Account e: entities ) {
//                //for other tables replace the code bellow with
//                //extractDataAsList in a loop to fill the data.
//                out.printf( "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
//                        logic.extractDataAsList( e ).toArray() );
//            }
            logic.getAll().forEach( e -> out.printf( "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                    logic.extractDataAsList( e ).toArray() ) );

            out.println( "<tr>" );
            //this is an example, for your other tables use getColumnNames from
            //logic to create headers in a loop.
            logic.getColumnNames().forEach( c -> out.printf( "<th>%s</th>", c ) );
//            out.println( "<th>ID</th>" );
//            out.println( "<th>Displayname</th>" );
//            out.println( "<th>Username</th>" );
//            out.println( "<th>Password</th>" );
            out.println( "</tr>" );
            out.println( "</table>" );
            out.printf( "<div style=\"text-align: center;\"><pre>%s</pre></div>", toStringMap( request.getParameterMap() ) );
            out.println( "</body>" );
            out.println( "</html>" );
        }
    }

    private String toStringMap( Map<String, String[]> m ) {
        StringBuilder builder = new StringBuilder();
        for( String k: m.keySet() ) {
            builder.append( "Key=" ).append( k )
                    .append( ", " )
                    .append( "Value/s=" ).append( Arrays.toString( m.get( k ) ) )
                    .append( System.lineSeparator() );
        }
        return builder.toString();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        log( "GET" );
        processRequest( request, response );
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        log( "POST" );
        AccountLogic logic = LogicFactory.getFor( "Account" );
        Account account = logic.updateEntity( request.getParameterMap() );
        logic.update( account );
        processRequest( request, response );
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Sample of Account View Normal";
    }

    private static final boolean DEBUG = true;

    public void log( String msg ) {
        if( DEBUG ){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
            getServletContext().log( message );
        }
    }

    public void log( String msg, Throwable t ) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
        getServletContext().log( message, t );
    }
}
