package passage_system;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RequestHandler
 */
@WebServlet("/RequestHandler")
public class RequestHandler extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	private Database database = new Database();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("good");
		request.setCharacterEncoding("UTF-8");
	    String requestHand = (String) request.getParameter("request");
	    
	    /*Calendar c = Calendar.getInstance();
	    
	    c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	    
	    System.out.println("Date1 " + c.getTime().toGMTString());
	    System.out.println("Date2 " + c.getTime().toString());
	    System.out.println("Date3 " + c.getTime().toLocaleString());
	    System.out.println("Date4 " + c.getTime().toInstant());
	    
	    Calendar cal = Calendar.getInstance();
	    
	    cal.set(Calendar.DAY_OF_MONTH, Calendar.MONDAY);	
	    
	    System.out.println("Date1_cal " + cal.getTime().toGMTString());
	    System.out.println("Date2_cal " + cal.getTime().toString());
	    System.out.println("Date3_cal " + cal.getTime().toLocaleString());
	    System.out.println("Date4_cal " + cal.getTime().toInstant());
	    */
	    SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd"); //HH:mm:ss");
	    
        String curDate = dateFormat.format(new Date());
        System.out.println(curDate);
	    
	    if(requestHand.equals("late")) {
	    	String answ =  database.dateFinder(curDate, "", "", "Опоздание", "");
		    response.setContentType("text/html; charset=UTF-8");
		    response.getWriter().println( answ );
	    }
	    if(requestHand.equals("todayInf")) {
	    	String answ =  database.dateFinder(curDate, "", "", "", "");
		    response.setContentType("text/html; charset=UTF-8");
		    response.getWriter().println( answ );
	    }
	    if(requestHand.equals("notApper")) {
	    	String answ =  database.notCom(curDate);
		    response.setContentType("text/html; charset=UTF-8");
		    response.getWriter().println( answ );
	    }
	    
		
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
