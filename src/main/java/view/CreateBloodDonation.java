package view;

import entity.Account;
import entity.BloodBank;
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
import logic.BloodDonationLogic;
import entity.BloodDonation;
import logic.BloodBankLogic;

/**
 *
 * @author Shariar (Shawn) Emami
 */
@WebServlet(name = "CreateBloodDonation", urlPatterns = {"/CreateBloodDonation"})
public class CreateBloodDonation extends HttpServlet {

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
            out.println("Bank ID:<br>");
            //instead of typing the name of column manualy use the static vraiable in logic
            //use the same name as column id of the table. will use this name to get date
            //from parameter map.
            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.BANK_ID);
            out.println("<br>");

            out.println("Blood Group:<br>");
            //out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.BLOOD_GROUP );

            out.printf("<select name=\"%s\" id=\"%s\">\n"
                    + "    <option value=\"A\">A</option>\n"
                    + "    <option value=\"B\">B</option>\n"
                    + "    <option value=\"AB\">AB</option>\n"
                    + "    <option value=\"O\">O</option>\n"
                    + "  </select>", BloodDonationLogic.BLOOD_GROUP, BloodDonationLogic.BLOOD_GROUP);

//            out.printf( "select name=\"%s\"", BloodDonationLogic.BLOOD_GROUP);
//            out.printf( "option name=\"%s\" value=\"%s\">A</option>", BloodDonationLogic.BLOOD_GROUP, "A");
//            out.printf( "option name=\"%s\" value=\"%s\">B</option>", BloodDonationLogic.BLOOD_GROUP, "B");
//            out.printf( "option name=\"%s\" value=\"%s\">AB</option>", BloodDonationLogic.BLOOD_GROUP, "AB");
//            out.printf( "option name=\"%s\" value=\"%s\">O</option>", BloodDonationLogic.BLOOD_GROUP, "O");
//            out.println( "</select>");
//            out.printf( "<input type=\"radio\" name=\"%s\" value=\"A\">A<br>", BloodDonationLogic.RHESUS_FACTOR );
//            out.printf( "<input type=\"radio\" name=\"%s\" value=\"B\">B<br>", BloodDonationLogic.RHESUS_FACTOR );
//            out.printf( "<input type=\"radio\" name=\"%s\" value=\"AB\">AB<br>", BloodDonationLogic.RHESUS_FACTOR );
//            out.printf( "<input type=\"radio\" name=\"%s\" value=\"O\">O<br>", BloodDonationLogic.RHESUS_FACTOR );
            out.println("<br>");

            out.println("Created:<br>");
            out.printf("<input type=\"datetime-local\" pattern=\"yyyy-MM-dd'T'kk:mm:ss\" step=\"1\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.CREATED);
            out.println("<br>");

//            out.println( "ID:<br>" );
//            out.printf( "<input type=\"number\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.ID );
//            out.println( "<br>" );
            out.println("Milliliters:<br>");
            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.MILLILITERS);
            out.println("<br>");

            out.println("Rhesus Factor:<br>");
            out.printf("<input type=\"radio\" name=\"%s\" value=\"+\">Positive<br>", BloodDonationLogic.RHESUS_FACTOR);
            out.printf("<input type=\"radio\" name=\"%s\" value=\"-\" checked>Negative<br>", BloodDonationLogic.RHESUS_FACTOR);
            out.println("<br>");

            out.println("<input type=\"submit\" name=\"view\" value=\"Add and View\">");
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

        BloodDonationLogic bloodDonationLogic = LogicFactory.getFor("BloodDonation");
        BloodBankLogic bloodBankLogic = LogicFactory.getFor( "BloodBank" );
        try {
            BloodDonation bloodDonation = bloodDonationLogic.createEntity(request.getParameterMap());
             BloodBank bloodBank = bloodBankLogic.getWithId(Integer.parseInt(request.getParameterMap().get(BloodDonationLogic.BANK_ID)[0]));             
             bloodDonation.setBloodBank(bloodBank);

            bloodDonationLogic.add(bloodDonation);
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        if (request.getParameter("add") != null) {
            //if add button is pressed return the same page
            processRequest(request, response);
        } else if (request.getParameter("view") != null) {
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect("BloodDonationTable");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a Blood Donation Entity";
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
