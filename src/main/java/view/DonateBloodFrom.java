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
import logic.BloodDonationLogic;
import logic.DonationRecordLogic;
import logic.LogicFactory;
import logic.PersonLogic;

@WebServlet(name = "DonateBloodFrom", urlPatterns = {"/DonateBloodFrom"})
public class DonateBloodFrom extends HttpServlet {

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
            out.println("<title>Create Account</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            //Person
            out.println("<br>");
            out.println("<legend>Person</legend>");
            out.println("<form method=\"post\">");
            out.println("<div style=\"border:2px solid black;padding:1rem;\">");

            
            out.println( "Birth Date:<br>" );
            out.printf( "<input type=\"datetime-local\" pattern=\"yyyy-MM-dd'T'kk:mm:ss\" step=\"1\" name=\"%s\" value=\"\"><br>", PersonLogic.BIRTH );
            out.println( "<br>" );
            
            out.println( "ID:<br>" );
            out.printf( "<input type=\"number\" name=\"%s\" value=\"\"><br>", PersonLogic.ID );
            out.println( "<br>" );
            
            out.println( "First Name:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.FIRST_NAME );
            out.println( "<br>" );
            
            out.println( "Last Name:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.LAST_NAME );
            out.println( "<br>" );
            
            out.println( "Phone:<br>" );
            out.printf( "<input type=\"number\" name=\"%s\" value=\"\"><br>", PersonLogic.PHONE );
            out.println( "<br>" );
            
            out.println( "Address:<br>" );
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", PersonLogic.ADDRESS );
            out.println( "<br>" );
            out.println("</div>");
            //Blood
            out.println("<br>");
            out.println("<br>");
            out.println("<legend>Blood</legend>");
            out.println("<div style=\"border:2px solid black;padding:1rem;\">");
            out.println("Blood Bank ID:<br>");
            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.BANK_ID);
            out.println("<br>");
            out.println("Blood Group:<br>");
            out.printf("<select name=\"%s\" id=\"%s\">\n"
                    + "    <option value=\"A\">A</option>\n"
                    + "    <option value=\"B\">B</option>\n"
                    + "    <option value=\"AB\">AB</option>\n"
                    + "    <option value=\"O\">O</option>\n"
                    + "  </select>", BloodDonationLogic.BLOOD_GROUP, BloodDonationLogic.BLOOD_GROUP);
            out.println("<br>");
            out.println("ID:<br>");
            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.ID);
            out.println("<br>");
            out.println("Milliliters:<br>");
            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.MILLILITERS);
            out.println("<br>");
            out.println("Rhesus Factor:<br>");
            out.printf("<input type=\"radio\" name=\"%s\" value=\"+\">Positive<br>", BloodDonationLogic.RHESUS_FACTOR);
            out.printf("<input type=\"radio\" name=\"%s\" value=\"-\" checked>Negative<br>", BloodDonationLogic.RHESUS_FACTOR);
            out.println("<br>");
            out.println("</div>");
            //Administration
            out.println("<br>");
            out.println("<br>");
            out.println("<legend>Administration</legend>");
            out.println("<div style=\"border:2px solid black;padding:1rem;\">");
            out.println("Tested:<br>");
            out.printf("<input type=\"radio\" name=\"%s\" value=\"true\"> Yes<br>", DonationRecordLogic.TESTED);
            out.printf("<input type=\"radio\" name=\"%s\" value=\"false\" checked> No<br>", DonationRecordLogic.TESTED);
            out.println("<br>");
            out.println("Administrator:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.ADMINISTRATOR);
            out.println("<br>");
            out.println("Hospital:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.HOSPITAL);
            out.println("<br>");
            out.println("Created:<br>");
            out.printf("<input type=\"datetime-local\" pattern=\"yyyy-MM-dd'T'kk:mm:ss\" step=\"1\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.CREATED);
            out.println("<br>");
            out.println("</div>");
            out.println("<br>");
            out.println("<br>");
            out.println("<input type=\"submit\" name=\"add\" value=\"Add\">");
            out.println("</form>");

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
        AccountLogic aLogic = LogicFactory.getFor("Account");
        String username = request.getParameter(AccountLogic.USERNAME);
        if (aLogic.getAccountWithUsername(username) == null) {
            try {
                Account account = aLogic.createEntity(request.getParameterMap());
                aLogic.add(account);
            } catch (Exception ex) {
                errorMessage = ex.getMessage();
            }
        } else {
            //if duplicate print the error message
            errorMessage = "Username: \"" + username + "\" already exists";
        }
        if (request.getParameter("add") != null) {
            //if add button is pressed return the same page
            processRequest(request, response);
        } else if (request.getParameter("view") != null) {
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect("AccountTable");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a Donate Blood From";
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
