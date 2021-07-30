package view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Shariar (Shawn) Emami
 */
@WebServlet( name = "SampleForm", urlPatterns = { "/SampleForm" } )
public class SampleForm extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            out.println( "<!DOCTYPE html>" );
            out.println( "<html>" );
            out.println( "<head>" );
            out.println( "<title>Servlet Sample3Servlet</title>" );
            out.println( "</head>" );
            out.println( "<body>" );
            out.println( "<div style=\"text-align: center;\">" );
            out.println( "<div style=\"display: inline-block; text-align: left;\">" );
            out.println( "<form action=\"SampleForm\" method=\"post\">" );
            out.println( "First name:<br>" );
            out.println( "<input type='text' name=\"firstname\" value=\"Mickey\"><br>" );
            out.println( "Last name:<br>" );
            out.println( "<input type=\"text\" name=\"secondname\" value=\"Mouse\"><br><br>" );
            out.println( "<input type=\"radio\" name=\"gender\" value=\"male\" checked> Male<br>" );
            out.println( "<input type=\"radio\" name=\"gender\" value=\"female\"> Female<br>" );
            out.println( "<input type=\"radio\" name=\"gender\" value=\"other\"> Other<br><br>" );
            out.println( "<input type=\"checkbox\" name=\"car\" value=\"car\"> Car<br>" );
            out.println( "<input type=\"checkbox\" name=\"fly\" value=\"alien\"> Fly<br><br>" );
            out.println( "<input type=\"date\" name=\"bday\" min=\"1900-01-01\" max=\"2007-12-30\"><br><br>" );
            out.println( "<select name=\"sub\">" );
            out.println( "<option value=\"volvo\">Volvo</option>" );
            out.println( "<option value=\"saab\">Saab</option>" );
            out.println( "<option value=\"opel\">Opel</option>" );
            out.println( "<option value=\"audi\">Audi</option>" );
            out.println( "</select><br><br>" );
            out.println( "<input type=\"submit\" name=\"submit\" value=\"Submit\">" );
            out.println( "<input type=\"submit\" name=\"view\" value=\"Submit and View\">" );
            out.println( "</form>" );
            out.println( "<pre>" );
            out.println( "Submitted keys and values:" );
            out.println( toStringMap( request.getParameterMap() ) );

            if( request.getParameterMap().get( "bday" ).length == 0 ){
                out.println( "EMPTY" );
            } else {
                out.println( request.getParameterMap().get( "bday" ).length );
            }
            if( request.getParameterMap().get( "firstname" ).length == 0 ){
                out.println( "EMPTY" );
            } else {
                out.println( request.getParameterMap().get( "firstname" ).length );
            }
            out.println( "</pre>" );
            out.println( "</div>" );
            out.println( "</div>" );
            out.println( "</body>" );
            out.println( "</html>" );
        }
    }

    private String toStringMap( Map<String, String[]> values ) {
        StringBuilder builder = new StringBuilder();
        values.forEach( ( k, v ) -> builder.append( "Key=" ).append( k )
                .append( ", " )
                .append( "Value/s=" ).append( Arrays.toString( v ) )
                .append( System.lineSeparator() ) );
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
        //you can add you logic here
        if( request.getParameter( "view" ) != null ){
            response.sendRedirect( "UsernameTableViewNormal" );
        } else if( request.getParameter( "submit" ) != null ){
            processRequest( request, response );
        }

        Map<String, String[]> map = request.getParameterMap();
        if( map.containsKey( "view" ) ){
            response.sendRedirect( "UsernameTableViewNormal" );
        } else if( map.containsKey( "submit" ) ){
            processRequest( request, response );
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Sample page of multiple Eelements";
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
