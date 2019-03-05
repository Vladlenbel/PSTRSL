package passage_system;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;


public class Database {
	
	private final String user = /*"root"*/"root"/*"passagesys"*/;
    private final String url = "jdbc:mysql://localhost:3306/passage_system?useUnicode=true&characterEncoding=UTF-8";
    private final String password =/*"serverps"*/"valdistroer"/*"AstZhq4"*/;

    private Connection connection;
    private Statement statement;
    private SQLException ex = new SQLException();
    private ResultSet resultSet;
    
    private int firstStart = 0;
    
    public Database() {
    	 try {
         	// System.out.println("class foundStart");
         	 Class.forName("com.mysql.jdbc.Driver");
              //System.out.println("class foundFin");
          } catch (Exception e) {
         	 	//System.out.println("class problem1");
         	 	//System.out.println(e);
         	 	//System.out.println("class problem2");
         	 	e.toString();
         	 	System.out.println(e.toString());
         	 	System.out.println("error connection");
          }
     }
    
    private void sendQuery(String query) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
        		ex = e;
        } finally {
            try {
            	System.out.println(ex.toString()); 
                connection.close();
                statement.close();
            } catch (SQLException e) {

            }
        }
    }
    
    private void closeDB() {
        try {
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
        	System.out.println("Ошибка");
        }
    }  
    
    public void addRecordTime(String  id ) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String curDate = dateFormat.format(new Date());
        
        String isNormal ;
    	if (statusWorker(curDate) == 0) {
    		 isNormal = "101";
    	}
    	else {
    		isNormal = "100";
    	}
    	//System.out.println(curDate);
    	
    	
    		String query = "INSERT INTO worker VALUES(" + "\"" +  id + "\""  + "," +
    					"\"" + curDate.toString( ) + "\"" +  "," +
    					"\"" + isNormal + "\"" + ");" ;
    		
    	sendQuery(query);
    	
    }
    
    public String getSoundName(String id) {
    	String answ = null;
    	String query = "SELECT filename FROM sound INNER JOIN workerfio ON "
    			+ "  sound.personellNumber = workerfio.personellNumber WHERE workerIdCard = '" + id + "';";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        answ = resultSet.getString("filename");
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	return answ ;
    	
    }
    
    public String dateFinder (String dateStart, String dateFin, String surn, String statusCom, String depart) {
    //	System.out.println("FIO ="+statusCom.replaceAll("\n","")+"sgdf");
    	//if (surn.equals("null") == false) {
    	String[] surname = surn.split("\\s+");
    	int status = 0;
    	int normEntr = 0;
    	if (statusCom.equals("") == false) {
	    	if (statusCom.replaceAll("\n","").equals("Опоздание")) {
	    		status = 101;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("Вход")) {
	    		status = 100;
	    		normEntr = 102;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("Выход")) {
	    		status = 200;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("Автовыход")) {
	    		status = 201;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("Не явился")) {
	    		status = 300;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("Ранний выход")) {
	    		status = 202;
	    		}
    	}
    	String answ = new String();
    	
    	String query = "SELECT * FROM (worker INNER JOIN workerfio ON worker.workerId = workerfio.personellNumber)"
    			+ "INNER JOIN department ON workerfio.department = department.id"
    			+ " WHERE eventTime >=" + "\"" + dateStart+ "\"";	
    	if (dateFin.equals("") == false) {
    		dateFin = dateFin + " 23:59:59";
    		//System.out.println(dateFin);
    		query += "AND eventTime <=" + "\"" + dateFin.replaceAll("\n","") + "\"";
    	}
    	if (surn.equals("") == false) {
    		query += "AND surname =" + "\"" + surname[0].replaceAll("\n","") + "\"";
    	}
    	if (surn.equals("") == false) {
    		query += "AND name =" + "\"" + surname[1].replaceAll("\n","") + "\"";
    	}
    	if (surn.equals("") == false) {
    		query += "AND patronymic =" + "\"" + surname[2].replaceAll("\n","") + "\"";
    	}
    	if (statusCom.equals("") == false) {
    		query += "AND statuscom =" + "\"" + status + "\"";
    	}
    	if (status == 100) {
    		query += "AND statuscom =" + "\"" + normEntr + "\"";
    	}
    	if (depart.equals("") == false) {
    		query += "AND title =" + "\"" + depart.replaceAll("\n","") + "\"";
    	}
    	query += "AND status =" + "\"" + "Работает" + "\"";
    	query += "ORDER BY eventTime" ;
    	System.out.println(query);
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);;
	      //  answ += "<html><head><meta charset= \"UTF-8\" ></head>";
	        answ += "<table border= \" 1\">"+  
	        		   "<tr>"+  
	        		   "<th>ID</th>" + 
	        		  "<th>ФИО</th>"+ 
	        		  "<th>Время</th>"+ 
	        		  "<th>Статус</th>"+ 
	        		  "<th>Отдел</th>"+
	        		  "</tr>";
	        while (resultSet.next()) {
	        	String statusWord = null;
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 101) {
	        			statusWord = "Опоздание";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 100) {
	        			statusWord = "Вход";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 102) {
	        			statusWord = "Вход";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 200) {
	        			statusWord = "Выход";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 201) {
	        			statusWord = "Автовыход";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 300) {
	        			statusWord = "Не явился";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 202) {
	        			statusWord = "Ранний уход";
	        		}
	        		
	        	int count = 0;
	        	
	        	if  ( Integer.parseInt( resultSet.getString("statuscom")) == 300 && count == 0 && resultSet.getString("surname").equals("Unknown")) {
	        		count++;
	        	}
	        	if( Integer.parseInt( resultSet.getString("statuscom")) == 300 && count == 0   ) {
	        		
	        		String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultDay.substring(0,11) +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}
	        	
	        	if( resultSet.getString("surname").equals("Unknown") && count == 0 ) {
		        	String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultDay +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}
	        	if( Integer.parseInt( resultSet.getString("statuscom")) == 101 && count == 0) {
	        		
	        		String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultDay +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}

	        	/*if( Integer.parseInt( resultSet.getString("statuscom")) == 101 && count == 0) {
		        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("eventTime") +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}*/
	        	if( Integer.parseInt( resultSet.getString("statuscom")) == 201 && count == 0) {
	        		
	        		String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#ff8c00\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#ff8c00\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#ff8c00\">"+ resultDay +" </td>";
		        	   answ += "<td style=\"color:#ff8c00\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#ff8c00\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}
	        	
	        	if(Integer.parseInt( resultSet.getString("statuscom")) == 202 && count == 0) {
	        		
	        		String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#8b008b\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#8b008b\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#8b008b\">"+ resultDay +" </td>";
		        	   answ += "<td style=\"color:#8b008b\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#8b008b\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}

	        	if ((Integer.parseInt( resultSet.getString("statuscom")) == 100 || 
	        			Integer.parseInt( resultSet.getString("statuscom")) == 102 ||
	        				Integer.parseInt( resultSet.getString("statuscom")) == 200 ) && count == 0) {
	        		
	        		String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        	
	        	   answ += "<tr>";
	        	   answ += "<td>"+ resultSet.getString("personellNumber") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
	        	   answ += "<td>"+ resultDay +" </td>";
	        	   answ += "<td>"+ statusWord +" </td>";
	        	   answ += "<td>"+ resultSet.getString("title") +" </td>";
	        	   answ += "</tr>";
	        	}

	        
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	
    	 //System.out.println(answ);
    	return answ;
    }
    
    private int statusWorker (String date) {
    	int good = 0;
    	String query = "SELECT * FROM workingHours WHERE id = 1;";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        	good++;
	        	//System.out.println("aaa11");
	        }
	       // System.out.println(good);
	        if (good == 0) {
	        	return 0;
	        }
	  
	        	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        }   	
    
    	
    return  1;	
    }

    public String addMesForWorker (String surnMes, String soundMes, String typeCom) {
    	
    	if(surnMes.equals("")) {
    		return "<font size=\"3\" color=\"red\" face=\"Arial\">Введите фамилию сотрудника </font>";
    	}
    	String[] fio = surnMes.split("\\s+");
    	String personalNum = new String();
    	String isCompl = new String();
		String tableNum = " SELECT distinct personellNumber FROM workerfio WHERE surname =" + "\"" + fio[0] + "\""+
    	"AND name =" + "\"" + fio[1] + "\"" + "AND patronymic =" + "\"" + fio[2] + "\"";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(tableNum);;
	        while (resultSet.next()) {
	        	personalNum += resultSet.getString("personellNumber") ;
	        }
	        if (personalNum.equals("")) {
	        	return "<font size=\"3\" color=\"red\" face=\"Arial\">Сотрудник c фамилией " +surnMes +" не найден </font>";
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
        if (soundMes.equals("Выберите сообщение")) {
        	return "<font size=\"3\" color=\"red\" face=\"Arial\">Не указано сообщение, которое следует оставить </font>";
        }
        if (typeCom.equals("Выберите способ оповещения")) {
        	return "<font size=\"3\" color=\"red\" face=\"Arial\">Не выбран способ оповещения </font>";
        }
    	//System.out.println(personalNum);
    	String sendMess = "INSERT INTO message ( personellNumber,fileToPlay, notification_type,"
    			+ " isComplete) VALUES(" + "\"" +  personalNum + "\""  + "," +
				"\"" + soundMes  + "\"" +  "," + "\"" + typeCom + "\"" +  "," +
				"\"" + "20" + "\"" +  ");" ;
    	
    	sendQuery(sendMess);
		return "<font size=\"3\" color=\"green\" face=\"Arial\">Сообщение для сотрудника "+surnMes+" успешно добавлено"+" </font>";
    }
    
  /*  public void task() throws SchedulerException
    {
        // Запускаем Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // Извлекаем планировщик из schedule factory
        Scheduler scheduler = schedulerFactory.getScheduler();
         
        // текущее время
        long ctime = System.currentTimeMillis(); 
         
        // Запускаем JobDetail с именем задания,
        // группой задания и классом выполняемого задания
        JobDetail jobDetail = 
            new JobDetail("jobDetail2", "jobDetailGroup2", AddRecord.class);
        // Запускаем CronTrigger с его именем и именем группы
        CronTrigger cronTrigger = new CronTrigger("cronTrigger", "triggerGroup2");
        try {
            // Устанавливаем CronExpression
            CronExpression cexp = new CronExpression("0/5 * * * * ?");
            // Присваиваем CronExpression CronTrigger'у
            cronTrigger.setCronExpression(cexp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Планируем задание с помощью JobDetail и Trigger
        scheduler.scheduleJob(jobDetail, cronTrigger);
         
        // Запускаем планировщик
        scheduler.start();
    }*/
   public String getLastName() {
    	String lastName = new String();
    	int index = 0;
    	String query = "SELECT *  FROM workerfio;";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        	lastName += "<option value='";
	        	//System.out.println(resultSet.getString("surname"));
	        		lastName+= resultSet.getString("surname");
	        		lastName+= "'>";
	        		lastName+= resultSet.getString("surname");
	        		lastName+= "</option>";
	        	//index++;
	        }
	        	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        } 
    	// System.out.println(lastName);
    	return lastName;
    }
   
   public String getDepar() {
	  // getHoursWorked( "", "1");
	   
   	String depar = new String();
   	int index = 0;
   	String query = "SELECT *  FROM department;";
   	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        int kol = 0;
	        while (resultSet.next()) {
	        	if (kol != 0) {
	        		depar += ",";
	        	}
	        	depar+= resultSet.getString("title");
	        	kol++;
	        	/*depar += "<option value='";
	        	//System.out.println(resultSet.getString("surname"));
	        	depar+= resultSet.getString("title");
	        	depar+= "'>";
	        	depar+= resultSet.getString("title");
	        	depar+= "</option>";
	        	//index++;*/
	        }
	        	
   	}catch (SQLException e) {

       } finally {
           closeDB();
       } 
   	 //System.out.println(depar);
   	return depar;
   }
   public String getFIO() {
	   //System.out.println("Индекс запускатора до: "+ firstStart);
	   if (firstStart == 0) {
		   System.out.println("Timer start " + new Date());
		   new TimeListener().start();    //запускатор расписания
		   firstStart++;
	   }
	   
	   
	  // System.out.println("Индекс запускатора после: "+ firstStart);
	   	String fio = new String();
	   	int index = 0;
	   	String query = "SELECT *  FROM workerfio;";
	   	try{
		        connection = DriverManager.getConnection(url, user, password);
		        statement = connection.createStatement();
		        resultSet = statement.executeQuery(query);
		        int kol = 0;
		        while (resultSet.next()) {
		        	if (kol != 0) {
		        		fio += ",";
		        	}
		        	fio+= resultSet.getString("surname");
		        	fio+= " ";
		        	fio+= resultSet.getString("name");
		        	fio+= " ";
		        	fio+= resultSet.getString("patronymic");
		        	   kol++;
		        	/*fio += "<option value='";
		        	//System.out.println(resultSet.getString("surname"));
		        	fio+= resultSet.getString("surname");
		        	fio+= " ";
		        	fio+= resultSet.getString("name");
		        	fio+= " ";
		        	fio+= resultSet.getString("patronymic");
		        	fio+= "'>";
		        	fio+= resultSet.getString("surname");
		        	fio+= " ";
		        	fio+= resultSet.getString("name");
		        	fio+= " ";
		        	fio+= resultSet.getString("patronymic");
		        	fio+= "</option>";
		        	//index++;*/
		        }
		        	
	   	}catch (SQLException e) {

	       } finally {
	           closeDB();
	       } 
	   	
	   	// System.out.println(fio);
	   	return fio;
	   }
   
   public String getStatus() {
	   	String status = new String();
	   	
	   	String query = "SELECT DISTINCT statuscom  FROM worker;";
	   	try{
		        connection = DriverManager.getConnection(url, user, password);
		        statement = connection.createStatement();
		        resultSet = statement.executeQuery(query);
		        int kol = 0;
		        while (resultSet.next()) {
		        	if (resultSet.getString("statuscom").equals("102") == false) {
		        		if (kol != 0 ) {
		        		status += ",";
		        		}
		        	}
		        	
		    	    	if (resultSet.getString("statuscom").equals("101")) {
		    	    		status += "Опоздание";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("100")) {
		    	    		status += "Вход";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("200")) {
		    	    		status += "Выход";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("201")) {
		    	    		status += "Автовыход";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("300")) {
		    	    		status += "Не явился";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("202")) {
		    	    		status += "Ранний выход";
		    	    		}
		        	
		        	//status+= resultSet.getString("statuscom");
		        	kol++;
		        	/*depar += "<option value='";
		        	//System.out.println(resultSet.getString("surname"));
		        	depar+= resultSet.getString("title");
		        	depar+= "'>";
		        	depar+= resultSet.getString("title");
		        	depar+= "</option>";
		        	//index++;*/
		        }
		        	
	   	}catch (SQLException e) {

	       } finally {
	           closeDB();
	       } 
	   //	 System.out.println(status);
	   	return status;
	   }
   
   String notCom (String date) {
	   String answ = new String();
	   
   	//int good = 0;
   	String query = "select * from workerfio inner join department ON workerfio.department = department.id"
   			+ " where workerIdCard != \"null\" "
   			+ "and personellNumber NOT IN "
   			+ "(select workerId from worker where eventTime >="+ "\"" + date+ "\""+" group by workerId)";
   	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        answ += "<table border= \" 1\">"+  
	        		   "<tr>"+  
	        		   "<th>ID</th>" + 
	        		  "<th>ФИО</th>"+ 
	        		  "<th>Отдел</th>"+
	        		  "</tr>";
	        
	        while (resultSet.next()) {
	        	int count = 0;
	        	if  ( resultSet.getString("surname").equals("Unknown")) {
	        		count++;
	        	}
	        	if (count == 0) {
	        	answ += "<tr>";
	        	   answ += "<td>"+ resultSet.getString("personellNumber") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("title") +" </td>";
	        	   answ += "</tr>";
	        	}
	  
	        }
   	}catch (SQLException e) {

       } finally {
           closeDB();
       }  
	   
	   
	   return answ;
   }
   
   public String birthday () {
	   //getHoursWorked("1","СТУ","1","1");
	   String answ = new String();
	   
	   SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM"); //HH:mm:ss");
       String curDate = dateFormat.format(new Date());
	   
       
       String query = "select * from workerfio inner join department"
       		+ " ON workerfio.department = department.id where birthday like '" + curDate + "%';";
       
       
      	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        
	       
	       int isEmpty = 0;
	       while (resultSet.next()) {
	    	   isEmpty++;
	       }
	      // System.out.println(isEmpty);
	        //System.out.println(resultSet.wasNull());
	        if (isEmpty != 0) {
	        	resultSet.beforeFirst();
	        answ += "<div class =\"birtdayTitle\">";	
	        answ += "<font size=\"5\" color=\"black\" face=\"Arial\">Сегодня день рождения </font>";
	        answ += "</div>";
	        answ += "<div class =\"birtdayTable\">";
	        answ += "<table border= \" 1\">"+  
	        		   "<tr>"+  
	        		  "<th>ФИО</th>"+ 
	        		  "<th>Отдел</th>"+
	        		  "<th>Должность</th>" +
	        		  "<th>Исполняется</th>" +
	        		  "</tr>";
	        while (resultSet.next()) {
	        	answ += "<tr>";
	        	   answ += "<td>"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("title") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("position") +" </td>";
	        	   
	        	   SimpleDateFormat todayYear = new SimpleDateFormat("YYYY"); //HH:mm:ss");
	               String curYear = todayYear.format(new Date());
	               int curYearInt = Integer.parseInt(curYear);
	               String answYear =  resultSet.getString("birthday");
	               answYear = answYear.substring(6);
	               int answYaerInt = Integer.parseInt(answYear);
	               answYaerInt = curYearInt - answYaerInt;
	               answ += "<td>"+ Integer.toString(answYaerInt) +" </td>";
	        	   answ += "</tr>";
	        	  // System.out.println("Answer" + answ);
	        }
	     }
	        answ += "</div>";  
	        
	        
	       /* answ += "<div class =\"birtdayNextDayTitle\">";	
	        answ += "<font size=\"5\" color=\"black\" face=\"Arial\">Завтра день рождения </font>";
	        answ += "</div>";*/
   	}catch (SQLException e) {

       } finally {
           closeDB();
       }  
	   
     // 	System.out.println("Answer" + answ);
       return answ;
   }
   
   
   public void findNotCom(String date) {  //простановка статуса не явился
   	String query = "select * from workerfio inner join department ON workerfio.department = department.id"
      			+ " where workerIdCard != \"null\" "
      			+ "and personellNumber NOT IN "
      			+ "(select workerId from worker where eventTime >="+ "\"" + date+ "\""+" group by workerId)";
      	try{
   	        connection = DriverManager.getConnection(url, user, password);
   	        statement = connection.createStatement();
   	        resultSet = statement.executeQuery(query);
   	        while (resultSet.next()) {
   	        	
   	        	String insrtNotComQuery = "INSERT INTO worker VALUES(" + 
   	    				"\"" +  resultSet.getString("workerIdCard") + "\""  + "," +
   	    				"\"" + date.toString()+" 23:59:00" + "\"" +  "," +
   	    				"\"" + resultSet.getString("personellNumber")   + "\""  + "," +
   	    				"\"" + 300 + "\"" + ");" ;
   	    	   
   	        	sendQuery(insrtNotComQuery);
   	        	
   	        	 //resultSet.getString("personellNumber") ;
   	        	// resultSet.getString("workerIdCard");
   	        	   
   	  
   	        }
      	}catch (SQLException e) {

          } finally {
              closeDB();
          }  
   	   
   }
   
   public String  getHoursWorked(String calStart, String calFin, String department,/* String fioWorker,*/String month) {
	   
	   String hoursWorked = new String();
	   String result = new String();
	   
	   WorkerHourWorkList  workerList = new WorkerHourWorkList();
	   
	   int mounthInt = 1;
	   
	   int dayMonth = 31;
	   
	 /*  Date now = new Date();     // Gets the current date and time
	   int year = now.getYear();
	   */
	   
	   Calendar nowYear = Calendar.getInstance();
	    // 
	   int year = nowYear.get(Calendar.YEAR);
	   
	  /*  System.out.println("Current Year is : " + nowYear.get(Calendar.YEAR)
	    	+" Number date: " + nowYear.get(Calendar.DAY_OF_WEEK));*/
	   
	String dateStart = new String();
	String dateFinish = new String();
			
	if(month.equals(" ") == false) {
		 dateStart = Integer.toString(year) +"-"+ month +"-"+ "1 00:00:00";
		 dateFinish = Integer.toString(year) +"-"+ month +"-"+ "31 23:59:00";
	}
	
	if(calStart.replaceAll("\n","").equals("") == false && calFin.replaceAll("\n","").equals("") == false) {
		 dateStart = calStart.replaceAll("\n","") + " 00:00:00";
		 dateFinish = calFin.replaceAll("\n","") + " 23:59:59";
	}
	//   if (department.equals("") == false) {
	String query = "SELECT * FROM (worker INNER JOIN workerfio ON worker.workerId = workerfio.personellNumber)"
			+ "INNER JOIN department ON workerfio.department = department.id"
			+ " WHERE eventTime >=" + "\"" + dateStart+ "\""
			+ "AND eventTime <=" + "\"" + dateFinish + "\"";
	
	if (department.equals("") == false) {
		query += "AND title =" + "\"" + department.replaceAll("\n","") + "\"";
	}
	//query += "AND workerId = \"10240\" ";
	//query += "AND workerId = \"10300\" ";
	query += "AND status = \"Работает\" AND surname != \"Unknown\" ORDER BY surname, workerId, eventTime" ;	  
	
	System.out.println(query);
	   	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	 /* int kol = 0;
	        
	        while (kol == 0) {
	        	resultSet.next();
	        	String dateTh = resultSet.getString("eventTime");
	        	String yearTh = dateTh.substring(0, 4);
	        	String monthTh = dateTh.substring(5, 7);
	        	String dayTh = dateTh.substring(8, 10);
	        	String hoyrTh = dateTh.substring(11, 13);
	        	String minTh = dateTh.substring(14, 16);
	        	String secTh = dateTh.substring(17, 19);
	        	
	        	System.out.println("current date: " + dateTh);
	        	System.out.println("Year: "+ yearTh);
	        	System.out.println("Month: "+ monthTh);
	        	System.out.println("Day: "+ dayTh);
	        	System.out.println("Hour: "+ hoyrTh);
	        	System.out.println("Minute: "+ minTh);
	        	System.out.println("Second: "+ secTh);
	        	kol++;
	        }*/
	       
	        
	        String id_1 = new String();
	        String id_2 = new String();
	        
	        String date_1 = new String();
	        String date_2 = new String();
	        
	        String status_1 = new String();
	        String status_2 = new String();
	        
	        long tempLoseTimeDay = 0;
	        long hourWork =0;
	        int autoEx = 0;
	        int late = 0;
	        int notCom = 0;
	        int exitEqrly = 0;
	        long loseTime = 0;
	        
	        long dinnerTime = 1800;
	        
	        boolean isLast = false;
	        
	        	
	        resultSet.next();
	        
  	        id_1 = resultSet.getString("workerID");
  	        date_1 = resultSet.getString("eventTime");
  	        status_1 = resultSet.getString("statuscom");
  	        
        	/*String dateTh = resultSet.getString("eventTime");
        	String yearTh = dateTh.substring(0, 4);
        	String monthTh = dateTh.substring(5, 7);
        	String dayTh = dateTh.substring(8, 10);
        	String hourTh = dateTh.substring(11, 13);
        	String minTh = dateTh.substring(14, 16);
        	String secTh = dateTh.substring(17, 19);*/
  	        
  	        
  	        while (resultSet.next()) {
  	        
  	        	
  	        	id_2 = resultSet.getString("workerID");
  	  	        date_2 = resultSet.getString("eventTime");
  	  	        status_2 = resultSet.getString("statuscom");
  	  	        
  	  	        //System.out.println("st_1: " + status_1);
  	  	       // System.out.println("st_2: " + status_2);
  	  	        if (status_1.equals("300")) {
  	  	    	
  	  	       
  	  	        	//	System.out.println(resultSet.getString("workerId")+ " "+ resultSet.getString("eventTime")+ " "  + resultSet.getString("statuscom"));
  	  	        	
	        		notCom++;//System.out.println("зашел1");
	        		
	        	
	        		if( status_2.equals("300") ){
	        			notCom++;//System.out.println("зашел2");
	        			if(resultSet.isLast() == false) {
							resultSet.next();  
						}
	        		}else {
	        			//id_1 = id_2;
	        			date_1 = date_2;
	        			status_1 = status_2;
	        			//System.out.println("зашел2.1");
	        			resultSet.next();
	        			id_2 = resultSet.getString("workerID");
	      	  	        date_2 = resultSet.getString("eventTime");
	      	  	        status_2 = resultSet.getString("statuscom");
	        		}
	        	}
  	  	        if (status_1.equals("300") == false){
  	  	       // System.out.println("зашел3");
	        		if(status_1.equals("101")) {
	        			late++;
	        		}
	        		if(status_2.equals("201")) {
	        			autoEx++;
	        		}
	        		if(status_2.equals("202")) {
	        			exitEqrly++;
	        		}
  	        //	if (id_1.equals(id_2)) {
  	        		//if ( date_1.substring(8, 10).equals(date_2.substring(8, 10)) ) {
	        		//System.out.println(date_1);
	        		//System.out.println(date_2);
	        		
  	        			String hourTh_1 = date_1.substring(11, 13);
  	        			String minTh_1 = date_1.substring(14, 16);
  	        			String secTh_1 = date_1.substring(17, 19);
  	        			
  	        			/*System.out.println(date_1);
	        			System.out.println(hourTh_1);
	        			System.out.println(minTh_1);
	        			System.out.println(secTh_1);*/
  	        			
  	        			String hourTh_2 = date_2.substring(11, 13);
  	        			String minTh_2 = date_2.substring(14, 16);
  	        			String secTh_2 = date_2.substring(17, 19);
  	        			
  	        			
  	        			long time_1 = Integer.parseInt(hourTh_1) * 3600 + 
  	        					Integer.parseInt(minTh_1) * 60 + Integer.parseInt(secTh_1);
  	        			
  	        			long time_2 = Integer.parseInt(hourTh_2) * 3600 + 
  	        					Integer.parseInt(minTh_2) * 60 + Integer.parseInt(secTh_2);
  	        			
  	        			hourWork += time_2 - time_1;
  	        			/*System.out.println("time1:" + time_1);
  	        			System.out.println("time2:" + time_2);
  	        			System.out.println("hourWork:" + hourWork);*/
  	        			if (resultSet.isLast()) {
  	        				resultSet.previous();
  	        				tempLoseTimeDay = 0;
	        					String fio = resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic");
	        					String title = resultSet.getString("title");
	        					
	        					
	        					long hourW = 0;
	        					long minW = 0;
	        					long secW = 0;
	        					
	        					hourW = hourWork/3600;
	        					minW = (hourWork - hourW*3600)/60;
	        					secW = hourWork - hourW*3600 - minW*60;
	        					String hourWorkSt = new String(hourW+":"+minW+":"+secW);
	        					//System.out.println(hourWork);
	        					
	        					long hourL = 0;
	        					long minL = 0;
	        					long secL = 0;
	        					
	        					hourL = loseTime/3600;
	        					minL = (loseTime - hourL*3600)/60;
	        					secL = loseTime - hourL*3600 - minL*60;
	        					String loseTimeSt = new String(hourL+":"+minL+":"+secL);

	        				workerList.addWorkerHourWorkToList(new WorkerHourWork(fio, title, hourWorkSt, late, autoEx, loseTimeSt,notCom, exitEqrly));
	        				/*System.out.println(fio + " " + title + " " + hourWorkSt+ " " +"late: "+
	        				late + " " +"autoEx: " + autoEx + " " + loseTimeSt+ " "+"notCom: "+ notCom);
	        				System.out.println("");*/
	        				hourWork = 0;
	        				loseTime = 0;
	        				late = 0;
	        				autoEx = 0;
	        				notCom = 0;
	        				exitEqrly = 0;
  	        			}
  	        			
  	        			
  	        			resultSet.next();
  	        			
  	        			if(resultSet.getString("statuscom").equals("300") ) {
  	        				while(resultSet.getString("statuscom").equals("300") & id_1.equals(resultSet.getString("workerID")) ) {
  	        					notCom++;
  	        					resultSet.next();
  	        					//System.out.println("зашел2");
  	        				}
  	        			}
  	        			
  	        			
  	        			if( id_2.equals(resultSet.getString("workerID"))  ) {
  	        				
  	        			
	  	        			if ( date_2.substring(8, 10).equals(resultSet.getString("eventTime").substring(8, 10)) ) {
	  	        				String hourTemp = resultSet.getString("eventTime").substring(11, 13);
	  	  	        			String minTemp = resultSet.getString("eventTime").substring(14, 16);
	  	  	        			String secTemp = resultSet.getString("eventTime").substring(17, 19);
	  	  	        			long tempTime = 0;	  	  	        			  	  	        		
	  	  	        			 tempTime = Integer.parseInt(hourTemp) * 3600 + 
	  	  	        					Integer.parseInt(minTemp) * 60 + Integer.parseInt(secTemp);
	  	  	        		//System.out.println("temp:" + resultSet.getString("workerID") + "  " + resultSet.getString("eventTime") + "tempTime: "+ tempTime + "Time2: " + time_2);
	  	  	        			tempLoseTimeDay += tempTime - time_2;
	  	        			}
	  	        			else
	  	        				if ( date_2.substring(8, 10).equals(resultSet.getString("eventTime").substring(8, 10)) == false ) {
	  	        				if(tempLoseTimeDay > dinnerTime) {
	  	        					//System.out.println(resultSet.getString("workerID") + "  " + tempLoseTimeDay);
	  	        					loseTime += tempLoseTimeDay - dinnerTime;
	  	        					hourWork -= dinnerTime;
	  	        					tempLoseTimeDay = 0;
	  	        					
	  	        					//System.out.println(resultSet.getString("workerID") + "  " + resultSet.getString("eventTime"));
	  	        				}
	  	        				else {
	  	        					//System.out.println("else1 " + resultSet.getString("workerID") + "  " + tempLoseTimeDay);
	  	        					hourWork -= dinnerTime;
	  	        					tempLoseTimeDay = 0;
	  	        					//System.out.println("else2 " + resultSet.getString("workerID") + "  " + tempLoseTimeDay);
	  	        				}
	  	        			}
  	        			}else {
  	        				resultSet.previous();
  	        				tempLoseTimeDay = 0;
  	        					String fio = resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic");
  	        					String title = resultSet.getString("title");
  	        					
  	        					
  	        					long hourW = 0;
  	        					long minW = 0;
  	        					long secW = 0;
  	        					
  	        					hourW = hourWork/3600;
  	        					minW = (hourWork - hourW*3600)/60;
  	        					secW = hourWork - hourW*3600 - minW*60;
  	        					String hourWorkSt = new String(hourW+":"+minW+":"+secW);
  	        					//System.out.println(hourWork);
  	        					
  	        					long hourL = 0;
  	        					long minL = 0;
  	        					long secL = 0;
  	        					
  	        					hourL = loseTime/3600;
  	        					minL = (loseTime - hourL*3600)/60;
  	        					secL = loseTime - hourL*3600 - minL*60;
  	        					String loseTimeSt = new String(hourL+":"+minL+":"+secL);

  	        				workerList.addWorkerHourWorkToList(new WorkerHourWork(fio, title, hourWorkSt, late, autoEx, loseTimeSt,notCom, exitEqrly));
  	        				/*System.out.println(fio + " " + title + " " + hourWorkSt+ " " +"late: "+
  	        				late + " " +"autoEx: " + autoEx + " " + loseTimeSt+ " "+"notCom: "+ notCom);
  	        				System.out.println("");*/
  	        				hourWork = 0;
  	        				loseTime = 0;
  	        				late = 0;
  	        				autoEx = 0;
  	        				notCom = 0;
  	        				exitEqrly = 0;
  	        				//if(resultSet.isLast() == false) {
  	        					resultSet.next();  
  	        				//} 
  	        				
  	        			}
  	        			
  	        		//}
	        	}
  	  	   // System.out.println("зашел4");
  	  	        
  	  	    if(resultSet.getString("statuscom").equals("300") & id_1.equals(resultSet.getString("workerID")) ) {
  				while(resultSet.getString("statuscom").equals("300") && id_1.equals(resultSet.getString("workerID"))  ) {
  					notCom++;
  					resultSet.next();
  					//System.out.println("зашел2");
  				}
  				if(id_2.equals(resultSet.getString("workerID")) == false  ) {
  				resultSet.previous();
					String fio = resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic");
					String title = resultSet.getString("title");
					
					
					long hourW = 0;
					long minW = 0;
					long secW = 0;
					
					hourW = hourWork/3600;
					minW = (hourWork - hourW*3600)/60;
					secW = hourWork - hourW*3600 - minW*60;
					String hourWorkSt = new String(hourW+":"+minW+":"+secW);
					//System.out.println(hourWork);
					
					long hourL = 0;
					long minL = 0;
					long secL = 0;
					
					hourL = loseTime/3600;
					minL = (loseTime - hourL*3600)/60;
					secL = loseTime - hourL*3600 - minL*60;
					String loseTimeSt = new String(hourL+":"+minL+":"+secL);

				workerList.addWorkerHourWorkToList(new WorkerHourWork(fio, title, hourWorkSt, late, autoEx, loseTimeSt,notCom,exitEqrly));
				/*System.out.println(fio + " " + title + " " + hourWorkSt+ " " +"late: "+
				late + " " +"autoEx: " + autoEx + " " + loseTimeSt+ " "+"notCom: "+ notCom);
				System.out.println("");*/
				hourWork = 0;
				loseTime = 0;
				late = 0;
				autoEx = 0;
				notCom = 0;
				exitEqrly = 0;
				
					//if(resultSet.isLast() == false) {
						resultSet.next();  
					//}
  				}
				
  			}
  	  	    
  	        			id_1 = resultSet.getString("workerID");
  	        	        date_1 = resultSet.getString("eventTime");
  	        	        status_1 = resultSet.getString("statuscom");
  	    //    	}
  	        	
  	        	
	        	
  	        }
  	     
  	    
		}catch (SQLException e) {
	
		} finally {
	       closeDB();
		}  
	   	//Collections.sort(workerList.getWorkerHourWorkList(), String.CASE_INSENSITIVE_ORDER);
	   	result += "<table border= \" 1\">"+  
     		  "<tr>"+  
     		  "<th>ФИО</th>" + 
     		  "<th>Отдел</th>"+ 
     		  "<th>Отработанное время</th>"+ 
     		  "<th>Время отсутсвия</th>"+ 
     		  "<th>Опоздание</th>"+ 
     		  "<th>Автовыход</th>"+
     		  "<th>Не явился</th>"+
     		  "<th>Ранниый выход</th>"+
     		  "</tr>";
	   // System.out.println("1111112");
	       
	      for (WorkerHourWork hourWorkL: workerList.getWorkerHourWorkList()){
	    	  result += "<tr>";
	    	  result += "<td style=\"color:#0000cd\">"+ hourWorkL.getFIO() +" </td>";
	    	  result += "<td style=\"color:#0000cd\">"+ hourWorkL.getTitle() +" </td>";
	    	  result += "<td style=\"color:#0000cd\">"+ hourWorkL.getHourWork() +" </td>";
	    	  result += "<td style=\"color:#0000cd\">"+ hourWorkL.getLoseTime() +" </td>";
	    	  result += "<td style=\"color:#0000cd\">"+ hourWorkL.getLate() +" </td>";
	    	  result += "<td style=\"color:#0000cd\">"+ hourWorkL.getAutoEx() +" </td>";
	    	  result += "<td style=\"color:#0000cd\">"+ hourWorkL.getNotCom() +" </td>";
	    	  result += "<td style=\"color:#0000cd\">"+ hourWorkL.getExitEarly() +" </td>";
	    	  result += "</tr>";
	    	/*System.out.println(hourWorkL.getFIO() + " " + hourWorkL.getTitle() + " " + hourWorkL.getHourWork()+ " " +"late: "+
					hourWorkL.getLate() + " " +"autoEx: " + hourWorkL.getAutoEx() + " " + hourWorkL.getLoseTime()+ " "+"notCom: "+ hourWorkL.getNotCom());
					System.out.println("");*/
        } 
	   // System.out.println("111111");
	      return result;
   }
   
   public void autoExit (String date) {
	   String id1 = new String();
	   String stat1 = new String();
	   String id2 = new String();	   String stat2 = new String();
	   
	   String query = "SELECT * FROM (worker INNER JOIN workerfio ON worker.workerId = workerfio.personellNumber)"
   			+ "INNER JOIN department ON workerfio.department = department.id"
   			+ " WHERE eventTime >=" + "\"" + date+ "\""+" ORDER BY workerId, eventTime";
	  /* String query = "SELECT * FROM  worker WHERE eventTime >" 
     			+ "\"" + date + "\""+" ORDER BY workerId";*/
	   //System.out.println(date + " 00:00:00");
	  // System.out.println(query);
     	try{
  	        connection = DriverManager.getConnection(url, user, password);
  	        statement = connection.createStatement();
  	        resultSet = statement.executeQuery(query);
  	        
  	        resultSet.next();
  	        
  	        id1 = resultSet.getString("workerID");
        	stat1 = resultSet.getString("statuscom");
        	
        	
  	        while (resultSet.next()) {
 	        	id2 = resultSet.getString("workerID");
  	        	stat2 = resultSet.getString("statuscom");
  	        	//System.out.println("first rec "+ "id1: " +  id1 + " stat1:" + stat1);
  	        	//System.out.println("second rec " +"id2: " +  id2+ " stat2:"  + stat2);
  	        	//System.out.println(date);
  	        	//System.out.println(resultSet.getString("workerID"));
  	        	
  	        	String outTime = " 17:30:00"; 
  	        		
  	        		if (id1.equals(id2)) {
  	        			//System.out.println("first if");
  	        			//id1 = id2;
  	        			stat1 = stat2;
  	        		}
  	        		else {
  	        			if (Integer.parseInt(stat1) < 200){
  	        				resultSet.previous();
  	        			
  	        				if (Integer.parseInt(resultSet.getString("eventTime").substring(11, 13)) >= 17 &&
  	        					Integer.parseInt(resultSet.getString("eventTime").substring(14, 16))>= 30) {
  	        				outTime =" " + resultSet.getString("eventTime").substring(11, 13)+ ":" +
  	        						(Integer.parseInt(resultSet.getString("eventTime").substring(14, 16))+1) + ":"+"00";
  	        				}	
  	        			//System.out.println("Else if write table");
  	        			String queryOut = "INSERT INTO worker VALUES(" + 
  	     	    				"\"" +  resultSet.getString("workerIdCard") + "\""  + "," +
  	     	    				"\"" + date.toString( ) + outTime + "\"" +  "," +
  	     	    				"\"" + resultSet.getString("personellNumber")   + "\""  + "," +
  	     	    				"\"" + 201 + "\"" + ");" ;
  	        			//System.out.println(queryOut);
  	        			sendQuery(queryOut);
  	        			resultSet.next();
  	        			id1 = id2;
  	        			stat1 = stat2;
  	        			
  	        			}else {
  	        				resultSet.previous();
  	        				if (Integer.parseInt(resultSet.getString("eventTime").substring(11, 13)) < 17) {
  	        					String  sqlSafeOff = "SET SQL_SAFE_UPDATES = 0;";
  	        					sendQuery(sqlSafeOff);
  	        				
  	        					String queryOutEarly = "update worker set statuscom = '202' where workerId = " + 
  	        						"\"" + resultSet.getString("personellNumber")   + "\""  + "and eventTime = " +
  	  	     	    				"\"" + resultSet.getString("eventTime") + "\"" +  ";" ;
  	  	        			//System.out.println(queryOut);
  	        					sendQuery(queryOutEarly);
  	        					resultSet.next();
  	        					id1 = id2;
  	    	        			stat1 = stat2;
  	  	        			
  	        					String  sqlSafeOn = "SET SQL_SAFE_UPDATES = 1;";
  	        					sendQuery(sqlSafeOn);	
  	        				}else if (Integer.parseInt(resultSet.getString("eventTime").substring(11, 13)) <= 17 &&
		        					Integer.parseInt(resultSet.getString("eventTime").substring(14, 16)) <= 29 ) {
	  	        			
	  	        			//System.out.println("Last else");
	  	        				String  sqlSafeOff = "SET SQL_SAFE_UPDATES = 0;";
	  	        				sendQuery(sqlSafeOff);
	  	        				
	  	        				String queryOutEarly = "update worker set statuscom = '202' where workerId = " + 
	  	        						"\"" + resultSet.getString("personellNumber")   + "\""  + "and eventTime = " +
	  	  	     	    				"\"" + resultSet.getString("eventTime") + "\"" +  ";" ;
	  	  	        			//System.out.println(queryOut);
	  	  	        			sendQuery(queryOutEarly);
	  	  	        			resultSet.next();
	  	  	        			
	  	  	        			id1 = id2;
	  	  	        			stat1 = stat2;
	  	  	        			String  sqlSafeOn = "SET SQL_SAFE_UPDATES = 1;";
		        				sendQuery(sqlSafeOn);
	  	  	        			
	  	        				
  	        				}else {
  	        					resultSet.next();
  	        					id1 = id2;
  	        					stat1 = stat2;
  	        				}
  	        			}
  	        		}
  	        		
  	  	        	if(resultSet.isLast()) {
  	  	        		if (Integer.parseInt(stat2) < 200){
  	  	        			
  	  	        		if (Integer.parseInt(resultSet.getString("eventTime").substring(11, 13)) >= 17 &&
  	        					Integer.parseInt(resultSet.getString("eventTime").substring(14, 16))>= 30) {
  	        				outTime =" " + resultSet.getString("eventTime").substring(11, 13)+ ":" +
  	        						(Integer.parseInt(resultSet.getString("eventTime").substring(14, 16))+1) + ":"+"00";
  	        			}
  	  	        			//System.out.println("Else if write table");
  	  	        			String queryOut = "INSERT INTO worker VALUES(" + 
  	  	     	    				"\"" +  resultSet.getString("workerIdCard") + "\""  + "," +
  	  	     	    			"\"" + date.toString( ) + outTime + "\"" +  "," +
  	  	     	    				"\"" + resultSet.getString("personellNumber")   + "\""  + "," +
  	  	     	    				"\"" + 201 + "\"" + ");" ;
  	  	        			//System.out.println(queryOut);
  	  	        			sendQuery(queryOut);
  	  	        		}else {
  	        				resultSet.previous();
  	        				if (Integer.parseInt(resultSet.getString("eventTime").substring(11, 13)) < 17) {
  	        					String  sqlSafeOff = "SET SQL_SAFE_UPDATES = 0;";
  	        					sendQuery(sqlSafeOff);
  	        				
  	        					String queryOutEarly = "update worker set statuscom = '202' where workerId = " + 
  	        						"\"" + resultSet.getString("personellNumber")   + "\""  + "and eventTime = " +
  	  	     	    				"\"" + resultSet.getString("eventTime") + "\"" +  ";" ;
  	  	        			//System.out.println(queryOut);
  	        					sendQuery(queryOutEarly);
  	        					resultSet.next();
  	        					id1 = id2;
  	    	        			stat1 = stat2;
  	  	        			
  	        					String  sqlSafeOn = "SET SQL_SAFE_UPDATES = 1;";
  	        					sendQuery(sqlSafeOn);	
  	        				}else if (Integer.parseInt(resultSet.getString("eventTime").substring(11, 13)) <= 17 &&
		        					Integer.parseInt(resultSet.getString("eventTime").substring(14, 16)) <= 29 ) {
	  	        			
	  	        			//System.out.println("Last else");
	  	        				String  sqlSafeOff = "SET SQL_SAFE_UPDATES = 0;";
	  	        				sendQuery(sqlSafeOff);
	  	        				
	  	        				String queryOutEarly = "update worker set statuscom = '202' where workerId = " + 
	  	        						"\"" + resultSet.getString("personellNumber")   + "\""  + "and eventTime = " +
	  	  	     	    				"\"" + resultSet.getString("eventTime") + "\"" +  ";" ;
	  	  	        			//System.out.println(queryOut);
	  	  	        			sendQuery(queryOutEarly);
	  	  	        			resultSet.next();
	  	  	        			
	  	  	        			id1 = id2;
	  	  	        			stat1 = stat2;
	  	  	        			String  sqlSafeOn = "SET SQL_SAFE_UPDATES = 1;";
		        				sendQuery(sqlSafeOn);
	  	  	        			
	  	        				
  	        				}else {
  	        					resultSet.next();
  	        					id1 = id2;
  	        					stat1 = stat2;
  	        				}
  	        			}
  	  	        	}
  	        	
  	  
  	        }
  	        
     	 }catch (SQLException e) {

         } finally {
             closeDB();
         }  
  	   
   }
   
 
   
}
