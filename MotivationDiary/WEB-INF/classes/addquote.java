import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class addquote extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);  
        if (session == null || "user".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String quote = request.getParameter("quote");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Add Quote Result</title>");
        out.println("<link rel='stylesheet' type='text/css' href='c.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        if (quote == null || quote.isEmpty()) {
            out.println("<h1>Quote Submission Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<p>Quote cannot be empty!</p>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/motivationdiary", "root", "root");
            String query = "INSERT INTO quotes (quote) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, quote);
            int r = pstmt.executeUpdate();

            if (r > 0) {
                out.println("<h1>Quote Added Successfully!</h1>");
                out.println("<div class='success-message'>");
                out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z'/></svg>");
                out.println("<p>Your motivational quote has been added.</p>");
                out.println("</div>");
            } else {
                out.println("<h1>Failed to Add Quote</h1>");
                out.println("<div class='error-message'>");
                out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z'/></svg>");
                out.println("<p>There was an error adding your quote.</p>");
                out.println("</div>");
            }

            out.println("<div class='form-group'>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            out.println("</div>");

            pstmt.close();
            con.close();
        } catch (Exception e) {
            out.println("<h1>Error</h1>");
            out.println("<div class='error-message'>");
            out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z'/></svg>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<a href='viewquotes' class='button'>Back to Quotes</a>");
            out.println("</div>");
        }

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}