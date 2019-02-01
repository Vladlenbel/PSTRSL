package passage_system;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimeListener extends Thread {
	
	
	@Override
    public void run() {
		//TimerTask tasknew = new TimerScheduleFixedRateDelay();
		//Date date = new Date();

		Calendar nowDate = Calendar.getInstance();
		int year  = nowDate.get(Calendar.YEAR) - 1900;
		int month  = nowDate.get(Calendar.MONTH) ;
		int day  = nowDate.get(Calendar.DATE);
		//Calendar newDate =  Calendar.getInstance();
		
		//newDate.set(2019,0,26);
		
		
		//nowDate.get(Calendar.DAY_OF_WEEK);
		
		/*SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String strdate2 = "02-04-2013 11:35:42";
		String strdate = Integer.toString(day)+"-"+Integer.toString(month)+"-"+Integer.toString(year)+" 22:55:00";
		Date date = new Date(strdate);*/
		Date date = new Date(year,month,day,16,36);
		//Date date = new GregorianCalendar();
	      // ¬ывод текущей даты и времени с использованием toString()
	     // System.out.println("Date:"+ year + " "+ month + " " + " "+ day);
	      //System.out.println("Day of week: "+newDate.get(Calendar.DAY_OF_WEEK));
		Timer timer = new Timer();
		//Date date = new Date(year, month, day, 22, 48);
		timer.schedule(new GoOutTime(), date , 86400000);
	}

}


class GoOutTime extends TimerTask {
	 Database database = new Database();
	  SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd"); //HH:mm:ss"); 
    //  String curDate = dateFormat.format(new Date());
      
    public void run() {
    	Calendar nowDate = Calendar.getInstance();
    	String curDate = dateFormat.format(new Date());
    		if (nowDate.get(Calendar.DAY_OF_WEEK) != 1 || nowDate.get(Calendar.DAY_OF_WEEK) != 7 ) {
    			System.out.println("Today date:" + curDate);
    	    	database.findNotCom(curDate);
    	    	database.autoExit(curDate);
    		}
    	
    }
    
   
 }