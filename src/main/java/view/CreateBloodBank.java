
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
import logic.LogicFactory;
import logic.BloodBankLogic;
import logic.BloodDonationLogic;
import entity.BloodDonation;
import entity.BloodBank;


/**
 *
 * @author William
 */

/*public static String OWNER_ID = "owner_id";
public static String PRIVATELY_OWNED = "privately_owned";
public static String ESTABLISHED = "established";
public static String NAME : String = "name";
public static String EMPLOYEE_COUNT = "employee_count";
public static String ID = "id";*/
@WebServlet( name = "CreateBloodBank", urlPatterns = { "/CreateBloodBankTable" } )
public class CreateBloodBank extends HttpServlet {
            private String errorMessage = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create Blood Donation</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");
            out.println("Owner ID:<br>");

            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", BloodBankLogic.OWNER_ID);
            out.println("<br>");

     
            out.println("Privately Owned:<br>");
            out.printf("<input type=\"radio\" name=\"%s\" value=\"+\">Is privately Owned<br>", BloodBankLogic.PRIVATELY_OWNED);
            out.printf("<input type=\"radio\" name=\"%s\" value=\"-\" checked>Is not privately Owned<br>", BloodBankLogic.PRIVATELY_OWNED);
            out.println("<br>");

            out.println("Created:<br>");
            out.printf("<input type=\"datetime-local\" pattern=\"yyyy-MM-dd'T'kk:mm:ss\" step=\"1\" name=\"%s\" value=\"\"><br>", BloodBankLogic.ESTABLISHED);
            out.println("<br>");

            out.println("Blood Bank Name:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodBankLogic.NAME);
            out.println("<br>");
            
            out.println("Employee Count:<br>");

            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", BloodBankLogic.EMPLOYEE_COUNT);
            out.println("<br>");

                        if (errorMessage != null && !errorMessage.isEmpty()) {
                out.println("<p color=red>");
                out.println("<font color=red size=4px>");
                out.println(errorMessage);
                out.println("</font>");
                out.println("</p>");
            }
            out.println("<pre>");
            out.println("Submitted keys and values:");
            out.println(toStringMap(request.getParameterMap()));
            out.println("</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            }


    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach((k, v) -> builder.append("Key=").append(k)
                .append(", ")
                .append("Value/s=").append(Arrays.toString(v))
                .append(System.lineSeparator()));
        return builder.toString();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * get method is called first when requesting a URL. since this servlet will
     * create a host this method simple delivers the html code. creation will be
     * done in doPost method.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * this method will handle the creation of entity. as it is called by user
     * submitting data through browser.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
            //tbd
        try {

        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        if (request.getParameter("add") != null) {
            //if add button is pressed return the same page
            processRequest(request, response);
        } else if (request.getParameter("view") != null) {
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect("BloodBankTable");
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a Blood Bank Entity";
    }

    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
}
