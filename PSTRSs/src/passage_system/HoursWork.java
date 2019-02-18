package passage_system;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HoursWork
 */
@WebServlet("/HoursWork")
public class HoursWork extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Database database = new Database();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HoursWork() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("UTF-8");
		String calStart = (String) request.getParameter("calStart");
		String calFin = (String) request.getParameter("calFin");
		String title = (String) request.getParameter("depart");
		String month = (String) request.getParameter("month");
		
		 response.setContentType("text/html; charset=UTF-8");
		  response.getWriter().println( database.getHoursWorked(calStart, calFin, title, month) );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
