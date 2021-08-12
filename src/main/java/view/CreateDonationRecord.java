package view;


import entity.DonationRecord;
import entity.Person;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodDonationLogic;
import logic.DonationRecordLogic;
import logic.LogicFactory;
import logic.PersonLogic;

/**
 *
 * @author Milad Mobini
 * @author Shariar (Shawn) Emami
 */
@WebServlet(name = "CreateDonationRecord", urlPatterns = {"/CreateDonationRecord"})
public class CreateDonationRecord extends HttpServlet {

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

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create Donation Record</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");
            out.println("Person ID:<br>");
            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.PERSON_ID);
            out.println("<br>");
            out.println("Blood Donation ID:<br>");
            out.printf("<input type=\"number\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.DONATION_ID);
            out.println("<br>");
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
            out.printf( "<input type=\"datetime-local\" pattern=\"yyyy-MM-dd'T'kk:mm:ss\" step=\"1\" name=\"%s\" value=\"\"><br>", DonationRecordLogic.CREATED );
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
        DonationRecordLogic drLogic = LogicFactory.getFor("DonationRecord");
            try {
                DonationRecord donationRecord = drLogic.createEntity(request.getParameterMap());
                
                BloodDonationLogic bloodDonationLogic = LogicFactory.getFor( "BloodDonation" );
                donationRecord.setBloodDonation(bloodDonationLogic.getWithId(Integer.parseInt(request.getParameterMap().get(DonationRecordLogic.DONATION_ID)[0])));
                
                PersonLogic personLogic = LogicFactory.getFor( "Person" );
                donationRecord.setPerson(personLogic.getWithId(Integer.parseInt(request.getParameterMap().get(DonationRecordLogic.PERSON_ID)[0])));
                
                drLogic.add(donationRecord);
            } catch (Exception ex) {
                errorMessage = ex.getMessage();
            }
        if (request.getParameter("add") != null) {
            //if add button is pressed return the same page
            processRequest(request, response);
        } else if (request.getParameter("view") != null) {
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect("DonationRecordTable");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a Donation Record Entity";
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
