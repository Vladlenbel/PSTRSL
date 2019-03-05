package passage_system;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
		//Date date = new Date(year,month,day,23,45);
		//Date date = new Date(119,1,5,9,36);
		//Date date = new GregorianCalendar();
	      // Вывод текущей даты и времени с использованием toString()
	     // System.out.println("Date:"+ year + " "+ month + " " + " "+ day);
	      //System.out.println("Day of week: "+newDate.get(Calendar.DAY_OF_WEEK));
		Timer timer = new Timer();
		//Date date = new Date(year, month, day, 22, 48);
		timer.scheduleAtFixedRate(new GoOutTime(), 1*60*1000 , 20*60*1000); //смотреть на задержку
	}

}


class GoOutTime extends TimerTask {
	 Database database = new Database();
	  SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd"); //HH:mm:ss"); 
	  SimpleDateFormat HourMInFormat = new SimpleDateFormat("HH:mm:ss");
    //  String curDate = dateFormat.format(new Date());
	@Override   
    public void run() {
    	Calendar nowDate = Calendar.getInstance();
    	String curDate = dateFormat.format(new Date());
    	String curHourMin = HourMInFormat.format(new Date());
    		if (nowDate.get(Calendar.DAY_OF_WEEK) != 1 && nowDate.get(Calendar.DAY_OF_WEEK) != 7 ) {
    			int hour = Integer.parseInt(curHourMin.substring(0, 2));
    			int min = Integer.parseInt(curHourMin.substring(3, 5));
    			System.out.println("Time schedule " +  curHourMin );
    			if(hour >= 21 && min >= 00) {//23:39
	    			//System.out.println("begin");
	    			System.out.println("Today date: " + curDate +" Number date: " + nowDate.get(Calendar.DAY_OF_WEEK) );
	    			System.out.println("Today date with hour: " + new Date() );
	    			//System.out.println("End");
	    	    	database.autoExit(curDate);
	    			database.findNotCom(curDate);
    			}
    		}
    	
    }
    
   
 }