package controller.authentication;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import javax.servlet.annotation.*;

@WebServlet("/Logout")
public class LogoutController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		Cookie[] cookieArray= request.getCookies(); 
		for(int i=0; i<cookieArray.length; i++) {
			if(cookieArray[i].getName().equals("JSESSIONID")) {
				cookieArray[i].setMaxAge(0);
				response.addCookie(cookieArray[i]);
			} 
		}
		response.sendRedirect("index.jsp");
	}
}
