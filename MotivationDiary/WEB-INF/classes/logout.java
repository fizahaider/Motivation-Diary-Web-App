import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class logout extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Logged Out</title>");
        out.println("<link rel='stylesheet' type='text/css' href='c.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container logout-container'>");
        out.println("<h1>You have been logged out</h1>");
        out.println("<div class='success-message'>");
        out.println("<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'><path d='M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z'/></svg>");
        out.println("<p>Thank you for using Motivation Diary</p>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<a href='login.html' class='button'>Login Again</a>");
        out.println("<a href='index.html' class='button'>Return Home</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}